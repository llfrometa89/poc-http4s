package io.github.llfrometa89.interpreters
import cats.effect.Sync
import io.github.llfrometa89.domain.utils.{Mailer, ReceiverTransferEmail, SenderTransferEmail, WelcomeAccountEmail}

trait MailerInstances
    extends WelcomeAccountMailerInstances
    with SenderTransferMailerInstances
    with ReceiverTransferMailerInstances

trait WelcomeAccountMailerInstances {

  implicit def instanceWA[F[_]: Sync] = new Mailer[F, WelcomeAccountEmail.type] {
    def send(email: String, variables: Map[String, String]): F[Unit] =
      Sync[F].delay(println(s"--->>> Welcome Account email =$email sent"))
  }
}

trait SenderTransferMailerInstances {

  implicit def instanceST[F[_]: Sync] = new Mailer[F, SenderTransferEmail.type] {
    def send(email: String, variables: Map[String, String]): F[Unit] =
      Sync[F].delay(println(s"--->>> Sender Transfer email =$email sent"))
  }
}

trait ReceiverTransferMailerInstances {

  implicit def instanceRT[F[_]: Sync] = new Mailer[F, ReceiverTransferEmail.type] {
    def send(email: String, variables: Map[String, String]): F[Unit] =
      Sync[F].delay(println(s"--->>> Receiver Transfer email =$email sent"))
  }
}
