package swhelp

import chisel3._

class UseLoop extends Module {
  val out = IO(Output(UInt(8.W)))

  out := 5.U

  loop(5) { (cnt: UInt) =>
    printf("using: %d ", cnt)
    out := Mux(cnt === 3.U, 1.U, 0.U)
  }

  printf("out: %d\n", out)
}
