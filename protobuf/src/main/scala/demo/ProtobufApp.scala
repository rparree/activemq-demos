package demo

import demo.RegistrantProtos.Registrant.GenderType

/**
 * todo
 */
object ProtobufApp extends App {

  val registrant = RegistrantProtos.Registrant.newBuilder()
    .setEmail("jennifer@work.com")
    .setGender(GenderType.FEMALE)
    .setUsername("jenny").build()

  val array = registrant.toByteArray

  array.foreach(b => print(b + " "))
  
  
  

}
