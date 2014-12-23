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
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FinalDecision extends ActionBarActivity{
	
	String username,city_name,restau_name;
	int stars;
	TextView tx;
	SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lastlayout);
		CallDecision cd=new CallDecision();
		cd.execute();
		tx=(TextView)findViewById(R.id.textView1);
		Button btn=(Button)findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it=new Intent("com.example.btp.myaccount");
				startActivity(it);
				
			}
		});
	}

private class CallDecision extends AsyncTask<Void, Void, String>
{
	InputStream is;
	@Override
	protected String doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
	String result=null;
	try{
		String website= "http://192.168.50.32/LinkProtocol/servlet/finalservlet";
		System.setProperty("http.proxyHost", "192.168.50.32");
		System.setProperty("http.proxyPort", "8090");
		HttpClient client= new DefaultHttpClient();
		HttpPost request=new HttpPost(website);
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 
		 String getuserid=pref.getString("username",null);
		 String signreq=pref.getString("request",null);
		 JSONObject jobj=new JSONObject();
		 jobj.accumulate("CUSERID", getuserid);
		 jobj.accumulate("SIGNREQ", signreq);
		 StringEntity se = new StringEntity(jobj.toString());
		request.setEntity(se);
		HttpResponse response=client.execute(request);
	    HttpEntity httpEntity = response.getEntity();
		is = httpEntity.getContent();
		}
		catch(JSONException ex)
		{
			ex.printStackTrace();
	
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
				sb.append(line);
			}
			is.close();
			result=sb.toString();
			Log.i("hello",result);
			//Log.e("JSON", json);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		
		return result;
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		
		if(Integer.parseInt(result)==1)
			{
			
			tx.setText("Claimed Location is Correct!!!Thanks!!");
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 
			username=pref.getString("username",null);
			city_name=pref.getString("CITY",null);
			restau_name=pref.getString("RESTAU",null);
			stars=pref.getInt("STARS", 0);
			stars++;
			db=openOrCreateDatabase("registration",MODE_PRIVATE, null);
			 db.execSQL("UPDATE myaccount SET Stars ="+stars+" WHERE  Username ='"+ username+"' and City='"+city_name+"' and Restau='"+restau_name+"'");
			// db.execSQL("UPDATE registrationtable3 SET tscore =tscore+10 WHERE  Username ='"+ username+"' and City='"+city_name+"'");
			 Intent it=new Intent("com.example.btp.truec");
			 startActivity(it);
			}
		else
		{
			//tx.setText("Claimed Location is False!!!Sorry!!!");
			Intent it=new Intent("com.example.btp.falsec");
			 startActivity(it);
		}
		//tx.setText(result);
		super.onPostExecute(result);
	}
}
}
