package io.github.llfrometa89.interpreters

import cats.Applicative
import cats.implicits._
import io.github.llfrometa89.domain.model.Account
import io.github.llfrometa89.domain.repositories.AccountRepository

object DB {
  var memory = Map.empty[String, Account]
}

trait AccountRepositoryInstances {

  implicit def instanceInMemory[F[_]: Applicative]: AccountRepository[F] = new AccountRepository[F] {

    def findByNo(no: String): F[Option[Account]] =
      DB.memory.get(no).pure[F]

    def save(account: Account): F[Account] = {
      DB.memory += (account.no -> account)
      account.pure[F]
    }

    def findAll: F[List[Account]] =
      DB.memory.toList.map(_._2).pure[F]
  }
}

object AccountRepositoryInstances extends AccountRepositoryInstances
