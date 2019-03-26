package io.github.llfrometa89.http.controllers.v1
import cats.effect.Sync
import io.github.llfrometa89.http.core.Controller
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object AccountController extends Controller {

  def findBy[F[_]: Sync]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name => Ok(s"...............>>>$name")
    }
  }

  def routes[F[_]: Sync]: HttpRoutes[F] = findBy
}
