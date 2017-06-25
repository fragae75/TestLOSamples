/**
 * 
 * The thread simulates a device (terminal) and generates MQTT device trafic
 * 
 * 
 */

package com.test.LOSamples;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import javax.swing.JTextArea;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.google.gson.Gson;


public class RunGenerateTrafic implements Runnable {

	private String sDeviceUrn;
	private String sTopic;
	private long lTempoEnvoi;
	private long lNbEchantillons;
	private boolean bPublish;
	private boolean bDeviceMode;
	private JTextArea textPane;
	
	public RunGenerateTrafic(	String sDeviceUrn,
								String sTopic,
								long lNbEchantillons, 
								long lTempoEnvoi, 
								boolean bDeviceMode,
								boolean bPublish, 
								JTextArea textPane){
		this.sDeviceUrn = sDeviceUrn;
		this.sTopic = sTopic;
		this.lTempoEnvoi = lTempoEnvoi;
		this.lNbEchantillons = lNbEchantillons;
		this.bDeviceMode = bDeviceMode;
		this.bPublish = bPublish;
		this.textPane = textPane;
		
	}

	/**
	 * 
	 * Simulates a container with very basic data : engine on/off, door open on/off, speed, temperature (20°C +/- 3
	 * , hygrometry (50 +/- 20)
	 * 
	 */
	public void run() {
		  
        Random rand = new Random();

        // *** data to push ***
        DeviceData data = new DeviceData();
        
        boolean bEngineOn;
        boolean bDoorOpen;
        int i;
        Double dLocStart[] = { 45.759723, 4.84223 };
        Double dLocNext[] = dLocStart;
        Double dLocStop[] = { 48.872015 + rand.nextDouble()*3, 2.348264 + rand.nextDouble()*3};

    
        // String for encoding to JSON
        String sContent;
        
		// décaler le lancement du thread de 0 à 2 secondes
		try {
			Thread.sleep(1000+rand.nextLong()%1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
        try {
        	MqttClient sampleClient = new MqttClient(TestLOSamples.SERVER, sDeviceUrn, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            if (bPublish)
            {            	
            	if (bDeviceMode)
            		connOpts.setUserName("json+device"); // selecting mode "Device"
            	else
            		connOpts.setUserName("json+bridge"); // selecting mode "Device"
            	
	            connOpts.setPassword(TestLOSamples.sAPIKey.toCharArray()); // passing API key value as password
	            connOpts.setCleanSession(true);
	            connOpts.setKeepAliveInterval(TestLOSamples.MQTT_KEEP_ALIVE);
	
	            // Connection
	            System.out.println("Connecting to broker: " + TestLOSamples.SERVER + " Device mode = " + bDeviceMode);
        		textPane.append("Connecting to broker: " + TestLOSamples.SERVER + " Device mode = " + bDeviceMode + "\n");
	            sampleClient.connect(connOpts);
	            System.out.println("Connected");
        		textPane.append("Connected" + "\n");
            }
            
            for (i=0; i<lNbEchantillons; i++)
            {
            	// engineOn à 66%
            	bEngineOn = rand.nextBoolean() | rand.nextBoolean() ;
            	// streamId
                data.s = TestLOSamples.sStreamID;
                // value: JSON object...
                data.v = new HashMap<String, Object>();
                // Engine off => speed ~ 0
                if (bEngineOn)
                {
                	data.v.put("Speed", rand.nextInt(130));
                	data.v.put("DoorOpen", false);
            		data.v.put("DoorOpenDuration", 0);
                } 
                else
                {
                	data.v.put("Speed", 0);
                	bDoorOpen = rand.nextBoolean() | rand.nextBoolean() | rand.nextBoolean();
                	data.v.put("DoorOpen", bDoorOpen);
                	if (bDoorOpen)
                		data.v.put("DoorOpenDuration", 15 * (1+rand.nextInt(3)));
                	else
                		data.v.put("DoorOpenDuration", 0);
                }
                // 50 +/- 20
                data.v.put("Hygrometry", 50 + rand.nextInt(20));
                // double 
                data.v.put("tempC", TestLOSamples.arrondi(20+(double)(rand.nextInt()%300)/100, 2));
                data.v.put("engineOn", bEngineOn);
                // location (lat/lon)
                dLocNext[0] += (dLocStop[0] - dLocStart[0])/lNbEchantillons;
                dLocNext[1] += (dLocStop[1] - dLocStart[1])/lNbEchantillons;
                data.loc = dLocNext;
//                data.loc = new Double[] { 45.759723, 4.84223 };
                // model
                data.m = TestLOSamples.DATA_MODEL;
                // tags
                data.t = Arrays.asList(TestLOSamples.DATA_TAG);
                // encoding to JSON
                // {"s":"test","m":"Sample02","v":{"Speed":0,"DoorOpen":true,"engineOn":false,"DoorOpenDuration":1,"Hygrometry":55,"tempC":19.19},"t":["sample.01"],"loc":[45.759723,4.84223]}
                sContent = new Gson().toJson(data);


        		// On met le curseur à la fin de la requête précédente
        		textPane.setCaretPosition(textPane.getDocument().getLength());
    	    	LocalDateTime now = LocalDateTime.now();
    	    	String sTime = now.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS ", Locale.FRENCH));
        		
                if (bPublish)
                {
		            // Publish data
                    System.out.print(String.valueOf(i));
                    System.out.println(" - Publishing message: " + sDeviceUrn + " - " + sTopic + " - " + sContent);
            		textPane.append(sTime + String.valueOf(i) + " - Pub msg: " + sDeviceUrn + " - " + sTopic + " - " + sContent + "\n");
		            MqttMessage message = new MqttMessage(sContent.getBytes());
		            message.setQos(0);
		            sampleClient.publish(sTopic, message);
		            System.out.println("Message published "+ sTopic);
                }
                else
                {
                    System.out.print(String.valueOf(i));
                    System.out.println(" - Simulate Publishing message: " + sDeviceUrn + sContent);
            		textPane.append(sTime + String.valueOf(i) + " - Simulate Pub msg: " + sDeviceUrn + sContent + "\n");
                }
                
                // Temporisation entre 2 envois
                try {
					Thread.sleep(lTempoEnvoi);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }

            if (bPublish)
            {
            	// Disconnection
	            sampleClient.disconnect();
	            System.out.println(sDeviceUrn + " - Disconnected");
            }
            //System.exit(0);
            

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
		  
	  } 
	}