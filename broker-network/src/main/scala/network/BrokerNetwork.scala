
package network

import java.net.URI
import java.nio.file.Files
import javax.jms.{Message, MessageListener, Session, TextMessage}

import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.broker.BrokerService

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
    val connection = factory.createConnection()
    connection.start()

    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val queue = session.createQueue("demoQueue")

    val consumer = session.createConsumer(queue)
    consumer.setMessageListener(new MessageListener {
      override def onMessage(message: Message): Unit = message match {

        case t: TextMessage => println(s"queue on $name received ${t.getText}")
        case _ => println(s"queue on $name  received non text message: $message")
      }
    })


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
    val connection = factory.createConnection()
    connection.start()

    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val producer = session.createProducer(null)
    val queue = session.createQueue("demoQueue")
    for (a <- 11 to 20)
      producer.send(queue, session.createTextMessage(s"message via broker1: #$a"))
  }


}
