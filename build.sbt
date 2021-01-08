name := "fixub"

version := "1.0"

scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
  "co.fs2" %% "fs2-io" % "2.4.6",
  "dev.profunktor" %% "console4cats" % "0.8.1",
  "org.http4s" %% "http4s-core" % "0.21.14"
)

enablePlugins(JavaAppPackaging)
enablePlugins(GraalVMNativeImagePlugin)
