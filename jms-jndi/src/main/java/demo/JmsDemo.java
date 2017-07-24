package demo;

import org.apache.activemq.protobuf.BufferInputStream;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

/**
 * todo
 */
public class JmsDemo {

    public static void main(String[] args) throws Exception {

        InitialContext context = new InitialContext(); //looks for jndi.properties
        ConnectionFactory cnf = (ConnectionFactory) context.lookup("myJmsFactory");
        Destination destination = (Destination) context.lookup("queue/JMSDemoQueue");

        Connection connection = cnf.createConnection("admin", "admin");
        Session session = connection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);

        MessageProducer producer = session.createProducer(destination);
        Message msg = session.createTextMessage("hello some text from legacy Java");
        producer.send(msg);


    }
}
