# chisel-playground
Some playground for Chisel experiments

This is a collection of small Chisel example circuits.

Author: Martin Schoeberl (martin@jopdesign.com)

## Projects

### altera

# JTAG communication

Explores the JTAG based communication with Altera's alt_jtag_atlantic component.

Not much information is publicly available, see:

 * [Tommy's version in Verilog as part of yariv](https://github.com/tommythorn/yarvi)
 * [Usage with Bluespec Verilog](https://github.com/thotypous/alterajtaguart)

For a first experiment use the nios2-terminal with the design to see an echo of
a character typed inceremented by one (type 'a' and the echo is 'b').
