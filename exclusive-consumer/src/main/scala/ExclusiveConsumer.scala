import java.util.Properties
import javax.jms._
import javax.naming.InitialContext

import resource._

import scala.annotation.tailrec

/**
 * todo
 */
object ExclusiveConsumer extends App {
  val consumerName = Option(System.getProperty("consumer.name")).get

  val jndiProps = new Properties()
  jndiProps.load(getClass.getResourceAsStream("/jndi.properties"))


  for (context <- managed(new InitialContext)) {

    val cnf = context.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]

    val connection = cnf.createConnection()
    connection.start()
    
    val xclusiveQueueName = {
      val b = StringBuilder.newBuilder
      val queue = context.lookup("xclusive").asInstanceOf[Queue]
      b.append(queue.getQueueName ).append("?consumer.exclusive=true")
      b.append(Option(System.getProperty("consumer.priority")).map(s=>"&consumer.priority="+s).getOrElse(""))
      b.toString()
    }
    println(s"Using queue name $xclusiveQueueName")


    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val xclusiveQueue  = session.createQueue(xclusiveQueueName)
    val consumer = session.createConsumer(xclusiveQueue)
    receive(consumer)
    
    @tailrec
    def receive(consumer : MessageConsumer) : Unit = {
      Option(consumer.receive(30000)) match {
        case Some(message) => println(s"Received '${message.asInstanceOf[TextMessage].getText}' by ${consumerName}")
          receive(consumer)
        case None => println("No more text message received for 30s")
      }
    }

    connection.close() // relying on it to close the other resources
  }

}
