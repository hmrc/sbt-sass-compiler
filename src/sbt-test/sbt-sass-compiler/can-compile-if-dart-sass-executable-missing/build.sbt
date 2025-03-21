import java.nio.file.{Files, Paths, Path}
import scala.util.Using
import de.larsgrefer.sass.embedded.SassCompilerFactory
import de.larsgrefer.sass.embedded.util.PropertyUtils
import scala.sys.process._

lazy val tmpDir = System.getProperty("java.io.tmpdir")
lazy val executableFile = Paths.get(s"${tmpDir}/de.larsgrefer.sass.embedded.connection.BundledPackageProvider/${PropertyUtils.getHostVersion()}/dart-sass/${PropertyUtils.getDartSassVersion()}/dart-sass/sass")

lazy val recreateBug = taskKey[Unit]("Delete dart-sass executable")

lazy val root = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    recreateBug     := {
      // Some users may notice the sass executable file gets quarantined (maybe) by antivirus software.
      // This `recreateBug` is to remove the executable and prove our updated code will be able to attempt to put the file back in place
      // By using the SassCompilerFactory.bundled in this test, we can see whether this bug still exists in future, without failing our tests
      if(Files.notExists(executableFile)) {
        println("sass binary does not exist, executable path in test may need updating")
        sys.exit(0)
      }
      Files.delete(executableFile)
      Using(SassCompilerFactory.bundled()) { _ =>
        println("Deleting sass binary didn't cause an error, does bug still exist?") 
        sys.exit(0)
      }
    },
    version                := "0.1",
    scalaVersion           := "2.13.12",
  )
