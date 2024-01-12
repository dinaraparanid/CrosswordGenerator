ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "CrosswordGenerator",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.10.0",
    libraryDependencies += "dev.zio" %% "zio" % "2.1-RC1",
    libraryDependencies += "dev.zio" %% "zio-streams" % "2.1-RC1",
    libraryDependencies += "com.formdev" % "flatlaf-intellij-themes" % "3.2.5",
    libraryDependencies += "com.github.pcorless.icepdf" % "icepdf-core" % "7.1.3",
    libraryDependencies += "com.github.pcorless.icepdf" % "icepdf-viewer" % "7.1.3",
    libraryDependencies += "com.itextpdf" % "itext-core" % "8.0.2",
    libraryDependencies += "com.carlosedp" %% "zio-channel" % "0.5.5"
  )
