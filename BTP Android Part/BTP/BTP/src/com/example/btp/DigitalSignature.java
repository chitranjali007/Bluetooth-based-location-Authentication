package com.example.btp;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

public class DigitalSignature {
	  PrivateKey priv;
	  PublicKey pub;
	  KeyPairGenerator keyGen;
	  byte[] encodedPub,encodedPri;
	 DigitalSignature(){
			
			
			try {
					keyGen = KeyPairGenerator.getInstance("RSA");
					//SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		            keyGen.initialize(128); // 128 bit key
		            KeyPair pair = keyGen.generateKeyPair();
		            priv = pair.getPrivate();
		            pub = pair.getPublic();
		             encodedPub=pub.getEncoded();
		             encodedPri=priv.getEncoded();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	public String getPubKey() {
	 
		
		return(encodedPub.toString());

		
	}
	public String getPriKey() {
		//byte[] encoding = priv.getEncoded();
		return(encodedPri.toString());

		
	}

}
