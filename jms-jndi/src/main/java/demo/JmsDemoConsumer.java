package demo;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.region.*;

import javax.jms.*;
import javax.jms.Destination;
import javax.naming.InitialContext;

/**
 * todo
 */
public class JmsDemoConsumer {

    public static void main(String[] args) throws Exception {

        InitialContext initialContext = new InitialContext();
        ConnectionFactory cnf = (ConnectionFactory) initialContext.lookup("myJmsFactory");
        Destination destination = (Destination) initialContext.lookup("queue/JMSDemoQueue");
        Connection connection = cnf.createConnection("admin", "admin");
        Session session = connection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener((Message m)->{
            TextMessage tms = (TextMessage) m;
            try {
                System.out.append("Received message with text").println(tms.getText());
            } catch (JMSException e) {
                System.out.println("ooops.");
            }
        });

        connection.start();

    }
}
