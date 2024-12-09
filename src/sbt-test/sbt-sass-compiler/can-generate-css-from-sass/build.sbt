lazy val root = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    version := "0.1",
    scalaVersion := "2.13.12",
  )
