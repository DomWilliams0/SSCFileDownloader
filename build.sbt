name := "SSCFileDownloader"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq("org.jsoup" % "jsoup" % "1.7.2")

// logging
libraryDependencies ++= Seq("ch.qos.logback" % "logback-classic" % "1.1.3")
libraryDependencies ++= Seq("com.typesafe.scala-logging" %% "scala-logging" % "3.1.0")

libraryDependencies ++= Seq("com.typesafe" % "config" % "1.3.0")