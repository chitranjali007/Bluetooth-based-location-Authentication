package com.example.btp;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


//import com.example.hello.MakeJsonObj;
import com.example.btp.GetMethod;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
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
import android.widget.TextView;
import android.os.Build;

import com.example.btp.GetMethod;
public class login extends ActionBarActivity {
	
	String name,password,city,email, pubkey,userid,PrivKey;
	EditText Name1,Password1,City1,Email1,Userid1;
	TextView nametxt;
	Button bn,bn2;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);
        Name1=(EditText)findViewById(R.id.Name);
        Name1.setTextColor(Color.WHITE);
        Password1=(EditText)findViewById(R.id.Password);
        Password1.setTextColor(Color.WHITE);
        City1=(EditText)findViewById(R.id.City);
        City1.setTextColor(Color.WHITE);
        Email1=(EditText)findViewById(R.id.Email);
        Email1.setTextColor(Color.WHITE);
        Userid1=(EditText)findViewById(R.id.Userid);
        Userid1.setTextColor(Color.WHITE);
    	bn=(Button)findViewById(R.id.button1);
        bn2=(Button)findViewById(R.id.button2);
     //   nametxt=(TextView)findViewById(R.id.textView1);
    	final Intent it=new Intent(this,MainActivity.class);
        //going back to login page 
        bn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it=new Intent("com.example.btp.Next");
			
				startActivity(it);
				
			}
		});
        
        
   	bn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			  
			DigitalSignature dig=new DigitalSignature();//generating public key
 		    pubkey=dig.getPubKey();//generating public key
 		    PrivKey=dig.getPriKey();
			Asyncho asynch=new Asyncho();
			asynch.execute();
			
		}
	});
     
    	/*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
    }
    
    private class Asyncho extends AsyncTask<Void, Void, String>{
    	
    	@Override
 		protected String doInBackground(Void... arg0) {
 			// TODO Auto-generated method stub
 			name=Name1.getText().toString();
 			String st=null;
 				
            
			password=Password1.getText().toString();
			city=City1.getText().toString();
			email=Email1.getText().toString();
			userid=Userid1.getText().toString();
 			GetMethod post= new GetMethod();
 			
 			
 			
 			String url="http://192.168.50.32/LinkProtocol/servlet/Registeration";
 			
 			List<NameValuePair> params=new ArrayList<NameValuePair>();
 			params.add(new BasicNameValuePair("name", name));
 			params.add(new BasicNameValuePair("pass", password));
 			params.add(new BasicNameValuePair("city", city));
 			params.add(new BasicNameValuePair("email", email));
 			params.add(new BasicNameValuePair("userid", userid));
 			params.add(new BasicNameValuePair("pubkey", pubkey));
 			st=post.PostRegDetails(url, params);
 			 SQLiteDatabase db=openOrCreateDatabase("registration",MODE_PRIVATE, null);
				db.execSQL("CREATE TABLE IF NOT EXISTS registrationtable3"+"(Name VARCHAR,City VARCHAR,EmailId VARCHAR,PrivKey BLOB,UserId VARCHAR,Password VARCHAR,seqNo INTEGER,tscore INTEGER)");		
				db.execSQL("INSERT INTO registrationtable3 VALUES('"+name+"','"+city+"','"+email+"','"+PrivKey+"','"+userid+"','"+password+"',"+1+","+30+");");
				/* Cursor resultSet = db.rawQuery("Select * from registrationtable where Name='"+name+"'",null);
			       resultSet.moveToFirst();
			       String username = resultSet.getString(0);
			       String password = resultSet.getString(1);
			       Log.w(username, password);*/
				return st;
 			
			
 		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			//TextView tx=(TextView)findViewById(R.id.textView1);
			//tx.setText(result);
			if(result!=null)
			{
				//View v=new View(getApplicationContext());  
				
				bn2.setVisibility(bn2.VISIBLE);
			}
			
			
		}
}


  //  @Override
   /* public boolean onCreateOptionsMenu(Menu menu) {
        
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
   /* public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }*/
    
 
}
