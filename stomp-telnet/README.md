# STOMP 1.2 / ActiveMQ

This is a small tutorial explaining the use of  [STOMP 1.2](http://stomp.github.io/stomp-specification-1.2.html) using telnet

## Setup

This tutorial is based on ActiveMQ 5.10. You can download a standalone version [http://activemq.apache.org/activemq-5100-release.html](http://activemq.apache.org/activemq-5100-release.html).

The stomp connector is enabled out-of-the-box. Should you however be using a different version (e.g, Fuse or Karaf based) then ensure
the following connector is enabled in the `activemq.xml` configuration file
```xml
<transportConnector name="stomp"   uri="stomp://localhost:61613"/>
```

Start the activemq broker (we are starting the broker in the foreground , which is easy during testing and development)

```bash
$ cd apache-activemq-5.10.0
$ bin/activemq console
```

The tutorial below uses the hawtio console. When using activemq in standalone mode it is best you
use the chrome plugin to connect to your broker:

There is an excellent blog entry explaining how to connect: [http://sensatic.net/activemq/activemq-and-hawtio.html](http://sensatic.net/activemq/activemq-and-hawtio.html)

## Publish messages using STOMP

### Connect

```java
telnet
telnet> open localhost 61613
Trying 127.0.0.1...
Connected to localhost.
Escape character is '^]'.
```
Some information on the protocol (from [wikipedia](http://en.wikipedia.org/wiki/Streaming_Text_Oriented_Messaging_Protocol)):
> Communication between client and server is through a "frame" consisting of a number of lines.
 The first line contains the command, followed by headers in the form <key>: <value> (one per line),
  followed by a blank line and then the body content, ending in a null character. Communication between server and client is through a MESSAGE,
RECEIPT or ERROR frame with a similar format of headers and body content.

**Note**: The null character null character `^@` can be  send to a linux terminal using  `ctrl+@`  (don't forget to use the shift)

In the hawtio console you should be able to see the connection (`remoteAddress` child under `clientConnectors>stomp`)

### Next start a session

The frame below initiates the session The `accept-version` and `host` headers are mandatory for STOMP 1.2 clients.

```java
CONNECT
accept-version:1.2
host:localhost
login:admin
passcode:admin

^@
CONNECTED
heart-beat:0,0
session:ID:hp-8570w-56234-1388604777495-2:1
server:ActiveMQ/5.10.0
version:1.2
```

The hawtio console will now also show the client session

Now that we have a session, we can use the following  client frames

- `SEND` - sends a message to a destination
- `SUBSCRIBE` - subscribe to a destination
- `UNSUBSCRIBE` - unsubscribe from a destination
- `BEGIN` - starts a transaction
- `COMMIT` - commits the transaction
- `ABORT` - rollback the transaction
- `ACK` - acknowledge of a message
- `NACK` - tell server we did not consume the message
- `DISCONNECT` - disconnect (always use the `receipt` header)

The server will respond with one of the following frames

- `MESSAGE` - message received by a subscriber
- `RECEIPT` - a response indicating successful receipt of the frame sent by the client
- `ERROR` - indicates something went wrong

### Send a message

Below is an example [SEND](http://stomp.github.io/stomp-specification-1.2.html#SEND) frame.  It uses the [Header receipt](http://stomp.github.io/stomp-specification-1.2.html). This
makes it easier to see the successful reception of the frame. The example send command below
also has the recommended [content-type](http://stomp.github.io/stomp-specification-1.2.html#Header_content-type) header (note the)

```java
SEND
destination:SomeQueue
content-type:text/plain
receipt:123

hello^@
RECEIPT
receipt-id:123
```

Consult the hawtio console and observe the message has been delivered

### disconnect

The frame below disconnects from the server

```java
DISCONNECT
receipt:789

^@

RECEIPT
receipt-id:


Connection closed by foreign host.
```

## Consume message using STOMP

Start a telnet a new telnet session and connect to the broker as before.

### Subscribe to a destination

The frame below subscribes to the `SomeQueue` destination. You need to pass
the [id](http://stomp.github.io/stomp-specification-1.2.html#SUBSCRIBE_id_Header) header
as we could have multiple open subscriptions in one session (this `id` is now indicated on each message which we receive)

```java
SUBSCRIBE
id:0
destination:SomeQueue
ack:client

^@
```

You will immediately receive messages which were already on the queue. If you would go
into hawtio and send a message, you will also receive that message instantly in the telnet STOMP session.

We are using `client` (acknowledgement)[http://stomp.github.io/stomp-specification-1.2.html#SUBSCRIBE_ack_Header],
other options are `client` and `client-individual`. Using `client` acknowledgement allows us to explicitly send `ACK` or `NACK` frames to indicate whether we want to acknowledge the receipt of the dispatched messages .

Make sure you look at the hawtio console and notice how messages are dispatched but not yet dequeued. 

### Acknowledge messages

A received MESSAGE frame might look like:

```java
MESSAGE
ack:ID\chp-8570w-60528-1388615318930-10\c1
message-id:ID\chp-8570w-60528-1388615318930-6\c1\c1\c1\c1
destination:/queue/SomeQueue
timestamp:1388615697080
expires:0
subscription:0
persistent:true
priority:0

hello
```

Notice the inclusion of the `ack` header. This header is used for the acknowledgement and is sent when using `client` and `client-individual` acknowledgement modes.

To acknowledge all the received messages use the following frame

```java
ACK
id:ID\chp-8570w-60528-1388615318930-10\c1
subscription:0

^@
```

This ACK frame is a *cumulative acknowledgment*, meaning all received messages are acknowledged. This is also standard behaviour when using Java JMS. 
Using an acknowledge mode of `client-individual` allows you to acknowledge individual messages. Also note
you have to pass the `subscription` header. This is because acknowledgement id's could be equal between multiple subscriptions in a single session.

GO ahead an notice the changes to the queue using the hawtio console.

### Unsubscribe

To unsubscribe you just send a UNSUBSCRIBE frame containing the subscription value

```java
UNSUBSCRIBE
id:0
receipt:  

^@
```

You can now disconnect as before.




