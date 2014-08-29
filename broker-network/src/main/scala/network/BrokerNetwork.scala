
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

/**
 * todo
 */
object BrokerNetwork extends App {


  setupBrokerChild("tcp://localhost:61718", "broker2")
  setupBrokerChild("tcp://localhost:61719", "broker3")
  // setupBroker1AndSendMessage(None)
  setupBroker1AndSendMessage(Some(URI.create("static://(tcp://localhost:61718,tcp://localhost:61719)")))

  def setupBrokerChild(connectorURL: String, name: String) {
    val brokerService = new BrokerService
    brokerService.setUseJmx(true)

    brokerService.addConnector(connectorURL)

    brokerService.setDataDirectory(Files.createTempDirectory(name + "Data").toString)
    brokerService.setBrokerName(name)
    brokerService.start()

    val factory = new ActiveMQConnectionFactory(connectorURL)
    
    future {
      for (connection <- managed(factory.createConnection());
           session <- managed(connection.createSession(false, Session.AUTO_ACKNOWLEDGE));
           consumer <- managed(session.createConsumer(session.createQueue("demoQueue")))) {


        println(s"name is listening (10 sec time/out)")
        connection.start()


        demo.Helper.receiveText(consumer, 10 seconds) {
          m => println(s"queue on $name received ${m.getText}")
        }

      }
      println(s"Stopping broker $name")
      brokerService.stop()
    }
  }


  def setupBroker1AndSendMessage(bridge: Option[URI] = None) {
    val brokerService = new BrokerService
    brokerService.setUseJmx(true)
    brokerService.addConnector("tcp://localhost:61717")
    brokerService.setDataDirectory(Files.createTempDirectory("broker1Data").toString)
    brokerService.setBrokerName("broker1")

    bridge.map(u => brokerService.addNetworkConnector(u))

    brokerService.start()

    val factory = new ActiveMQConnectionFactory("tcp://localhost:61717")
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
    brokerService.stop()
    println(s"Stopped broker1")

  }


}
