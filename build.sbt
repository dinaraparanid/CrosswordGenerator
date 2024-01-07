ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "CrosswordGenerator",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.10.0",
    libraryDependencies += "dev.zio" %% "zio" % "2.1-RC1",
    libraryDependencies += "com.formdev" % "flatlaf-intellij-themes" % "3.2.5",
  )
