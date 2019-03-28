package io.github.llfrometa89.domain.utils

sealed trait MailerType
case object WelcomeAccountEmail   extends MailerType
case object SenderTransferEmail   extends MailerType
case object ReceiverTransferEmail extends MailerType

trait Mailer[F[_], MT <: MailerType] {
  def send(email: String, variables: Map[String, String]): F[Unit]
}
