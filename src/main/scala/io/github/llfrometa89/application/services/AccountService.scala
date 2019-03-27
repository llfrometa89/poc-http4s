package io.github.llfrometa89.application.services

import io.github.llfrometa89.application.dto.CreateAccount

trait AccountService[F[_]] {
  def open(req: CreateAccount.Request): F[CreateAccount.Response]
  def balance: F[Unit]
}

object AccountService {
  def apply[F[_]](implicit ev: AccountService[F]): AccountService[F] = ev
}
