name := "DeniseProblem"

version := "0.1"

scalaVersion := "2.12.10"

val circeVersion = "0.11.1"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.1",
  "org.slf4j" % "slf4j-nop" % "1.7.26",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.1",
  "com.typesafe.play" %% "play-json" % "2.7.4",
  "com.github.tminglei" %% "slick-pg" % "0.18.0",
  "com.github.tminglei" %% "slick-pg_play-json" % "0.18.0"
)

