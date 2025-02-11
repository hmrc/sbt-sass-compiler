lazy val root = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    version                := "0.1",
    scalaVersion           := "2.13.12",
    TaskKey[Unit]("rewrite") := {
      val sassToAppend = """body p {
                           |  color: green;
                           |}""".stripMargin
      IO.append(new File("src/main/assets/stylesheets/application.scss"), sassToAppend)
    },
    TaskKey[Unit]("check") := {
      val compiledCSS = IO.read((Assets / WebKeys.public).value / "stylesheets" / "application.css").strip()
      val expectedCSS = IO.read(new File("expected-compiled.css")).strip()
      if (compiledCSS != expectedCSS) {
        sys.error(s"compiled CSS did not match the expected CSS\n\n$compiledCSS\n\n--------\n\n$expectedCSS")
      }
    }
  )
