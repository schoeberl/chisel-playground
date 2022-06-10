package swhelp

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class FsmTest extends AnyFlatSpec with ChiselScalatestTester {

  "fsm" should "abc" in {
    test(new UseFsm()) { dut =>
      dut.clock.step(7)
    }

  }
}
