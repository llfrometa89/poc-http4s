package io.github.llfrometa89.application.services

import cats.Monad
import cats.implicits._
import io.github.llfrometa89.application.dto.Transfer
import io.github.llfrometa89.domain.enums.EmailKey
import io.github.llfrometa89.domain.gateways.MailerGateway
import io.github.llfrometa89.domain.services.AccountService

class TransferMoneyService[F[_]: Monad](accountService: AccountService[F], mailerGateway: MailerGateway[F]) {

  def transfer(req: Transfer.Request): F[Transfer.Response] =
    for {
      result <- accountService.transfer(req.senderAccountNo, req.receiverAccountNo, req.amount)
      (senderAccount, receiverAccount) = result
      _ <- mailerGateway.send(
        template = "receiver-transfer-email",
        senderAccount.email,
        Map(EmailKey.NAME -> senderAccount.name, EmailKey.AMOUNT -> req.amount.toString()))
      _ <- mailerGateway.send(
        template = "sender-transfer-email",
        receiverAccount.email,
        Map(EmailKey.NAME -> receiverAccount.name, EmailKey.AMOUNT -> req.amount.toString()))
    } yield "Success transfer"
}
