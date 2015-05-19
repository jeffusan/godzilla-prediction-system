name := "GDS root project"

lazy val root = project.in(file(".")).
  aggregate(gdsJS, gdsJVM).
  settings(
    publish := {},
    publishLocal := {}
  )

lazy val gds = crossProject.in(file(".")).
  settings(
    name := "Godzilla Prediction System",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.11.6"
  ).
  jvmSettings(
    // Add JVM-specific settings here
  ).
  jsSettings(
    // Add JS-specific settings here
  )

lazy val gdsJVM = gds.jvm
lazy val gdsJS = gds.js


fork in run := true