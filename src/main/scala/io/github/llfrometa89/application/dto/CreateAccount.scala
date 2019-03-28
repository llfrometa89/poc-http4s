package io.github.llfrometa89.application.dto
import cats.Applicative
import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.github.llfrometa89.domain.model.Amount
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

object CreateAccount {

  case class AccountRequest(name: String, email: String, amount: Option[Amount])
  object AccountRequest {
    implicit val accountRequestDecoder: Decoder[AccountRequest] = deriveDecoder[AccountRequest]
    implicit def accountRequestEntityDecoder[F[_]: Sync]: EntityDecoder[F, AccountRequest] = jsonOf

    implicit val accountRequestEncoder: Encoder[AccountRequest] = deriveEncoder[AccountRequest]
    implicit def accountRequestEntityEncoder[F[_]: Applicative]: EntityEncoder[F, AccountRequest] = jsonEncoderOf
  }

  case class AccountResponse(no: String)
  object AccountResponse {
    implicit val accountResponseDecoder: Decoder[AccountResponse] = deriveDecoder[AccountResponse]
    implicit def accountResponseEntityDecoder[F[_]: Sync]: EntityDecoder[F, AccountResponse] = jsonOf

    implicit val accountResponseEncoder: Encoder[AccountResponse] = deriveEncoder[AccountResponse]
    implicit def accountResponseEntityEncoder[F[_]: Applicative]: EntityEncoder[F, AccountResponse] = jsonEncoderOf
  }

  type Request  = AccountRequest
  type Response = AccountResponse
}
