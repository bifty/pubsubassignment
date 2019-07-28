package com.bharath.jsm.card;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CardApp {

    public static void main(String[] args) throws NamingException {
        InitialContext context = new InitialContext();
        Topic topic = (Topic) context.lookup("topic/cardTopic");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {

            Purchase purchase = new Purchase();
            purchase.setId("1");
            purchase.setDescription("Software Architect");
            purchase.setAmount(450f);

            for (int i = 1; i <= 10; i++) {
                jmsContext.createProducer().send(topic, purchase);
            }

            System.out.println("Purchase done");

        }

    }
}
