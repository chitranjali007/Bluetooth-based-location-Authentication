import java.math.BigInteger;
import java.security.MessageDigest;
public class checkSign {
	static class Keys{
	    BigInteger n = 
	                  new BigInteger("951386374109");
	    BigInteger d = 
	                  new BigInteger("279263220413");
	    BigInteger e = new BigInteger("17");
	  }
	 public boolean verify(String reply){
		 int blockSize = 12;
		 Keys aliceKeys = new Keys();
		 checkSign obj=new checkSign();
		// digSign obj1 = new digSign();
		
    String bobSignedMsg = reply;
    int bobMsgLen = Integer.parseInt(
            bobSignedMsg.substring(
              bobSignedMsg.length() - 4));
System.out.println(
        "\n12. Bob's calculated msg len: "
                          + bobMsgLen);
String bobMsgText = bobSignedMsg.substring(0,bobMsgLen);
System.out.println(
"\n13. Bob's extracted msg text: "
 + bobMsgText);
String bobExtractedSignature =
bobSignedMsg.substring(
bobMsgLen,bobSignedMsg.length() - 4);
System.out.println(
"\n14. Bob's extracted extended digital "
      + "signature: " 
          + bobExtractedSignature);
//Bob decrypts the extended digital
//signature using Alice's public key.
String bobDecryptedExtendedSignature =
    obj.doRSA(bobExtractedSignature,
      aliceKeys.e,aliceKeys.n,blockSize);

//Bob decodes the extended digital signature.
String bobDecodedExtendedSignature = 
        obj.decode(
          bobDecryptedExtendedSignature);

System.out.println(
  "\n15. Bob's decoded extended digital "
        + "signature: "
          + bobDecodedExtendedSignature);
String bobDecodedSignature = 
bobDecodedExtendedSignature.
        substring(0,40);
System.out.println(
"\n16. Bob's decoded digital signature: "
 + bobDecodedSignature);
//Bob computes the msg digest for comparison
//with the decoded signature.
byte[] bobDigest = 
obj.digestIt(bobMsgText.getBytes());

System.out.println("\n17. Bob's digest: " 
    + obj.byteArrayToHexStr(bobDigest));
if(bobDecodedSignature.equals(
    obj.byteArrayToHexStr(bobDigest))){
/*System.out.println(
              "\n18. Bob's conclusion: "
                   + "Valid signature");*/
	return true;
}else{
/*System.out.println(
              "\n18. Bob's conclusion: "
                 + "Invalid signature");*/
	return false;
}//end else

}//end main
	 String decode(String encodedMsg){
		  String temp = "";
		  String decodedText = "";
		  for(int cnt = 0; cnt < encodedMsg.length();
		                                     cnt += 2){
		    temp = encodedMsg.substring(cnt,cnt + 2);
		    //Convert two numeric text characters to a
		    // value of type int.
		    int val = Integer.parseInt(temp) + 32;
		    //Convert the ASCII character values to
		    // numeric String values and build the
		    // output String one character at a time.
		    decodedText += String.valueOf((char)val);
		  }//end for loop
		  return decodedText;
		}//end decode
	 String doRSA(String inputString,
             BigInteger exp,
             BigInteger n,
             int blockSize){

  BigInteger block;
  BigInteger output;
  String temp = "";
  String outputString = "";

  //Iterate and process one block at a time.
  for(int cnt = 0; cnt < inputString.length();
                             cnt += blockSize){
    //Get the next block of characters
    // and encapsulate them in a BigInteger
    // object.
    temp = inputString.substring(
                          cnt,cnt + blockSize);

    block = new BigInteger(temp);
    //Raise the block to the power exp, apply
    // the modulus operand n, and save the
    // remainder.  This is the essence of the
    // RSA algorithm.
    output = block.modPow(exp,n);

    //Convert the numeric result to a
    // four-character string, appending leading
    // zeros as necessary.
    temp = output.toString();
    while(temp.length() < blockSize){
      temp = "0" + temp;
    }//end while

    //Build the outputString blockSize
    // characters at a time.  Each character
    // in the inputString results in one
    // character in the outputString.
    outputString += temp;
  }//end for loop

  return outputString;
}//end doRSA
	 byte[] digestIt(byte[] dataIn){
		  byte[] theDigest = null;
		  try{
		    //Create a MessageDigest object
		    // implementing the SHA algorithm, as
		    // supplied by SUN
		    MessageDigest msgDigest = 
		       MessageDigest.getInstance("SHA", "SUN");
		    //Feed the byte array to the digester.  Can
		    // accommodate multiple calls if needed
		    msgDigest.update(dataIn);
		    //Complete the digestion and save the
		    // result
		    theDigest = msgDigest.digest();
		  }catch(Exception e){System.out.println(e);}

		  //Return the digest value to the calling
		  // method as an array of bytes.
		  return theDigest;
		}//end digestIt()
	 String byteArrayToHexStr(byte[] data){
		  String output = "";
		  String tempStr = "";
		  int tempInt = 0;
		  for(int cnt = 0;cnt < data.length;cnt++){
		    //Deposit a byte into the 8 lsb of an int.
		    tempInt = data[cnt]&0xFF;
		    //Get hex representation of the int as a
		    // string.
		    tempStr = Integer.toHexString(tempInt);
		    //Append a leading 0 if necessary so that
		    // each hex string will contain two
		    // characters.
		    if(tempStr.length() == 1)
		                       tempStr = "0" + tempStr;
		    //Concatenate the two characters to the
		    // output string.
		    output = output + tempStr;
		  }//end for loop
		  return output.toUpperCase();
		}//end byteArrayToHexStr*/

}

