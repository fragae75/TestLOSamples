
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
	 * R�cup�re une cl� d'API depuis le fichier cle.txt
	 * 
	 */
	public static String GetAPIKey(String sKeyFile){
		FileInputStream fis;
	    FileChannel fc;
	
	    sAPIKey = "Vide => "+sKeyFile;
	    try {
	      //Cr�ation d'un nouveau flux de fichier
	      fis = new FileInputStream(new File(sKeyFile));
	      //On r�cup�re le canal
	      fc = fis.getChannel();
	      //On en d�duit la taille
	      int size = (int)fc.size();
	
	      //On cr�e un buffer correspondant � la taille du fichier
	      ByteBuffer bBuff = ByteBuffer.allocate(size);
	
	      //D�marrage de la lecture
	      fc.read(bBuff);
	
	      //On pr�pare � la lecture avec l'appel � flip
	      bBuff.flip();
	      
	      //Puisque nous avons utilis� un buffer de byte afin de r�cup�rer les donn�es
	      //Nous pouvons utiliser un tableau de byte
	      //La m�thode array retourne un tableau de byte
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
