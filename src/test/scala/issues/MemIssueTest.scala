package issues

import chisel3._

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class MemIssueTest extends AnyFlatSpec with ChiselScalatestTester {

  "mem" should "work" in {
    test(new MemIssue()).withAnnotations(Seq(WriteVcdAnnotation, VerilatorBackendAnnotation)) { dut =>

      dut.io.wrAddr.poke(0.U)
      dut.io.rdAddr.poke(3.U)
      dut.io.write.poke(true.B)
      for (i <- 1 until 10) {
        dut.io.din.poke(i.U)
        dut.clock.step(1)
      }

    }

  }
}
