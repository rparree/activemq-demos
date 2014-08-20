package virtualtopics.ootb

import javax.jms.{ConnectionFactory, TextMessage}
import javax.naming.InitialContext

import org.springframework.scala.jms.core.JmsTemplate
import resource._

import scala.annotation.tailrec

/**
 * todo
 */
object VirtualTopicQueueConsumer extends App {

  val consumerName = Option(System.getProperty("consumer.name")).get

  

  for (context <- managed(new InitialContext)) {
    val cnf = context.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    
    val queueName  = s"Consumer.${consumerName}.VirtualTopic.SampleTopic" 
    
    val factory = new org.springframework.jms.connection.SingleConnectionFactory(cnf)
    val template = new JmsTemplate(factory)
    
    
    val queue = template.execute(s=>s.createQueue(queueName))

    println(s"receiving on $queueName")
    @tailrec
    def receive() : Unit = {
      template.receive(queue) match {
        case Some(textMessage: TextMessage) => println(textMessage.getText)
          receive()
        case None => println("No text message received")
      }
    }

    receive()
    factory.destroy()
  }
  
  
}
