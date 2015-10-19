package jms

import javax.jms._
import javax.naming.InitialContext

import demo.Helper._
import org.apache.activemq.command.ActiveMQQueue
import resource._

import scala.concurrent.duration._


object Consumer extends App {

  // myJmsFactory, queue/JMSDemoQueue (see jndi.properties)

  for (ctx <- managed(new InitialContext())) {
    val factory = ctx.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    val destinationName = "queue/JMSDemoQueue" // "dynamicQueues/FooQueue"
    val queue = ctx.lookup(destinationName).asInstanceOf[ActiveMQQueue]

    println(s"Receiving from queue with physical name: ${queue.getPhysicalName} and properties ${queue.getProperties}  ")



    for (connection <- managed(factory.createConnection("admin","admin"));
         session <- managed(connection.createSession(false, Session.AUTO_ACKNOWLEDGE));
         consumer <- managed(session.createConsumer(queue))) {

      println("Starting receiving (timeout is 30s)")
      connection.start()

      receiveAndPrintText(consumer, 30 seconds)
      

    }
  }

}

