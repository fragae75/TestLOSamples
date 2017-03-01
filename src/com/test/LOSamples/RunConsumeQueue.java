package com.test.LOSamples;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.test.LOSamples.TestLOSamples.QueueTypes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

import javax.swing.JTextArea;

/**
 * 
 * 
 * the thread connects to LO in bridge mode and consumes messages from a queue.
 * - if Fifo : You MUST first create the FIFO in your LO account and bind it from a route, ex : ~event.v1.data.new.#
 * - if pubsub : 
 * 
 */
public class RunConsumeQueue implements Runnable {

	private JTextArea textPane;
	private String sQueueName;
	private JTextArea textPaneSubscribe;
	private QueueTypes queueType;
    private MqttClient mqttClient = null;
	
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
			case LORA_ROUTER:
				this.sQueueName = "router/" + sTopicName;
			break;
			case LORA_FIFO:
				this.sQueueName = "fifo/" + sTopicName;
			break;
		}
		
		this.textPaneSubscribe = textPaneSubscribe;
	}
	
	public void finalize(){
		
        System.out.println(sQueueName + " - Finalize");
        // close client
        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                mqttClient.disconnect();
	            System.out.println(sQueueName + " - Queue Disconnected");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
	}
	
    /**
     * Basic "MqttCallback" that handles messages as JSON device commands,
     * and immediately respond.
     */
    public class SimpleMqttCallback implements MqttCallback {
        private MqttClient mqttClient;
        private Gson gson = new Gson();

        public SimpleMqttCallback(MqttClient mqttClient) {
            this.mqttClient = mqttClient;
        }

        public void connectionLost(Throwable throwable) {
            System.out.println(sQueueName +" : Event Connection lost");
            mqttClient.notifyAll();
        }

        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
	    	LocalDateTime now = LocalDateTime.now();
	    	String sTime = now.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS ", Locale.FRENCH));
	    	
            System.out.println("Received message from queue - " + sQueueName + " : " + mqttMessage);
    		textPane.setCaretPosition(textPane.getDocument().getLength());
    		textPane.append(sTime + " Queue " + sQueueName + " : " + mqttMessage + "\n");
    		
            // Decoding the LoRa message
	        if (queueType == QueueTypes.LORA_ROUTER || queueType == QueueTypes.LORA_FIFO)
	        {
	            JsonObject mqttPayload = gson.fromJson(new String(mqttMessage.getPayload()), JsonObject.class);
//	            String liveObjectsPayload = mqttPayload.get("payload").getAsString();
//	            LoraData loraData = gson.fromJson(liveObjectsPayload, LoraData.class);
//	            System.out.println("Decoded LoRa message - " + loraData);
	        }

        }

        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            System.out.println("Delivery complete");
        }
    }
    
	@Override
	public void run() {
	    String APP_ID = "app:" + UUID.randomUUID().toString();
	
//	    MqttClient mqttClient = null;
	    try {
	    	LocalDateTime now = LocalDateTime.now();
	    	String sTime = now.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS ", Locale.FRENCH));

	        mqttClient = new MqttClient(TestLOSamples.SERVER, APP_ID, new MemoryPersistence());
	
	        // register callback (to handle received commands
	        mqttClient.setCallback(new SimpleMqttCallback(mqttClient));
	
	        MqttConnectOptions connOpts = new MqttConnectOptions();
	        connOpts.setUserName("json+bridge"); // selecting mode "Bridge"
	        // Can be 2 different keys between regular LO device vs Lora device
	        if (queueType == QueueTypes.LORA_ROUTER || queueType == QueueTypes.LORA_FIFO)
	        {
		        System.out.println("LORA API Key");
	        	connOpts.setPassword(TestLOSamples.sAPILoraKey.toCharArray()); // passing API key value as password
	        }
	        else
	        {
		        System.out.println("LiveObjects API Key");
	        	connOpts.setPassword(TestLOSamples.sAPIKey.toCharArray()); // passing API key value as password
	        }
	        connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(TestLOSamples.MQTT_KEEP_ALIVE);
 
	        // Connection
	        System.out.printf("Connecting to broker: %s ...%n", TestLOSamples.SERVER);
	        mqttClient.connect(connOpts);
	        System.out.println("... connected.");
	
	        // Subscribe to commands
	        System.out.println("Subscribing to queue " + sQueueName);
            mqttClient.subscribe(sQueueName);
	        System.out.println(sQueueName + "... subscribed.");
    		textPaneSubscribe.setCaretPosition(textPaneSubscribe.getDocument().getLength());
    		textPaneSubscribe.append(sTime + " Subscribe to Queue : " + sQueueName + "\n");
	
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
		            System.out.println(sQueueName + " - Queue Disconnected");
	            } catch (MqttException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	} // Run
}