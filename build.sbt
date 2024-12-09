ThisBuild / organization := "uk.gov.hmrc"

lazy val `sbt-sass-compiler` = project
  .in(file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    scalaVersion     := "2.12.19",
    majorVersion     := 0,
    sbtPlugin        := true,
    isPublicArtefact := true
  )
