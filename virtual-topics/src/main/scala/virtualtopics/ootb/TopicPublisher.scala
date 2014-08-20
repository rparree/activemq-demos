package virtualtopics.ootb

import javax.jms.{Topic, ConnectionFactory}
import javax.naming.InitialContext

import org.springframework.scala.jms.core.JmsTemplate
import resource._

/**
 * todo
 */
object TopicPublisher extends App {

  for (context <- managed(new InitialContext)) {
    val cnf = context.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    val topic = context.lookup("SampleTopic").asInstanceOf[Topic]
    val factory = new org.springframework.jms.connection.SingleConnectionFactory(cnf)
    val template = new JmsTemplate(factory)
    
    
    template.send(topic)(s=>s.createTextMessage("hello"))


   

    factory.destroy()
  }
  
}
