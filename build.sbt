name := "TCP Server"

version := "0.0.1"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-deprecation", "-feature")

resolvers += "spray" at "http://repo.spray.io/"

libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.2.3",
    "com.typesafe.akka" %% "akka-testkit" % "2.2.3"
)
