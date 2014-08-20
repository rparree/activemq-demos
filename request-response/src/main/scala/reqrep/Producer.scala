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

  for (context <- managed(new InitialContext) ) {
    val cnf = context.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    val queue = context.lookup("queue/reqres").asInstanceOf[Destination]

    val template = new JmsTemplate(new org.springframework.jms.connection.SingleConnectionFactory(cnf))

    val replyQueue = template.execute(session => session.createTemporaryQueue())

    val someStrings = "blink" :: "road" :: Nil

    val requestMap = (someStrings map {
      s =>
        val id  = UUID.randomUUID().toString
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

    // just one for now...
    template.receive(replyQueue) match {
      case Some(inMessage: TextMessage) => {
        val origStr = requestMap(inMessage.getJMSCorrelationID)
        print(s"$origStr -> ${inMessage.getText}")
      }
      case _ => println("expected a text message")
    }


  }

}
