import java.nio.charset.Charset
import java.nio.file.Paths
import java.util.concurrent.Executors

import cats.effect.Console.io._
import cats.effect._
import cats.implicits._
import fs2.Pipe
import scodec.Attempt
import scodec.Codec
import scodec.Decoder
import scodec.Err
import scodec.GenCodec
import scodec.bits.BitVector
import scodec.stream.StreamDecoder

object Main extends IOApp {
  val ChunkSize = 4096
  val DefaultInputEncoding: Charset = Charset.forName("Windows-1250")

  val bytesToBitVectors: Pipe[IO, Byte, BitVector] = _.chunks.map(_.toArray).map(BitVector(_))

  def decode(charsetName: Option[String]): Pipe[IO, Byte, String] =
    bytes =>
      fs2
        .Stream
        .eval(charsetName.map(ch => IO(Charset.forName(ch))).getOrElse(DefaultInputEncoding.pure[IO]))
        .flatMap(charset => bytes.through(org.http4s.internal.decode[IO](org.http4s.Charset(charset))))

  override def run(args: List[String]): IO[ExitCode] =
    Blocker[IO]
      .use { blocker =>
        fs2
          .io
          .stdin[IO](ChunkSize, blocker)
          .through(decode(args.headOption))
          .through(fs2.text.utf8Encode[IO])
          .through(fs2.io.stdout[IO](blocker))
          .compile
          .drain
          .as(ExitCode.Success)
      }

}
