package issues

import chisel3._

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class MemIssueTest extends AnyFlatSpec with ChiselScalatestTester {

  "mem" should "work" in {
    test(new MemIssue()).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

      dut.io.wrAddr.poke(1.U)
      dut.io.rdAddr.poke(1.U)
      dut.io.din.poke(2.U)
      dut.io.write.poke(false.B)
      dut.clock.step(1)
      dut.io.din.poke(3.U)
      dut.io.write.poke(true.B)
      dut.clock.step(1)
      dut.io.din.poke(4.U)
      dut.io.write.poke(false.B)
      dut.clock.step(5)

    }

  }
}
