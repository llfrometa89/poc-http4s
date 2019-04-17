package io.github.llfrometa89.http.middleware

import cats.data.{Kleisli, OptionT}
import cats.effect.Sync
import cats.implicits._
import cats.{Applicative, Functor}
import io.circe.parser._
import io.circe.{Decoder, Encoder, HCursor, Json}
import io.github.llfrometa89.http.middleware.AuthedService111.AuthedService111
import org.http4s.server.Middleware
import org.http4s.util.CaseInsensitiveString
import org.http4s.{BasicCredentials, HttpRoutes, Request, Response, Status}

case class RequestContext(platform: String)

object RequestContext {

  type RequestContextMiddleware[F[_], T] =
    Middleware[OptionT[F, ?], AuthedRequest111[F, T], Response[F], Request[F], Response[F]]

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

trait RequestContextMessages

case object RequestContextMissing extends RequestContextMessages

object RequestContextInterceptor {
  import RequestContext._

  val `X-Request-Context` = "X-Request-Context"

  type BasicAuthenticator111[F[_], A] = BasicCredentials => F[Option[A]]

  def apply[F[_]: Sync, A](): RequestContextMiddleware[F, A] =
    challenged(challenge)

  def challenge[F[_]: Applicative, A]: Kleisli[F, Request[F], Either[RequestContextMessages, AuthedRequest111[F, A]]] =
    Kleisli { req =>
      val resp = req.headers.get(CaseInsensitiveString(`X-Request-Context`)) match {
        case Some(rcValue) =>
          parse(rcValue.value)
            .flatMap(_.as[RequestContext])
            .map(AuthedRequest111(_, req))
            .asInstanceOf[Either[RequestContextMessages, AuthedRequest111[F, A]]]
        case _ => Left(RequestContextMissing)
      }
      println(s".............>>>>>>>>>>>>>>> resp = $resp")

      resp.pure[F]
    }

  def challenged[F[_], A](challenge: Kleisli[F, Request[F], Either[RequestContextMessages, AuthedRequest111[F, A]]])(
      routes: AuthedService111[A, F])(implicit F: Sync[F]): HttpRoutes[F] =
    Kleisli { req =>
      challenge
        .mapF(OptionT.liftF(_))
        .run(req)
        .flatMap {
          case Right(authedRequest) =>
            println(s".............>>>>>>>>>>>>>>> authedRequest = $authedRequest")

            routes(authedRequest)
          case Left(challenge) =>
            println(s".............>>>>>>>>>>>>>>> challenge = $challenge")
            OptionT.some(Response(Status.BadRequest))
        }
    }
}

final case class AuthedRequest111[F[_], A](authInfo: A, req: Request[F])

object AuthedRequest111 {
  def apply[F[_]: Functor, T](getUser: Request[F] => F[T]): Kleisli[F, Request[F], AuthedRequest111[F, T]] =
    Kleisli(request => getUser(request).map(user => AuthedRequest111(user, request)))
}

object AuthedService111 {

  type AuthedService111[T, F[_]] = Kleisli[OptionT[F, ?], AuthedRequest111[F, T], Response[F]]

  def apply[T, F[_]](pf: PartialFunction[AuthedRequest111[F, T], F[Response[F]]])(
      implicit F: Applicative[F]): AuthedService111[T, F] =
    Kleisli(req => pf.andThen(OptionT.liftF(_)).applyOrElse(req, Function.const(OptionT.none)))

}

trait Auth111 {
  object as111 {
    def unapply[F[_], A](ar: AuthedRequest111[F, A]): Option[(Request[F], A)] =
      Some(ar.req -> ar.authInfo)
  }
}
