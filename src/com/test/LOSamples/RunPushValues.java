package com.test.LOSamples;

import javax.swing.JTextArea;

import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.google.gson.Gson;

public class RunPushValues implements Runnable {
	private String sCSVFilePush;
	private long lTempoPush;
	private String sStreamIDPush;
	private String sDeviceTopicPush;
	private String sDeviceUrnPrefixPush;
	private boolean bPublishPush;
	private boolean bDeviceModePush;
	private String sTown;
	private JTextArea textPane;
	

	public RunPushValues (	String sCSVFilePush, 
							long lTempoPush, 
							String sStreamIDPush, 
							String sDeviceTopicPush,
							String sDeviceUrnPrefixPush, 
							boolean bDeviceModePush,
							boolean bPublish,
							String sTown,
							JTextArea textPaneReceive)
	{
		this.sCSVFilePush = sCSVFilePush;
		this.lTempoPush = lTempoPush;
		this.sStreamIDPush = sStreamIDPush;
		this.sDeviceTopicPush = sDeviceTopicPush;
		this.sDeviceUrnPrefixPush = sDeviceUrnPrefixPush;
		this.bDeviceModePush = bDeviceModePush;
		this.bPublishPush = bPublish;
		this.sTown = sTown;
		this.textPane = textPaneReceive;
	}

	public static boolean isValidDate(String sDate) {
	    boolean bValid = false;

	    try {
		    bValid = sDate.charAt(2)=='/' & sDate.charAt(5)=='/';
	    } catch (Exception ignore) {}

	    return bValid;
	}
	
	
	public void run() {
		
		
        // *** data to push ***
        DeviceData data = new DeviceData();
        
        // String for encoding to JSON
        String sContent;

		CSVReader reader = null;

        try {
        	MqttClient sampleClient = new MqttClient(TestLOSamples.SERVER, sDeviceUrnPrefixPush, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            if (bPublishPush)
            {            	
            	if (bDeviceModePush)
            		connOpts.setUserName("json+device"); // selecting mode "Device"
            	else
            		connOpts.setUserName("json+bridge"); // selecting mode "Device"
            	
	            connOpts.setPassword(TestLOSamples.sAPIKey.toCharArray()); // passing API key value as password
	            connOpts.setCleanSession(true);
	            connOpts.setKeepAliveInterval(TestLOSamples.MQTT_KEEP_ALIVE);
	
	            // Connection
	            System.out.println("Connecting to broker: " + TestLOSamples.SERVER + " Device mode = " + bDeviceModePush);
        		textPane.append("Connecting to broker: " + TestLOSamples.SERVER + " Device mode = " + bDeviceModePush + "\n");
	            sampleClient.connect(connOpts);
	            System.out.println("Connected");
        		textPane.append("Connected" + "\n");
            }
            
    		try {
    			reader = new CSVReader(new FileReader(sCSVFilePush), ';');
    			String [] nextLine;
    			try {
    				// Start parsing values
    				while ((nextLine = reader.readNext()) != null) {
    	            	// streamId
    	                data.s = sStreamIDPush;
    	                // value: JSON object...
    	                data.v = new HashMap<String, Object>();
    					
    	                String sDate = nextLine[0];
    	                
    	                if (isValidDate(sDate) && nextLine.length == 7)
    	                {
        	                String [] sNumbers = sDate.split("/");
    	                	String sHour = nextLine[1];
    	                	
    	                	String sFormatedDate = String.format("%s-%s-%s %02d:00:00", sNumbers[2], sNumbers[1], sNumbers[0], Integer.parseInt(sHour));

//    	                	data.ts = sFormatedDate;
    	                	
    	                	Timestamp timestamp = Timestamp.valueOf(sFormatedDate); 
    	                	Instant instant = timestamp.toInstant();
    	                	String myTimestamp = instant.toString();
    	                	data.ts = instant.toString() ;
    	                	
    	                	/*
    	                	Timestamp timestamp = Timestamp.valueOf("2014-01-01 00:00:00"); 
    	                	Timestamp timestamp = new Timestamp(1388552400); 

    	                	*/
    	                	data.v.put("Town", sTown);
    	                	try{
    	                		data.v.put("PM25", Integer.valueOf(nextLine[2]));
        	    			} catch (NumberFormatException e) {
    	                		data.v.put("PM25", 0);
        	    			}
    	                	try{
        	                	data.v.put("PM10", Integer.valueOf(nextLine[3]));
	    	    			} catch (NumberFormatException e) {
	    	                	data.v.put("PM10", 0);
	    	    			}
    	                	try{
        	                	data.v.put("O3", Integer.valueOf(nextLine[4]));
	    	    			} catch (NumberFormatException e) {
	    	                	data.v.put("O3", 0);
	    	    			}
    	                	try{
        	                	data.v.put("N02", Integer.valueOf(nextLine[5]));
	    	    			} catch (NumberFormatException e) {
	    	                	data.v.put("N02", 0);
	    	    			}
    	                	try{
        	                	data.v.put("CO", Integer.valueOf(nextLine[6]));
	    	    			} catch (NumberFormatException e) {
	    	                	data.v.put("CO", 0);
	    	    			} 
    	                	
    	                	
	    	                // model
	    	                data.m = TestLOSamples.DATA_MODEL_PUSH;
	    	                // tags
	    	                data.t = Arrays.asList(TestLOSamples.DATA_TAG_PUSH);
	    	                // encoding to JSON
	    	                // {"s":"test","m":"Sample02","v":{"Speed":0,"DoorOpen":true,"engineOn":false,"DoorOpenDuration":1,"Hygrometry":55,"tempC":19.19},"t":["sample.01"],"loc":[45.759723,4.84223]}
	    	                sContent = new Gson().toJson(data);
	
	
	    	        		// On met le curseur � la fin de la requ�te pr�c�dente
	    	        		textPane.setCaretPosition(textPane.getDocument().getLength());
	    	    	    	LocalDateTime now = LocalDateTime.now();
	    	    	    	String sTime = now.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS ", Locale.FRENCH));
	    	        		
	    	                if (bPublishPush)
	    	                {
	    			            // Publish data
	    	                    System.out.println(" - Publishing message: " + sDeviceUrnPrefixPush + " - " + sDeviceTopicPush + " - " + sContent);
	    	            		textPane.append(sTime + " - Pub msg: " + sDeviceUrnPrefixPush + " - " + sDeviceTopicPush + " - " + sContent + "\n");
	    			            MqttMessage message = new MqttMessage(sContent.getBytes());
	    			            message.setQos(0);
	    			            sampleClient.publish(sDeviceTopicPush, message);
	    			            System.out.println("Message published "+ sDeviceTopicPush);
	    	                }
	    	                else
	    	                {
	    	                    System.out.println(" - Simulate Publishing message: " + sDeviceUrnPrefixPush  + sContent);
	    	            		textPane.append(sTime + " - Simulate Pub msg: " + sDeviceUrnPrefixPush  + sContent + "\n");
	    	                }
	    	                
	    	                // Temporisation entre 2 envois
	    	                try {
	    						Thread.sleep(lTempoPush);
	    					} catch (InterruptedException e) {
	    						// TODO Auto-generated catch block
	    						e.printStackTrace();
	    					}

    	                } // if
    	                
    					System.out.println("");
    	                
    				} // While
    				
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

  /*  		
            for (i=0; i<lNbEchantillons; i++)
            {
            	// engineOn � 66%
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


        		// On met le curseur � la fin de la requ�te pr�c�dente
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
*/
            if (bPublishPush)
            {
            	// Disconnection
	            sampleClient.disconnect();
	            System.out.println(sDeviceUrnPrefixPush + " - Disconnected");
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
