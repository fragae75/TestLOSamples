/*
 * 
 * Main class
 * 
 * 
 */


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

	final static String API_KEY_FILE = "cle.txt";
	final static String API_LORA_KEY_FILE = "cleLora.txt";
	
	final static String DEFAULT_DEVICE_TOPIC = "dev/data";
	final static String DATA_MODEL = "Sample02";
	final static String DATA_TAG = "Sample.02";
	public static String sStreamID = "StreamSample02";
	public static String sStreamIDOAB = "android357329073120059";
	public static String sDataModelOAB = "ModelOABDemoApp00";
	public static String sDataTagOAB = "OABDemoApp.00";

	final static int MQTT_KEEP_ALIVE = 28;
	
	final static int TEMPO_ENVOIS = 1000;
    final static int NB_ECHANTILLONS = 1;
    final static int NB_DEVICES = 1;
    final static int NB_DEVICES_OAB = 1;
    final static int NB_MAX_DEVICES = 1000;
	
    final static String DEFAULT_FIFO = "flgFifo";
    final static String DEFAULT_PUBSUB = "data";
    final static String DEFAULT_ROUTER = "~event/v1/data/new/#";
    // For a specific device "~event/v1/data/new/Topicxxx";
    // For all LoRa devices
    final static String DEFAULT_LORA_ROUTER = "~event/v1/data/new/urn/lora/#";
    // For a specific device
    // final String ROUTING_KEY_FILTER = "router/~event/v1/data/new/urn/lora/0123456789ABCDEF/#";
    final static String DEFAULT_LORA_FIFO = "flglorafifo";
    final static String[] LISTE_ROUTERS = {
    		DEFAULT_ROUTER, 
    		"~event/v1/data/eventprocessing/fired", 
    		"~event/v1/data/new/urn/msisdn",
    		"~event/v1/log/new/mqtt"};
//    		"~event/v2/assets/{ns}/{id}/connected",
//    		"~event/v2/assets/{ns}/{id}/disconnected",
//    		"~event/v2/assets/{ns}/{id}/currentParams",
//    		"~event/v0/assets/{ns}/{id}/currentResources"};
    final static String[] LISTE_LORA_ROUTERS = {DEFAULT_LORA_ROUTER};

	static TestLOFenetre fenetreTestLOSamples;
    static boolean bPublish = false; 					// false => simulation sur la console
	static String sAPIKey = "Empty => cle.txt"; 		// A récupérer dans la classe CleAPI
	static String sAPILoraKey = "Empty => cleLora.txt"; // A récupérer dans la classe CleAPI
	static String sServer = SERVER;
	static String sDeviceUrnPrefix = DEVICE_URN_PREFIX;
	static String sDeviceTopic = DEFAULT_DEVICE_TOPIC;
	static boolean bDeviceMode = true;
	static long lTempoEnvoi = TEMPO_ENVOIS;
    static long lNbDataPerDevice= NB_ECHANTILLONS;
    static int nbDevices = NB_DEVICES;
	static long lTempoEnvoiOAB = TEMPO_ENVOIS;
    static long lNbDataPerDeviceOAB = NB_ECHANTILLONS;
    static int nbDevicesOAB = NB_DEVICES_OAB;

    static String sQueueName = DEFAULT_FIFO;
    static QueueTypes queueType = QueueTypes.FIFO;
    
    // Push AirParif values
	final static String DEVICE_URN_PUSH_AIRPARIF = "TESTFLG-PUSHAirParif"; 
	final static String DEFAULT_DEVICE_TOPIC_PUSH = "dev/data";
	final static String DATA_MODEL_PUSH = "SampleAirParif03";
	final static String DATA_TAG_PUSH = "SampleAirParif.03";
    final static int PUSH_MIN_PERIOD_VALUE = 100;
    final static int PUSH_PERIOD_VALUE = 1000; 
    
	static boolean bDeviceModePush = true;
	static String sDeviceTopicPush = DEFAULT_DEVICE_TOPIC_PUSH;
	public static String sStreamIDPush = "StreamPushAirParif";
	static String sDeviceUrnPush = DEVICE_URN_PUSH_AIRPARIF;
    static String sCSVFilePush = "20160601_20170616-PA04C_auto.csv";
    static long lTempoPush = PUSH_PERIOD_VALUE;
    static String sTown = "Paris Centre";
    
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
		ROUTER,
		LORA_ROUTER,
		LORA_FIFO;
	}

	
	
	public static void main(String[] args) {

       // Récupération clé d'API
		sAPIKey = CleLiveObjectsAPI.GetAPIKey(API_KEY_FILE);
		System.out.println("Clé API : " + sAPIKey);
		sAPILoraKey = CleLiveObjectsAPI.GetAPIKey(API_LORA_KEY_FILE);
		System.out.println("Clé API Lora : " + sAPILoraKey);
       
		fenetreTestLOSamples = new TestLOFenetre();
	}
	

}
