package com.example.btp;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class CityStar extends ActionBarActivity implements OnItemSelectedListener{
	Spinner spinner1;ListView listview;
	
	 SQLiteDatabase db;
	 Cursor resultSet;
	 String username=null,city_name=null,restau_name=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.citystar);
		
		//String getuserid=pref.getString("username",null);
		
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		
		spinner1.setOnItemSelectedListener(this);
		
		 listview=(ListView)findViewById(R.id.listView1);
		

			
		
	}

	 public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			Toast.makeText(parent.getContext(), 
				"OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
				Toast.LENGTH_SHORT).show();
	
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 
	
			Editor editor = pref.edit();
		
			editor.putString("CITY", parent.getItemAtPosition(pos).toString());
	    	editor.commit();
			 db=openOrCreateDatabase("registration",MODE_PRIVATE, null);
			db.execSQL("CREATE TABLE IF NOT EXISTS citystar"+"(city VARCHAR,Restau VARCHAR)");		
				
		 		 resultSet = db.rawQuery("select * from citystar",null);
		 		if(resultSet.moveToFirst()==false)
		  		{
		  		db.execSQL("INSERT INTO citystar VALUES('Bangalore','McDonalds');");
				db.execSQL("INSERT INTO citystar VALUES('Bangalore','PizzaHut');");
				db.execSQL("INSERT INTO citystar VALUES('Bangalore','The Laziz');");
				db.execSQL("INSERT INTO citystar VALUES('Bangalore','ChinaTown');");
				db.execSQL("INSERT INTO citystar VALUES('Gwalior','MotiMahal');");
				db.execSQL("INSERT INTO citystar VALUES('Gwalior','Dominos');");
				db.execSQL("INSERT INTO citystar VALUES('Gwalior','Chawala Chicken');");
				db.execSQL("INSERT INTO citystar VALUES('Gwalior','Indian Cofee House');");
				db.execSQL("INSERT INTO citystar VALUES('Noida','The Eden');");
				db.execSQL("INSERT INTO citystar VALUES('Noida','Fraser Suites');");
				db.execSQL("INSERT INTO citystar VALUES('Noida','The Awestruck');");
				db.execSQL("INSERT INTO citystar VALUES('Noida','Resorto');");
				db.execSQL("INSERT INTO citystar VALUES('Mumbai','Cafe Mondegar');");
				db.execSQL("INSERT INTO citystar VALUES('Mumbai','The Resort');");
				db.execSQL("INSERT INTO citystar VALUES('Mumbai','Hotel Taj');");
				db.execSQL("INSERT INTO citystar VALUES('Mumbai','Hotel Magnin');");
				}
			 
			 
			 final ArrayList<String> list = new ArrayList<String>();
			 
			 resultSet = db.rawQuery("Select Restau from citystar where city='"+parent.getItemAtPosition(pos).toString()+"'",null);
			 resultSet.moveToFirst();
			 while(resultSet.isAfterLast()==false) 
		    	{
		    	
		    	
		       String restau = resultSet.getString(0);
		       list.add(restau);
		     //  String password = resultSet.getString(1);
		         Log.w("hellooo", restau);
		         resultSet.moveToNext();
		        
		    	}
			 ArrayAdapter adapter = new ArrayAdapter(this,
			         android.R.layout.simple_list_item_1, list);
			         listview.setAdapter(adapter);
			         listview.setVisibility(listview.VISIBLE);
			         listview.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View arg1,
								int position, long id) {
							// TODO Auto-generated method stub
							String val =(String) parent.getItemAtPosition(position);
							  Toast.makeText(getApplicationContext(), val ,
							          Toast.LENGTH_SHORT).show();
					
							  SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 
							
							  Editor editor=pref.edit();;
							
							   
							  
							  editor.putString("RESTAU",val);
							
							  editor.commit();
					    	username=pref.getString("username",null);
					    	 // username="bb";
					  		city_name=pref.getString("CITY",null);
					  		restau_name=pref.getString("RESTAU",null);
					    	
							db.execSQL("CREATE TABLE IF NOT EXISTS myaccount"+"(Username VARCHAR,City VARCHAR,Restau VARCHAR,Stars INTEGER)");
							 Log.w("string","table \"myaccount\" created...");
							resultSet = db.rawQuery("Select * from myaccount where Username='"+ username +"' and City='"+city_name+"' and Restau='"+restau_name+"'",null);
							   if(resultSet.moveToFirst()==false)
							   {
								   Log.w("empty","empty");
								   db.execSQL("INSERT INTO myaccount VALUES('"+username+"','"+city_name+"','"+restau_name+"',"+0+");");
								   Log.w("Insertion","data inserted");
							   }
							   else
							   {
								   Log.w("helloo!!!",resultSet.getString(0)+" "+resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getInt(3));
								   editor=pref.edit();;
								   editor.putInt("STARS",resultSet.getInt(3));
								   editor.commit();
								   Intent it=new Intent("com.example.btp.myaccount");
								   startActivity(it);
							  
							   }
						}
					});
			         
				}
		 
		  @Override
		  public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		  }
		 


}
