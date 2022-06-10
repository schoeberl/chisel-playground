package swhelp

import chisel3._
import chisel3.util._

object loop {
  def apply(i: Int)(foo: (UInt) => Unit) = {
    val reg = RegInit(0.U(log2Ceil(i).W))
    val Max = (i-1).U
    reg := Mux(reg === Max, 0.U, reg + 1.U)
    println("generate a counter form 0 up to " + (i-1))
    foo(reg)
  }
}
