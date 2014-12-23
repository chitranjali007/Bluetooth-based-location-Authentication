package com.example.btp;
import java.math.BigInteger;
import java.security.MessageDigest;

import android.util.Log;


public class digSign {

	  static class Keys{
	    BigInteger n = 
	                  new BigInteger("951386374109");
	    BigInteger d = 
	                  new BigInteger("279263220413");
	    BigInteger e = new BigInteger("17");
	  }
	  public String digsn(String msg){
		    Keys aliceKeys = new Keys();
		    String aliceMsg = msg;
		   
		    int blockSize = 12;

		    
		    digSign obj = new digSign();

		    byte[] aliceDigest = 
		               obj.digestIt(aliceMsg.getBytes());
		    
		    int tailLen = aliceDigest.length % blockSize;
		    int extendLen = 0;
		    if((tailLen > 0)){
		                 extendLen = blockSize - tailLen;
		    }
		    byte[] aliceExtendedDigest = 
		        new byte[aliceDigest.length + extendLen];

		   
		    System.arraycopy(aliceDigest,0,
		                     aliceExtendedDigest,0,
		                     aliceDigest.length);
		    String aliceExtendedDigestAsHex = 
		    	      obj.byteArrayToHexStr(aliceExtendedDigest);

		    	   
		    	    String aliceEncodedDigest = 
		    	            obj.encode(aliceExtendedDigestAsHex);
		    	    String aliceSignature = obj.doRSA(
		    	    	      aliceEncodedDigest,aliceKeys.d,aliceKeys.n,
		    	    	                                      blockSize);
		    	    	 
		    	    	    String aliceSignedMsg = 
		    	                       aliceMsg + aliceSignature;
		    	   
		    	    String aliceMsgLenAsStr = 
	                          "" + aliceMsg.length();

	    if((aliceMsgLenAsStr.length() > 4) 
	            || (aliceMsgLenAsStr.length() <= 0)){
	      System.out.println(
	                        "Message length error.");
	      System.exit(0);
	    }
	    if(aliceMsgLenAsStr.length() == 1){
	                         aliceMsgLenAsStr = "000"
	                              + aliceMsgLenAsStr;
	    }else if(aliceMsgLenAsStr.length() == 2){
	      aliceMsgLenAsStr = "00" + aliceMsgLenAsStr;
	    }else if(aliceMsgLenAsStr.length() == 3){
	      aliceMsgLenAsStr = "0" + aliceMsgLenAsStr;
	    }else if(aliceMsgLenAsStr.length() == 1){
	                        aliceMsgLenAsStr = "000" 
	                             + aliceMsgLenAsStr;
	    }
	    aliceSignedMsg = aliceSignedMsg + aliceMsgLenAsStr;
	    Log.i("hello: ","\n11. Alice's signed msg with msg length "+ "appended: \n" + aliceSignedMsg);
	    return(aliceSignedMsg);

	  }




String encode(String msg){
  byte[] textChars = msg.getBytes();
  String temp = "";
  String encodedMsg = "";


  for(int cnt = 0; cnt < msg.length();
                                        cnt++){
    temp = String.valueOf(
                     textChars[cnt] - ' ');
    
    if(temp.length() < 2) temp = "0" + temp;
    encodedMsg += temp;
  }//end for loop
  return encodedMsg;
}

String doRSA(String inputString,
             BigInteger exp,
             BigInteger n,
             int blockSize){

  BigInteger block;
  BigInteger output;
  String temp = "";
  String outputString = "";

  for(int cnt = 0; cnt < inputString.length();
                             cnt += blockSize){
    
    temp = inputString.substring(
                          cnt,cnt + blockSize);

    block = new BigInteger(temp);
    
    output = block.modPow(exp,n);

    
    temp = output.toString();
    while(temp.length() < blockSize){
      temp = "0" + temp;
    }
    outputString += temp;
  }//end for loop

  return outputString;
}
byte[] digestIt(byte[] dataIn){
  byte[] theDigest = null;
  try{
    
    MessageDigest msgDigest = 
       MessageDigest.getInstance("SHA");
    
    msgDigest.update(dataIn);
   
    theDigest = msgDigest.digest();
  }catch(Exception e){System.out.println(e);}

 
  return theDigest;
}
String byteArrayToHexStr(byte[] data){
  String output = "";
  String tempStr = "";
  int tempInt = 0;
  for(int cnt = 0;cnt < data.length;cnt++){
    
    tempInt = data[cnt]&0xFF;
    
    tempStr = Integer.toHexString(tempInt);
  
    if(tempStr.length() == 1)
                       tempStr = "0" + tempStr;
   
    output = output + tempStr;
  }
  return output.toUpperCase();
}
private String padTheMsg(
        String msgIn,int block,int origMsgLen){
  byte[] msgData = msgIn.getBytes();
  int msgInLen = msgData.length;
  int tailLength = msgInLen%block;
  int padLength = 0;
  if((block - tailLength >= 4))
    padLength = block - tailLength;
  else 
    padLength = 2*block - tailLength;
    

  String msgLenAsStr = "" + origMsgLen;

  if((msgLenAsStr.length() > 4) 
               || (msgLenAsStr.length() <= 0)){
    System.out.println(
                      "Message length error.");
    System.exit(0);
  }
  if(msgLenAsStr.length() == 1){
    msgLenAsStr = "000" + msgLenAsStr;
  }else if(msgLenAsStr.length() == 2){
    msgLenAsStr = "00" + msgLenAsStr;
  }else if(msgLenAsStr.length() == 3){
    msgLenAsStr = "0" + msgLenAsStr;
  }else if(msgLenAsStr.length() == 1){
    msgLenAsStr = "000" + msgLenAsStr;
  }//end else
  
  byte[] msgLenAsBytes = 
                        msgLenAsStr.getBytes();
    
  
  byte[] thePad = new byte[padLength];

  
  for(int cnt = 0;cnt < thePad.length - 4;
                                        cnt++){
    thePad[cnt] = '=';
  }
  System.arraycopy(msgLenAsBytes,0,thePad,
                          thePad.length - 4,4);

  byte[] output = 
                new byte[msgInLen + padLength];

 
  System.arraycopy(msgData,0,output,0,
                                     msgInLen);
  System.arraycopy(
     thePad,0,output,msgInLen,thePad.length);
     
 
  String outputAsStr = new String(output);
  return outputAsStr;
}

}

