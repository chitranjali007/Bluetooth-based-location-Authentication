package com.example.btp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.R.string;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

public class SendReplyLca extends AsyncTask<String, Void, Void>{
	
	String st="",pubkey="";
	InputStream is =null;
	
	
	protected Void doInBackground(String...json) {
		// TODO Auto-generated method stub
		String website= "http://192.168.50.32/LinkProtocol/servlet/verify";
		System.setProperty("http.proxyHost", "192.168.50.32");
		System.setProperty("http.proxyPort", "8090");
		//httpPost.setHeader("Accept", "application/json");
        //httpPost.setHeader("Content-type", "application/json");
		HttpClient client= new DefaultHttpClient();
		HttpPost request=new HttpPost(website);
		HttpResponse response=null;
		try {
			StringEntity se = new StringEntity(json[0]);
			request.setEntity(se);
			response = client.execute(request);
			HttpEntity httpEntity = response.getEntity();
			is = httpEntity.getContent();
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch(Exception ex){
			ex.printStackTrace();
		}
		//HttpEntity httpEntity = response.getEntity();
		//InputStream is = httpEntity.getContent();

		
		
		
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
		//return null;
	}

	

}
