package issues

import chisel3._

class MemIssue extends Module {
  val io = IO(new Bundle {
    val din = Input(UInt(16.W))
    val dout = Output(UInt(16.W))
    val wrAddr, rdAddr = Input(UInt(4.W))
    val write = Input(Bool())
  })

  val mem = SyncReadMem(4, UInt(16.W))
  io.dout := mem.read(io.rdAddr)
  when (io.write) {
    mem.write(io.wrAddr, io.din)
  }

}
