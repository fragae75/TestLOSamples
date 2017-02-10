package com.test.LOSamples;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JTextArea;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.google.gson.Gson;

/* 
 * 
 * Simuler le fonctionnement de l'application de démo Android de OAB 
19/01/2017 10:34:27 	android357329073120059 	{"metadata":null,"streamId":"android357329073120059","created":"2017-01-19T09:34:40.555Z","location":{"lon":2.2953308,"lat":48.8007208},"model":"","id":"588088300cf21dcc553912af","value":{"revmin":0,"temperature":-20,"hygrometry":0},"timestamp":"2017-01-19T09:34:27Z","tags":null} 	
19/01/2017 10:34:27 	android357329073120059 	{"metadata":null,"streamId":"android357329073120059","created":"2017-01-19T09:34:40.557Z","location":{"lon":2.2953308,"lat":48.8007208},"model":"","id":"588088300cf21dcc553912b0","value":{"revmin":0,"temperature":-20,"hygrometry":0},"timestamp":"2017-01-19T09:34:27Z","tags":null}
19/01/2017 10:34:17 	android357329073120059 	{"metadata":null,"streamId":"android357329073120059","created":"2017-01-19T09:34:24.187Z","location":{"lon":2.2953308,"lat":48.8007208},"model":"","id":"588088200cf2b77e7d4a7f91","value":{"revmin":9999,"temperature":120,"hygrometry":100},"timestamp":"2017-01-19T09:34:17Z","tags":null} 
*
*/

public class RunOABAppTraffic  implements Runnable {

	private String sDeviceUrn;
	private String sStreamId = "android357329073120059";
	private String sDataModel = "ModelOABDemoApp00";
	private String sDataTag = "OABDemoApp.00";
	private long lTempoEnvoi;
	private long lNbEchantillons;
	private boolean bPublish;
	private JTextArea textPane;
	
	public RunOABAppTraffic (	String sDeviceUrn, 
								long lNbEchantillons, 
								long lTempoEnvoi, 
								boolean bPublish, 
								JTextArea textPane){
		this.sDeviceUrn = sDeviceUrn;
		this.lTempoEnvoi = lTempoEnvoi;
		this.lNbEchantillons = lNbEchantillons;
		this.bPublish = bPublish;
		this.textPane = textPane;
	}

	public void run() {
		  
        Random rand = new Random();

        // *** data to push ***
        DeviceData data = new DeviceData();
        
        int i;
        // Start = Lyon, Stop = Paris
        Double dLocStart[] = { 45.759723, 4.84223 };
        Double dLocNext[] = dLocStart;
        Double dLocStop[] = { 48.872015, 2.348264 };

    
        // String for encoding to JSON
        String sContent;
        
        try {
        	MqttClient sampleClient = new MqttClient(TestLOSamples.SERVER, sDeviceUrn, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            if (bPublish)
            {
	            connOpts.setUserName("json+device"); // selecting mode "Device"
	            connOpts.setPassword(TestLOSamples.sAPIKey.toCharArray()); // passing API key value as password
	            connOpts.setCleanSession(true);
	
	            // Connection
	            System.out.println("Connecting to broker: " + TestLOSamples.SERVER);
        		textPane.append("Connecting to broker: " + TestLOSamples.SERVER + "\n");
	            sampleClient.connect(connOpts);
	            System.out.println("Connected");
        		textPane.append("Connected" + "\n");
            }
            
            for (i=0; i<lNbEchantillons; i++)
            {
            	// streamId
                data.s = sStreamId;
                // value: JSON object...
                data.v = new HashMap<String, Object>();
                // Hygrometrie : 0 - 100
            	data.v.put("hygrometry", rand.nextInt(140) - 20);
            	// T° de -20 à 120
            	data.v.put("temperature", rand.nextInt(140) - 20);
        		// Rev/min : 0 - 9999
            	data.v.put("revmin", rand.nextInt(9999));
                // location (lat/lon)
                dLocNext[0] += (dLocStop[0] - dLocStart[0])/lNbEchantillons;
                dLocNext[1] += (dLocStop[1] - dLocStart[1])/lNbEchantillons;
                data.loc = dLocNext;
                // model
                data.m = sDataModel;
                // tags
                data.t = Arrays.asList(sDataTag);
                // encoding to JSON
                // {"s":"test","m":"Sample02","v":{"Speed":0,"DoorOpen":true,"engineOn":false,"DoorOpenDuration":1,"Hygrometry":55,"tempC":19.19},"t":["sample.01"],"loc":[45.759723,4.84223]}
                sContent = new Gson().toJson(data);
                
        		// On met le curseur à la fin de la requête précédente
        		textPane.setCaretPosition(textPane.getDocument().getLength());
        		
                 if (bPublish)
                {
		            // Publish data
                    System.out.print(String.valueOf(i));
                    System.out.println(" - Publishing message: " + sDeviceUrn + sContent);
            		textPane.append(String.valueOf(i) + " - Pub msg: " + sDeviceUrn + sContent + "\n");
		            MqttMessage message = new MqttMessage(sContent.getBytes());
		            message.setQos(0);
		            sampleClient.publish("dev/data", message);
		            System.out.println("Message published");
                }
                else
                {
                    System.out.print(String.valueOf(i));
                    System.out.println(" - Simulate Publishing message: " + sDeviceUrn + sContent);
            		textPane.append(String.valueOf(i) + " - Simulate Pub msg: " + sDeviceUrn + sContent + "\n");
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
	            System.out.println("Disconnected");
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