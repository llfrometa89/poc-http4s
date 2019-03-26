package io.github.llfrometa89

import cats.effect.{ExitCode, IO, IOApp, Sync}
import cats.implicits._
import io.github.llfrometa89.http.core.WebServer

object Main extends IOApp {

//  def program[F[_]]: F[ExitCode] =
//    for {
//      resp <-
//    } yield resp

  def run(args: List[String]): IO[ExitCode] = WebServer.stream[IO].compile.drain.as(ExitCode.Success)

}
