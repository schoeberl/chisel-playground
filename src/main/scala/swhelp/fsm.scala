package swhelp

import chisel3._
import chisel3.util._

object state {
  def apply(start: UInt)(block:  => Unit) = {
    // how to access the state register?
    block
  }
}

object next {
  def apply(cond: Bool, next: UInt) = {
    // how to access the state register?
    // we could
  }
}

object fsm {
  def apply(start: UInt)(foo: => Unit) = {
    val stateReg = RegInit(start)
    foo
  }
}
