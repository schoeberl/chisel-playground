/*
 * Play ground for Chisel syntax checks.
 * 
 * Author: Martin Schoeberl (martin@jopdesign.com)
 * 
 */

package snippets

import chisel3._
import chisel3.Driver

/**
 * The blinking LED component.
 */

class Hello extends Module {
  val io = IO(new Bundle {
    val iport = Input(UInt(8.W))
    val oport = Output(UInt(8.W))
  })

  io.oport := io.iport & "hf0".U
}

/**
 * An object containing a main() to generate the Verilog code.
 */
object Hello {
  def main(args: Array[String]): Unit = {
    chisel3.Driver.execute(Array("--target-dir", "generated"), () => new Hello())
  }
}
