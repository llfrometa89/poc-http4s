package io.github.llfrometa89.domain.gateways

trait MailerGateway[F[_]] {

  def send(template: String, email: String, variables: Map[String, String]): F[Unit]
}
