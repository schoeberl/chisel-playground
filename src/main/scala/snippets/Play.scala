/*
 * Copyright: 2015, Technical University of Denmark, DTU Compute
 * Author: Martin Schoeberl (martin@jopdesign.com)
 * License: Simplified BSD License
 * 
 * Code snippets for Chisel slides.
 * Does not perform any useful function.
 * 
 */

package snippets

import scala.io.Source._
import Chisel._
// this is Chisel 2 syntax by importing Chisel._ instead of chisel3._
// import chisel3.iotesters.PeekPokeTester

// TODO: this code should all be changed to real Chisel 3 or simply dropped

object Helper {

  // this example could probably be improved
  def fileRead(fileName: String): Vec[UInt] = {
    val source = fromFile(fileName)
    val byteArray = source.map(_.toByte).toArray
    source.close()
    val arr = new Array[UInt](byteArray.length)
    for (i <- 0 until byteArray.length) {
      arr(i) = byteArray(i).asUInt()
    }
    val rom = Vec[UInt](arr)
    rom
  }
}

class AluFields extends Bundle {
  val function = UInt(2)
  val inputA = UInt(8)
  val inputB = UInt(8)
  val result = UInt(8)
}

class AluIO extends Bundle {
  val function = UInt(INPUT, 2)
  val inputA = UInt(INPUT, 8)
  val inputB = UInt(INPUT, 8)
  val result = UInt(OUTPUT, 8)
}

class Channel extends Bundle {
  val data = UInt(INPUT, 32)
  val ready = Bool(OUTPUT)
  val valid = Bool(INPUT)
}

class ParamChannel(n: Int) extends Bundle {
  val data = UInt(INPUT, n)
  val ready = Bool(OUTPUT)
  val valid = Bool(INPUT)
}

class ChannelUsage extends Bundle {
  val input = new Channel()
  val output = new Channel().flip()
}

class AluOp extends Bundle {
  val op = UInt(width = 4)
}

class DecodeExecute extends Bundle {
  val rs1 = UInt(width = 32)
  val rs2 = UInt(width = 32)
  val immVal = UInt(width = 32)
  val aluOp = new AluOp()
}

class ExecuteMemory extends Bundle {
  val abc = new Bool()
}

class ExecuteIO extends Bundle {
  val dec = new DecodeExecute().asInput
  val mem = new ExecuteMemory().asOutput
}

class Adder extends Module {
  val io = IO(new Bundle {
    val a = UInt(INPUT, 4)
    val b = UInt(INPUT, 4)
    val result = UInt(OUTPUT, 4)
  })

  val addVal = io.a + io.b
  io.result := addVal
}

object Adder {
  def apply(a: UInt, b: UInt) = {
    val adder = Module(new Adder)
    adder.io.a := a
    adder.io.b := b
    adder.io.result
  }

}

class UseAdder extends Module {
  val io = IO(new Bundle {})

  val x = UInt(3, 4)
  val y = UInt(4, 4)

  // classic instantiation
  val adder = Module(new Adder)
  adder.io.a := x
  adder.io.b := y
  val result = adder.io.result

  // hide in a function
  def add(x: UInt, y: UInt): UInt = {
    val adder = Module(new Adder)
    adder.io.a := x
    adder.io.b := y
    adder.io.result
  }

  val res1 = add(x, y)

  // better use the factory method
  val myAdder = Adder(x, y)

}

class ParamAdder(n: Int) extends Module {
  val io = IO(new Bundle {
    val a = UInt(INPUT, n)
    val b = UInt(INPUT, n)
    val result = UInt(OUTPUT, n)
  })

  val addVal = io.a + io.b
  io.result := addVal
}

class Decode extends Module {
  val io = IO(new Bundle {
    val toExe = new DecodeExecute().asOutput
  })
}

class Execute extends Module {
  val io = IO(new ExecuteIO())
}

class Memory extends Module {
  val io = IO(new Bundle {
    val fromExe = new ExecuteMemory().asInput
  })
}

class Count extends Module {
  val io = IO(new Bundle {
    val cnt = UInt(OUTPUT, 8)
  })

  val cntReg = Reg(init = UInt(0, 8))

  //cntReg := Mux(cntReg === UInt(100), UInt(0), cntReg + UInt(1))

  cntReg := cntReg + UInt(1)
  when(cntReg === UInt(100)) {
    cntReg := UInt(0)
  }

  io.cnt := cntReg
}

class CPU extends Module {
  val io = IO(new Bundle {
    val leds = UInt(OUTPUT, 4)
  })

  val dec = Module(new Decode())
  val exe = Module(new Execute())
  val mem = Module(new Memory())

  // TODO: here we run into different bundles handling between Chisel 2 and 3, to be explored
//  dec.io <> exe.io
//  mem.io <> exe.io

  val adder = Module(new Adder())

  val ina = Wire(UInt(width = 4))
  val inb = Wire(UInt(width = 4))

  adder.io.a := ina
  adder.io.b := inb
  val result = adder.io.result

  val a = UInt(1, width = 8)
  val b = UInt(0, width = 8)
  val d = UInt(3, width = 8)

  val cond = a =/= b

  val c = Mux(cond, a, b)

  val condition = cond
  val trueVal = a
  val falseVal = b

  val selection = Mux(cond, trueVal, falseVal)

  // (a | b) & ~(c ^ d)

  val c1 = Bool(true)
  val c2 = Bool(false)
  val c3 = Bool(false)

  val v = Wire(UInt(5))
  when(condition) {
    v := UInt(0)
  }

  when(c1) {
    v := UInt(1)
  }
  when(c2) {
    v := UInt(2)
  }

  when(c1) {
    v := UInt(1)
  }
    .elsewhen(c2) {
      v := UInt(2)
    }
    .otherwise {
      v := UInt(3)
    }

  // Where is here the default assignment?
  // Does this work only in the compatibility version or also in Chisel 3?
  val latch = Wire(UInt(width = 5))
  when(cond) {
    latch := UInt(3)
  }

  def addSub(add: Bool, a: UInt, b: UInt) =
    Mux(add, a + b, a - b)

  val res = addSub(cond, a, b)

  def rising(d: Bool) = d && !Reg(next = d)

  val edge = rising(cond)

  val risingEdge = d & !Reg(next = d)

  def counter(n: UInt) = {

    val cntReg = Reg(init = UInt(0, 8))

    cntReg := cntReg + UInt(1)
    when(cntReg === n) {
      cntReg := UInt(0)
    }
    cntReg
  }

  val conter100 = counter(UInt(100))

  // was the old way to declare a Vec
  // val myVec = Vec.fill(3) { SInt(width = 10) }
  val myVec = Wire(Vec(3, SInt(width = 10)))
  val y = myVec(2)
  myVec(0) := SInt(-3)

  // A register file
  val vecReg = Reg(Vec(32, SInt(width = 32)))

  val ch32 = new ParamChannel(32)
  val add8 = Module(new ParamAdder(8))

  val inVal = Wire(UInt(width = 8))
  inVal := 42.U

  val shiftReg = Reg(init = UInt(0, 8))

  // The following assignment throw FIRRTL out
  // This should be legal Chisel code (it was in Chisel 2!)
  // TODO: file an issue
  // shiftReg(0) := inVal

  for (i <- 1 until 8) {
    //shiftReg(i) := shiftReg(i - 1)
  }

  val useA = false

  class Base extends Module {
    val io = new Bundle {}
  } // following breaks in Chisel 3 compatibility { val io = new Bundle() }
  class VariantA extends Base {}

  class VariantB extends Base {}

  val m = if (useA) Module(new VariantA())
  else Module(new VariantB())
}

/**
  * TODO: fix or drop, this has a runtime exception in Chisel 3 missing probably a Wire()
  */
class Play(size: Int) extends Module {
  val io = IO(new Bundle {
    val out = UInt(OUTPUT, size)
    val a = UInt(INPUT, 4)
    val b = UInt(INPUT, 4)
    val c = UInt(INPUT, 4)
    val d = UInt(INPUT, 4)
    val result = UInt(OUTPUT, 4)
  })

  val r1 = Reg(init = UInt(0, size))
  r1 := r1 + UInt(1)

  val nextVal = r1
  val r = Reg(next = nextVal)

  val initReg = Reg(init = UInt(0, 8))
  initReg := initReg + UInt(1)

  printf("Counting %x\n", r1)
  //  println()

  val a = io.a
  val b = io.b
  val c = io.c
  val d = io.d

  val addVal = a + b
  val orVal = a | b
  val boolVal = a >= b

  def adder(v1: UInt, v2: UInt) = v1 + v2

//  val add1 = adder(a, b)
//  val add2 = adder(c, d)

  val cpu = Module(new CPU())

//  val cores = new Array[Module](32)
//  for (j <- 0 until 32)
//    cores(j) = Module(new CPU())
//
//  io.out := r1
}

/*
/**
  * Test the counter by printing out the value at each clock cycle.
  */
class PlayTester(c: Play) extends PeekPokeTester(c) {

  //  step(10)
  for (i <- 0 until 5) {
    println(i.toString)
    println(peek(c.io.out).toString)
    step(1)
  }

  for (i <- 0 until 5) {
    println(i.toString)
  }
}

 */

/**
  * Create a counter and a tester.
  */
/*
object PlayTester {
  def main(args: Array[String]): Unit = {

    chisel3.iotesters.Driver.execute(Array("--target-dir", "generated"),
      () => new Play(4)) {
      f => new PlayTester(f)
    }
  }
}

class AdderTester(dut: Adder) extends PeekPokeTester(dut) {

  // Set input values
  poke(dut.io.a, 3)
  poke(dut.io.b, 4)
  // Execute one iteration
  step(1)
  // Print the result
  val res = peek(dut.io.result)
  println(res.toString())

  // Or compare against expected value
  expect(dut.io.result, 7)
}

 */

/**
  * Create a counter and a tester.
  */
/*
object AdderTester {
  def main(args: Array[String]): Unit = {

    chisel3.iotesters.Driver.execute(Array("--target-dir", "generated"),
      () => new Adder()) {
      f => new AdderTester(f)
    }
  }
}

 */

// A simple class
class Example {
  // A field, initialized in the constructor
  var n = 0

  // A setter method
  def set(v: Integer) {
    n = v
  }

  // Another method
  def print() {
    println(n)
  }

  def foo() {

    // Value is a constant
    val i = 0
    // No new assignment; will not compile
    //i = 3

    // Variable can change the value
    var v = "Hello"
    v = "Hello World"

    // Type usually inferred, but can be declared
    var s: String = "abc"
  }

  def bar() {

    // Loops from 0 to 9
    // Automatically creates loop value i
    for (i <- 0 until 10) {
      println(i)
    }

    for (i <- 0 until 10) {
      if (i % 2 == 0) {
        println(i + " is even")
      } else {
        println(i + " is odd")
      }
    }

    // An integer array with 10 elements
    val numbers = new Array[Integer](10)
    for (i <- 0 until numbers.length) {
      numbers(i) = i * 10
    }
    println(numbers(9))

    // List of integers
    val list = List(1, 2, 3)
    println(list)
    // Different form of list construction
    val listenum = 'a' :: 'b' :: 'c' :: Nil
    println(listenum)

  }
}

// A singleton object

object Example {

  // The start of a Scala program
  def main(args: Array[String]): Unit = {

    val e = new Example()
    e.print()
    e.set(42)
    e.print()

    e.bar()
  }
}
