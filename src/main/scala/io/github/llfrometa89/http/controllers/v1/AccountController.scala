package io.github.llfrometa89.http.controllers.v1

import cats.effect.Sync
import cats.implicits._
import io.github.llfrometa89.application.dto.CreateAccount.AccountRequest
import io.github.llfrometa89.application.dto.Transfer.TransferRequest
import io.github.llfrometa89.application.services.AccountApplicationService
import io.github.llfrometa89.http.controllers._
import io.github.llfrometa89.implicits._
import io.github.llfrometa89.http.core.Controller
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object AccountController extends Controller {

  def routes[F[_]: Sync]: HttpRoutes[F] = {

    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes.of[F] {
      case POST -> Root / ACCOUNTS =>
        for {
          account <- AccountApplicationService.open[F](
            AccountRequest("Livan Frometa", "example@example.com", Some(200)))
          resp <- Ok(account)
        } yield resp
      case POST -> Root / ACCOUNTS / TRANSFER =>
        for {
          result <- AccountApplicationService.transfer[F](TransferRequest("1234", "9876", 100))
          resp   <- Ok(result)
        } yield resp
    }
  }
}
