# activemq-demos

It is recommended to install hawtio in your JBoss A-MQ or Karaf installation
. Look at [hawtio on fuse installation](http://hawt.io/getstarted/#Using_Fuse__Fabric8__Apache_Karaf_or_Apache_Servicemix)

Various ActiveMQ Demos

## The Demos

- **jms-jndi** Very simple demo showing use of `jndi.properties` and `dynamicQueues`
- **request-response** Basic Request-Response MEP implementation (uses scala-spring for JMS templates). Only one of
 the sent messages is correlated, to illustrate it works. (Has the `exec-maven-plugin` configured under two profiles)
- **broker-network** Demo which sets up a network of three brokers. Then sending on to broker1 and
 receiving on broker2 and broker3. Use `setupBroker1AndSendMessage(None)` to setup the brokers without a network to see
 the difference. JMX is enabled, use the following to enable jolokia for Hamtio:
   ```
    -javaagent:jolokia-jvm-1.2.2-agent.jar=port=8777,host=localhost,user=smx,password=smx
   ```
- **wildcards** shows how messages sent to "demo.news.europe|asia|etc" are received using `demo.news.*`
- **exclusive-consumer** demonstrates exclusive consumption on a queue. Run each consumer by passing a name and
*optionally* pass a consumer priority
  ```bash
  mvn -q compile exec:java -Dconsumer.name=consumerA -Dconsumer.priority=5
  ```
  Publish messages using hawtio on the `demo.xclusive` queue
- **virtual-topics** demonstrates out-of-the-box (ootb) virtual topic
    ```bash
    mvn -q -P consumer -Dconsumer.name=A compile exec:java
    ```
    to run the producer
    ````bash
    mvn -P publisher exec:java
    ```
- **composite-destination** You need to manually copy/paste the composite destination into the activemq configuration
 file (see [composite-queue.conf.xml](composite-destination/src/main/etc/composite-queue.conf.xml)). Then
 run the client (using maven) and observe the queue size in hawtio
- **fail-over** Run the two brokers
   ```bash
   mvn -P broker -Dbroker.name=One -q activemq:run
   # and
   mvn -P broker -Dbroker.name=Two -q activemq:run
   ```
   Then run the consumer
   ```bash
   mvn compile exec:java -Dexec.mainClass=failover.FailOverConsumer
   ```
   And finally the producer
   ```bash
   mvn compile exec:java -Dexec.mainClass=failover.FailoverClient
   ```
   The stop brokerOne ,and see how both client reconnect to brokerTwo. Then start brokerOne again and
   stop brokerTwo and see how it takes over again.
- **stomp-telnet** has its own [README.md](stomp-telnet/README.md)
- **websockets** has its own [README.md](websockets/README.md) 
   

