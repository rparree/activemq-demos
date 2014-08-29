package virtualtopics.ootb

import javax.jms.{ConnectionFactory, Topic}
import javax.naming.InitialContext

import demo.Helper._
import org.springframework.scala.jms.core.JmsTemplate
import resource._

/**
 * todo
 */
object TopicPublisher extends App {

  for (context <- managed(new InitialContext)) {
    val cnf = context.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    val topic = context.lookup("SampleTopic").asInstanceOf[Topic]
    for (factory <- singleConnectionFactory(cnf)) {
      val template = new JmsTemplate(factory)
      template.convertAndSend(topic,"hello")
    }
  }

}
