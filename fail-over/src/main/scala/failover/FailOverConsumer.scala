package failover

import javax.jms.{Destination, ConnectionFactory, TextMessage}
import javax.naming.InitialContext

import org.springframework.scala.jms.core.JmsTemplate
import resource._

import scala.annotation.tailrec

/**
 * todo
 */
object FailOverConsumer extends App {


  

  for (context <- managed(new InitialContext)) {
    val cnf = context.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    
    
    val factory = new org.springframework.jms.connection.SingleConnectionFactory(cnf)
    val queue = context.lookup("FailOverQueue").asInstanceOf[Destination]
    val template = new JmsTemplate(factory)
    
    

    @tailrec
    def receive() : Unit = {
      template.receive(queue) match {
        case Some(textMessage: TextMessage) => println(textMessage.getText)
          receive()
        case _ => println("No text message received")
      }
    }

    receive()
    factory.destroy()
  }
  
  
}
