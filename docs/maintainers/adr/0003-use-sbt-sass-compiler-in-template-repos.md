# Use sbt-sass-compiler in template repos

* Status: accepted
* Date: 2025-02-17

Technical Story: [PLATUI-3398](https://jira.tools.tax.service.gov.uk/browse/PLATUI-3398)

## Context and Problem Statement

There are 2 repos used to create new frontend services on the Platform via the Catalogue, both of which add in `sbt-sassify`
as a plugin dependency. This ADR looks at the decision to remove this depepdency from new services, and potentially replace 
with `sbt-sass-compiler`. Frontend template repos are [init-service](https://github.com/hmrc/init-service) and
[hmrc-frontend-scaffold.g8](https://github.com/hmrc/hmrc-frontend-scaffold.g8). 

## Decision Drivers

* `sbt-sassify` is using a deprecated Sass implementation (LibSass) and we are encouraging teams to move away from it, 
  particularly due to `govuk-frontend` no longer supporting LibSass
* All new frontends from both templates are currently created with Sass compilation wiring and placeholder `.scss` files in
  place
* PlatUI have already published a blogpost asking teams to start moving to `sbt-sass-compiler`

## Considered Options

* Option 1: Do nothing
* Option 2: Remove Sass compilation wiring from template repos
* Option 3: Replace `sbt-sassify` with `sbt-sass-compiler`

## Decision Outcome

Chosen option: "Option 3: Replace `sbt-sassify` with `sbt-sass-compiler`".

## Pros and Cons of the Options
* Doing nothing would lead to new frontends being created with deprecated plugins, meaning teams would need to additional
  work as soon as they create a new service to replace the plugin
* Removing Sass compilation wiring from template repos was discussed, as not all frontends will be adding their own custom
  CSS, particularly CSS requiring Sass compilation. However, it was decided that the process to add in Sass compilation
  when a service **does** require is complex enough that we should provide wiring out-of-the-box
* Based on the above, "Option 3: Replace `sbt-sassify` with `sbt-sass-compiler`" was decided as the best option to leave
  in place wiring for new frontends to add their own CSS with Sass compilation, without using a deprecated Sass implementation.
