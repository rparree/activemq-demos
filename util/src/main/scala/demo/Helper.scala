package demo

import javax.jms._

import org.springframework.jms.connection.{UserCredentialsConnectionFactoryAdapter, SingleConnectionFactory}
import org.springframework.scala.jms.core.JmsTemplate
import resource._

import scala.annotation.tailrec
import scala.concurrent.duration._

/**
  * todo
  */
object Helper {
  def singleConnectionFactory(cnf: ConnectionFactory) = {
    val u = new UserCredentialsConnectionFactoryAdapter()
    u.setTargetConnectionFactory(cnf)
    u.setUsername("admin")
    u.setPassword("admin")
    val d = new SingleConnectionFactory(u)
    makeManagedResource(d) {
      _.destroy()
    }(Nil)
  }


  val receiveAndPrintText = receiveText(_: MessageConsumer, _: Duration)(m => println(s"received ${m.getText}"))
  val receiveFromTemplateAndPrintText = receiveText(_: JmsTemplate, _: Destination)(m => println(s"received ${m.getText}"))

  @tailrec
  def receiveText(consumer: MessageConsumer, timeOut: Duration = 10.seconds)(txtReceive: (TextMessage) => Unit): Unit = {
    Option(consumer.receive(timeOut.toMillis)) match {
      case Some(m: TextMessage) => txtReceive(m)
        receiveText(consumer, timeOut)(txtReceive)
      case Some(m: Message) => println("Received a non-text message")
        receiveText(consumer, timeOut)(txtReceive)

      case None => println("Timed out");
    }
  }

  @tailrec
  def receiveText(jmsTemplate: JmsTemplate, d: Destination)(txtReceive: (TextMessage) => Unit): Unit = {
    jmsTemplate.receive(d) match {
      case Some(m: TextMessage) => txtReceive(m)
        receiveText(jmsTemplate, d)(txtReceive)
      case Some(m: Message) => println("Received a non-text message")
        receiveText(jmsTemplate, d)(txtReceive)

      case None => println("Timed out");
    }
  }
}
