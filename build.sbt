trapExit := false
scalacOptions in Test ++= Seq("-Yrangepos")
lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "pstoltz.rockpaperscissors",
      scalaVersion := "2.12.3",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "rockpaperscissors",
    libraryDependencies += "org.specs2" %% "specs2-core" % "4.0.2" % "test"
  )