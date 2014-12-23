package com.example.btp;

import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class cupon extends ActionBarActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cupon);
		TextView tx=(TextView)findViewById(R.id.textView3);
		
		//new String[10];
		String str[]= {"N5KLM886HU","JU80OA2VV2","R4Q5Y7B77U","DJJD273ND0","NCJJSHW1234","SUD13242CHI"};
		Random r=new Random();
		int n=r.nextInt(6)%6;
		tx.setText(str[n]);
		Button btn=(Button)findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it=new Intent("com.example.btp.");
				startActivity(it);
				
			}
		});
		
		
	}

}
