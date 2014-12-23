package com.example.btp;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;

public class Allowed extends ActionBarActivity {
	SQLiteDatabase db;
	public Allowed() {
		// TODO Auto-generated constructor stub
	
	 db=openOrCreateDatabase("registration", MODE_PRIVATE, null);
	}
	public boolean canClaim()
	{
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 
        final String getuserid=pref.getString("username",null);
		 Cursor resultSet = db.rawQuery("Select tscore from registrationtable2 where UserId='"+getuserid+"'",null);
	       resultSet.moveToFirst();
	       int score=resultSet.getInt(0);
	       if(score>=15)
		return true;
	       else
	    	   return false;
	}
	
}
