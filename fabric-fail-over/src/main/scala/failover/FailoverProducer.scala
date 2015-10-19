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
object FailoverProducer extends App {

  
//  System.setProperty("java.naming.provider.url", "discovery:(fabric:amq-east)?reconnectDelay=1000")
//  System.setProperty("zookeeper.password", "masterkey")

//  System.setProperty("zookeeper.url", "localhost:33337")
  System.setProperty("java.naming.provider.url", "failover:(tcp://localhost:18000,tcp://localhost:18001)")
  
  
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
