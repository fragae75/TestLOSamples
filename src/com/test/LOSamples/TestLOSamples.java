
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
	final static String DEVICE_URN_PREFIX = "urn:lo:nsid:sensor:TESTFLG"; // => "urn:lo:nsid:sensor:TESTFLGXXXXn";
	final static String DEVICE_URN_PREFIX_OAB = "urn:lo:nsid:sensor:TESTFLGAPPOAB"; // => "urn:lo:nsid:sensor:TESTFLGXXXXn";
	
	final static String DATA_MODEL = "Sample02";
	final static String DATA_TAG = "Sample.02";
	public static String sStreamID = "StreamSample02";
	public static String sStreamIDOAB = "android357329073120059";
	public static String sDataModelOAB = "ModelOABDemoApp00";
	public static String sDataTagOAB = "OABDemoApp.00";

	final static int TEMPO_ENVOIS = 1000;
    final static int NB_ECHANTILLONS = 110;
    final static int NB_DEVICES = 100;
    final static int NB_DEVICES_OAB = 1;
    final static int NB_MAX_DEVICES = 1000;
	
	static TestLOFenetre fenetreTestLOSamples;
    static boolean bPublish = false; 					// false => simulation sur la console
	static String sAPIKey = "Empty => cle.txt"; 		// A récupérer dans la classe CleAPI
	static String sServer = SERVER;
	static String sDeviceUrnPrefix = DEVICE_URN_PREFIX;
	static long lTempoEnvoi = TEMPO_ENVOIS;
    static long lNbDataPerDevice= NB_ECHANTILLONS;
    static int nbDevices = NB_DEVICES;
	static long lTempoEnvoiOAB = TEMPO_ENVOIS;
    static long lNbDataPerDeviceOAB = NB_ECHANTILLONS;
    static int nbDevicesOAB = NB_DEVICES_OAB;

    static String sFifoName = "myFifo";
    static QueueTypes queueType = QueueTypes.PUBSUB;
    
	public static double arrondi(double d, int digit)
	{
		double val = d;
		
		val = val*Math.pow(10, digit);
		val = Math.round(val);
		val = val /Math.pow(10, digit);
		
		return val;
	}

	public enum QueueTypes{
		PUBSUB,
		FIFO,
		ROUTER;
	}

	
	
	public static void main(String[] args) {

       // Récupération clé d'API
		sAPIKey = CleLiveObjectsAPI.GetAPIKey();
		System.out.println("Clé API : " + sAPIKey);
       
		fenetreTestLOSamples = new TestLOFenetre();
	}
	

}
