package io.github.llfrometa89.application.dto
import cats.Applicative
import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.github.llfrometa89.domain.model.Amount
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

object Transfer {

  case class TransferRequest(senderAccountNo: String, receiverAccountNo: String, amount: Amount)
  object TransferRequest {
    implicit val accountRequestDecoder: Decoder[TransferRequest] = deriveDecoder[TransferRequest]
    implicit def accountRequestEntityDecoder[F[_]: Sync]: EntityDecoder[F, TransferRequest] = jsonOf

    implicit val accountRequestEncoder: Encoder[TransferRequest] = deriveEncoder[TransferRequest]
    implicit def accountRequestEntityEncoder[F[_]: Applicative]: EntityEncoder[F, TransferRequest] = jsonEncoderOf
  }

  type Request = TransferRequest
  type Response = String
}
