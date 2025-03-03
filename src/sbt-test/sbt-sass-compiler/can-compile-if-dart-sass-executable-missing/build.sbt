import java.nio.file.{Files, Paths}
import scala.sys.process._

lazy val tempDir = Paths.get("/tmp/de.larsgrefer.sass.embedded.connection.BundledPackageProvider")

lazy val root = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    version                := "0.1",
    scalaVersion           := "2.13.12",
    TaskKey[Unit]("createSass") := {
      val sassToWrite = """.some-class {
                          |  display: none;
                          |}""".stripMargin
      IO.append(new File("src/main/assets/stylesheets/print.scss"), sassToWrite)
    }
  )

ThisBuild / commands += Command.command("deleteSassEmbedded") { state =>
  if (Files.exists(tempDir)) {
    println(s"Deleting $tempDir ...")
    s"rm -rf $tempDir".! // Works when outside of scripted sandbox
  } else {
    println(s"$tempDir does not exist")
  }
  state
}

ThisBuild / commands += Command.command("findSassEmbedded") { state =>
  Files.exists(tempDir)
  state
}
