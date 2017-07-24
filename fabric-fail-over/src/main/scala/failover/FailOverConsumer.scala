package failover

import javax.jms.{ConnectionFactory, Destination}
import javax.naming.InitialContext

import demo.Helper
import demo.Helper._
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter
import org.springframework.scala.jms.core.JmsTemplate
import resource._

/**
 * todo
 */
object FailOverConsumer extends App {

//  System.setProperty("java.naming.provider.url", "discovery:(fabric:amq-west)")
  System.setProperty("java.naming.provider.url",
    "failover:(tcp://localhost:19000,tcp://localhost:19001)")
//  System.setProperty("zookeeper.password", "masterkey")


  

  for (context <- managed(new InitialContext)) {
    val cnf = context.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    val s = new UserCredentialsConnectionFactoryAdapter()
    s.setTargetConnectionFactory(cnf)
    s.setUsername("admin")
    s.setPassword("admin")
    
    val queue = context.lookup("FailOverQueue").asInstanceOf[Destination]
    for (factory <- singleConnectionFactory(s)) {
      val template = new JmsTemplate(factory)

      template.javaTemplate.setReceiveTimeout(10000)
      Helper.receiveFromTemplateAndPrintText(template,queue)
    }
  }
  
  
}
