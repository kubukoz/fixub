import java.io.{File, PrintWriter}

import scala.io.Source

object Main {
  val DefaultInputEncoding = "Windows-1250"

  def main(args: Array[String]) {
    args.length match {
      case 0 | 1 =>
        println("usage: fixub input output [src-encoding]")
        sys.exit(1)
      case _ =>
        val srcFileName = args.head

        val destFileName = args(1)

        val encoding = args.drop(2).headOption.getOrElse(DefaultInputEncoding)

        val pw = new PrintWriter(new File(destFileName))

        try {
          val src = Source.fromFile(srcFileName, encoding)
          val lines = src.getLines.toList
          val result = lines.mkString("\n")

          pw.write(result)

          src.close()

          println(s"Converted ${lines.length} lines")
        } finally {
          pw.close()
        }
    }
  }
}
