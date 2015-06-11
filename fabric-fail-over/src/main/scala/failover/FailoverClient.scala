package failover

import javax.jms.{Destination, ConnectionFactory}
import javax.naming.{Context, InitialContext}


import demo.Helper._
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter
import org.springframework.scala.jms.core.JmsTemplate
import resource._

/**
 * todo
 */
object FailoverClient extends App {

  
  System.setProperty("java.naming.provider.url", "discovery:(fabric:amq-east)?reconnectDelay=1000")
  System.setProperty("zookeeper.password", "masterkey")


  
  
  for (context <- managed(new InitialContext)) {
    val cnf = context.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    val queue = context.lookup("FailOverQueue").asInstanceOf[Destination]
    val s = new UserCredentialsConnectionFactoryAdapter()
    s.setTargetConnectionFactory(cnf)
    s.setUsername("admin")
    s.setPassword("admin")
    for (factory <- singleConnectionFactory(s)) {
      val template = new JmsTemplate(factory)

      for (i <- 1 until 100) {
        template.convertAndSend(queue, s"message $i")
        println(s"sent message $i")
      }
    }

  }

}
