package com.example.btp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class myaccount extends ActionBarActivity{
	Button b3;
	RatingBar ratingBar;
	TextView txtRatingValue,name1,city1,hotel1;
	String username=null,city_name=null,restau_name=null;
	float stars=0.0f;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myaccount);
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
		b3=(Button)findViewById(R.id.button3);
		name1=(TextView)findViewById(R.id.name);
		city1=(TextView)findViewById(R.id.city);
		hotel1=(TextView)findViewById(R.id.hotel);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		
		username=pref.getString("username",null);
		name1.setText(username);
		Log.i("username: ",username);

		city_name=pref.getString("CITY",null);
        city1.setText(city_name);
        Log.i("city: ",city_name);
        restau_name=pref.getString("RESTAU",null);
		hotel1.setText(restau_name);
        Log.i("restau: ",restau_name);

		stars=pref.getInt("STARS", 0);
		 Log.i("restau: ",restau_name);
		ratingBar.setRating(stars);
		 Log.i("restau: ",restau_name);
		ratingBar.setFocusable(false);
		//ratingBar.o
		
		b3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it=new Intent("com.example.btp.Bluetooth");
				startActivity(it);
				
			}
		});
		//addListenerOnRatingBar();
		//addListenerOnButton();
	}
	
	
	
	 /* public void addListenerOnRatingBar() {
		  
			ratingBar = (RatingBar) findViewById(R.id.ratingBar);
			txtRatingValue = (TextView) findViewById(R.id.text1);
		 
			//if rating value is changed,
			//display the current rating value in the result (textview) automatically
			ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
				public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
		 
					txtRatingValue.setText(String.valueOf(rating));
		 
				}
			});
		  }*/
		 
		 /* public void addListenerOnButton() {
		 
			ratingBar = (RatingBar) findViewById(R.id.ratingBar);
			b3 = (Button) findViewById(R.id.button3);
		 
			//if click on me, then display the current rating value.
			b3.setOnClickListener(new OnClickListener() {
		 
				@Override
				public void onClick(View v) {
		 
					Toast.makeText(myaccount.this,
						String.valueOf(ratingBar.getRating()),
							Toast.LENGTH_SHORT).show();
		 
				}
		 
			});
		 
		  }*/
}