

autoScalaLibrary := true
managedScalaInstance := false

lazy val commonSettings = Seq(
  organization := "max.feldman",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.4"
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "InfraModule",
    libraryDependencies ++= Seq("org.scalaj" %% "scalaj-http" % "2.3.0")
  )

unmanagedBase in Compile := baseDirectory.value / "lib"

exportJars := true