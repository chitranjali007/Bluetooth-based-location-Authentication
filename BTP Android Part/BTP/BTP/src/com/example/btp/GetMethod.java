package com.example.btp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

public class GetMethod {

	public String PostRegDetails(String url,List<NameValuePair> params) {
		String st="",pubkey="";
		InputStream is =null;
	
		try
		{
		JSONObject jobj=new JSONObject();
		//Log.i("name", params.get(0).getValue());
		//Log.i("name", params.get(1).getValue());
		
		jobj.accumulate("NAME", params.get(0).getValue());
		jobj.accumulate("PASSWORD",params.get(1).getValue());
		jobj.accumulate("CITY",params.get(2).getValue());
		jobj.accumulate("EMAIL",params.get(3).getValue());
		jobj.accumulate("USERID",params.get(4).getValue());
		jobj.accumulate("PUBKEY",params.get(5).getValue());
		
		//Log.i("name",jobj.getString("NAME"));
		//Log.i("pass",jobj.getString("PASSWORD"));
		String website= url;
		//Log.i("chitruuu", "heoooele");
		
		System.setProperty("http.proxyHost", "192.168.50.32");
	//	Log.i("chitruuu", "2");
		System.setProperty("http.proxyPort", "8090");
		//Log.i("chitruuu", "3");
		
		//httpPost.setHeader("Accept", "application/json");
        //httpPost.setHeader("Content-type", "application/json");
		HttpClient client= new DefaultHttpClient();
		//Log.i("chitruuu", "4");
		HttpPost request=new HttpPost(website);
	//	Log.i("chitruuu", "5");
		StringEntity se = new StringEntity(jobj.toString());
		request.setEntity(se);
		
		
		HttpResponse response=client.execute(request);
		Log.i("req_string", "registration details sent to Registration...");
		//HttpEntity httpEntity = response.getEntity();
		//InputStream is = httpEntity.getContent();

		
		HttpEntity httpEntity = response.getEntity();
		   Log.w("string","user registered to servlwt!!!");
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

		return st;
	}
	
	
}
