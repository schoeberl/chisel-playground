package issues

import chisel3._
import chisel3.iotesters.PeekPokeTester

class Shift extends Module {
  val io = IO(new Bundle{ val data = Output(UInt(8.W))})

  io.data := 0.U

// val a = (1.U << 1).asUInt()
  val a = 1.U << 1
  val b = 3.U
  println(a, b)

  // IntelliJ editor complains on 'a': Type mismatch, expected UInt, actual: Bits
  when(b >= a) {
    io.data := 1.U
  }

  printf("io.data=%d\n", io.data)
}

class ShiftTester (dut: Shift) extends PeekPokeTester(dut) {

  step(2)
}

// IntelliJ is sometimes confused when using class and object with the same name (and compiling it in the terminal?)
object Shift extends App {
  // chisel3.Driver.execute(Array("--target-dir", "generated"), () => new Shift())
  chisel3.iotesters.Driver.execute(Array("--target-dir", "generated"),
    () => new Shift()) {
    f => new ShiftTester(f)
  }
}
