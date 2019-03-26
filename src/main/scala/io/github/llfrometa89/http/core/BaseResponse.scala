package io.github.llfrometa89.http.core

case class BaseResponse[T, M](data: T, messages: Option[M])

object BaseResponse {
  def apply[T, M](data: T): BaseResponse[T, M] = BaseResponse(data, None)
}

case class Messages(errors: List[String], warning: List[String])
