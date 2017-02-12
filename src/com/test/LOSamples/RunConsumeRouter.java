
package com.test.LOSamples;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

import javax.swing.JTextArea;

/**
 * Application connects to LO and consumes messages from a FIFO queue.
 *
 * You MUST first create a FIFO called "~data" in your LO account.
 *
 */
public class RunConsumeRouter implements Runnable {

	private static JTextArea textPane;
	private static String sRouterName;
	
	public RunConsumeRouter(String sRouterName, 
							JTextArea textPane){
		this.textPane = textPane;
		this.sRouterName = sRouterName;
	}
	
    /**
     * Basic "MqttCallback" that handles messages as JSON device commands,
     * and immediately respond.
     */
    public static class SimpleMqttCallback implements MqttCallback {
        private MqttClient mqttClient;

        public SimpleMqttCallback(MqttClient mqttClient) {
            this.mqttClient = mqttClient;
        }

        public void connectionLost(Throwable throwable) {
            System.out.println("Connection lost");
            mqttClient.notifyAll();
        }

        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
            System.out.println("Received message from FIFO queue - " + mqttMessage);
    		textPane.setCaretPosition(textPane.getDocument().getLength());
    		textPane.append("Received message from FIFO queue - " + mqttMessage + "\n");
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
            final String ROUTING_KEY_FILTER = "~event/v1/data/new/#";
            System.out.printf("Consuming from Router with filter '%s'...%n", ROUTING_KEY_FILTER);
            mqttClient.subscribe(String.format("router/%s", ROUTING_KEY_FILTER));
            System.out.println("... subscribed.");
	
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