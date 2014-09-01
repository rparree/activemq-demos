import com.edc4it.sbt.activemq.SbtActiveMQ
import sbt.Keys._
import sbt._

object ActiveMQDemos extends Build {

  import BuildSettings._
  import Dependencies._

  lazy val parent = Project(id = "activemq-demos",
    base = file("."))
    .aggregate(jmsJNDI, requestResponse, util, failOver, compositeDestination,
      exclusiveConsumer, wildcards, brokerNetwork, virtualTopics, embeddedBrokers, mqtt)
    .settings(basicSettings: _*)

  lazy val util = basicSpringScalaProject("util")

  lazy val jmsJNDI = basicProject("jms-jndi") dependsOn util
  lazy val requestResponse = basicSpringScalaProject("request-response") dependsOn util

  lazy val brokerNetwork = basicProject("broker-network") dependsOn util
    .settings(javaOptions in run += "-javaagent:../lib/jolokia-jvm-1.2.2-agent.jar=port=8777,host=localhost,user=smx,password=smx",
      fork in run := true)
  lazy val wildcards = basicSpringScalaProject("wildcards") dependsOn util
  lazy val exclusiveConsumer = basicSpringScalaProject("exclusive-consumer") dependsOn util
  lazy val compositeDestination = basicSpringScalaProject("composite-destination") dependsOn util
  lazy val failOver = basicSpringScalaProject("fail-over").dependsOn(util)
    .enablePlugins(SbtActiveMQ) // brokers configured in project's build.sbt

  lazy val virtualTopics = basicSpringScalaProject("virtual-topics") dependsOn util
  lazy val embeddedBrokers = basicSpringScalaProject("embedded-broker")
    .settings(
      libraryDependencies ++= compile(xbean, activeIO),
      libraryDependencies ++= compile(springDB: _*)

    )
    .dependsOn(util)

  lazy val mqtt = Project(id = "mqtt", base = file("mqtt"))
    .settings(basicSettings: _*)
    .settings(libraryDependencies ++= compile(mqttClient, activemq))

  def basicProject(name: String) = Project(id = name, base = file(name))
    .settings(basicSettings: _*)
    .settings(libraryDependencies ++= compile(activemq, scalaARM))


  def basicSpringScalaProject(name: String) = basicProject(name)
    .settings(libraryDependencies ++= compile(springScalaJMS: _*))


}
