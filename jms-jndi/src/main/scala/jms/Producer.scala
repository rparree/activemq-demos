package jms

import javax.naming.InitialContext
import javax.jms.{Destination, Session, ConnectionFactory}


/**
 * todo
 */
object Producer extends App {
  
  // myJmsFactory, queue/JMSDemoQueue (see jndi.properties)
  
  val ctx = new InitialContext()
  val factory= ctx.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]

  val destinationName = "queue/JMSDemoQueue" // "dynamicQueues/FooQueue"
  val destination = ctx.lookup(destinationName).asInstanceOf[Destination]
  
  

  val connection = factory.createConnection()
  val session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE)
  
  val producer = session.createProducer(destination)
  
  
  
  producer.send(session.createTextMessage("hello"))

  session.close()
  connection.close()
  ctx.close()
  
  println("Message has been sent, take a look at http://localhost:8181/activemqweb/queues.jsp")
  
  
  
  
}
