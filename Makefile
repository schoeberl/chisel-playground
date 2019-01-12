default:
	sbt run

table-chisel:
	sbt "runMain issues.Table"

table-verilog:
	sbt "runMain issues.Table xx"

clean:
	git clean -fd
