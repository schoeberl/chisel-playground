package swhelp

import chisel3._
import chisel3.util._

class UseFsm extends Module {

  // Why are capital words wrong for the Enum?
  // Scala is not case sensitive!
  // val Aab :: ddd :: xxx :: Nil = Enum(3)

  // (current: UInt) => // we could pass the current state this way, but
  // this is not very elegant
  // Maybe we can use that magic thing of default values for functions?
  // This is probably too much magic.

  val idle :: state1 :: state2 :: Nil = Enum(3)

  fsm(idle) {
    state(idle) {
      // do something in this state
      // decide on next state with a condition and the next state
      next(1.U < 2.U, state1)
      next(1.U === 2.U, state2)
    }
    state(state1) {

    }
    state(state2) {

    }
  }
}
