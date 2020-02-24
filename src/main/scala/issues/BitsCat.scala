package issues



import Chisel._
import chisel3.iotesters.PeekPokeTester

class BitsCat extends Module {
  val io = IO(new Bundle{ val data = Output(UInt(width = 8))})

  io.data := Cat(1.U, Bits("b00"))
}

// class BitsCatTest (dut: BitsCat) extends Tester(dut) {
class BitsCatTest (dut: BitsCat) extends PeekPokeTester(dut) {
  println("bits: " + peek(dut.io.data))
}

object BitsCat extends App {
  chisel3.iotesters.Driver.execute(Array("--target-dir", "generated"),
    () => new BitsCat()) {
    f => new BitsCatTest(f)
  }

/*
      chiselMainTest(Array("--genHarness", "--test", "--backend", "c",
        "--compile", "--targetDir", "generated"),
        () => Module(new BitsCat())) {
        f => new BitsCatTest(f)
      }
*/
}
