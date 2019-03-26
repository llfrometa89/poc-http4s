package io.github.llfrometa89.domain.services

import java.util.Date
import cats.Monad
import cats.implicits._
import io.github.llfrometa89.domain.model._

trait AccountService[F[_]] {

  implicit val monad: Monad[F]

  def open(no: String, name: String, rate: Option[BigDecimal], accountType: AccountType): F[Account]
  def close(no: String, closeDate: Date): F[Account]
  def debit(no: String, amount: Amount): F[Account]
  def credit(no: String, amount: Amount): F[Account]
  def transfer(from: String, to: String, amount: Amount): F[(Account, Account)] =
    for {
      a <- debit(from, amount)
      b <- credit(to, amount)
    } yield (a, b)
}

object AccountService {
  def apply[F[_]](implicit F: AccountService[F]): AccountService[F] = F
}

object AccountMessage {
  case class AlreadyExistAccount(no: String) extends Exception(s"Already existing account with no $no")
  case class NotFoundAccount(no: String)     extends Exception(s"Not found account with no $no")
}
