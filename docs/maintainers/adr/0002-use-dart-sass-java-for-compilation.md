# Use dart-sass-java for Sass compilation using Dart Sass

* Status: accepted
* Date: 2024-01-06

Technical Story: [PLATUI-3423](https://jira.tools.tax.service.gov.uk/browse/PLATUI-3423)

## Context and Problem Statement

There are numerous different tools and libraries that will perform the compilation of Sass to CSS using Dart Sass. These
are documented in the [Dart Sass docs](https://sass-lang.com/dart-sass/). These includes:
* Dart Sass’s stand-alone command-line executable
* Javascript library via `npm`
* Java library `sass-embedded-host` (from [dart-sass-java](https://github.com/larsgrefer/dart-sass-java/))

PlatUI wanted to investigate the above approaches, to see which would be the best to use in our Sass compilation plugin.

## Decision Drivers

* The need to minimise additional work by service developers, in terms of both writing additional code and installing new
  tools or frameworks
* The need for any solution to work as part of the automated build pipelines
* The risk for any specific third party solution of code "rot", i.e. support being dropped outside of MDTP (as seen with
  [sbt-sassify](https://github.com/irundaia/sbt-sassify))

## Considered Options

* Option 1: Dart Sass’s stand-alone command-line executable
* Option 2: Javascript library via `npm`
* Option 3: Java library `sass-embedded-host` (from [dart-sass-java](https://github.com/larsgrefer/dart-sass-java/))

## Decision Outcome

Chosen option 3: "Java library `sass-embedded-host`"

## Pros and Cons of the Options
* Pro of `sass-embedded-host`: Java does require installation on a local development machine, but that will already be a
  prerequisite for Scala developers on MDTP. Using either `npm` or the standalone CLI executable would require additional downloads to any individual developer 
  machine to run locally. Whilst the `sbt-sass-compiler` plugin could initiate this download and installation,
  it still presents a potential risk to PlatUI in terms of increased support queries, version management of frameworks
  such as Node or `npm`.
* Pro of `sass-embedded-host`: Using the standalone CLI executable would require additional download and installation to build Jenkins, which is a risk
  that would need to be discussed and signed off by Build & Deploy, which is not the case with `dart-sass-java`.
* Con of `sass-embedded-host`: The [dart-sass-java](https://github.com/larsgrefer/dart-sass-java/) library currently only has one main maintainer. 
  However, it is being frequently updated at the time of writing this ADR. If the library drops out of maintenance,
  we have investigated the other solutions and know that they would be technically possible (see code examples on 
  [PLATUI-3423](https://jira.tools.tax.service.gov.uk/browse/PLATUI-3423)).
