package io.github.llfrometa89.domain.utils

import scala.util.Random

trait Generator[V] {
  def generate: V
}

class AccountNoGenerator extends Generator[String] {

  def generate: String = new Random().nextInt(10000).toString
}
