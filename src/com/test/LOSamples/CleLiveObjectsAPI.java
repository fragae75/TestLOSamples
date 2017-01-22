
package com.test.LOSamples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CleLiveObjectsAPI  {
	static String sAPIKey = "";
	
	/*
	 * Récupère une clé d'API depuis le fichier cle.txt
	 * 
	 */
	public static void GetAPIKeyFromFile(){
		FileInputStream fis;
	    FileChannel fc;
	
	    try {
	      //Création d'un nouveau flux de fichier
	      fis = new FileInputStream(new File("cle.txt"));
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
	      
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	}

	public static String GetAPIKey()
	{
		GetAPIKeyFromFile();
		return sAPIKey;
	}
}
