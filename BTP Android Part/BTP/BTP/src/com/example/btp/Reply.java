package com.example.btp;

import notice.Notice;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import roboguice.activity.event.OnCreateEvent;

public class Reply extends ActionBarActivity{

	
	SendReplyLca reply=new SendReplyLca(); 
	SQLiteDatabase db;
	JSONObject jclaim=null;
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.reply);
			TextView tx=(TextView)findViewById(R.id.Clocation);
			Button yesBtn=(Button)findViewById(R.id.btnYes);
			Button noBtn=(Button)findViewById(R.id.btnNo);
			final TextView tx1=(TextView)findViewById(R.id.textView2);
			//taking claimer's request from previous page...
			final String request=getIntent().getExtras().getString("request");
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 
	        final String getuserid=pref.getString("username",null);
		//	tx.setText(request);
			//extracting location from claimer's request...
			try {
				 jclaim=new JSONObject(request);
				
		        
		       // tx.setText(request+" "+getuserid);
				String clocation=jclaim.getString("LOCATION");//+"\n"+jclaim.getString("SIGNED_REQUEST")+"\n"+getuserid;
				//printing the location of claimer in verifiers textbox
				tx.setText(clocation);
			} catch (JSONException e1) {
		
				e1.printStackTrace();
			}
			//creating reply...
			final JSONObject jobj=new JSONObject();
	    //extracting verifiers session username
			
			db=openOrCreateDatabase("registration", MODE_PRIVATE, null);
			//db.execSQL("CREATE TABLE IF NOT EXISTS registrationtable2"+"(Name VARCHAR,City VARCHAR,EmailId VARCHAR,PrivKey BLOB,UserId VARCHAR,Password VARCHAR,seq_no INTEGER)");
			//db.execSQL("INSERT INTO registrationtable VALUES('don','gwl','adsf',123,'boss','bass');");
			Cursor resultSet = db.rawQuery("Select City from registrationtable3 where UserId='"+getuserid+"'",null);
			resultSet.moveToFirst();
			 final String vlocation=resultSet.getString(0);
			// tx1.setText(vlocation);
			 //checking spatio temporal coreelation for verifier and updating its current location 
			// db.execSQL("UPDATE registrationtable3 SET City='"++"'");
		//	db.execSQL("");
				
			
				  
			yesBtn.setOnClickListener(new OnClickListener() {
				
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
				//SRL.execute(request);
					try{
						jobj.accumulate("VERUSERNAME", getuserid);
						jobj.accumulate("VERLOCATION", vlocation);
						jobj.accumulate("CREQUEST", jclaim.getString("SIGNED_REQUEST"));
						jobj.accumulate("YESORNO", 1);
						 digSign dg=new digSign();
						String vreply=dg.digsn(jobj.toString());
						jobj.accumulate("VREPLY",vreply);
						
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					//tx1.setText(jobj.toString());
					reply.execute(jobj.toString());
					Intent it=new Intent("com.example.btp.verifyLast");
					startActivity(it);
			
				}
			});
			noBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
						
						//tx.setText(getuserid);
					try{
						jobj.accumulate("VERUSERNAME", getuserid);
						jobj.accumulate("VERLOCATION", vlocation);
						jobj.accumulate("CREQUEST", jclaim.getString("SIGNED_REQUEST"));
						jobj.accumulate("YESORNO", 0);
						 digSign dg=new digSign();
						String vreply=dg.digsn(jobj.toString());
						jobj.accumulate("VREPLY",vreply);
						
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					reply.execute(jobj.toString());
					Intent it=new Intent("com.example.btp.verifyLast");
					startActivity(it);
				}
			});
	
	}
		
}
