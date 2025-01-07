
# sbt-sass-compiler

This is an SBT plugin to compile CSS from Sass files.

`sbt-sass-compiler` is built on [Dart Sass](https://sass-lang.com/dart-sass/). It is intended as a replacement for
[sbt-sassify](https://github.com/irundaia/sbt-sassify), which uses the deprecated [LibSass](https://sass-lang.com/libsass/)
implementation.

## When to use sbt-sass-compiler

If you are using the precompiled CSS bundled with `play-frontend-hmrc`, you do not need to use this plugin. If you have custom SASS that is fairly simple, consider whether you can achieve the same results with just CSS. 

Should you still need to provide your own custom SASS for any reason, then this plugin should be used instead of `sbt-sassify`.

## Migrating from sbt-sassify

If you are currently using the `sbt-sassify` plugin, this plugin replaces it. To add the `sbt-sass-compiler` plugin, first make sure you have removed the `sbt-sassify` plugin which will look something like this in your `project/plugins.sbt` file:

```
addSbtPlugin("io.github.irundaia" % "sbt-sassify" % "x.x.x")
```

Then add the following line to your `project/plugins.sbt` file, being sure to replace x.x.x with the version of `sbt-sass-compiler` you want to use:

```
addSbtPlugin("uk.gov.hmrc" % "sbt-sass-compiler" % "x.x.x")
```

You may need to include the ivy2 artefact resolver url in your `project/plugins.sbt`:

```
resolvers += Resolver.url("HMRC-open-artefacts-ivy2", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(
    Resolver.ivyStylePatterns
)
```

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
