lazy val root = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    version                := "0.1",
    scalaVersion           := "2.13.12",
    TaskKey[Unit]("check") := {
      val compiledCSS = IO.read((Assets / WebKeys.public).value / "stylesheets" / "application.css")
      val expectedCSS = IO.read(new File("expected-compiled.css"))
      if (compiledCSS != expectedCSS) {
        sys.error("compiled CSS did not match the expected CSS")
      }
    }
  )
