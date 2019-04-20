package io.github.llfrometa89.http.middleware

import cats.data.{Kleisli, OptionT}
import cats.effect.Sync
import cats.implicits._
import cats.{Applicative, Functor}
import io.circe.parser._
import io.circe.{Decoder, Encoder, HCursor, Json}
import io.github.llfrometa89.http.middleware.RequestContext.RequestContextService
import org.http4s.server.Middleware
import org.http4s.util.CaseInsensitiveString
import org.http4s.{BasicCredentials, HttpRoutes, Request, Response, Status}

case class RequestContext(platform: String)

object RequestContext {

  type RequestContextService[T, F[_]] = Kleisli[OptionT[F, ?], RequestContextServiceInterRequest[F, T], Response[F]]

  type RequestContextMiddleware[F[_], T] =
    Middleware[OptionT[F, ?], RequestContextServiceInterRequest[F, T], Response[F], Request[F], Response[F]]

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

  def challenge[F[_]: Applicative, A]: Kleisli[F, Request[F], Either[RequestContextMessages, RequestContextServiceInterRequest[F, A]]] =
    Kleisli { req =>
      val resp = req.headers.get(CaseInsensitiveString(`X-Request-Context`)) match {
        case Some(rcValue) =>
          parse(rcValue.value)
            .flatMap(_.as[RequestContext])
            .map(RequestContextServiceInterRequest(_, req))
            .asInstanceOf[Either[RequestContextMessages, RequestContextServiceInterRequest[F, A]]]
        case _ => Left(RequestContextMissing)
      }

      resp.pure[F]
    }

  def challenged[F[_], A](challenge: Kleisli[F, Request[F], Either[RequestContextMessages, RequestContextServiceInterRequest[F, A]]])(
      routes: RequestContextService[A, F])(implicit F: Sync[F]): HttpRoutes[F] =
    Kleisli { req =>
      challenge
        .mapF(OptionT.liftF(_))
        .run(req)
        .flatMap {
          case Right(rcRequest) => routes(rcRequest)
          case Left(_) => OptionT.some(Response(Status.BadRequest))
        }
    }
}

final case class RequestContextServiceInterRequest[F[_], A](authInfo: A, req: Request[F])

object RequestContextServiceInterRequest {
  def apply[F[_]: Functor, T](getUser: Request[F] => F[T]): Kleisli[F, Request[F], RequestContextServiceInterRequest[F, T]] =
    Kleisli(request => getUser(request).map(user => RequestContextServiceInterRequest(user, request)))
}

object RequestContextService {

  def apply[T, F[_]](pf: PartialFunction[RequestContextServiceInterRequest[F, T], F[Response[F]]])(
      implicit F: Applicative[F]): RequestContextService[T, F] =
    Kleisli(req => pf.andThen(OptionT.liftF(_)).applyOrElse(req, Function.const(OptionT.none)))

}

trait RequestContextOps {
  object asRequestContext {
    def unapply[F[_], A](ar: RequestContextServiceInterRequest[F, A]): Option[(Request[F], A)] =
      Some(ar.req -> ar.authInfo)
  }
}
