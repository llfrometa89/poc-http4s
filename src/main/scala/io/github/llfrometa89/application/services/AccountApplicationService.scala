package io.github.llfrometa89.application.services

import cats.Monad
import cats.implicits._
import io.github.llfrometa89.application.converters.AccountConverter
import io.github.llfrometa89.application.dto.CreateAccount
import io.github.llfrometa89.application.dto.CreateAccount.Response
import io.github.llfrometa89.domain.model.Savings
import io.github.llfrometa89.domain.services.AccountService
import io.github.llfrometa89.domain.utils.Generator

object AccountApplicationService {

  def open[F[_]: Monad: AccountService](req: CreateAccount.Request)(implicit G: Generator[String]): F[Response] =
    for {
      accountNo <- G.generate.pure[F]
      account   <- AccountService[F].open(accountNo, req.name, req.amount, Savings)
    } yield AccountConverter.toDTO(account)

  def balance[F[_]]: F[Unit] = ???
}
