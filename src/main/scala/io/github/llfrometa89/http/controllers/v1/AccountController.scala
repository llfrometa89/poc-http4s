package io.github.llfrometa89.http.controllers.v1

import cats.effect.Sync
import io.github.llfrometa89.domain.model.Savings
import io.github.llfrometa89.domain.repositories.AccountRepository
import io.github.llfrometa89.domain.services.AccountService
import io.github.llfrometa89.http.core.Controller
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import io.github.llfrometa89.implicits._
import io.github.llfrometa89.http.controllers._

object AccountController extends Controller {

  def routes[F[_]: Sync]: HttpRoutes[F] = {

    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes.of[F] {
      case POST -> Root / ACCOUNTS =>
        AccountService[F].open("12345", "Livan Frometa", None, Savings)
        Ok(s"...............>>>123")
      case GET -> Root / ACCOUNTS =>
        val accounts = AccountRepository[F].findAll
        Ok(s"...............>>>accounts=$accounts")
    }
  }

}
