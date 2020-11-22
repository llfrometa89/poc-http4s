package io.github.llfrometa89.infrastructure.repositories

import cats.Applicative
import cats.implicits._
import io.github.llfrometa89.domain.model.Account
import io.github.llfrometa89.domain.repositories.AccountRepository

class AccountInMemoryRepository[F[_]: Applicative] extends AccountRepository[F] {

  def findByNo(no: String): F[Option[Account]] =
    DB.memory.get(no).pure[F]

  def save(account: Account): F[Account] = {
    DB.memory += (account.no -> account)
    account.pure[F]
  }

  def findAll: F[List[Account]] =
    DB.memory.toList.map(_._2).pure[F]
}
