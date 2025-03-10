import java.nio.file.{Files, Paths}
import scala.sys.process._

lazy val sassEmbeddedHostVersion = "4.0.2"
lazy val dartSassVersion = "1.83.4"
lazy val tempDir = Paths.get(s"/tmp/de.larsgrefer.sass.embedded.connection.BundledPackageProvider/${sassEmbeddedHostVersion}/dart-sass/${dartSassVersion}/dart-sass")

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
    findSassEmbedded       := { Files.exists(tempDir) },
    version                := "0.1",
    scalaVersion           := "2.13.12",
    createSass             := {
      val sassToWrite = """.some-class {
                          |  display: none;
                          |}""".stripMargin
      IO.append(new File("src/main/assets/stylesheets/print.scss"), sassToWrite)
    }
  )
