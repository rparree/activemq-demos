
package network

import java.net.URI
import java.nio.file.Files
import javax.jms.Session

import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.broker.BrokerService
import resource._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._


case class BrokerInfo(uri : String, name: String)

object BrokerNetwork extends App {


  val brokerInfo1 = BrokerInfo("tcp://localhost:61717", "broker1")
  val brokerInfo2 = BrokerInfo("tcp://localhost:61718", "broker2")
  val brokerInfo3 = BrokerInfo("tcp://localhost:61719", "broker3")

  setupBroker(brokerInfo2)
  setupBroker(brokerInfo3)

  val bridge = Some(URI.create(s"static://(${brokerInfo2.uri},${brokerInfo3.uri})"))

  setupBroker(brokerInfo1, bridge)

  consume(brokerInfo2)
  consume(brokerInfo3)

  produce(brokerInfo1)

  def setupBroker(brokerInfo : BrokerInfo, bridge: Option[URI] = None) {
    val brokerService = new BrokerService
    brokerService.setUseJmx(true)

    brokerService.addConnector(brokerInfo.uri)

    brokerService.setDataDirectory(Files.createTempDirectory(brokerInfo.name + "Data").toString)
    brokerService.setBrokerName(brokerInfo.name)
   // bridge.map(u => brokerService.addNetworkConnector(u))
    bridge match {
      case Some(uri) =>brokerService.addNetworkConnector(uri)
      case None => println("setting up no bridge")
    }

    brokerService.start()


  }


  def produce(broker : BrokerInfo) {


    val factory = new ActiveMQConnectionFactory(broker.uri)
    for (connection <- managed(factory.createConnection());
         session <- managed(connection.createSession(false, Session.AUTO_ACKNOWLEDGE));
         producer <- managed(session.createProducer(null))) {

      val queue = session.createQueue("demoQueue")
      for (a <- 11 to 20)
        producer.send(queue, session.createTextMessage(s"message via broker1: #$a"))
      println(s"broker1 has sent the messages")
    }

    println(s"Stopping broker1 in 30s")
    Thread.sleep(30000)
    //brokerService.stop()
    println(s"Stopped broker1")

  }

  def consume(broker : BrokerInfo): Unit ={
    val factory = new ActiveMQConnectionFactory(broker.uri)

    future {
      for (connection <- managed(factory.createConnection());
           session <- managed(connection.createSession(false, Session.AUTO_ACKNOWLEDGE));
           consumer <- managed(session.createConsumer(session.createQueue("demoQueue")))) {


        println(s"${broker.name} is listening (10 sec time/out)")
        connection.start()


        demo.Helper.receiveText(consumer, 10 seconds) {
          m => println(s"queue on ${broker.name} received ${m.getText}")
        }

      }
      //brokerService.stop()
    }
  }




}
