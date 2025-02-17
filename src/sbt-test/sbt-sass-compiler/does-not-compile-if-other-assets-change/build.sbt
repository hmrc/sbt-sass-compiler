lazy val root = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    version                := "0.1",
    scalaVersion           := "2.13.12",
    TaskKey[Unit]("createjs") := {
      val jsToWrite = """console.log("Hello, World!")""".stripMargin
      IO.append(new File("src/main/assets/javascripts/application.js"), jsToWrite)
    }
  )
