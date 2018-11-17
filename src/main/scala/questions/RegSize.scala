/*
 * Copyright: 2016, Technical University of Denmark, DTU Compute
 * Author: Martin Schoeberl (martin@jopdesign.com)
 * License: Simplified BSD License
 * 
 * Testing register width inference according to original tutorial code.
 * 
 */

package questions

import Chisel._

/**
 * A simple, configurable counter that wraps around.
 */
class RegSize() extends Module {
  val io = new Bundle {
    val result = UInt(OUTPUT, 4)
  }

  // TODO: check of this is still relevant, probably delete
}
