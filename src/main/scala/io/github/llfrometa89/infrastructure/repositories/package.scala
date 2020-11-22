package io.github.llfrometa89.infrastructure

import io.github.llfrometa89.domain.model.Account

package object repositories {

  object DB {
    var memory = Map.empty[String, Account]
  }
}
