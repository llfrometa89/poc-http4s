name := "http4s_testing"

version := "0.1"

scalaVersion := "2.12.8"

scalacOptions ++= Seq("-Ypartial-unification")

lazy val V = new {
  val paradiseVersion = "2.1.1"
  val http4sVersion   = "0.20.0-M6"
  val circeVersion    = "0.11.1"
  val logbackVersion  = "1.2.3"
  val monocleVersion  = "1.5.0"
  val kindProjectorVersion = "0.9.9"
}

libraryDependencies ++= Seq(
  "org.http4s"                 %% "http4s-blaze-server" % V.http4sVersion,
  "org.http4s"                 %% "http4s-blaze-client" % V.http4sVersion,
  "org.http4s"                 %% "http4s-circe"        % V.http4sVersion,
  "org.http4s"                 %% "http4s-dsl"          % V.http4sVersion,
  "io.circe"                   %% "circe-generic"       % V.circeVersion,
  "io.circe"                   %% "circe-parser"        % V.circeVersion,
  "ch.qos.logback"             % "logback-classic"      % V.logbackVersion,
  "com.github.julien-truffaut" %% "monocle-core"        % V.monocleVersion,
  "com.github.julien-truffaut" %% "monocle-macro"       % V.monocleVersion,
  compilerPlugin("org.spire-math" %% "kind-projector" % V.kindProjectorVersion)
)
addCompilerPlugin("org.scalamacros" % "paradise" % V.paradiseVersion cross CrossVersion.full)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
  "-Xfatal-warnings",
)
