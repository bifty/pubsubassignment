package com.bharath.jms.security;


import com.bharath.jsm.card.Purchase;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SecurityApp {

    public static void main(String[] args) throws NamingException, JMSException {
        InitialContext context = new InitialContext();
        Topic topic = (Topic) context.lookup("topic/cardTopic");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {
            JMSConsumer consumer = jmsContext.createSharedConsumer(topic, "sharedConsumer");
            JMSConsumer consumer2 = jmsContext.createSharedConsumer(topic, "sharedConsumer");

            for (int i = 1; i <= 10; i += 2) {
                Message message = consumer.receive();
                Purchase purchase = message.getBody(Purchase.class);
                System.out.println("Consumer 1 checks purchase: " + purchase.getDescription());

                Message message2 = consumer2.receive();
                Purchase purchase2 = message2.getBody(Purchase.class);
                System.out.println("Consumer 2 checks purchase: " + purchase2.getDescription());
            }

        }

    }
}
