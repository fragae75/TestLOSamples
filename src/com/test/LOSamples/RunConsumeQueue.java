package com.test.LOSamples;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.test.LOSamples.TestLOSamples.QueueTypes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Objects;
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

    public class eventPayload{
    	String tenantId;
    	String timeStamp;
    	class firingRule{
    		String Id;
    		String name;
    		boolean enabled;
    		String[] matchingRuleIds;
    		String[] aggregationKeys;
    		String firingType;
    	};
    	String restofpayload;
	};

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

        public void messageArrived(String sQueue, MqttMessage mqttMessage) throws Exception {
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
	        if (sQueue.contentEquals("router/~event/v1/data/eventprocessing/fired"))
	        {
	        	String sLiveObjectsPayload;
	        	JsonObject mqttPayload = gson.fromJson(new String(mqttMessage.getPayload()), JsonObject.class);
	            sLiveObjectsPayload = mqttPayload.get("payload").getAsString();
//	            sTenantId = mqttPayload.get("tenantId").getAsString();
//	            sFiringRule = mqttPayload.get("firingRule").getAsString();
	            System.out.println("Event : " + sLiveObjectsPayload); 
	            checkEventForIFTTT(sLiveObjectsPayload);
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

	
	public static void doPushIFTTT(String sFiringNumber)
	{
		Thread t;
		RunPushIFTTT pushIFTTT = new RunPushIFTTT(
				TestLOSamples.sIFTTTURL,
				sFiringNumber,
				TestLOSamples.fenetreTestLOSamples.textPaneSend,
				TestLOSamples.fenetreTestLOSamples.textPaneReceive,
				TestLOSamples.fenetreTestLOSamples.textPaneIFTTT);

		t = new Thread(pushIFTTT);
		t.start();
        System.out.println("Thread : pushIFTTT");
	}


	/**
	 * 
	 * Try to match the Firing rule event with the IFTTT Firing rule list
	 * 
	 */
	
// Exemple of event payload
	
/* ==> Received message from queue - router/~event/v1/data/eventprocessing/fired : 
 * {"payload": 
 * 		"{
 * 			\"tenantId\":\"56ab3a090cf2ff600fce9ad9\",
 * 			\"timestamp\":\"2017-07-15T10:56:27.124Z\",
 * 			\"firingRule\":
 * 			{
 * 				\"id\":\"0b4e0dcd-4634-4ef2-96ce-7ceefd95b584\",
 * 				\"name\":\"testFR0StreamSample02-01\",
 * 				\"enabled\":true,
 * 				\"matchingRuleIds\": [\"e5ec7927-be87-48dd-be42-8c01d13004d0\"],
 * 				\"aggregationKeys\": [\"metadata.source\"],
 * 				\"firingType\":\"ALWAYS\"
 * 			},
 * 			\"matchingContext\":
 * 			{
 * 				\"tenantId\":\"56ab3a090cf2ff600fce9ad9\",
 * 				\"timestamp\":\"2017-07-15T10:56:27.114Z\",
 * 				\"matchingRule\":
 * 				{
 * 					\"id\":\"e5ec7927-be87-48dd-be42-8c01d13004d0\",
 * 					\"name\":\"Test temperature > 20\",
 * 					\"enabled\":true,
 * 					\"dataPredicate\":
 * 					{
 * 						\">\":[{\"var\":\"value.temperature\"},20]}},
 * 						\"data\":
 * 						{
 * 							\"streamId\":\"android357329073120059\",
 * 							\"timestamp\":\"2017-07-15T10:56:27.106Z\",
 * 							\"location\":{\"lat\":48.872015,\"lon\":2.348264},
 * 							\"model\":\"ModelOABDemoApp00\",
 * 							\"value\":
 * 							{
 * 								\"revmin\":7505,
 * 								\"hygrometry\":98,
 * 								\"temperature\":92},
 * 								\"tags\":[\"OABDemoApp.00\"],
 * 								\"metadata\":
 * 								{
 * 									\"source\":\"URN:LO:NSID:SENSOR:TESTFLGAPPOAB00000\",
 * 									\"connector\":\"mqtt\"
 * 								}
 * 							}
 * 					}
 * 			}"}
*/
	private void checkEventForIFTTT(String sTmp){
		
		int index;
		char c;
		int iTemperature;
		String sFiringNumber = new String("");
		String sTemperatureNumber = new String("");
		String sTemperature = new String (",\"temperature\":");
		String sFiringRules = new String("firingRule\":{\"id\":\"");
//		String sTmp = new String("{\"payload\": \"{\"tenantId\":\"56ab3a090cf2ff600fce9ad9\",\"timestamp\":\"2017-07-15T18:33:58.973Z\",\"firingRule\":{\"id\":\"0b4e0dcd-4634-4ef2-96ce-7ceefd95b584\",\"name\":\"testFR0StreamSample02-01\",\"enabled\":true,\"matchingRuleIds\":[\"e5ec7927-be87-48dd-be42-8c01d13004d0\"],\"aggregationKeys\":[\"metadata.source\"],\"firingType\":\"ALWAYS\"},\"matchingContext\":{\"tenantId\":\"56ab3a090cf2ff600fce9ad9\",\"timestamp\":\"2017-07-15T18:33:58.971Z\",\"matchingRule\":{\"id\":\"e5ec7927-be87-48dd-be42-8c01d13004d0\",\"name\":\"Test temperature > 20\",\"enabled\":true,\"dataPredicate\":{\">\":[{\"var\":\"value.temperature\"},20]}},\"data\":{\"streamId\":\"android357329073120059\",\"timestamp\":\"2017-07-15T18:33:58.962Z\",\"location\":{\"lat\":48.872015,\"lon\":2.348264},\"model\":\"ModelOABDemoApp00\",\"value\":{\"revmin\":6082,\"hygrometry\":6,\"temperature\":59},\"tags\":[\"OABDemoApp.00\"],\"metadata\":{\"source\":\"URN:LO:NSID:SENSOR:TESTFLGAPPOAB00000\",\"connector\":\"mqtt\"}}}}\"}");
		//"{"tenantId":"56ab3a090cf2ff600fce9ad9","timestamp":"2017-07-15T18:33:58.973Z","firingRule":{"id":"0b4e0dcd-4634-4ef2-96ce-7ceefd95b584","name":"testFR0StreamSample02-01","enabled":true,"matchingRuleIds":["e5ec7927-be87-48dd-be42-8c01d13004d0"],"aggregationKeys":["metadata.source"],"firingType":"ALWAYS"},"matchingContext":{"tenantId":"56ab3a090cf2ff600fce9ad9","timestamp":"2017-07-15T18:33:58.971Z","matchingRule":{"id":"e5ec7927-be87-48dd-be42-8c01d13004d0","name":"Test temperature > 20","enabled":true,"dataPredicate":{">":[{"var":"value.temperature"},20]}},"data":{"streamId":"android357329073120059","timestamp":"2017-07-15T18:33:58.962Z","location":{"lat":48.872015,"lon":2.348264},"model":"ModelOABDemoApp00","value":{"revmin":6082,"hygrometry":6,"temperature":59},"tags":["OABDemoApp.00"],"metadata":{"source":"URN:LO:NSID:SENSOR:TESTFLGAPPOAB00000","connector":"mqtt"}}}}";
//		sTmp.contains("firingRule\":{\"id\":\"");
		// Firing Number
		index = sTmp.indexOf(sFiringRules);
		// Found Firing rule
		if (index != -1)
		{
			index += sFiringRules.length();
			for (c=sTmp.charAt(index); c!='\"'; index++, c=sTmp.charAt(index))
			{
				sFiringNumber += sTmp.charAt(index);
			}
			
			ListIterator li = TestLOSamples.lIFTTTEvents.listIterator();
			String str = new String();
			while(li.hasNext()){
		        str = (String)li.next();
				if (Objects.equals(sFiringNumber, str)){
		        	System.out.println(str);
		        	doPushIFTTT(sFiringNumber);
		        }
			}

		}
		// Temperature
		iTemperature = 0;
		index = sTmp.indexOf(sTemperature);
		if (index != -1)
		{
			index += sTemperature.length();
			for (c=sTmp.charAt(index); c!='}'; index++, c=sTmp.charAt(index))
			{
				sTemperatureNumber += sTmp.charAt(index);
			}
			iTemperature = (int)Integer.valueOf(sTemperatureNumber);
		}
	}
}