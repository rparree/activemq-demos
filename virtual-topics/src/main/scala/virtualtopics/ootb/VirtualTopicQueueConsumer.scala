package virtualtopics.ootb

import javax.jms.ConnectionFactory
import javax.naming.InitialContext

import demo.Helper
import demo.Helper._
import org.springframework.scala.jms.core.JmsTemplate
import resource._

/**
 * todo
 */
object VirtualTopicQueueConsumer extends App {

  val consumerName = readLine("Consumer name: ")


  for (context <- managed(new InitialContext)) {
    val cnf = context.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    
    val queueName = s"Consumer.$consumerName.VirtualTopic.SampleTopic" // or even Consumer.$consumerName.VirtualTopic.*

    for (factory <- singleConnectionFactory(cnf)) {
      val template = new JmsTemplate(factory)



      val queue = template.execute(s => s.createQueue(queueName))
      println(s"receiving on queue: $queueName")
      template.javaTemplate.setReceiveTimeout(10000)
      Helper.receiveFromTemplateAndPrintText(template,queue)

    }
  }


}
