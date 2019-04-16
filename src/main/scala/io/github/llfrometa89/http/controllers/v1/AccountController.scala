package io.github.llfrometa89.http.controllers.v1

import cats.effect.Sync
import cats.implicits._
import io.github.llfrometa89.application.dto.CreateAccount.AccountRequest
import io.github.llfrometa89.application.dto.Transfer.TransferRequest
import io.github.llfrometa89.application.services.AccountApplicationService
import io.github.llfrometa89.http.controllers._
import io.github.llfrometa89.implicits._
import io.github.llfrometa89.http.core.Controller
import io.github.llfrometa89.http.middleware.BasicAuth111
import io.github.llfrometa89.http.middleware.RequestContext.AuthMiddleware111
import org.http4s.{AuthedService, BasicCredentials, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import org.http4s.server.middleware.authentication.BasicAuth
import org.http4s.server.middleware.authentication.BasicAuth.BasicAuthenticator

object AccountController extends Controller {

  def routes[F[_]: Sync]: HttpRoutes[F] = {

    val dsl = new Http4sDsl[F] {}
    import dsl._

    val realm = "testrealm"

    val authStore: BasicAuthenticator[F, String] = (creds: BasicCredentials) => {
      if (creds.username == "username" && creds.password == "password")
        Sync[F].pure(Some(creds.username))
      else Sync[F].pure(None)
    }

    val basicAuth: AuthMiddleware[F, String] = BasicAuth(realm, authStore)

    def authRoutes: HttpRoutes[F] =
      basicAuth(AuthedService[String, F] {
        case GET -> Root / "protected" as user =>
          Ok(s"This page is protected using HTTP authentication; logged in as $user")
      })

    val basicAuth111: AuthMiddleware111[F, String] = BasicAuth111()

    def authRoutes111: HttpRoutes[F] =
      basicAuth111(AuthedService[String, F] {
        case GET -> Root / "protected2" as user =>
          Ok(s".........>>>> as $user")
      })

    HttpRoutes.of[F] {
      case req @ POST -> Root / ACCOUNTS =>
        for {
          accountRequest <- req.as[AccountRequest]
          account        <- AccountApplicationService.open[F](accountRequest)
          resp           <- Ok(account)
        } yield resp
      case req @ POST -> Root / ACCOUNTS / TRANSFER =>
        for {
          transferRequest <- req.as[TransferRequest]
          result          <- AccountApplicationService.transfer[F](transferRequest)
          resp            <- Ok(result)
        } yield resp
    } <+> authRoutes <+> authRoutes111
  }
}
