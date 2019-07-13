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

object Main extends IOApp {
  val ChunkSize = 4096
  val DefaultInputEncoding: Charset = Charset.forName("Windows-1250")

  val bytesToBitVectors: Pipe[IO, Byte, BitVector] = _.chunks.map(_.toArray).map(BitVector(_))

  override def run(args: List[String]): IO[ExitCode] =
    args match {
      case srcFileName :: destFileName :: remainingArgs =>
        val codec: IO[Decoder[String]] =
          remainingArgs.headOption
            .map(name => IO(Charset.forName(name)))
            .getOrElse(DefaultInputEncoding.pure[IO])
            .map {
              scodec.codecs.string(_).emap {
                case "" => Attempt.failure(Err("Empty string"))
                case s  => Attempt.successful(s)
              }
            }

        Blocker[IO].use { implicit blocker =>
          codec.flatMap { implicit c =>
            convertAndSave(from = srcFileName, to = destFileName).compile.drain
          }
        } *> putStrLn("Converted file").as(ExitCode.Success)
      case _ =>
        putError("usage: fixub input output [src-encoding]").as(ExitCode.Error)
    }

  private def convertAndSave(from: String, to: String)(implicit blocker: Blocker, stringDecoder: Decoder[String]) =
    fs2.io.file
      .readAll[IO](
        Paths.get(from),
        blocker,
        chunkSize = ChunkSize
      )
      .through(bytesToBitVectors)
      .through(scodec.stream.decode.pipe[IO, String])
      .through(fs2.text.utf8Encode[IO])
      .through(fs2.io.file.writeAll[IO](Paths.get(to), blocker))
}
