package io.github.llfrometa89

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import io.github.llfrometa89.application.services.{OpenAccountService, TransferMoneyService}
import io.github.llfrometa89.domain.gateways.MailerGateway
import io.github.llfrometa89.domain.repositories.AccountRepository
import io.github.llfrometa89.domain.services.AccountService
import io.github.llfrometa89.domain.utils.AccountNoGenerator
import io.github.llfrometa89.infrastructure.controllers.WebServer
import io.github.llfrometa89.infrastructure.controllers.v1.AccountController
import io.github.llfrometa89.infrastructure.gateways.MailerSendGridGateway
import io.github.llfrometa89.infrastructure.repositories.AccountInMemoryRepository
import org.http4s.HttpRoutes

object Main extends IOApp {

  val accountNoGenerator                       = new AccountNoGenerator
  val accountRepository: AccountRepository[IO] = new AccountInMemoryRepository[IO]
  val mailerGateway: MailerGateway[IO]         = new MailerSendGridGateway[IO]
  val accountService                           = new AccountService[IO](accountRepository)
  val openAccountService                       = new OpenAccountService[IO](accountService, accountNoGenerator, mailerGateway)
  val transferMoneyService                     = new TransferMoneyService[IO](accountService, mailerGateway)
  val accountController                        = new AccountController[IO](openAccountService, transferMoneyService)
  implicit val accountRotes: HttpRoutes[IO]    = accountController.routes

  def run(args: List[String]): IO[ExitCode] = WebServer.run[IO].compile.drain.as(ExitCode.Success)

}
