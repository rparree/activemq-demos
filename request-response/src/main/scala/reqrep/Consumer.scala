package reqrep

import javax.jms.{ConnectionFactory, Destination, TextMessage}
import javax.naming.InitialContext

import demo.Helper.singleConnectionFactory
import org.apache.activemq.DestinationDoesNotExistException
import org.springframework.scala.jms.core.JmsTemplate
import resource._

import scala.annotation.tailrec

object Consumer extends App {


  for (context <- managed(new InitialContext)) {
    val cnf = context.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    val queue = context.lookup("queue/reqres").asInstanceOf[Destination]

    for (factory <- singleConnectionFactory(cnf)) {
      val template = new JmsTemplate(factory)


      println("Starting receiving. Timeout is 20s")
      template.javaTemplate.setReceiveTimeout(20000)
      receive()

      @tailrec
      def receive(): Unit = {
        template.receive(queue) match {
          case Some(inMessage: TextMessage) => try {
            template.send(inMessage.getJMSReplyTo)(
              s => {
                val correlationID = inMessage.getJMSCorrelationID
                println(s"received message with #$correlationID, sending reply")
                val outMsg = s.createTextMessage(inMessage.getText.toUpperCase)
                outMsg.setJMSCorrelationID(correlationID)
                outMsg
              })
          }
          catch {
            case e if e.getCause.isInstanceOf[DestinationDoesNotExistException] => println("The client already closed the reply queue")
          }
            receive()
          case None => print("received no message (timeout)")
          case _ => println("was expecting a text message")
            receive()
        }

      }

    }
  }


}
