
# activemq-demos

Please consult the setup instructions further below.

- **jms-jndi** Very simple demo showing use of `jndi.properties` and `dynamicQueues`
- **request-response** Basic Request-Response MEP implementation (uses scala-spring for JMS templates). Only one of
 the sent messages is correlated, to illustrate it works. Run the consumer first
- **broker-network** Demo which sets up a network of three brokers. Then sending on to broker1 and
 receiving on broker2 and broker3. Use `setupBroker1AndSendMessage(None)` to setup the brokers without a network to see
 the difference. Jolokia enabled; connect using Hamtio Chrome extension (port: `8777`, path : `/jolokia`, username/password: `smx/xms`:
- **wildcards** shows how messages sent to "demo.news.europe|asia|etc" are received using `demo.news.*`
- **virtual-topics** illustrates a Queue receiver on a topic.
- **exclusive-consumer** demonstrates exclusive consumption on a queue. 
- **composite-destination** You need to manually copy/paste the composite destination into the activemq configuration
 file (see [composite-queue.conf.xml](composite-destination/src/main/etc/composite-queue.conf.xml)). Then
 run the client (using maven) and observe the queue size in hawtio
- **fail-over** Run the two brokers using the [sbt-activemq](https://github.com/edc4it/sbt-activemq/) plugin. You can
eiher start them in the same sbt process or in separate processes (which makes killing the broker possible). 
- **stomp-telnet** has its own [README.md](stomp-telnet/README.md)
- **websockets** has its own [README.md](websockets/README.md) 
- **embedded-broker** Showing embedde brokers as well as demonstrating jdbc journalled persistence. Has its own
[README.md](embedded-broker/README.md)
- **mqtt** Illustrates use of mqtt protocol. Has its own [README.md](mqtt/README.md) 


# Setup 


These demos have been developed and tested with ActiveMQ 5.10, Java 7-8 and SBT 0.13.5.

## ActiveMQ

You could run against the standalone version, or prepare a kara instance. The instructions for setting up
your karaf instance are below. 



   

