// Author: Martin Schoeberl (martin@jopdesign.com)
// License: this code is released into the public domain, see README.md and http://unlicense.org/

package issues

import chisel3._
import chisel3.util._

/**
  * FIFO with memory and read and write pointers.
  * Extra shadow register to handle the one cycle latency of the synchronous memory.
  */
class MemFifo[T <: Data](gen: T, depth: Int) extends Fifo(gen: T, depth: Int) {

  def counter(depth: Int, incr: Bool): (UInt, UInt) = {
    val cntReg = RegInit(0.U(log2Ceil(depth).W))
    val nextVal = Mux(cntReg === (depth - 1).U, 0.U, cntReg + 1.U)
    when(incr) {
      cntReg := nextVal
    }
    (cntReg, nextVal)
  }

  val mem = SyncReadMem(depth, gen)

  val incrRead = WireInit(false.B)
  val incrWrite = WireInit(false.B)
  val (readPtr, nextRead) = counter(depth, incrRead)
  val (writePtr, nextWrite) = counter(depth, incrWrite)

  val emptyReg = RegInit(true.B)
  val fullReg = RegInit(false.B)

  val op = WireDefault(io.enq.valid ## 1.U)
  val doWrite = WireDefault(false.B)

  val memReadPtr = Mux(incrRead, nextRead, readPtr)
  // val data = WireDefault(0xcafe.U) //
  val data = mem.read(3.U, true.B)

  switch(op) {
    is("b00".U) {}
    is("b01".U) { // read
    }
    is("b10".U) { // write

    }
    is("b11".U) { // read and write
      when(!emptyReg) {
        fullReg := false.B
        when(fullReg) {
          emptyReg := false.B
        }.otherwise {
          emptyReg := nextRead === nextWrite
        }
        incrRead := true.B
      }
      when(!fullReg) {
        doWrite := true.B
        emptyReg := false.B
        when(emptyReg) {
          fullReg := false.B
        }.otherwise {
          fullReg := nextWrite === nextRead
        }
        incrWrite := true.B
      }
    }
  }

  mem.write(0.U, io.enq.bits)
  io.deq.bits := mem.read(3.U)

  // read during write behaviour
  val forwardReg = RegNext(io.enq.bits)
  when(doWrite && (memReadPtr === writePtr)) {
    data := forwardReg
  }

  io.enq.ready := true.B
  io.deq.valid := true.B

}
