/**
 *  This thread will push IFTTT event related to Live Objects Events
 */

package com.test.LOSamples;

import java.io.IOException;

import javax.swing.JTextArea;

import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;


public class RunPushIFTTT implements Runnable {

	private JTextArea textPaneSend;
	private JTextArea textPaneReceive;
	private JTextArea textPaneIFTTT;
	private String sIFTTTURL;
	private String sFiringNumber;

	public RunPushIFTTT (String sIFTTTURL, String sFiringNumber, JTextArea textPaneSend, JTextArea textPaneReceive, JTextArea textPaneIFTTT){
		this.sIFTTTURL = sIFTTTURL;
		this.sFiringNumber = sFiringNumber;
		this.textPaneSend = textPaneSend;
		this.textPaneReceive = textPaneReceive;
		this.textPaneIFTTT = textPaneIFTTT;
	}

    
    public String postCommand(String sURL, String sPayload) throws ClientProtocolException, IOException{
    	try{
    		
			String stringJSON = Request.Post(sURL) //ajout du lien pour l'envoie de commande définis dans le swagger
			        .useExpectContinue()
//			        .addHeader("X-API-Key", TestHTTPLO.sAPIKey)         //ajout de la clé
			        .version(HttpVersion.HTTP_1_1)
			        .bodyString(sPayload, ContentType.APPLICATION_JSON)
			        .execute()                                 //Exécution de la requête
			        .returnContent().toString();               //Récupération et formatage de la réponse à l'envoie de commande

			System.out.println("La r�ponse à l'envoie de commande est : " + stringJSON);
			return stringJSON;
    	}
    	catch (HttpResponseException e){
			
    		System.out.println("La r�ponse à l'envoie de commande est : " + e.getMessage());
			return e.getMessage();
    		
    	}
	}
	
	@Override
	public void run() {

		try {
/*
			while (true){
                // Temporisation 1 sec
                try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
*/			
//			String sPayload = "{ \"value1\" : \"1\", \"value2\" : \"2\", \"value3\" : \"3\" }";
			String sPayload = "{ \"value1\" : \"" + sFiringNumber + "\", \"value2\" : \"2\", \"value3\" : \"3\" }";
			String sReturn = postCommand (sIFTTTURL, sPayload);
			
	        System.out.println("IFTTT : " + sIFTTTURL + " Payload : " + sPayload);
	        System.out.println("Return : " + sReturn);
			textPaneSend.setCaretPosition(textPaneSend.getDocument().getLength());
			textPaneSend.append("IFTTT : " + sIFTTTURL + "\n");
			textPaneSend.setCaretPosition(textPaneSend.getDocument().getLength());
			textPaneSend.append("    Payload : " + sPayload + "\n");
			textPaneReceive.setCaretPosition(textPaneReceive.getDocument().getLength());
			textPaneReceive.append("Return : " + sReturn + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
