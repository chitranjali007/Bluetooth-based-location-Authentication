package manager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

public class BluetoothComunication extends Thread {
	 
	private int whatMsgBT;
	private int whatMsgNotice;
	
	private Handler handler;

	private BluetoothSocket socket;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	
	public BluetoothComunication(Handler handler, int whatMsgBT, int whatMsgNotice){
		this.handler = handler;
		this.whatMsgBT = whatMsgBT;
		this.whatMsgNotice = whatMsgNotice;
	}
	
	public void openComunication(BluetoothSocket socket){
		this.socket = socket;
		start();
	}
	 
	@Override
	public void run() {
		 super.run();
		
		 String nameBluetooth;
		
		 try {
			 nameBluetooth = socket.getRemoteDevice().getName();
			 dataInputStream = new DataInputStream(socket.getInputStream());
			 dataOutputStream = new DataOutputStream(socket.getOutputStream());
			
			 sendHandler(whatMsgNotice, "Input and Output streams are Ready!!");
			 
			 while (true) {
				 if(dataInputStream.available() > 0){
					 byte[] msg = new byte[dataInputStream.available()];
					 dataInputStream.read(msg, 0, dataInputStream.available());
					 
					 /*String msge=msg.toString();
					 String website= "http://192.168.71.58/LinkProtocol/servlet/Registeration";
						System.setProperty("http.proxyHost", "192.168.71.58");
						System.setProperty("http.proxyPort", "8090");
						//httpPost.setHeader("Accept", "application/json");
				        //httpPost.setHeader("Content-type", "application/json");
						HttpClient client= new DefaultHttpClient();
						HttpPost request=new HttpPost(website);
						StringEntity se = new StringEntity(msg.toString());
						request.setEntity(se);
					
						
						HttpResponse response=client.execute(request);
					 String url="http://192.168.71.58/LinkProtocol/servlet/Registeration";*/
					sendHandler(whatMsgBT,new String(msg));
			
				 }
			 }
		 } catch (IOException e) {
			 e.printStackTrace(); 
			 
			 dataInputStream = null;
			 dataOutputStream = null;
			 
			 sendHandler(whatMsgNotice, "Error in IOStream or NO Device found");
		 }
	}
	
	public void sendMessageByBluetooth(String msg){
		try {
			if(dataOutputStream != null){
				dataOutputStream.write(msg.getBytes());
			}else{
				sendHandler(whatMsgNotice, "dataOutputStream is null.");
			}
			
		} catch (IOException e) {
			e.printStackTrace(); 
			 
			sendHandler(whatMsgNotice, "Exception in Output stream");
		}
	}
	
	public void sendHandler(int what, Object object){
		handler.obtainMessage(what, object).sendToTarget();
		
	}
           
	 public void stopComunication(){ 
		try {
			if(dataInputStream != null && dataOutputStream != null){
				dataInputStream.close();
				dataOutputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
	 
 }