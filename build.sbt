name := "http4s_testing"

version := "0.1"

scalaVersion := "2.12.8"

lazy val V = new {
  val cats                      = "1.5.0"
  val catsEffect                = "1.0.0"
  val logger4s                  = "0.3.1"
  val scalatest                 = "3.0.0"
  val monocleVersion            = "1.5.0"
  val paradiseVersion           = "2.1.1"
}


libraryDependencies ++= Seq(
  "org.typelevel"                  %% "cats-core"        % V.cats,
  "org.typelevel"                  %% "cats-effect"      % V.catsEffect,
  "com.github.julien-truffaut"     %% "monocle-core"     % V.monocleVersion,
  "com.github.julien-truffaut"     %% "monocle-macro"    % V.monocleVersion,
  "org.pure4s"                     %% "logger4s-cats"    % V.logger4s,
)
addCompilerPlugin("org.scalamacros" % "paradise" % V.paradiseVersion cross CrossVersion.full)
