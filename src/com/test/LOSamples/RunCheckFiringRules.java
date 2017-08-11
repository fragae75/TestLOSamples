package com.test.LOSamples;

import java.awt.Font;
import java.io.IOException;

import javax.swing.JTextArea;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class RunCheckFiringRules implements Runnable {

	private JTextArea textPaneReceive;
	private String sMatchingRuleToCheck;

	
	public RunCheckFiringRules (String sMatchingRule, JTextArea textPaneReceive){
		this.textPaneReceive = textPaneReceive;
		this.sMatchingRuleToCheck = sMatchingRule;
	}

    public void getData() throws JSONException, ClientProtocolException, IOException {
    	
		boolean bFound = false;
		
    	System.out.println("Requete : "+ TestLOSamples.URL_GET_MATCHING_RULE);

    	//StringJSON est le résultat en String de la requête : elle contient les données
    	String stringJSON = Request.Get(TestLOSamples.URL_GET_FIRING_RULE) 
				.addHeader("X-API-Key", TestLOSamples.sAPIKey)     				//ajout de la clé d'API Live Objects
				.addHeader("Content-Type", "application/json")     				//content type
				.connectTimeout(1000)
		        .socketTimeout(1000)
		        .execute()             //Lancement de la requête
		        .returnContent().toString(); //Récupération et formatage (en String) des donées envoyées dans la variable stringJSON
		
		//conversion de stringJSON en JSONArray
		JSONArray dataArray = new JSONArray(stringJSON);
		
		//parcours du la tableau pour récupérer chaque lot de données
		int longueur = dataArray.length();
		
		// On met le curseur � la fin de la requ�te pr�c�dente
		textPaneReceive.setCaretPosition(textPaneReceive.getDocument().getLength());
		// Affichage de la requete
		textPaneReceive.append("\n");
		textPaneReceive.append("Checked " + longueur + " Firing rules :\n");

		/*
		 * {
			    {
			        "id": "2924c2a8-0d32-475f-aac9-194bc5eca400",
			        "name": "rule_2",
			        "enabled": true,
			        "matchingRuleIds": [
			            "de9c06fc-4b2b-4f27-883d-489fb882652f"
			        ],
			        "firingType": "SLEEP",
			        "sleepDuration": "PT10S"
			    },
		 * }
		 * 
		 */
		
		for(int i = 0; i < longueur; i++){
			
			JSONObject dataJson = (JSONObject)dataArray.get(i); //dataJSON représente une valeurs de données dans le tableau
			JSONObject jsoDataPredicate,jsoAnd;
			String sMatchingRuleId,sFiringRuleId, sFiringType, sName, sFormula;
			boolean bEnabled;
			JSONArray matchingRuleArray = new JSONArray();


			try
			{
//				jsoDataPredicate = dataJson.getJSONObject("dataPredicate");
//				sFormula = jsoDataPredicate.toString();
				sFiringRuleId = dataJson.getString("id");
				sName = dataJson.getString("name");
				bEnabled = dataJson.getBoolean("enabled");
				sFiringType = dataJson.getString("firingType");
				matchingRuleArray = dataJson.getJSONArray("matchingRuleIds");
				
				for(int j = 0; j < matchingRuleArray.length() ; j++){
					sMatchingRuleId = matchingRuleArray.get(j).toString();
					if (sMatchingRuleId.equals(sMatchingRuleToCheck)) {
						bFound = true;
						if (bEnabled) {
							textPaneReceive.append("Enabled Rule " + sName + 
									", Firing type : " + sFiringType + 
									", Id : " + sFiringRuleId + 
									", contains Matching rule Id : " + sMatchingRuleId + "\n");
							System.out.println("Enabled Rule " + sName + 
									", Firing type : " + sFiringType + 
									", Id : " + sFiringRuleId + 
									", contains Matching rule Id : " + sMatchingRuleId + "\n");
						}
						else {
							
							textPaneReceive.append("Disable Rule " + sName + 
									", Firing type : " + sFiringType + 
									", Id : " + sFiringRuleId + 
									", contains Matching rule Id : " + sMatchingRuleId + "\n");
							System.out.println("Disable Rule " + sName + 
									", Firing type : " + sFiringType + 
									", Id : " + sFiringRuleId + 
									", contains Matching rule Id : " + sMatchingRuleId + "\n");
						}
					}
				}

				//Affichage de chaque élément du tableau JSON
				System.out.println("Firing rule "+ i +" ==> "+ dataJson.toString());
				
			}
			catch (JsonIOException | JsonSyntaxException | ClassCastException | IllegalStateException | NullPointerException e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());             
			} // try
			
		} // for
		
		if (!bFound)
			textPaneReceive.append("No Rule found \n");
			
		// On met le curseur � la fin de la requ�te pr�c�dente
		textPaneReceive.setCaretPosition(textPaneReceive.getDocument().getLength());
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
