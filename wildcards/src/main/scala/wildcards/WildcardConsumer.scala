package wildcards

import javax.naming.InitialContext

import demo.Helper._
import org.apache.activemq.ActiveMQConnectionFactory
import org.springframework.scala.jms.core.JmsTemplate
import resource._

/**
 * todo
 */
object WildcardConsumer extends App {


  for (context <- managed(new InitialContext)) {
    val cnf = context.lookup("myJmsFactory").asInstanceOf[ActiveMQConnectionFactory]
    for (factory <- singleConnectionFactory(cnf)) {
      val template = new JmsTemplate(factory)

      val queue = template.execute(s => s.createQueue("demo.news.*"))
      template.javaTemplate.setReceiveTimeout(10000)
      println("Starting to receive (timeout is 10s)")
     
      demo.Helper.receiveFromTemplateAndPrintText(template,queue)
      
      
    }


  }


}
