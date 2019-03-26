package io.github.llfrometa89

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import io.github.llfrometa89.http.core.WebServer

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = WebServer.run[IO].compile.drain.as(ExitCode.Success)

}
