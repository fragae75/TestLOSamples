package com.test.LOSamples;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.test.LOSamples.TestLOSamples.QueueTypes;

import java.util.UUID;

import javax.swing.JTextArea;

/**
 * Application connects to LO and consumes messages from a FIFO queue.
 *
 * You MUST first create a FIFO called "~data" in your LO account.
 *
 */
public class RunConsumeQueue implements Runnable {

	private JTextArea textPane;
	private String sQueueName;
	private JTextArea textPaneSubscribe;
	private QueueTypes queueType;
//   final String ROUTING_KEY_FILTER = "~event/v1/data/new/#";
	
	public RunConsumeQueue(	String sTopicName, 
						QueueTypes queueType,
						JTextArea textPane,
						JTextArea textPaneSubscribe ){
		this.textPane = textPane;
		this.queueType = queueType;
		switch (queueType){
			case PUBSUB:
				this.sQueueName = "pubsub/" + sTopicName;
			break;
			case FIFO:
				this.sQueueName = "fifo/" + sTopicName;
			break;
			case ROUTER:
				this.sQueueName = "router/" + sTopicName;
			break;
		}
		
		this.textPaneSubscribe = textPaneSubscribe;
	}
	
    /**
     * Basic "MqttCallback" that handles messages as JSON device commands,
     * and immediately respond.
     */
    public class SimpleMqttCallback implements MqttCallback {
        private MqttClient mqttClient;

        public SimpleMqttCallback(MqttClient mqttClient) {
            this.mqttClient = mqttClient;
        }

        public void connectionLost(Throwable throwable) {
            System.out.println("Connection lost");
            mqttClient.notifyAll();
        }

        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
            System.out.println("Received message from queue - " + sQueueName + mqttMessage);
    		textPane.setCaretPosition(textPane.getDocument().getLength());
    		textPane.append("Queue " + sQueueName + " : " + mqttMessage + "\n");
        }

        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            System.out.println("Delivery complete");
        }
    }
    
	@Override
	public void run() {
	    String APP_ID = "app:" + UUID.randomUUID().toString();
	
	    MqttClient mqttClient = null;
	    try {
	        mqttClient = new MqttClient(TestLOSamples.SERVER, APP_ID, new MemoryPersistence());
	
	        // register callback (to handle received commands
	        mqttClient.setCallback(new SimpleMqttCallback(mqttClient));
	
	        MqttConnectOptions connOpts = new MqttConnectOptions();
	        connOpts.setUserName("json+bridge"); // selecting mode "Bridge"
	        connOpts.setPassword(TestLOSamples.sAPIKey.toCharArray()); // passing API key value as password
	        connOpts.setCleanSession(true);

	        // Connection
	        System.out.printf("Connecting to broker: %s ...%n", TestLOSamples.SERVER);
	        mqttClient.connect(connOpts);
	        System.out.println("... connected.");
	
	        // Subscribe to commands
	        System.out.println("Subscribing to queue " + sQueueName);
            mqttClient.subscribe(sQueueName);
	        System.out.println(sQueueName + "... subscribed.");
    		textPaneSubscribe.setCaretPosition(textPaneSubscribe.getDocument().getLength());
    		textPaneSubscribe.append("Subscribe to Queue : " + sQueueName + "\n");
	
	        synchronized (mqttClient) {
	            try {
					mqttClient.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	
	    } catch (MqttException me) {
	        me.printStackTrace();
	
	    } finally {
	        // close client
	        if (mqttClient != null && mqttClient.isConnected()) {
	            try {
	                mqttClient.disconnect();
	            } catch (MqttException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	} // Run
}