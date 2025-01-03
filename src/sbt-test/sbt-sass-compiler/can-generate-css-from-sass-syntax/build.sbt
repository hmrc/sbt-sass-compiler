lazy val root = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    version                := "0.1",
    scalaVersion           := "2.13.12",
    TaskKey[Unit]("check") := {
      val compiledCSS = IO.read((Assets / WebKeys.public).value / "stylesheets" / "application.css").strip()
      val expectedCSS = IO.read(new File("expected-compiled.css")).strip()
      if (!compiledCSS.contains(expectedCSS)) {
        sys.error(s"compiled CSS did not include the expected CSS\n\n$compiledCSS\n\n--------\n\n$expectedCSS")
      }}
    }
  )
