package com.test.LOSamples;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import javax.swing.JTextArea;
import javax.swing.JTextPane;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;


public class RunGetMatchingRules implements Runnable {

	private JTextArea textPaneReceive;

	
	public RunGetMatchingRules (JTextArea textPaneReceive){
		this.textPaneReceive = textPaneReceive;
	}

    public void getData() throws JSONException, ClientProtocolException, IOException {
    	
    	System.out.println("Requete : "+ TestLOSamples.URL_GET_MATCHING_RULE);

    	//StringJSON est le résultat en String de la requête : elle contient les données
    	String stringJSON = Request.Get(TestLOSamples.URL_GET_MATCHING_RULE) 	//concaténation du lien définis pour la requête+le streamID
				.addHeader("X-API-Key", TestLOSamples.sAPIKey)     				//ajout de la clé d'API Live Objects
				.addHeader("Content-Type", "application/json")     				//content type
				.connectTimeout(1000)
		        .socketTimeout(1000)
		        .execute()             //Lancement de la requête
		        .returnContent().toString(); //Récupération et formatage (en String) des donées envoyées dans la variable stringJSON
		
		//conversion de stringJSON en JSONArray
		JSONArray dataArray = new JSONArray(stringJSON);
//		JsonArray jsodataArray = new JsonArray(stringJSON);
		
		//parcours du la tableau pour récupérer chaque lot de données
		int longueur = dataArray.length();
		
		// On met le curseur � la fin de la requ�te pr�c�dente
		textPaneReceive.setCaretPosition(textPaneReceive.getDocument().getLength());
		// Affichage de la requete
		textPaneReceive.append("\n");
		textPaneReceive.append(longueur + " Matching rules :\n");

		/*
		 * {
		 * 		"dataPredicate":
		 * 			{"and":
		 * 				[
		 * 					{
		 * 						"<": [{"var":"value.hygrometry"},20]
		 * 					},
		 * 					{
		 * 						">":[{"var":"value.temperature"},20]
		 * 					}
		 * 				]
		 * 			},
		 * 			"name":"Test hygro < 20 && temp > 20",
		 * 			"id":"20373466-f52b-42ba-8d54-7ced856218f7",
		 * 			"enabled":true
		 * }
		 * 
		 */
		
		for(int i = 0; i < longueur; i++){
			
			JSONObject dataJson = (JSONObject)dataArray.get(i); //dataJSON représente une valeurs de données dans le tableau
			JSONObject jsoDataPredicate,jsoAnd;
			String sMatchingRuleId, sName, sFormula;
			boolean bEnabled;
			Font police;
			
			try
			{
				jsoDataPredicate = dataJson.getJSONObject("dataPredicate");
				sFormula = jsoDataPredicate.toString();
				sMatchingRuleId = dataJson.getString("id");
				sName = dataJson.getString("name");
				bEnabled = dataJson.getBoolean("enabled");
				
				//Affichage de chaque élément du tableau JSON
				System.out.println("Matching rule "+ i +" ==> "+ dataJson.toString());
				if (bEnabled)
					textPaneReceive.append("  " + i + " - \"" + sName + "\", Id = " + 
										sMatchingRuleId + " : " + sFormula + "\n");
				else
					textPaneReceive.append("  " + i + " (Disabled !) - \"" + sName + "\", Id = " + 
							sMatchingRuleId + " : " + sFormula + "\n");

			}
			catch (JsonIOException | JsonSyntaxException | ClassCastException | IllegalStateException | NullPointerException e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());             
			} // try
			
		}
	}

	@Override
	public void run() {
		try {
			getData();
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

}
