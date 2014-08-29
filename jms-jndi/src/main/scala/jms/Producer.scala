package jms

import javax.jms.{ConnectionFactory, Destination, Session}
import javax.naming.InitialContext

import resource._


/**
 * todo
 */
object Producer extends App {

  // myJmsFactory, queue/JMSDemoQueue (see jndi.properties)
  val destinationName = "queue/JMSDemoQueue" // "dynamicQueues/FooQueue"

  for (ctx <- managed(new InitialContext())) {
    val factory = ctx.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    val destination = ctx.lookup(destinationName).asInstanceOf[Destination]

    for (connection <- managed(factory.createConnection());
         session <- managed(connection.createSession(false, Session.AUTO_ACKNOWLEDGE));
         producer <- managed(session.createProducer(destination))) {

      println("Enter message to send (type exit to quit)")

      for (message <- io.Source.stdin.getLines().takeWhile(_ != "exit")) {
        producer.send(session.createTextMessage(message))
      }

      println("exiting...")

    }
  }
}
