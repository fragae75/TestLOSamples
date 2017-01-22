
package com.test.LOSamples;


import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class TestLOSamples {
	
    final static boolean B_PUBLISH = false; 	// false => simulation sur la console
	static String API_KEY = ""; 				// A récupérer dans la classe CleAPI

	final static String SERVER = "tcp://liveobjects.orange-business.com:1883";
//	final static String DEVICE_URN = "urn:lo:nsid:sensor:TESTFLGXXXXn";
	final static String DEVICE_URN_PREFIX = "urn:lo:nsid:sensor:TESTFLG";

	final static String DATA_MODEL = "Sample02";
	final static String DATA_TAG = "Sample.02";
	final static String STREAM_ID = "StreamSample02";

	final static int TEMPO_ENVOIS = 10000;
    final static int NB_ECHANTILLONS = 100;
    final static int NB_DEVICES = 100;
	
     
	public static double arrondi(double d, int digit)
	{
		double val = d;
		
		val = val*Math.pow(10, digit);
		val = Math.round(val);
		val = val /Math.pow(10, digit);
		
		return val;
	}

	/*
	 * Simule un groupe de devices fictifs
	 */
	public static void SimuleDevices()
	{
       Random rand = new Random();
       int i;
       Thread t[] = new Thread[NB_DEVICES];
       String sURNDevice;

        
       // Get the Application Key
       API_KEY = CleLiveObjectsAPI.GetAPIKey();
       System.out.println("Clé API : " + API_KEY);
        		
       // Création de NB_DEVICES
       for (i=0; i<NB_DEVICES; i++){
    	   sURNDevice = String.format("%S%05d", DEVICE_URN_PREFIX, i);
    	   t[i] = new Thread(new RunGenerateTrafic(sURNDevice, NB_ECHANTILLONS, TEMPO_ENVOIS, B_PUBLISH));
    	   t[i].start();

    	   // 0 to 2 sec delay between the device creations
    	   try {
    		   Thread.sleep(1000+rand.nextLong()%1000);
    	   } catch (InterruptedException e) {
    		   // TODO Auto-generated catch block
    		   e.printStackTrace();
    	   }
       }
	}
	
	/*
	 * Simule l'application de démo OAB
	 */
	public static void SimuleOABApp()
	{
       Thread t;
       
       // Get the Application Key
       API_KEY = CleLiveObjectsAPI.GetAPIKey();
       System.out.println("Clé API : " + API_KEY);
       
	   t = new Thread(new RunOABAppTraffic(DEVICE_URN_PREFIX+"APPOAB00", 2000, 5000, B_PUBLISH));
	   t.start();
		
	}

	
	
	public static void main(String[] args) {

		//Simuler un parc de devices
    	SimuleDevices();
    	// Simuler l'appli Android OAB
//    	SimuleOABApp();
	}
	

}
