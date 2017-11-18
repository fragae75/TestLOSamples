package com.test.LOSamples;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JTextArea;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.google.gson.Gson;


/**
 * @author franc
 *
 */
/**
 * @author franc
 *
 */
/**
 * @author franc
 *
 */
/**
 * @author franc
 *
 */
public class RunUDPToMQTT implements Runnable {

	private short iUDPPort;
	private String sDataModel; 
	private String sDataTag;
	private Boolean bDeviceMode;
//	private Boolean bPublish;
	private String sDeviceUrn;
	private String sStreamID;
	private String sTopic;
	private JTextArea textPaneSend;
	private JTextArea textPaneReceive;


	public RunUDPToMQTT  (
//			Boolean bPublish,
			Boolean bDeviceMode,
			String sDeviceUrn,
			String sStreamID,
			String sTopic,
			short iUDPPort, 
			String sDataModelUDPToMQTT, 
			String sDataTagUDPToMQTT,
			JTextArea textPaneSend,
			JTextArea textPaneReceive)
	{
//		this.bPublish= bPublish;
		this.bDeviceMode = bDeviceMode;
		this.sDeviceUrn = sDeviceUrn;
		this.sStreamID = sStreamID;
		this.sTopic = sTopic;
		this.iUDPPort = iUDPPort;
		this.sDataModel = sDataModelUDPToMQTT; 
		this.sDataTag = sDataTagUDPToMQTT;
		this.bDeviceMode = bDeviceMode;
//		this.bPublish = bPublish;
		this.sDeviceUrn = sDeviceUrn;
		this.sStreamID = sStreamID;
		this.textPaneSend = textPaneSend;
		this.textPaneReceive = textPaneReceive;
	}
	
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	/**
	 * Convert an array of Bytes to an hex string
	 * @param bytes
	 * @return Hex string
	 */
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public static String byteToHex(byte bByte) {
	    char[] hexChars = new char[2];
	    int v = bByte & 0xFF;
        hexChars[0] = hexArray[v >>> 4];
        hexChars[1] = hexArray[v & 0x0F];
	    return new String(hexChars);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

        DeviceData data = new DeviceData();
        Double dLoc[] = { 45.759723, 4.84223 };

        // Receive the Datagrams + send to Live Objects as a MQTT client
        try {
            //Création de la connexion côté serveur, en spécifiant un port d'écoute
            DatagramSocket server = new DatagramSocket(iUDPPort);
            
            while(true){

				// Pause mode
            	while (TestLOSamples.bUDPPause)
				{
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} // While bPushPause
            	
               //On s'occupe maintenant de l'objet paquet
               byte[] buffer = new byte[8192];
               DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                                
               //Cette méthode permet de récupérer le datagramme envoyé par le client
               //Elle bloque le thread jusqu'à ce que celui-ci ait reçu quelque chose.
               server.receive(packet);
               
               //nous récupérons le contenu de celui-ci et nous l'affichons
               String sExtraAllowedCharacters = new String("1234567890 &-_()*$=+!:;,?./§<>");
               String sReceivedStr = new String(packet.getData());
               String[] sArrayReceivedStr = sReceivedStr.split("\u0000");
               String sReceivedStrToEncode = new String("");
               String sReceivedStrToEncodeHex = new String("");

               // Encode as a string + 0xXX hexa for unallowed char
               for (int i=0; i<sArrayReceivedStr[0].length(); i++)
               {
            	   char c;
            	   c = sArrayReceivedStr[0].charAt(i);
            	   if ((c >= 'a' && c<='z') || (c >= 'A' && c<='Z') || sExtraAllowedCharacters.lastIndexOf((int)c)!=-1 ) 
            	   {
            		   sReceivedStrToEncode += Character.toString(c);
            	   }
            	   else
            	   {
            		   sReceivedStrToEncode += "0x";
            		   sReceivedStrToEncode += byteToHex((byte)c);
            	   }
            	   
               }
               
               // Encode as an hex payload
               for (int i=0; i<sArrayReceivedStr[0].length(); i++)
               {
            	   char c;
            	   c = sArrayReceivedStr[0].charAt(i);
        		   sReceivedStrToEncodeHex += byteToHex((byte)c);
               }

               System.out.print("Reçu de la part de " + packet.getAddress() + " depuis le port " + packet.getPort() + " : ");
               System.out.println("Received : "+sReceivedStr);
               System.out.println("Str : "+sReceivedStrToEncode);
               System.out.println("Hex : "+sReceivedStrToEncodeHex);
               textPaneReceive.setCaretPosition(textPaneReceive.getDocument().getLength());
               textPaneReceive.append("Reçu de la part de " + packet.getAddress() + 
            		   " depuis le port " + packet.getPort() + " : " + sReceivedStr + "\n");
               textPaneReceive.setCaretPosition(textPaneReceive.getDocument().getLength());
               textPaneReceive.append("Str : " + sReceivedStrToEncode + " - Hex : " + sReceivedStrToEncodeHex + "\n" );
               
               //On réinitialise la taille du datagramme, pour les futures réceptions
               packet.setLength(buffer.length);
               
               
               // Create a MQTT Client
	           try {
	        	   MqttClient sampleClient = new MqttClient(TestLOSamples.SERVER, sDeviceUrn, new MemoryPersistence());
	               MqttConnectOptions connOpts = new MqttConnectOptions();
	               if (TestLOSamples.bPublish)
	               {            	
	                	if (bDeviceMode)
	                		connOpts.setUserName("json+device"); // selecting mode "Device"
	                	else
	                		connOpts.setUserName("json+bridge"); // selecting mode "Device"
	                	
	    	            connOpts.setPassword(TestLOSamples.sAPIKey.toCharArray()); // passing API key value as password
	    	            connOpts.setCleanSession(true);
	    	            connOpts.setKeepAliveInterval(TestLOSamples.MQTT_KEEP_ALIVE);
	    	
	    	            // Connection
	    	       		textPaneSend.setCaretPosition(textPaneSend.getDocument().getLength());
	    	            System.out.println("Connecting to broker: " + TestLOSamples.SERVER + " Device mode = " + bDeviceMode);
	            		textPaneSend.append("Connecting to broker: " + TestLOSamples.SERVER + " Device mode = " + bDeviceMode + "\n");
	    	            sampleClient.connect(connOpts);
	    	            System.out.println("Connected");
	            		textPaneSend.append("Connected" + "\n");
	               }
               
		           // String for encoding to JSON
		           String sContent;
		           // streamId
	               data.s = sStreamID;
	               // value: JSON object...
	               data.v = new HashMap<String, Object>();
	               // Raw data received from UDP
	               data.v.put("UDPDataStr", sReceivedStrToEncode);
	               data.v.put("UDPDataHex", sReceivedStrToEncodeHex);
//	               data.v.put("UDPData", receivedBuffer);
	               // location (lat/lon)
	               data.loc = dLoc;
	               // model
	               data.m = sDataModel;
	               // tags
	               data.t = Arrays.asList(sDataTag);
	               // encoding to JSON
	               // {"s":sStreamID,"m":sDataModel,"v":{"UDPData": sReceivedStr},"t":[sDataTag],"loc":[45.759723,4.84223]}
	               sContent = new Gson().toJson(data);
	               
	               // On met le curseur à la fin de la requête précédente
	               textPaneSend.setCaretPosition(textPaneSend.getDocument().getLength());
	
	               LocalDateTime now = LocalDateTime.now();
	               String sTime = now.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSS ", Locale.FRENCH));
	       		
	               if (TestLOSamples.bPublish)
	               {
			            // Publish data
	                   System.out.println("MQTT message: " + sDeviceUrn + " - " + sTopic + " - " + sContent);
	                   textPaneSend.append(sTime + " - Pub msg: " + sDeviceUrn + " - " + sTopic + " - " + sContent + "\n");
	                   MqttMessage message = new MqttMessage(sContent.getBytes());
	                   message.setQos(0);
	                   sampleClient.publish(sTopic, message);
	                   System.out.println("Message published "+ sTopic);
	                   // Disconnection
	                   sampleClient.disconnect();
	                   System.out.println("Disconnected");
	               }
	               else
	               {
	                   System.out.println("Simul MQTT message: " + sDeviceUrn + " - " + sTopic + " - " + sContent);
	                   textPaneSend.append(sTime + " - Simulate Pub msg: " + sDeviceUrn + " - " + sTopic + " - " + sContent + "\n");
	               }
               
	           } catch (MqttException me) {
	                System.out.println("reason " + me.getReasonCode());
	                System.out.println("msg " + me.getMessage());
	                System.out.println("loc " + me.getLocalizedMessage());
	                System.out.println("cause " + me.getCause());
	                System.out.println("excep " + me);
	                me.printStackTrace();
	           } // catch
               
               
            } // while
         } catch (SocketException e) {
            e.printStackTrace();
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
		
	} // run

} // Class
