import java.util.Properties
import javax.jms._
import javax.naming.InitialContext
import scala.concurrent.duration._
import resource._

/**
 * todo
 */
object ExclusiveConsumer extends App {


  val consumerName = readLine("Consumer name: ")
  val digits = """(\d+)""".r
  val priority = readLine("Priority: ") match {
    case digits(d) => Some(d)
    case _ => None
  }

  println(s"name $consumerName with priority: ${priority.getOrElse("none")}")

  val jndiProps = new Properties()
  jndiProps.load(getClass.getResourceAsStream("/jndi.properties"))


  for (context <- managed(new InitialContext)) {

    val cnf = context.lookup("myJmsFactory").asInstanceOf[ConnectionFactory]

    for (connection <- managed(cnf.createConnection());
         session <- managed(connection.createSession(false, Session.AUTO_ACKNOWLEDGE))) {

      connection.start()

      val xclusiveQueueName = {
        val b = StringBuilder.newBuilder
        val queue = context.lookup("xclusive").asInstanceOf[Queue]
        b.append(queue.getQueueName).append("?consumer.exclusive=true")
        b.append(priority.map(s => "&consumer.priority=" + s).getOrElse(""))
        b.toString()
      }
      println(s"Using queue name $xclusiveQueueName")



      val xclusiveQueue = session.createQueue(xclusiveQueueName)
      for (consumer <- managed(session.createConsumer(xclusiveQueue))) {

        println("Starting receiving (timeout 30 seconds)")
        demo.Helper.receiveText(consumer, 30 seconds) {
          m => println(s"Received '${m.getText}' by $consumerName")
        }

      }
    }
  }

}
