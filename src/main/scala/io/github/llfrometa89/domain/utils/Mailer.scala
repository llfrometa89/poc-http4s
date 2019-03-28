package io.github.llfrometa89.domain.utils

sealed trait MailerType
case object BalanceType extends MailerType
case object WelcomeType extends MailerType

trait Mailer[F[_]] {
  def send[MT <: MailerType](email: String, mailerType: MT, variables: Map[String, String]): F[Unit]
}
