
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

### Standalone ActiveMQ with Hawtio Chrome plugin

Download from [http://activemq.apache.org/download.html](http://activemq.apache.org/download.html)

For connecting to hawtio to a standalone activemq check this [blog](http://sensatic.net/activemq/activemq-and-hawtio.html) 


### ActiveMQ and Hawtio on Karaf

First download and install [Karaf 3.0.x](http://karaf.apache.org/index/community/download.html). Then start karaf and
execute the following commands:

```bash
me@my-linux:apache-karaf-3.0.1> bin/karaf 
        __ __                  ____      
       / //_/____ __________ _/ __/      
      / ,<  / __ `/ ___/ __ `/ /_        
     / /| |/ /_/ / /  / /_/ / __/        
    /_/ |_|\__,_/_/   \__,_/_/         

  Apache Karaf (3.0.1)

Hit '<tab>' for a list of available commands
and '[cmd] --help' for help on a specific command.
Hit '<ctrl-d>' or type 'system:shutdown' or 'logout' to shutdown Karaf.

karaf@root()> feature:repo-add activemq 5.10.0
Adding feature url mvn:org.apache.activemq/activemq-karaf/5.10.0/xml/features
karaf@root()> feature:install activemq-broker

karaf@root()> feature:repo-add hawtio 1.4.14
Adding feature url mvn:io.hawt/hawtio-karaf/1.4.14/xml/features
karaf@root()> feature:install hawtio
karaf@root()> 
```


## Build environment 

You will need to install  [SBT 0.13](http://www.scala-sbt.org/download.html)

Example of running a demo 
```bash
$ me@mylinux:activemq-demos> sbt
Loading ...
[info] ...
...
> projects
[info]   * activemq-demos                                                                                                                                                                     
[info]     broker-network                                                                                                                                                                     
[info]     composite-destination                                                                                                                                                              
[info]     embedded-broker                                                                                                                                                                    
[info]     exclusive-consumer                                                                                                                                                                 
[info]     fail-over                                                                                                                                                                          
[info]     jms-jndi                                                                                                                                                                           
[info]     mqtt                                                                                                                                                                               
[info]     request-response                                                                                                                                                                   
[info]     util                                                                                                                                                                               
[info]     virtual-topics                                                                                                                                                                     
[info]     wildcards 

> project embedded-broker                                                                                                                                                                     
[info] Set current project to embedded-broker> 

> run 

Multiple main classes detected, select one to run:

 [1] spring.SpringDemo
 [2] embedded.EmbeddedDemo

Enter number: 1
...
...
...
```


   

