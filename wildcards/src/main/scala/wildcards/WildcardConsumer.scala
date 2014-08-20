package wildcards

import javax.jms.{ConnectionFactory, TextMessage}
import javax.naming.InitialContext

import org.springframework.scala.jms.core.JmsTemplate
import resource._

import scala.annotation.tailrec

/**
 * todo
 */
object WildcardConsumer extends App {

  
  for (context <- managed(new InitialContext)) {
    val cnf = context.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    val template = new JmsTemplate(new org.springframework.jms.connection.SingleConnectionFactory(cnf))

    val queue = template.execute(s=>s.createQueue("demo.news.*"))
    
    @tailrec
    def receive() : Unit = {
      template.receive(queue) match {
        case Some(textMessage: TextMessage) => println(textMessage.getText)
          receive()
        case _ => println("No text message received")
      }
    }
 
    receive()
  }

}
