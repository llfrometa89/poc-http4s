package io.github.llfrometa89.domain.utils

import scala.util.Random

trait Generator[V] {
  def generate: V
}

trait GeneratorInstances {

  implicit val accountNoGenerator: Generator[String] = new Generator[String] {
    override def generate: String = new Random().nextInt(10000).toString
  }

}

object GeneratorInstances extends GeneratorInstances
