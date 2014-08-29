import sbt.Keys._
import sbt._

object BuildSettings {

  lazy val basicSettings = Seq(
    version := "0.1.0-SNAPSHOT",
    organization := "com.edc4it",
    startYear := Some(2014),
    licenses := Seq("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    scalaVersion := "2.10.4",
    crossPaths := false,
    resolvers ++= Dependencies.resolutionRepos,
    scalacOptions ++= Seq("-target:jvm-1.7", "-deprecation"),
    javacOptions in compile ++= Seq("-source", "1.7", "-target", "1.7"),
    javacOptions in doc ++= Seq("-source", "1.7"),
    publishTo := Some(Resolver.file("file", new File(Path.userHome.absolutePath + "/.m2/repository")))
  )


}
