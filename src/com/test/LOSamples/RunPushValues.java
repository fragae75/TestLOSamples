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

/**
 * 
 * @author flg
 * thread for pushing Airparif pollution data
 */
public class RunPushValues implements Runnable {
	private String sCSVFilePush;
	private long lTempoPush;
	private String sStreamIDPush;
	private String sDeviceTopicPush;
	private String sDeviceUrnPush;
	private boolean bPublishPush;
	private boolean bDeviceModePush;
	private String sTown;
	private JTextArea textPane;
	
	/**
	 * 
	 * Put parameters on private variables
	 * 
	 * @param sCSVFilePush : CSV file
	 * @param lTempoPush : tempo (ms) to not overload the platform
	 * @param sStreamIDPush : stream ID
	 * @param sDeviceTopicPush : topic => dev/data
	 * @param sDeviceUrnPush : device urn
	 * @param bDeviceModePush : device/bridge mode
	 * @param bPublish : really publish or simulate
	 * @param sTown : location measurement
	 * @param textPaneReceive : windows edit panel
	 */
	public RunPushValues (	String sCSVFilePush, 
							long lTempoPush, 
							String sStreamIDPush, 
							String sDeviceTopicPush,
							String sDeviceUrnPush, 
							boolean bDeviceModePush,
							boolean bPublish,
							String sTown,
							JTextArea textPaneReceive)
	{
		this.sCSVFilePush = sCSVFilePush;
		this.lTempoPush = lTempoPush;
		this.sStreamIDPush = sStreamIDPush;
		this.sDeviceTopicPush = sDeviceTopicPush;
		this.sDeviceUrnPush = sDeviceUrnPush;
		this.bDeviceModePush = bDeviceModePush;
		this.bPublishPush = bPublish;
		this.sTown = sTown;
		this.textPane = textPaneReceive;
	}

	/*
	 * 
	 * Only check the 2 1st '/' of a date
	 * This will skip the 2 first CSV lines
	 * 
	 */
	public static boolean isValidDate(String sDate) {
	    boolean bValid = false;

	    try {
		    bValid = sDate.charAt(2)=='/' & sDate.charAt(5)=='/';
	    } catch (Exception ignore) {}

	    return bValid;
	}
	
	/**
	 * 
	 * Generate time stamped pollution values from Airparif csv files
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		
        // *** data to push ***
        DeviceData data = new DeviceData();
        
        // String for encoding to JSON
        String sContent;

        /**
         * CSV Parser
         */
        CSVReader reader = null;

        try {
        	// Paho MQTT client
        	MqttClient sampleClient = new MqttClient(TestLOSamples.SERVER, sDeviceUrnPush, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            
            // no simulation : really publish ! => connect the MQTT client
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
    	                
    	                // beware of isValidDate() not bullet proof !
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
	
	
	    	        		// On met le curseur à la fin de la requête précédente
	    	        		textPane.setCaretPosition(textPane.getDocument().getLength());
	    	    	    	LocalDateTime now = LocalDateTime.now();
	    	    	    	String sTime = now.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS ", Locale.FRENCH));
	    	        		
	    	                if (bPublishPush)
	    	                {
	    			            // Publish data
	    	                    System.out.println(" - Publishing message: " + sDeviceUrnPush + " - " + sDeviceTopicPush + " - " + sContent);
	    	            		textPane.append(sTime + " - Pub msg: " + sDeviceUrnPush + " - " + sDeviceTopicPush + " - " + sContent + "\n");
	    			            MqttMessage message = new MqttMessage(sContent.getBytes());
	    			            message.setQos(0);
	    			            sampleClient.publish(sDeviceTopicPush, message);
	    			            System.out.println("Message published "+ sDeviceTopicPush);
	    	                }
	    	                else
	    	                {
	    	                    System.out.println(" - Simulate Publishing message: " + sDeviceUrnPush  + sContent);
	    	            		textPane.append(sTime + " - Simulate Pub msg: " + sDeviceUrnPush  + sContent + "\n");
	    	                }
	    	                
	    	                // delay between 2 push
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

            if (bPublishPush)
            {
            	// Disconnection
	            sampleClient.disconnect();
	            System.out.println(sDeviceUrnPush + " - Disconnected");
            }
            

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
