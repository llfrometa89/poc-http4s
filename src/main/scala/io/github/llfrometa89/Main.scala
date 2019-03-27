package io.github.llfrometa89

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import io.github.llfrometa89.http.controllers.v1.AccountController
import io.github.llfrometa89.http.core.{Controller, WebServer}

object Main extends IOApp {

  val routes: Seq[ResourcePath] = Seq(
    ResourcePath(RoutePath.ACCOUNTS) controller Map(Version.V1 -> classOf[AccountController]) secured
  )

  def run(args: List[String]): IO[ExitCode] = WebServer.run[IO].compile.drain.as(ExitCode.Success)

}

object RoutePath {
  val ACCOUNTS = "accounts"
}

sealed case class ResourcePath(streamPath: String) {

  var controller: Map[String, Class[_ <: Controller]] = Map.empty[String, Class[_ <: Controller]]
  var isSecured                                       = false
  var streams: Seq[String]                            = Seq.empty[String]

  def /(child: String): ResourcePath = {
    streams = streams.+:(child)
    this
  }

  def controller(versionController: Map[String, Class[_ <: Controller]]): ResourcePath = {
    this.controller = versionController
    this
  }

  def secured: ResourcePath =
    withSecured(true)

  private def withSecured(secured: Boolean): ResourcePath = {
    this.isSecured = secured
    this
  }
}

object Version {
  final val V1: String  = "v1"
  final val V2: String  = "v2"
  final val V3: String  = "v3"
  final val V4: String  = "v4"
  final val V5: String  = "v5"
  final val V6: String  = "v6"
  final val V7: String  = "v7"
  final val V8: String  = "v8"
  final val V9: String  = "v9"
  final val V10: String = "v10"
}
