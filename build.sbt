name := "fixub"

version := "1.0"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "co.fs2" %% "fs2-io" % "1.1.0-M1",
  "dev.profunktor" %% "console4cats" % "0.8.0-M1",
  "org.scodec" %% "scodec-stream" % "1.2.1",
)

enablePlugins(JavaAppPackaging)
