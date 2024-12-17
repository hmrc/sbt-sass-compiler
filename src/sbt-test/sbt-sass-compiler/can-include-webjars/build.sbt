lazy val root = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    version                := "0.1",
    scalaVersion           := "2.13.12",
    libraryDependencies    += "uk.gov.hmrc" %% s"play-frontend-hmrc-play-30" % "11.7.0",
    resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2"),
    TaskKey[Unit]("check") := {
      val compiledCSS = IO.read((Assets / WebKeys.public).value / "stylesheets" / "application.css").strip()
      val expectedSnippet = IO.read(new File("expected-compiled-snippet.css")).strip()
      if (!compiledCSS.contains(expectedSnippet)) {
        sys.error(s"compiled CSS did not include the expected CSS\n\n$compiledCSS\n\n--------\n\n$expectedSnippet")
      }
    }
  )
