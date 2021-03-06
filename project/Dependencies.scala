import sbt._

object Dependencies {


  val springVersion = "3.2.4.RELEASE"
  val activeMqVersion = "5.11.0"
  val amqVersion = "6.1.1.redhat-449"
  val fabricVersion = "1.2.0.redhat-621048"


  val resolutionRepos = Seq(
    "repository.springsource.milestone" at "http://repo.springsource.org/milestone",
    "repository.springsource.snapshot" at "http://repo.springsource.org/snapshot",
    "fusesource.releases" at "https://repository.jboss.org/nexus/content/repositories/fs-releases/",
    "fusesource.ea" at "https://repository.jboss.org/nexus/content/groups/ea/",
    "oss.sonatype.org" at "https://oss.sonatype.org/content/repositories/snapshots/"
  )
  def compile(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")
  def provided(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "provided")
  def test(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "test")
  def runtime(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "runtime")

  def container(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "container")
  val activemq = "org.apache.activemq" % "activemq-all" % activeMqVersion
  val scalaARM = "com.jsuereth" %% "scala-arm" % "1.4"
  var xbean = "org.apache.xbean" % "xbean-spring" % "3.7"
  var activeIO = "org.apache.activemq" % "activeio-core" % "3.1.4"
  var mqttClient =  "org.fusesource.mqtt-client" % "mqtt-client" % "1.10"

  // Some reusable sets 
  var springScalaJMS = Seq(
    "org.springframework.scala" %% "spring-scala" % "1.0.0.BUILD-SNAPSHOT",
    "org.springframework" % "spring-jms" % springVersion,

    "commons-pool" % "commons-pool" % "1.6"
  )
  var springDB = Seq(
    "org.springframework" % "spring-jdbc" % springVersion,
    "com.mchange" % "c3p0" % "0.9.2.1",
    "mysql" % "mysql-connector-java" % "5.1.22",
    "commons-pool" % "commons-pool" % "1.6"
  )
  
  var fabric = Seq(
//      "org.jboss.amq" % "mq-fabric" % amqVersion,
  "io.fabric8" % "fabric-groups" % fabricVersion,
    "io.fabric8" % "fabric-zookeeper" % fabricVersion
  
  )


}
