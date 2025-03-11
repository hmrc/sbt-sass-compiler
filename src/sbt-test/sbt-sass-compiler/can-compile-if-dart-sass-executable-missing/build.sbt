import java.nio.file.{Files, Paths}
import scala.sys.process._

lazy val sassEmbeddedHostVersion = "4.0.2"
lazy val tempDir        = Paths.get(s"/tmp/de.larsgrefer.sass.embedded.connection.BundledPackageProvider/${sassEmbeddedHostVersion}")
lazy val executableFile = Paths.get(s"/tmp/de.larsgrefer.sass.embedded.connection.BundledPackageProvider/${sassEmbeddedHostVersion}/dart-sass/1.83.4/dart-sass/sass")

lazy val findSassEmbedded   = taskKey[Unit]("Search for dart-sass executable")
lazy val deleteSassEmbedded = taskKey[Unit]("Delete dart-sass directory")
lazy val createSass         = taskKey[Unit]("Create a sass file to trigger rebuild")

lazy val root = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    deleteSassEmbedded     := {
      if (Files.exists(tempDir)) {
        println(s"Deleting $tempDir ...")
        s"rm -rf $tempDir".! // Works when outside of scripted sandbox
      } else {
        println(s"$tempDir does not exist")
      }
    },
    findSassEmbedded       := { 
  if (Files.exists(executableFile)) {
    println("[info] sbt-sass-compiler: dart-sass executable found.")
  } else {
    println("[error] sbt-sass-compiler: The dart-sass version may have changed - the path to sass executable might need updating.")
    throw new RuntimeException("sbt-sass-compiler: dart-sass executable not found.")
  }
    },
    version                := "0.1",
    scalaVersion           := "2.13.12",
    createSass             := {
      val sassToWrite = """.some-class {
                          |  display: none;
                          |}""".stripMargin
      IO.append(new File("src/main/assets/stylesheets/print.scss"), sassToWrite)
    }
  )
