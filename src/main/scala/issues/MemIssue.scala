package issues

import chisel3._

class MemIssue extends Module {
  val io = IO(new Bundle {
    val din = Input(UInt(16.W))
    val dout = Output(UInt(16.W))
    val wrAddr, rdAddr = Input(UInt(4.W))
    val write = Input(Bool())
  })

  val cntReg = RegInit(0.U(2.W))
  cntReg := cntReg + 1.U

  val mem = SyncReadMem(4, UInt(16.W))
  // Using a Wire is the workaround for the bug
  // val data = Wire(UInt())
  val data = mem.read(io.rdAddr)
  when (io.write) {
    mem.write(io.wrAddr, io.din)
  }
  when(cntReg === 3.U) {
    data := 3.U
  }
  io.dout := data
}
