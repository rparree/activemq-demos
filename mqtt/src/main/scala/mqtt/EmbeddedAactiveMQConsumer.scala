package mqtt

import javax.jms._
import javax.naming.InitialContext

import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.scala.jms.core.JmsTemplate
import resource._


object EmbeddedAactiveMQConsumer extends App {

  val context = new ClassPathXmlApplicationContext("spring-config.xml")

  val template = context.getBean(classOf[JmsTemplate])
  
  val t= template.execute(s=>s.createTopic("topic.demo.foo"))


  template.receive(t) match {
    case Some(m: BytesMessage) => marshall(m)
    case _ => println("Expected a bytes message")
  }
  
  def marshall(message: BytesMessage): Unit = {
    val b = new Array[Byte](message.getBodyLength.toInt)
    message.readBytes(b)
    val msg = new String(b, "UTF-8")
    println(s"received $msg")
  }

}

