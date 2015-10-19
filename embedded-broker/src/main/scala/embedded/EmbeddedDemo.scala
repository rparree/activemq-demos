package embedded

import java.nio.file.FileSystems
import javax.jms.Session

import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.broker.BrokerService
import org.apache.activemq.command.ActiveMQQueue
import org.apache.activemq.store.kahadb.KahaDBStore
import resource._

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._


/**
 * todo
 */
object EmbeddedDemo extends App  {


  val broker = startBroker

  val factory = for
  factory.setUseAsyncSend(true)
  for (connection <- managed(factory.createConnection())) {
    val pf = future {

      for (session <- managed(connection.createSession(false, Session.AUTO_ACKNOWLEDGE));
           producer <- managed(session.createProducer(new ActiveMQQueue("Demo.Queue")))) {

        println("Starting to produce")
        for (i <- 1 until 500) {
          producer.send(session.createTextMessage("message" + 1))

          if (i == 1 || i % 100 == 0)
            println(s"send $i messages")

        }
        println("Have sent all messages")

      }


    }


    // receive

    connection.start()
    for (session <- managed(connection.createSession(false, Session.AUTO_ACKNOWLEDGE));
         consumer <- managed(session.createConsumer(new ActiveMQQueue("Demo.Queue")))) {

      println("Starting to consume")


      @tailrec
      def receive(n: Int): Int = {
        Option(consumer.receive(1000)) match {
          case Some(m) =>
            if ((n == 1) || (n % 100 == 0)) println(s"received $n messages")
            receive(n + 1)
          case None => println("Timed out"); n
        }
      }

      val receivedMessages = receive(1)
      println("Stopping consumption")
      connection.stop()
      println(s"Done...received $receivedMessages messages")
    }

  }
  broker.stop()


  def startBroker() = {
    val broker = new BrokerService

    val kahaDB = new KahaDBStore
    kahaDB.setDirectory(FileSystems.getDefault.getPath("./target", "activemq", "data").toFile)
    kahaDB.setJournalMaxFileLength(10240 * 100)

    kahaDB.setIndexWriteBatchSize(100) // small batch -> more frequent small writes
    kahaDB.setEnableIndexWriteAsync(true) // index write in separate thread

    broker.setPersistenceAdapter(kahaDB)

    broker.addConnector("vm://local")

    broker.start()
    broker
  }


}
