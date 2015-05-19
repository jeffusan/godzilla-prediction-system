name    := "Godzilla Prediction System"
version := "1.0-SNAPSHOT"
licenses := Seq("Apache-2.0" -> url("http://opensource.org/licenses/Apache-2.0"))
scalaVersion := "2.11.6"

resolvers ++= Seq(
  "Spray Repository" at "http://repo.spray.io",
  "Apache Staging" at "https://repository.apache.org/content/repositories/staging/"
)

libraryDependencies ++= {
  val akkaVersion  = "2.3.10"
  val sprayVersion = "1.3.3"
  val sparkVersion = "1.3.0"
  Seq(
    "com.typesafe.akka"  %%  "akka-actor"             % akkaVersion,
    "io.spray"           %%  "spray-can"              % sprayVersion,
    "io.spray"           %%  "spray-routing"          % sprayVersion,
    "io.spray"           %%  "spray-json"             % "1.3.1",
    "io.spray"           %%  "spray-httpx"            % sprayVersion,
    "org.apache.spark"   %%  "spark-core"             % sparkVersion,
    "org.apache.spark"   %%  "spark-sql"              % sparkVersion,
    // WebJars (i.e. client-side) dependencies
    "org.webjars" % "requirejs" % "2.1.17",
    "org.webjars" % "requirejs-text" % "2.0.10-3",
    "org.webjars" % "react" % "0.13.1",
    "org.webjars" % "jsx-requirejs-plugin" % "0.6.0",
    "org.webjars" % "jquery" % "2.1.4",
    "org.webjars" % "react-router" % "0.13.2"
  )
}

Twirl.settings

Revolver.settings

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

fork in run := true
