
name := "final_project2"

version := "0.1"

scalaVersion := "2.12.8"


libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "2.0.0-M4",
  "com.typesafe" % "config" % "1.3.4"
)

enablePlugins(JavaAppPackaging)