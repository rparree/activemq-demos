package reqrep

import java.util.UUID
import javax.jms._
import javax.naming.InitialContext

import org.springframework.scala.jms.core.JmsTemplate
import resource._


/**
 * todo
 */
object Producer extends App {
  type CorrelationId = String

  for (context <- managed(new InitialContext)) {
    val cnf = context.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    val queue = context.lookup("queue/reqres").asInstanceOf[Destination]
    
    for (factory <- demo.Helper.singleConnectionFactory(cnf)) {
      val template = new JmsTemplate(factory)

      val replyQueue = template.execute(session => session.createTemporaryQueue())

      val someStrings = "blink" :: "road" :: Nil

      val requestMap = (someStrings map {
        s =>
          val id = UUID.randomUUID().toString
          template.send(queue) {
            session =>
              val msg = session.createTextMessage(s)
              msg.setJMSReplyTo(replyQueue)
              msg.setJMSCorrelationID(id)
              msg
          }
          println(s"sent $s")
          (id, s)

      }).toMap

      template.javaTemplate.setReceiveTimeout(20000)
      println("Starting to receive (timout 20s)")
      receive()

      def receive(): Unit = {
        template.receive(replyQueue) match {
          case Some(inMessage: TextMessage) => {
            val origStr = requestMap(inMessage.getJMSCorrelationID)
            print(s"$origStr -> ${inMessage.getText}")
            receive()
          }
          case Some(msg) => println(s"expected a text message, received $msg")
            receive()
          case None => println("No more messages receives (timeout)")
        }

      }


    }
  }


}
