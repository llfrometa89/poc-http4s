package io.github.llfrometa89.implicits.instances

import io.github.llfrometa89.domain.utils.GeneratorInstances
import io.github.llfrometa89.interpreters._

trait AllInstances extends AccountServiceInstances with AccountRepositoryInstances with GeneratorInstances
//    with AccountAppServiceInstances
