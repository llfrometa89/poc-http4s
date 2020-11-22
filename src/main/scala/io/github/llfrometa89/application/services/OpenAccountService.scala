package io.github.llfrometa89.application.services

import cats.Monad
import cats.implicits._
import io.github.llfrometa89.application.converters.AccountConverter
import io.github.llfrometa89.application.dto.CreateAccount
import io.github.llfrometa89.domain.enums.EmailKey
import io.github.llfrometa89.domain.gateways.MailerGateway
import io.github.llfrometa89.domain.model.Savings
import io.github.llfrometa89.domain.services.AccountService
import io.github.llfrometa89.domain.utils._

class OpenAccountService[F[_]: Monad](
    accountService: AccountService[F],
    accountNoGenerator: Generator[String],
    mailerGateway: MailerGateway[F]) {

  def open(req: CreateAccount.Request)(
      ): F[CreateAccount.Response] =
    for {
      accountNo <- accountNoGenerator.generate.pure[F]
      account   <- accountService.open(accountNo, req.name, req.email, req.amount, Savings)
      _         <- mailerGateway.send(template = "welcome-account-email", req.email, Map(EmailKey.NAME -> req.name))
    } yield AccountConverter.toDTO(account)

}
