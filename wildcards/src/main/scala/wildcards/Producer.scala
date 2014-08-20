package wildcards

import java.util.Properties
import javax.jms.{ConnectionFactory, Queue}
import javax.naming.InitialContext

import org.springframework.scala.jms.core.JmsTemplate
import resource._

import scala.collection.JavaConversions._

/**
 * todo
 */
object Producer extends App {

  val jndiProps = new Properties()
  jndiProps.load(getClass.getResourceAsStream("/jndi.properties"))

  for (context <- managed(new InitialContext)) {
    val cnf = context.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    val queues = jndiProps.filterKeys(s => s.startsWith("queue.")).keysIterator.map(n => n.stripPrefix("queue.")).map(name => context.lookup(name).asInstanceOf[Queue])
    val factory = new org.springframework.jms.connection.SingleConnectionFactory(cnf)
    val template = new JmsTemplate(factory)

    
    queues.foreach { q =>
      println(s"Sent message to ${q.getQueueName}")
      template.send(q)(session => session.createTextMessage(s"message on ${q.getQueueName}"))
    }

    factory.destroy()
  }
  
  

}
