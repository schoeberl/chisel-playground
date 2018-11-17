package snippets

import Chisel._
import chisel3.iotesters.PeekPokeTester

class Generics[T <: Data](gen: T) extends Module {
  val io = new Bundle {
    val dout = Output(gen.cloneType)
    // val dout = Output(gen) // this does give a Chisel runtime exception
  }

  io.dout := 42.U
}

class GenericsTester(dut: Generics[UInt]) extends PeekPokeTester(dut) {

  peek(dut.io.dout)
}

object GenericsTester extends App {

  chisel3.iotesters.Driver.execute(Array("--target-dir", "generated"),
      () => new Generics(UInt(width = 8))) {
      f => new GenericsTester(f)
  }
}