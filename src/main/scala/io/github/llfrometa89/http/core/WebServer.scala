package io.github.llfrometa89.http.core

import cats.effect.{ConcurrentEffect, ContextShift, Timer}
import fs2.Stream
import io.github.llfrometa89.http.controllers.v1.AccountController
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.implicits._
import io.github.llfrometa89.implicits._
import org.http4s.server.middleware.Logger

object WebServer {

  def run[F[_]: ConcurrentEffect](implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {
    val httpApp                     = AccountController.routes[F].orNotFound
    val httpAppWithLoggerMiddleware = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)
    BlazeServerBuilder[F]
      .bindHttp(9000, "0.0.0.0")
      .withHttpApp(httpAppWithLoggerMiddleware)
      .serve
  }.drain

}
