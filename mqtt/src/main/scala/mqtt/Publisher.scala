package mqtt

import org.fusesource.hawtbuf.{AsciiBuffer, UTF8Buffer}
import org.fusesource.mqtt.client.{MQTT, QoS}

/**
 * todo
 */
object Publisher extends App {


  val mqtt = new MQTT()
  mqtt.setHost("localhost", 1883)
  println("v1")
  //  mqtt.setCleanSession(true)
  //  mqtt.setKeepAlive(1000)
  mqtt.setUserName("admin")
  mqtt.setPassword("admin")
  //  mqtt.setWillMessage("my last will")

  val connection = mqtt.futureConnection()
  connection.connect().await()


  val topic = new UTF8Buffer("/topic/demo")
  val msg = new AsciiBuffer("hello")
  //for (i <- 1 until 1000) {
    val future = connection.publish(topic, msg, QoS.AT_LEAST_ONCE, false)
 // }
  
  connection.disconnect().await()


}
