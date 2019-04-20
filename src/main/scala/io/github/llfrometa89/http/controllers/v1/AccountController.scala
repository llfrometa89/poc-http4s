package io.github.llfrometa89.http.controllers.v1

import cats.effect.Sync
import cats.implicits._
import io.github.llfrometa89.application.dto.CreateAccount.AccountRequest
import io.github.llfrometa89.application.dto.Transfer.TransferRequest
import io.github.llfrometa89.application.services.AccountApplicationService
import io.github.llfrometa89.http.controllers._
import io.github.llfrometa89.http.core.Controller
import io.github.llfrometa89.http.middleware.RequestContext.RequestContextMiddleware
import io.github.llfrometa89.http.middleware.{RequestContext, RequestContextInterceptor, RequestContextService}
import io.github.llfrometa89.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object AccountController extends Controller {

  def routes[F[_]: Sync]: HttpRoutes[F] = {

    val dsl = new Http4sDsl[F] {}
    import dsl._

    val requestContext: RequestContextMiddleware[F, RequestContext] = RequestContextInterceptor()

    def requestContextRoutes: HttpRoutes[F] = requestContext(
      RequestContextService[RequestContext, F] {
        case GET -> Root / "protected2" RequestContext rc => Ok(s"this is rc = $rc")
      }
    )

    val defaultRoutes = HttpRoutes.of[F] {
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
    }

    defaultRoutes <+> requestContextRoutes // <+> authRoutes
  }
}
