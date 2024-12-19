lazy val root = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    version                := "0.1",
    scalaVersion           := "2.13.12",
    TaskKey[Unit]("checkApplicationCss") := {
      val compiledCSS = IO.read((Assets / WebKeys.public).value / "stylesheets" / "application.css").strip()
      val expectedCSS = ""
      if (compiledCSS != expectedCSS) {
        sys.error(s"compiled CSS did not match the expected CSS\n\n$compiledCSS\n\n--------\n\n$expectedCSS")
      }
    },
    TaskKey[Unit]("checkPartialCss") := {
      val compiledCSS = IO.read((Assets / WebKeys.public).value / "stylesheets" / "include-partial.css").strip()
      val expectedCSS = ""
      if (compiledCSS != expectedCSS) {
        sys.error(s"compiled CSS did not match the expected CSS\n\n$compiledCSS\n\n--------\n\n$expectedCSS")
      }
    }
  )
