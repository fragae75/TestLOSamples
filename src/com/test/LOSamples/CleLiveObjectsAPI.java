
package com.test.LOSamples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CleLiveObjectsAPI  {
	static String sAPIKey = "";
	final static String DEFAULT_KEY = "cle.txt";
	
	/*
	 * Récupère une clé d'API depuis le fichier cle.txt
	 * 
	 */
	public static String GetAPIKey(String sKeyFile){
		FileInputStream fis;
	    FileChannel fc;
	
	    sAPIKey = "Vide => "+sKeyFile;
	    try {
	      //Création d'un nouveau flux de fichier
	      fis = new FileInputStream(new File(sKeyFile));
	      //On récupère le canal
	      fc = fis.getChannel();
	      //On en déduit la taille
	      int size = (int)fc.size();
	
	      //On crée un buffer correspondant à la taille du fichier
	      ByteBuffer bBuff = ByteBuffer.allocate(size);
	
	      //Démarrage de la lecture
	      fc.read(bBuff);
	
	      //On prépare à la lecture avec l'appel à flip
	      bBuff.flip();
	      
	      //Puisque nous avons utilisé un buffer de byte afin de récupérer les données
	      //Nous pouvons utiliser un tableau de byte
	      //La méthode array retourne un tableau de byte
	      byte[] tabByte = bBuff.array();
	      sAPIKey = new String(tabByte);
	      
	      System.out.println("Fichier cle.txt : " + sAPIKey);
	      return sAPIKey;
	      
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	      return sAPIKey;
	    } catch (IOException e) {
	      e.printStackTrace();
	      return sAPIKey;
	    }
	}

	public static String GetAPIKey()
	{
		GetAPIKey(DEFAULT_KEY);
		return sAPIKey;
	}
}
