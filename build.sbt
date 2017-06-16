name := """awsscala"""
organization := "com.taxis99"

val awsVersion = "1.11.67"

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

enablePlugins(GitVersioning)

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

scalaVersion := "2.12.2"

crossScalaVersions := Seq("2.10.6", "2.11.11", "2.12.2")

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-sqs" % awsVersion,
  "com.amazonaws" % "aws-java-sdk-sns" % awsVersion,
  "com.amazonaws" % "aws-java-sdk-s3" % awsVersion,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.mockito" % "mockito-all" % "1.10.19" % "test"
)

site.settings

site.includeScaladoc()

ghpages.settings

git.remoteRepo := "git@github.com:99taxis/awsscala.git"