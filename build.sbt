ThisBuild / version := "0.0.1"

ThisBuild / scalaVersion := "2.13.8"

ThisBuild / organization := "com.47deg"

lazy val sparkVersion = "3.2.0"

lazy val root = (project in file("."))
  .aggregate(core)
  .settings(noPublishSettings, commonSettings)

lazy val core = (project in file("core"))
  .settings(
    name := "spark-testing-base",
    commonSettings,
    publishSettings,
    libraryDependencies ++= sparkDependencies ++ commonDependencies
  )

lazy val sparkDependencies = List(
  "org.apache.spark" %% "spark-core"      % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-sql"       % sparkVersion,
  "org.apache.spark" %% "spark-hive"      % sparkVersion,
  "org.apache.spark" %% "spark-catalyst"  % sparkVersion,
  "org.apache.spark" %% "spark-yarn"      % sparkVersion,
  "org.apache.spark" %% "spark-mllib"     % sparkVersion
)
  .map(_.exclude("javax.servlet", "javax.servlet-api"))
  .map(_.exclude("org.glassfish", "javax.servlet"))
  .map(_.exclude("org.eclipse.jetty.orbit", "javax.servlet"))

lazy val commonSettings = Seq(
  javacOptions ++= Seq(
    "-source",
    "11",
    "-target",
    "11"
  ),
  javaOptions ++= Seq(
    "-Xms512M",
    "-Xmx2048M",
    "-XX:MaxPermSize=2048M",
    "-XX:+CMSClassUnloadingEnabled"
  ),
  scalacOptions ++= Seq(
    "-encoding",
    "UTF-8",
    "-deprecation",
    "-unchecked",
    "-Xfatal-warnings"
  ),
  Test / parallelExecution := false,
  fork                     := true,
  coverageHighlighting     := true
)

lazy val commonDependencies = Seq(
  "org.eclipse.jetty" % "jetty-util" % "9.3.11.v20160721",
  "junit"             % "junit"      % "4.12",
  "org.scalatest"    %% "scalatest"  % "3.0.9",
  "org.scalacheck"   %% "scalacheck" % "1.15.2"
)

def excludeFromAll(items: Seq[ModuleID], group: String, artifact: String) =
  items.map(_.exclude(group, artifact))

def excludeJavaxServlet(items: Seq[ModuleID]) =
  excludeFromAll(items, "javax.servlet", "servlet-api")

lazy val publishSettings = Seq()

lazy val noPublishSettings =
  publish / skip := true
