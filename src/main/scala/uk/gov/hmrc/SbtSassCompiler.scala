/*
 * Copyright 2024 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc

import sbt.*
import com.typesafe.sbt.web.SbtWeb
import sbt.Keys.*
import com.typesafe.sbt.web.SbtWeb.autoImport.*
import com.typesafe.sbt.web.Import.WebKeys.*
import de.larsgrefer.sass.embedded.SassCompilerFactory

import scala.collection.JavaConverters.*
import scala.util.{Failure, Success, Try, Using}
import java.nio.file.{Files, Path}

object SbtSassCompiler extends AutoPlugin {
  override def requires: Plugins      = SbtWeb
  override def trigger: PluginTrigger = AllRequirements

  object autoImport {
    val compileSass = TaskKey[Seq[File]]("compileSass", "Create .css files from .scss and .sass files.")
  }

  import autoImport.*

  override def projectSettings = Seq(
    Assets / excludeFilter                 := HiddenFileFilter || "*.sass" || "*.scss",
    Assets / managedResourceDirectories += (Assets / compileSass / resourceManaged).value,
    // define where to compile the sass into so that it can then be copied into public by SbtWeb
    Assets / compileSass / resourceManaged := webTarget.value / "sass" / "main",
    Assets / compileSass / excludeFilter   := HiddenFileFilter || "_*",
    Assets / compileSass / includeFilter   := "*.sass" || "*.scss",
    // make sure that we compile sass when assets are compiled by SbtWeb
    Assets / resourceGenerators += Assets / compileSass,
    // define how sass files should be compiled
    Assets / compileSass                   := Def
      .task {
        if (Try(Class.forName("org.irundaia.sbt.sass.SbtSassify")).isSuccess) {
          throw new RuntimeException(
            "SBT plugin conflict: sbt-sass-compiler cannot be used with sbt-sassify, remove sbt-sassify from your project/plugins.sbt"
          )
        }

        val sourceDir       = (Assets / sourceDirectory).value
        val sourcePath      = sourceDir.toPath
        val targetPath      = (Assets / compileSass / resourceManaged).value.toPath

        val allSassFilesFilter        = (Assets / compileSass / includeFilter).value -- HiddenFileFilter
        val sassAndPartialsFilesFound = sourceDir.globRecursive(allSassFilesFilter).get.filterNot(_.isDirectory)
        val sassFilesFound            = sassAndPartialsFilesFound.globRecursive(-"_*").get()

        val streamsValue = streams.value
        val logger       = streamsValue.log

        // Assets / webModules unpacks webjars to the webJarsDirectory target/web-modules/main
        val sassLoadPaths = List[java.io.File](
          (Assets / webJarsDirectory).value
        ).asJava

        // Compilation function that will be called if uncompiled Sass detected on run of `sbt assets`
        def compileSassToCssFiles(): Seq[sbt.File] = {
          Using(SassCompilerFactory.bundled()) { sassCompiler =>
            logger.info(s"sbt-sass-compiler: Sass compiling for ${sassFilesFound.length} files")
            val startInstant                                                   = System.currentTimeMillis
            val eitherCompiledCssFiles: Seq[Either[Throwable, (String, Path)]] = sassFilesFound.map { sassFile =>
              sassCompiler.setLoadPaths(sassLoadPaths) // no need to set a path of the current file as well
              Try(sassCompiler.compileFile(sassFile).getCss) match {
                case Success(compiledCss)      =>
                  val cssFile = targetPath
                    .resolve(sourcePath.relativize(sassFile.toPath))
                    .resolveSibling(sassFile.base + ".css")
                  Right((compiledCss, cssFile))
                case Failure(compilationError) => Left(compilationError)
              }
            }

            val errors: Seq[Throwable]           = eitherCompiledCssFiles.flatMap(_.left.toOption)
            val compiledCss: Seq[(String, Path)] = eitherCompiledCssFiles.flatMap(_.right.toOption)

            if (errors.nonEmpty) {
              throw new Exception(
                s"sbt-sass-compiler: ${errors.length} error(s) whilst compiling Sass files\n${errors.mkString("\n")}"
              )
            } else {
              val cssFiles: Seq[File] = compiledCss map { case (css, cssFile) =>
                IO.createDirectory(cssFile.getParent.toFile)
                IO.write(cssFile.toFile, css)
                cssFile.toFile
              }
              val endInstant          = System.currentTimeMillis

              logger.info(
                s"sbt-sass-compiler: Sass compilation done in ${endInstant - startInstant} ms. Number of CSS files generated: ${cssFiles.length}"
              )
              cssFiles
            }
          }.get
        }

        // this will recompile all Sass anytime any Sass file has changed since last run of task
        import sbt.util.CacheImplicits._
        val compileSassIfSourcesChanged: Seq[sbt.ModifiedFileInfo] => Seq[sbt.File] = {
          logger.info("sbt-sass-compiler: Checking for Sass files needing compilation")
          val existingCssFromTargetDirectory = targetPath.toFile.globRecursive("*.css").get
          Tracked.inputChanged[Seq[ModifiedFileInfo], Seq[File]](streamsValue.cacheStoreFactory.make("input")) {
            (anySassFileChanged: Boolean, _) =>
              if (anySassFileChanged || (sassFilesFound.nonEmpty && existingCssFromTargetDirectory.isEmpty)) {
                logger.info("sbt-sass-compiler: Uncompiled Sass files found")
                compileSassToCssFiles()
              } else {
                logger.info("sbt-sass-compiler: No changes to Sass files, sbt-sass-compiler skipping compilation")
                existingCssFromTargetDirectory
              }
          }
        }
        compileSassIfSourcesChanged(sassAndPartialsFilesFound.map(FileInfo.lastModified(_)))
      }
      .dependsOn(Assets / webModules)
      .value
  )

}
