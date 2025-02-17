lazy val root = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    version                := "0.1",
    scalaVersion           := "2.13.12",
    TaskKey[Unit]("createSass") := {
      val sassToWrite = """.print-link {
                          |  display: none;
                          |}""".stripMargin
      IO.append(new File("src/main/assets/stylesheets/print.scss"), sassToWrite)
    }
  )
