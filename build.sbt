ThisBuild / organization := "uk.gov.hmrc"

lazy val `sbt-sass-compiler` = project
  .in(file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    scalaVersion       := "2.12.19",
    majorVersion       := 0,
    sbtPlugin          := true,
    isPublicArtefact   := true,
    libraryDependencies ++= Seq(
      "de.larsgrefer.sass" % "sass-embedded-host" % "3.6.0"
    ),
    scriptedLaunchOpts := {
      val homeDir = sys.props
        .get("jenkins.home")
        .orElse(sys.props.get("user.home"))
        .getOrElse("")
      scriptedLaunchOpts.value ++
        Seq(
          "-Xmx1024M",
          "-Dplugin.version=" + version.value,
          s"-Dsbt.override.build.repos=${sys.props.getOrElse("sbt.override.build.repos", "false")}",
          // Global base is overwritten with <tmp scripted>/global and can not be reconfigured
          // We have to explicitly set all the params that rely on base
          s"-Dsbt.boot.directory=${file(homeDir) / ".sbt" / "boot"}",
          s"-Dsbt.repository.config=${file(homeDir) / ".sbt" / "repositories"}"
        )
    },
    scriptedBufferLog  := false,
    addSbtPlugin("com.github.sbt" % "sbt-web" % "1.5.8")
  )
