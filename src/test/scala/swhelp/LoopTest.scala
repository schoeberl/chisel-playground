package swhelp

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class LoopTest extends AnyFlatSpec with ChiselScalatestTester {

  "loop" should "run" in {
    test(new UseLoop()) { dut =>
      dut.clock.step(7)
    }
  }
}
