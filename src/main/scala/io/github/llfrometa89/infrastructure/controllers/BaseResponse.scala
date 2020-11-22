package io.github.llfrometa89.infrastructure.controllers

case class BaseResponse[T, M](data: T, messages: Option[M])

object BaseResponse {
  def apply[T, M](data: T): BaseResponse[T, M] = BaseResponse(data, None)
}

case class Messages(errors: List[String], warning: List[String])
