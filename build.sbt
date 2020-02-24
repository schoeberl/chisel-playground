
scalaVersion := "2.11.12"

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

 libraryDependencies += "edu.berkeley.cs" %% "chisel3" % "3.1.8"
 libraryDependencies += "edu.berkeley.cs" %% "chisel-iotesters" % "1.2.10"

// libraryDependencies += "edu.berkeley.cs" %% "chisel" % "2.2.38"
// libraryDependencies += "edu.berkeley.cs" %% "chisel-iotesters" % "1.2.2"
