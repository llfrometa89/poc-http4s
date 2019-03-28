package io.github.llfrometa89.application.services

import cats.Monad
import cats.implicits._
import io.github.llfrometa89.application.converters.AccountConverter
import io.github.llfrometa89.application.dto.CreateAccount
import io.github.llfrometa89.application.dto.Transfer
import io.github.llfrometa89.domain.model.Savings
import io.github.llfrometa89.domain.services.AccountService
import io.github.llfrometa89.domain.utils._

object AccountApplicationService {

  def open[F[_]: Monad: AccountService](req: CreateAccount.Request)(
      implicit G: Generator[String],
      mailerWA: Mailer[F, WelcomeAccountEmail.type]): F[CreateAccount.Response] =
    for {
      accountNo <- G.generate.pure[F]
      account   <- AccountService[F].open(accountNo, req.name, req.email, req.amount, Savings)
      _         <- AccountService[F].open("1234", req.name, req.email, req.amount, Savings)
      _         <- AccountService[F].open("9876", req.name, req.email, req.amount, Savings)
      _         <- mailerWA.send(req.email, Map("name" -> req.name))
    } yield AccountConverter.toDTO(account)

  def transfer[F[_]: Monad: AccountService](req: Transfer.Request)(
      implicit mailerST: Mailer[F, SenderTransferEmail.type],
      mailerRT: Mailer[F, ReceiverTransferEmail.type]): F[Transfer.Response] =
    for {
      result <- AccountService[F].transfer(req.senderAccountNo, req.receiverAccountNo, req.amount)
      (senderAccount, receiverAccount) = result
      _ <- mailerST.send(senderAccount.email, Map("name"   -> senderAccount.name, "amount"   -> req.amount.toString()))
      _ <- mailerRT.send(receiverAccount.email, Map("name" -> receiverAccount.name, "amount" -> req.amount.toString()))
    } yield "Success transfer"
}
