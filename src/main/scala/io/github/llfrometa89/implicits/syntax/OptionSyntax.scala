package io.github.llfrometa89.implicits.syntax

trait OptionSyntax {

  implicit class optionSyntax[A](opt: Option[A]) {
    def mapOrElse[B](fs: A => B, fn: => B): B = opt.fold[B](fn)(fs(_))
  }

}
