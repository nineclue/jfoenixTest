ThisBuild / scalaVersion := "3.0.1"
ThisBuild / version := "0.0.1"

val fxversion = "11.0.2"

// https://www.reddit.com/r/scala/comments/9fbzbk/build_file_snippet_for_using_javafx_with_java_11/
val osType: SettingKey[String] = SettingKey[String]("osType")

import scala.util.Properties
osType := {
	if (Properties.isLinux)
		"linux"
	else if (Properties.isMac)
		"mac"
	else if (Properties.isWin)
		"win"
	else
		throw new Exception(s"unknown os: ${Properties.osName}")
}

val javafxLib = file(sys.env.get("JAVAFX_LIB").getOrElse("Environmental variable JAVAFX_LIB is not set"))

lazy val root = (project in file("."))
	.settings(
		name := "openjfx-sbt",
		libraryDependencies ++= Seq(
			// JavaFX 11 jars are distributed for each platform
			"org.openjfx" % "javafx-controls" % fxversion classifier osType.value,
			"org.openjfx" % "javafx-graphics" % fxversion classifier osType.value,
			"org.openjfx" % "javafx-base" % fxversion classifier osType.value,
            "com.jfoenix" % "jfoenix" % "9.0.10"
        ),

		// scala-2.12.8: invoke javac directly since scalac does not yet support module syntax
		// without --module-path, "error: module not found: javafx.controls" occurs on "requires javafx.controls"
		Compile / unmanagedSourceDirectories -= (Compile / javaSource).value,
		Compile / compile := {
			val analysis = (Compile / compile).value
			val command = s"javac -d ${(Compile / classDirectory).value} --module-path $javafxLib ${((Compile / javaSource).value ** "*.java").get.mkString(" ")}"
			println(s"executing: $command")
			import scala.sys.process._
			command.!
			analysis
		},

		// JavaFX 11 jars are modules and cannot be embedded in the fat jar
		// The fat jar is still built to embed the scala-library jar
		assemblyExcludedJars in assembly := (fullClasspath in assembly).value.filter(_.data.getName.startsWith("javafx-")),
	)
