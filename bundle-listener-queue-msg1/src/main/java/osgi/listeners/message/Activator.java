/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package osgi.listeners.message;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Activator implements BundleActivator {
	
	//URL of the JMS server. DEFAULT_BROKER_URL will just mean that JMS server is on localhost
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
//	private static String url = "vm://localhost:8181";
     
    // default broker URL is : tcp://localhost:61616"
    private static String subject = "JCG_QUEUE"; // Queue Name.You can create any/many queue names as per your requirement. 
    
    Connection connection;
    Session session;
    MessageConsumer consumer;

    public void start(BundleContext context) throws JMSException {
    	
    	try {
			
    		// Getting JMS connection from the server
    		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
    		connection = connectionFactory.createConnection("karaf","karaf");
    		connection.start();
    		
    		// Creating session for seding messages
    		session = connection.createSession(false,
    				Session.AUTO_ACKNOWLEDGE);
    		
    		// Getting the queue 'JCG_QUEUE'
    		Destination destination = session.createQueue(subject);
    		
    		// MessageConsumer is used for receiving (consuming) messages
    		consumer = session.createConsumer(destination);
    		
    		// Here we receive the message.
    		Message message = consumer.receive();
    		
    		// We will be using TestMessage in our example. MessageProducer sent us a TextMessage
    		// so we must cast to it to get access to its .getText() method.
    		if (message instanceof TextMessage) {
    			TextMessage textMessage = (TextMessage) message;
    			System.out.println("Received message '" + textMessage.getText() + "'");
    		}
    		
    		consumer.close();
            session.close();
    		connection.close();
    		
		} catch (JMSException e) {
			System.out.println("Error '" + e.getMessage() + "'");
		}
    }

    public void stop(BundleContext context) throws JMSException {
    	consumer.close();
        session.close();
		connection.close();
        System.out.println("Paro Mensaje del bundle 1");
    }

}