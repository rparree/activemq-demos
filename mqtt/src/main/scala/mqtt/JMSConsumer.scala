package mqtt

import javax.jms._
import javax.naming.InitialContext

import resource._


object JMSConsumer extends App {


  def marshall(message: BytesMessage): Unit = {
    val b  =  new Array[Byte](message.getBodyLength.toInt)
    message.readBytes(b)
    val msg = new String(b, "UTF-8")
    println(s"received $msg")
  }

  for (ctx <- managed(new InitialContext())) {
    val factory = ctx.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]
    val destinationName = "JMSDemoTopic"
    val topic = ctx.lookup(destinationName).asInstanceOf[Topic]

    for (connection <- managed(factory.createConnection());
         session <- managed(connection.createSession(false, Session.AUTO_ACKNOWLEDGE));
         consumer <- managed(session.createDurableSubscriber(topic, "subscriber"))) {


      connection.start()
      Option(consumer.receive(30000)) match {
        case Some(m: BytesMessage) => marshall(m)
        case None => println("Timeout")
      }
    }


  }
}

