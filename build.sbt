name := "fixub"

version := "1.0"

scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
  "co.fs2" %% "fs2-io" % "2.2.2",
  "dev.profunktor" %% "console4cats" % "0.8.1",
  "org.scodec" %% "scodec-stream" % "2.0.0",
)

enablePlugins(JavaAppPackaging)
enablePlugins(GraalVMNativeImagePlugin)
