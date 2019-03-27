package io.github.llfrometa89.application.converters

import io.github.llfrometa89.application.dto.CreateAccount.AccountResponse
import io.github.llfrometa89.domain.model.Account

object AccountConverter {

  def toDTO(account: Account): AccountResponse = {
    AccountResponse(account.no)
  }

}
