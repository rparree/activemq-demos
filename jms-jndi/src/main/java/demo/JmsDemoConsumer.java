package demo;

import javax.jms.*;
import javax.naming.InitialContext;

/**
 * todo
 */
public class JmsDemoConsumer {

    public static void main(String[] args) throws Exception {

        InitialContext context = new InitialContext(); //looks for jndi.properties
        ConnectionFactory cnf = (ConnectionFactory) context.lookup("myJmsFactory");
        Destination destination = (Destination) context.lookup("queue/JMSDemoQueue");
        Connection connection = cnf.createConnection("admin", "admin");
        Session session = connection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);

        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(message -> {
            TextMessage message1 = (TextMessage) message;
            try {
                System.out.println(message1.getText());
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        });


        connection.start();
    }
}
