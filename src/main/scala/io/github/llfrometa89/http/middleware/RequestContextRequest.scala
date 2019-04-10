package io.github.llfrometa89.http.middleware
import cats.Functor
import cats.data.Kleisli
import org.http4s.{Request, Response}
import cats._
import cats.data._
import cats.implicits._
import org.http4s.server.Middleware

//class RequestContextMiddleware {}

case class RequestContext(userId: String, clientId: String, correlationId: String)

final case class RequestContextRequest[F[_], A](authInfo: A, req: Request[F])

object RequestContextRequest {
  def apply[F[_]: Functor, T](getUser: Request[F] => F[T]): Kleisli[F, Request[F], RequestContextRequest[F, T]] =
    Kleisli(request => getUser(request).map(user => RequestContextRequest(user, request)))
}

object RequestContext {

  type RequestContextMiddleware[F[_], T] =
    Middleware[OptionT[F, ?], RequestContextRequest[F, T], Response[F], Request[F], Response[F]]

  object as {
    def unapply[F[_], A](ar: RequestContextRequest[F, A]): Option[(Request[F], A)] =
      Some(ar.req -> ar.authInfo)
  }
}
