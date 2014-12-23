package com.example.btp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import com.example.btp.Bluetooth;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class SendRequestLca extends AsyncTask<String, Void, Void>{
	InputStream is=null;String st=null;
	
	protected  Void doInBackground(String...abc) {
		// TODO Auto-generated method stub
	//	Bluetooth b=new Bluetooth();
		
		try{
		String website= "http://192.168.50.32/LinkProtocol/servlet/claim";
		System.setProperty("http.proxyHost", "192.168.50.32");
		System.setProperty("http.proxyPort", "8090");
		//httpPost.setHeader("Accept", "application/json");
        //httpPost.setHeader("Content-type", "application/json");
		HttpClient client= new DefaultHttpClient();
		HttpPost request=new HttpPost(website);
		//String st=abc.toString();
		
		JSONObject jobj1=new JSONObject(abc[0]);
		JSONObject jb=new JSONObject();
		
		try{ 
			jb.accumulate("CUSERNAME",jobj1.get("CUSERNAME") );
	        jb.accumulate("LOCATION", jobj1.get("LOCATION"));
	        jb.accumulate("SERVICE_ID",jobj1.get("SERVICE_ID") );
	        jb.accumulate("SEQNO",jobj1.get("SEQNO"));
	        jb.accumulate("SIGNED_REQUEST", jobj1.get("SIGNED_REQUEST"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		StringEntity se = new StringEntity(jb.toString(),"UTF8");
		request.setEntity(se);
		
		HttpResponse response=client.execute(request);
	    HttpEntity httpEntity = response.getEntity();
		is = httpEntity.getContent();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			st=sb.toString();
			Log.i("hello",st);
			//Log.e("JSON", json);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		return null;

	//	return st;
		//return null;
	}

}
