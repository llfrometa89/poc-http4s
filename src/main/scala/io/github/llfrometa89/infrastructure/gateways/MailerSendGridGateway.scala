package io.github.llfrometa89.infrastructure.gateways

import cats.effect.Sync
import io.github.llfrometa89.domain.gateways.MailerGateway

class MailerSendGridGateway[F[_]: Sync] extends MailerGateway[F] {

  override def send(template: String, email: String, variables: Map[String, String]): F[Unit] =
    Sync[F].delay(println(s":::: template = $template / email =$email sent"))
}

//object WelcomeAccountMailerInstances {
//
//  implicit def instanceWA[F[_]: Sync] = new Mailer[F, WelcomeAccountEmail.type] {
//    def send(email: String, variables: Map[String, String]): F[Unit] =
//      Sync[F].delay(println(s"--->>> Welcome Account email =$email sent"))
//  }
//}
//
//object SenderTransferMailerInstances {
//
//  implicit def instanceST[F[_]: Sync] = new Mailer[F, SenderTransferEmail.type] {
//    def send(email: String, variables: Map[String, String]): F[Unit] =
//      Sync[F].delay(println(s"--->>> Sender Transfer email =$email sent"))
//  }
//}
//
//object ReceiverTransferMailerInstances {
//
//  implicit def instanceRT[F[_]: Sync] = new Mailer[F, ReceiverTransferEmail.type] {
//    def send(email: String, variables: Map[String, String]): F[Unit] =
//      Sync[F].delay(println(s"--->>> Receiver Transfer email =$email sent"))
//  }
//}
