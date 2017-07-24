package failover

import javax.jms.{ConnectionFactory, Destination}
import javax.naming.InitialContext

import demo.Helper
import demo.Helper._
import org.springframework.scala.jms.core.JmsTemplate
import resource._

/**
 * todo
 */
object FailOverConsumer extends App {


  

  for (context <- managed(new InitialContext)) {
    val cnf = context.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    
    
    val queue = context.lookup("FailOverQueue").asInstanceOf[Destination]
    for (factory <- singleConnectionFactory(cnf)) {
      val template = new JmsTemplate(factory)

      template.javaTemplate.setReceiveTimeout(1000000)
      Helper.receiveFromTemplateAndPrintText(template,queue)
    }
  }
  
  
}
