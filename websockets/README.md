# WebSockets demo

This is a small demo showing the use of Web Sockets to receive messages in the browser. Purposely it
is not (over) engineered to keep it simple for educational purposes. The code has been made a little DRY,
 but not to the extend where it would interfere with demonstration and educational objectives. If you are looking for a
 full blown JavaScript STOMP API, i suggest you look at [https://github.com/jmesnil/stomp-websocket](https://github.com/jmesnil/stomp-websocket)
 
## Setup

Ensure the following connector is enabled

```xml
<transportConnector name="ws" uri="ws://0.0.0.0:61614?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
```

Also make sure you have a http server installed. 
Easiest is to use the [http-server](https://www.npmjs.org/package/http-server) available from npm.

## Use the demo

Serve the pages off the same computer as where ActiveMQ is running. Then click the connect button. IF connection is
successful a subscription form will appear. Supply the name of the destination (queue or topic) and click `subscribe`

Then use the Hawtio console to send messages to the destination. Observe how the messages appear on the page (newest one in the top)