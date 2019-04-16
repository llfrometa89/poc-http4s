package io.github.llfrometa89.http.middleware

import cats.Applicative
import cats.data.{Kleisli, OptionT}
import cats.effect.Sync
import cats.implicits._
import io.circe.Decoder.Result
import org.http4s.headers.{`WWW-Authenticate`, Authorization}
import org.http4s.server.Middleware
import org.http4s.util.CaseInsensitiveString
import org.http4s.{AuthedRequest, AuthedService, BasicCredentials, Challenge, HttpRoutes, Request, Response, Status}
import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, Json}

case class RequestContext(platform: String)

object RequestContext {

  type AuthMiddleware111[F[_], T] =
    Middleware[OptionT[F, ?], AuthedRequest[F, T], Response[F], Request[F], Response[F]]

  implicit val encodeFoo = new Encoder[RequestContext] {
    final def apply(a: RequestContext): Json = Json.obj(
      ("platform", Json.fromString(a.platform))
    )
  }
  implicit val decodeFoo = new Decoder[RequestContext] {
    final def apply(c: HCursor): Decoder.Result[RequestContext] =
      for {
        platform <- c.downField("platform").as[String]
      } yield {
        new RequestContext(platform)
      }
  }
}

object BasicAuth111 {
  import RequestContext._

  val `X-Request-Context` = "X-Request-Context"

  type BasicAuthenticator111[F[_], A] = BasicCredentials => F[Option[A]]

  def apply[F[_]: Sync, A](): AuthMiddleware111[F, A] =
    challenged(challenge)

  def challenge[F[_]: Applicative, A]: Kleisli[F, Request[F], Either[Challenge, AuthedRequest[F, A]]] =
    Kleisli { req =>
      println(s".............>>>>>>>>>>>>>>> req = ${req.headers}")
      println(s".............>>>>>>>>>>>>>>> req = ${req.headers.get(CaseInsensitiveString(`X-Request-Context`))}")

      req.headers.get(CaseInsensitiveString(`X-Request-Context`)) match {
        case Some(rcValue) =>
          val rcObject = rcValue.value.asJson.as[RequestContext]
          println(s".............>>>>>>>>>>>>>>> rcObject.asJson = ${rcValue.value.asJson}")
          println(s".............>>>>>>>>>>>>>>> rcObject = $rcObject")

        case _ => Left(Challenge("Basic", "22222", Map.empty))
      }
//      validatePassword(validate, req).map {
//        case Some(authInfo) =>
      Right(AuthedRequest("1we", req)).asInstanceOf[Either[Challenge, AuthedRequest[F, A]]].pure[F]
//        case None =>
//          Left(Challenge("Basic", realm, Map.empty))
//      }
    }

  def challenged[F[_], A](challenge: Kleisli[F, Request[F], Either[Challenge, AuthedRequest[F, A]]])(
      @deprecatedName('service) routes: AuthedService[A, F])(implicit F: Sync[F]): HttpRoutes[F] =
    Kleisli { req =>
      challenge
        .mapF(OptionT.liftF(_))
        .run(req)
        .flatMap {
          case Right(authedRequest) =>
            routes(authedRequest)
          case Left(challenge) =>
            OptionT.some(Response(Status.Unauthorized).putHeaders(`WWW-Authenticate`(challenge)))
        }
    }

  private def validatePassword[F[_], A](validate: BasicAuthenticator111[F, A], req: Request[F])(
      implicit F: Applicative[F]): F[Option[A]] =
    req.headers.get(Authorization) match {
      case Some(Authorization(BasicCredentials(username, password))) =>
        validate(BasicCredentials(username, password))
      case _ =>
        F.pure(None)
    }
}