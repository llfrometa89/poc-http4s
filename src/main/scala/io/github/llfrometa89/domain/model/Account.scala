package io.github.llfrometa89.domain.model

import java.util.Date
import monocle.macros.Lenses

sealed trait AccountType
case object Checking extends AccountType
case object Savings  extends AccountType

sealed trait Account {
  def no: String
  def name: String
  def email: String
  def dateOfOpen: Date = today
  def dateOfClose: Option[Date]
  def balance: Balance
}

@Lenses final case class CheckingAccount(
    no: String,
    name: String,
    email: String,
    dateOfClose: Option[Date] = None,
    balance: Balance = Balance())
    extends Account

@Lenses final case class SavingsAccount(
    no: String,
    name: String,
    email: String,
    rateOfInterest: Amount = 0,
    dateOfClose: Option[Date] = None,
    balance: Balance = Balance())
    extends Account

object Account {

  sealed abstract class AccountError(error: String) extends Exception(error)
  case class AlreadyExistAccount(no: String)        extends AccountError(s"Already existing account with no $no")
  case class NotFoundAccount(no: String)            extends AccountError(s"Not found account with no $no")

  def validate(
      no: String,
      name: String,
      email: String,
      rate: Option[BigDecimal],
      accountType: AccountType): Either[Throwable, Account] = {
    //TODO validate no, name before create Account model
    val account = (accountType, rate) match {
      case (Checking, _)                   => CheckingAccount(no, name, email)
      case (Savings, Some(rateOfInterest)) => SavingsAccount(no, name, email, rateOfInterest)
      case (Savings, _)                    => SavingsAccount(no, name, email)
    }
    Right(account) //TODO Replace for real validation result
  }

}
