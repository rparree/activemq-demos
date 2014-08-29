# MQTT demo

## Setup 

**Important** At this moment running from sbt does not work (probably context loader problem).
For now run using intellij.

Make sure the mqtt connector is enabled in activemq. 

The mqtt protocol's specification can be downloaded from [here](http://public.dhe.ibm.com/software/dw/webservices/ws-mqtt/MQTT_V3.1_Protocol_Specific.pdf)

## Sniffing

Use the following to illustrate the data packets send between client and broker:

```bash
$ sudo tcpflow -c -D -i lo tcp port 1883
```

Explanation of the flags:

- `-c` to output to console as apposed to a file
- `-D` to output in HEX as apposed to text
- '-i lo' capture on localhost network
- 'tcp port 1883` capture traffic to/from port 1883

The output should include 4 messages (in dialogue style)

1. message: 1030 ... (1 0|00|0 0x30) means a `CONNECT` followed by 48 (0x30) bytes
2. message: 2002 ... (2 0|00|0 0x02) means a `CONNACK` followed by 2 bytes
3. message: 3214 ... (3 0|01|0 0x14) means a `PUBLISH` with a QoS 1 (at least once) followed by 20 bytes
4. message: 4002 0001 (4 ...) means a `PUBACK`, followed by 2 bytes (which contain the 16-bit unsigned message id: 1) 

