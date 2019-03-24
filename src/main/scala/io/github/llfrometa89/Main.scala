package io.github.llfrometa89

import cats.effect.{ExitCode, IO, IOApp, Sync}
import cats.implicits._

object Main extends IOApp {

  def program[F[_]: Sync]: F[ExitCode] =
    for {
      _          <- Sync[F].delay(println(s"hello"))
    } yield ExitCode.Success

  def run(args: List[String]): IO[ExitCode] = program[IO]

}
