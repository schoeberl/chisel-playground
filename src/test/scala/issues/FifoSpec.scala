// Author: Martin Schoeberl (martin@jopdesign.com)
// License: this code is released into the public domain, see README.md and http://unlicense.org/

package issues

import chisel3._
import chiseltest._
import firrtl.AnnotationSeq
import org.scalatest.flatspec.AnyFlatSpec

/**
  * Testing FIFO queue variations
  */
object testFifo {

  private def initIO(dut: Fifo[UInt]): Unit = {
    dut.io.enq.bits.poke(0xab.U)
    dut.io.enq.valid.poke(false.B)
    dut.io.deq.ready.poke(false.B)
  }

  private def push(dut: Fifo[UInt], value: BigInt): Unit = {
    dut.io.enq.bits.poke(value.U)
    dut.io.enq.valid.poke(true.B)
    // wait for slot to become available
    while (!dut.io.enq.ready.peekBoolean) {
      dut.clock.step()
    }
    dut.clock.step()
    dut.io.enq.bits.poke(0xcd.U) // overwrite with some value
    dut.io.enq.valid.poke(false.B)
  }

  private def pop(dut: Fifo[UInt], value: BigInt): Unit = {
    // wait for value to become available
    while (!dut.io.deq.valid.peekBoolean) {
      dut.clock.step()
    }
    // check value
    dut.io.deq.valid.expect(true.B)
    dut.io.deq.bits.expect(value.U, "Value should be correct")
    // read it out
    dut.io.deq.ready.poke(true.B)
    dut.clock.step()
    dut.io.deq.ready.poke(false.B)
  }


  def apply(dut: Fifo[UInt], expectedCyclesPerWord: Int = 1): Unit = {
    // some defaults for all signals
    initIO(dut)
    dut.clock.step()
    // write two values and expect it on the deq side after some time
    push(dut, 0x123)
    push(dut, 0x456)
    dut.clock.step(12)
    dut.io.enq.ready.expect(true.B)
    pop(dut, 0x123)
    dut.io.deq.valid.expect(false.B)
    dut.clock.step()
  }
}

class FifoSpec extends AnyFlatSpec with ChiselScalatestTester {
  private val defaultOptions: AnnotationSeq = Seq(WriteVcdAnnotation, VerilatorBackendAnnotation)
  "MemFifo" should "pass" in {
    test(new MemFifo(UInt(16.W), 4)).withAnnotations(defaultOptions)(testFifo(_))
  }
}
