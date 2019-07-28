package com.bharath.jms.alert;

import com.bharath.jsm.card.Purchase;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class AlertsApp {

    public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
        InitialContext context = new InitialContext();
        Topic topic = (Topic) context.lookup("topic/cardTopic");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {

            jmsContext.setClientID("alertApp");
            JMSConsumer consumer = jmsContext.createDurableConsumer(topic, "subscription1");
            // simulate application shutdown
            consumer.close();

            Thread.sleep(10000);

            // simulate application online again
            consumer = jmsContext.createDurableConsumer(topic, "subscription1");
            Message message = consumer.receive();
            Purchase purchase = message.getBody(Purchase.class);
            System.out.println("Alert purchase done!" + purchase.getDescription());

            consumer.close();
            jmsContext.unsubscribe("subscription1");

        }

    }
}
