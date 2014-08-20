package compositedestinations

import javax.jms.{ConnectionFactory, Destination}
import javax.naming.InitialContext

import org.springframework.scala.jms.core.JmsTemplate
import resource._

/**
 * todo
 */
object BroadCastingQueueSender extends App {

  for (context <- managed(new InitialContext)) {
    val cnf = context.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    val queue = context.lookup("BroadCastingQueue").asInstanceOf[Destination]
    val factory = new org.springframework.jms.connection.SingleConnectionFactory(cnf)
    val template = new JmsTemplate(factory)
    
    template.convertAndSend(queue,"message 1")
    template.send(queue)(s=>{
      val textMessage = s.createTextMessage("message 2 [gold]")
      textMessage.setObjectProperty("level","gold")
      textMessage
    })

    factory.destroy()
  }
  
}
