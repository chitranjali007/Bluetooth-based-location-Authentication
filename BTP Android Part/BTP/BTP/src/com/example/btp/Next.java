package com.example.btp;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

//import com.example.hello.MakeJsonObj;
//import com.example.hello.GetMethod;






import android.service.textservice.SpellCheckerService.Session;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.Build;

//import com.example.hello.GetMethod;
public class Next extends ActionBarActivity {

	SQLiteDatabase db;
EditText userid,password;
ImageButton reg,login;
String getuserid="",getpassword="";
TextView tv;
	
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.next);
	userid=(EditText)findViewById(R.id.UserId);
	userid.setTextColor(Color.WHITE);
	password=(EditText)findViewById(R.id.Password);
	password.setTextColor(Color.WHITE);
	reg=(ImageButton)findViewById(R.id.button1);
	login=(ImageButton)findViewById(R.id.button2);
	tv=(TextView)findViewById(R.id.tv1);
 login.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		 getpassword=password.getText().toString();
		 getuserid=userid.getText().toString();
		// TODO Auto-generated method stub
		 db=openOrCreateDatabase("registration", MODE_PRIVATE, null);
	//	db.execSQL("CREATE TABLE IF NOT EXISTS registrationtable3"+"(Name VARCHAR,City VARCHAR,EmailId VARCHAR,PrivKey BLOB,UserId VARCHAR,Password VARCHAR,seq_no INTEGER)");
		 Log.w("string1","table registrationtable3 created...");
		// db.execSQL("INSERT INTO registrationtable VALUES('don','gwl','adsf',123,'boss','bass');");
		 Cursor resultSet = db.rawQuery("Select Password from registrationtable3 where UserId='"+getuserid+"'",null);
	       resultSet.moveToFirst();
	       String pass=resultSet.getString(0);
	      
	       if(pass.equals(getpassword)) {
	    	   Log.w("pwd match","password matched...");
	    	   SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
	    	  Editor editor = pref.edit();
	    	  editor.putString("username", getuserid);
	    	  editor.commit();
	    	   Log.w("cookies","session varible of username created...");
	    	  tv.setTextColor(Color.WHITE);
	           tv.setText("welcome "+pref.getString("username", null));
	    	   Intent it=new Intent("com.example.btp.CityStar");
	    	   startActivity(it);
	       
	       }
		
	}
});
reg.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent it=new Intent("com.example.btp.login");
		startActivity(it);
		
	}
});  
  /* if (savedInstanceState == null) {
		getSupportFragmentManager().beginTransaction()
				.add(R.id.container, new PlaceholderFragment()).commit();
	}*/
	
}
public void onUpgrade(SQLiteDatabase database, int arg1, int arg2) 
{
	database.execSQL("DROP TABLE IF EXISTS registrationtable2 ");

}



/*@Override
public boolean onCreateOptionsMenu(Menu menu) {

	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
	if (id == R.id.action_settings) {
		return true;
	}
	return super.onOptionsItemSelected(item);
}*/

/**
 * A placeholder fragment containing a simple view.
 */
/*public static class PlaceholderFragment extends Fragment {

	public PlaceholderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		return rootView;
	}
}*/

 
}
