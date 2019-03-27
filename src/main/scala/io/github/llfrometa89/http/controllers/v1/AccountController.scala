package io.github.llfrometa89.http.controllers.v1

import cats.effect.Sync
import cats.implicits._
import io.github.llfrometa89.application.dto.CreateAccount.AccountRequest
import io.github.llfrometa89.application.services.AccountService
import io.github.llfrometa89.http.controllers._
import io.github.llfrometa89.http.core.Controller
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class AccountController[F[_]: Sync: AccountService] extends Controller[F] {

  def routes: HttpRoutes[F] = {

    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes.of[F] {
      case POST -> Root / ACCOUNTS =>
        for {
          account <- AccountService[F].open(AccountRequest("Livan Frometa", Some(200)))
          resp    <- Ok(account)
        } yield resp

      case GET -> Root / ACCOUNTS =>
        val accounts = AccountService[F].balance
        Ok(s"...............>>>accounts=$accounts")
    }
  }
}
