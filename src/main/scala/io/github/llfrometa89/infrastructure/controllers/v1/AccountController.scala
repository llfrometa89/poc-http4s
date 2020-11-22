package io.github.llfrometa89.infrastructure.controllers.v1

import cats.effect.Sync
import cats.implicits._
import io.github.llfrometa89.application.dto.CreateAccount.AccountRequest
import io.github.llfrometa89.application.dto.Transfer.TransferRequest
import io.github.llfrometa89.application.services.{OpenAccountService, TransferMoneyService}
import io.github.llfrometa89.infrastructure.controllers.{routes => routeNames}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class AccountController[F[_]: Sync](
    openAccountService: OpenAccountService[F],
    transferMoneyService: TransferMoneyService[F]) {

  def routes: HttpRoutes[F] = {

    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes.of[F] {
      case req @ POST -> Root / routeNames.Accounts =>
        for {
          accountRequest <- req.as[AccountRequest]
          account        <- openAccountService.open(accountRequest)
          resp           <- Ok(account)
        } yield resp
      case req @ POST -> Root / routeNames.Accounts / routeNames.Transfer =>
        for {
          transferRequest <- req.as[TransferRequest]
          result          <- transferMoneyService.transfer(transferRequest)
          resp            <- Ok(result)
        } yield resp
    }
  }
}
