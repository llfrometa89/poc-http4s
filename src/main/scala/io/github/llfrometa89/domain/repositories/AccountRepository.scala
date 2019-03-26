package io.github.llfrometa89.domain.repositories

import io.github.llfrometa89.domain.model.Account

trait AccountRepository[F[_]] {
  def findByNo(no: String): F[Option[Account]]
  def findAll: F[List[Account]]
  def save(account: Account): F[Account]
}

object AccountRepository {
  def apply[F[_]](implicit F: AccountRepository[F]): AccountRepository[F] = F
}
