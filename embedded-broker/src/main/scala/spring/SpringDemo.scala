package spring

import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.scala.jms.core.JmsTemplate

/**
 * todo
 */
object SpringDemo extends App {

  val context = new ClassPathXmlApplicationContext("/spring/spring-config.xml")

  val template = context.getBean(classOf[JmsTemplate])


  

  println("Enter message to send (type exit to quit)")
  for (message <- io.Source.stdin.getLines().takeWhile(_ != "exit")) {
    template.convertAndSend("queue/Test", message)
  }
  
  println("exiting...")

  context.close()

 
}
