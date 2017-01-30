
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
	

	final static String SERVER = "tcp://liveobjects.orange-business.com:1883";
//	final static String DEVICE_URN = "urn:lo:nsid:sensor:TESTFLGXXXXn";
	final static String DEVICE_URN_PREFIX = "urn:lo:nsid:sensor:TESTFLG";

	final static String DATA_MODEL = "Sample02";
	final static String DATA_TAG = "Sample.02";
	public static String sStreamID = "StreamSample02";

	final static int TEMPO_ENVOIS = 10000;
    final static int NB_ECHANTILLONS = 100;
    final static int NB_DEVICES = 100;
	
	static TestLOFenetre fenetreTestLOSamples;
    static boolean bPublish = false; 					// false => simulation sur la console
	static String sAPIKey = "Empty => cle.txt"; 		// A récupérer dans la classe CleAPI
	static String sServer = SERVER;
	static String sDeviceUrnPrefix = DEVICE_URN_PREFIX;
	static long lTempoEnvoi = TEMPO_ENVOIS;
    static long lNbEchantillons = NB_ECHANTILLONS;
    static int iNbDevices = NB_DEVICES;
     
	public static double arrondi(double d, int digit)
	{
		double val = d;
		
		val = val*Math.pow(10, digit);
		val = Math.round(val);
		val = val /Math.pow(10, digit);
		
		return val;
	}

	
	/*
	 * Simule l'application de démo OAB
	 */
	public static void SimuleOABApp()
	{
       Thread t;
       
       // Get the Application Key (cle.txt)
       sAPIKey = CleLiveObjectsAPI.GetAPIKey();
       System.out.println("Clé API : " + sAPIKey);
       
	   t = new Thread(new RunOABAppTraffic(DEVICE_URN_PREFIX+"APPOAB00", 2000, 5000, bPublish));
	   t.start();
		
	}

	
	
	public static void main(String[] args) {

       // Récupération clé d'API
		sAPIKey = CleLiveObjectsAPI.GetAPIKey();
		System.out.println("Clé API : " + sAPIKey);
       
		fenetreTestLOSamples = new TestLOFenetre();
		TestLOSamples.fenetreTestLOSamples.textPane.append("Main \n");
	}
	

}
