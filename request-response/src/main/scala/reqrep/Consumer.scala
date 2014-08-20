package reqrep

import javax.naming.InitialContext
import javax.jms.{TextMessage, Destination, ConnectionFactory}
import org.springframework.scala.jms.core.JmsTemplate

import scala.annotation.tailrec
import resource._
object Consumer extends App {


  for ( context <- managed(new InitialContext) ) {
    val cnf = context.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    val queue = context.lookup("queue/reqres").asInstanceOf[Destination]

    val template = new JmsTemplate(cnf)


    getMessage

    @tailrec
    def getMessage: Unit = {
      template.receive(queue) match {
        case Some(inMessage: TextMessage) => template.send(inMessage.getJMSReplyTo)(
          s => {
            val correlationID = inMessage.getJMSCorrelationID
            println(s"received message with #$correlationID, sending reply")
            val outMsg = s.createTextMessage(inMessage.getText.toUpperCase)
            outMsg.setJMSCorrelationID(correlationID)
            outMsg
          })
          
        case _ => println("was expecting a text message")
      }
      getMessage
    }

  }

}
