//package io.github.llfrometa89.interpreters
//
//import cats.effect.Sync
//import cats.implicits._
//import io.github.llfrometa89.application.converters.AccountConverter
//import io.github.llfrometa89.application.dto.CreateAccount
//import io.github.llfrometa89.application.dto.CreateAccount.Response
//import io.github.llfrometa89.application.services.{AccountService => AccountAppService}
//import io.github.llfrometa89.domain.model._
//import io.github.llfrometa89.domain.services.AccountService
//import io.github.llfrometa89.domain.utils.Generator
//
////trait AccountAppServiceInstances {
////
////  implicit def instance[F[_]: Sync: AccountService](implicit G: Generator[String]): AccountAppService[F] =
////    new AccountAppService[F] {
////      def open(req: CreateAccount.Request): F[Response] =
////        for {
////          accountNo <- G.generate.pure[F]
////          account   <- AccountService[F].open(accountNo, req.name, req.amount, Savings)
////        } yield AccountConverter.toDTO(account)
////
////      def balance: F[Unit] = ???
////    }
////}
