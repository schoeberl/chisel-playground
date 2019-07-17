default:
	sbt run

table-chisel:
	sbt "runMain issues.Table"

table-verilog:
	sbt "runMain issues.Table xx"

state-issue:
	sbt "runMain issues.StateTester"

clean:
	git clean -fd
