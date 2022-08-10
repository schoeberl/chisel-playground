package swhelp

import chisel3._

class Assign extends Module {
  val io = IO(new Bundle {
    val xD = Input(UInt(8.W))
    val xA = Input(UInt(8.W))
    val yD = Input(UInt(8.W))
    val yA = Input(UInt(8.W))
    val rA = Input(UInt(8.W))
    val out = Output(UInt(8.W))
  })

  val regs = Reg(Vec(4, UInt(8.W)))

  regs(io.xA) := io.xD
  regs(io.yA) := io.yD

  io.out := regs(io.rA)
}

object Assign extends App {
  emitVerilog(new Assign())
}
