resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2")
resolvers += Resolver.url("HMRC-open-artefacts-ivy2", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(
  Resolver.ivyStylePatterns
)

// NOTE adding sbt-sassify before sbt-sass-compiler here resulted in a
// binary incompatibility error, but we didn't get that in tests with
// the plugin added to real services, so might be related to how we are
// testing or to how sparse our test examples are and wouldn't be
// something that would be likely to occur in practice for consumers.
//
// addSbtPlugin("io.github.irundaia" % "sbt-sassify" % "1.5.2")

sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("uk.gov.hmrc" % "sbt-sass-compiler" % x)
  case _       => sys.error("""|The system property 'plugin.version' is not defined.
                         |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}

addSbtPlugin("io.github.irundaia" % "sbt-sassify" % "1.5.2")
