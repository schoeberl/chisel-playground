package snippets

import Chisel._

class Generics[T <: Data](gen: T) extends Module {
  val io = new Bundle {
    val dout = Output(gen.clone)
  }

  io.dout := 42.U
}

class GenericsTester(dut: Generics[UInt]) extends Tester(dut) {

  peek(dut.io.dout)
}

object GenericsTester extends App {

    chiselMainTest(Array("--compile", "--genHarness", "--test"),
      () => Module(new Generics(UInt(width = 8)))) {
      f => new GenericsTester(f)
  }
}