package io.github.llfrometa89.http.core

import cats.effect.Sync
import org.http4s.HttpRoutes

trait Controller[F[_]] {

  def routes: HttpRoutes[F]
}
