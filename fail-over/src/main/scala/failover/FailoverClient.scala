package failover

import javax.jms.{ConnectionFactory, Destination}
import javax.naming.InitialContext

import org.springframework.scala.jms.core.JmsTemplate
import resource._

/**
 * todo
 */
object FailoverClient extends App {

  for (context <- managed(new InitialContext)) {
    val cnf = context.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    val queue = context.lookup("FailOverQueue").asInstanceOf[Destination]
    val factory = new org.springframework.jms.connection.SingleConnectionFactory(cnf)
    val template = new JmsTemplate(factory)

    for (i <- 1 until 1000)
      template.convertAndSend(queue, s"message $i")
    
    factory.destroy()

  }

}
