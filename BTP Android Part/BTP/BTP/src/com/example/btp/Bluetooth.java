package com.example.btp;

import java.io.IOException;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import manager.*;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.btp.R;

import manager.BluetoothComunication;
import notice.Notice;
import task.BluetoothClientTask;
import task.BluetoothServiceTask;

import com.google.inject.Inject;

@SuppressLint("HandlerLeak")
@ContentView(R.layout.bluetooth)
public class Bluetooth extends RoboActivity{
	
	private final int BT_ACTIVATE = 0;
	private final int BT_VISIBLE = 1;

	public static int BT_TIMER_VISIBLE = 30; 

	@InjectView(R.id.edtMsg)
	private EditText edMsg;
	
	@InjectView(R.id.btnSend)
	private Button btnSend;
	
	@InjectView(R.id.btnService)
	private Button btnService;
	
	@InjectView(R.id.btnClient)
	private Button btnClient;
	
	@InjectView(R.id.lstHistoric)
	private ListView lstHistoric;
	
	@Inject
	private Notice notice;

	private BluetoothSocket socket;
	private BluetoothAdapter adaptador;
	private BluetoothComunication bluetoothComunication;
	private BluetoothClientTask bluetoothClientTask;
	private BluetoothServiceTask bluetoothServiceTask;
	
	private ArrayAdapter<String> historic;
	private List<BluetoothDevice> devicesFound; 

	private ProgressDialog progressDialog;	
	private EventsBluetoothReceiver eventsBTReceiver; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		edMsg.setTextColor(Color.WHITE);
		devicesFound = new ArrayList<BluetoothDevice>(); 

		configView();
		inicializaBluetooth();
		registerFilters();
	
	}
	
	public void configView(){
		historic = new ArrayAdapter<String>(this,	android.R.layout.simple_list_item_1);
		lstHistoric.setAdapter(historic);
	
		btnSend.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				if(bluetoothComunication != null){
					String msg = edMsg.getText().toString(); 
					
					if(msg.trim().length() > 0){
						edMsg.setText(""); 
						
						historic.add("YOUR CLAIMED LOCATION: " + msg); 
						historic.notifyDataSetChanged();	
						  SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); 
					      final String getuserid=pref.getString("username",null);
					      SQLiteDatabase db=null;
					      db=openOrCreateDatabase("registration", MODE_PRIVATE, null);
					     
					      Cursor resultSet = db.rawQuery("Select MAX(seqNo) from registrationtable3 where UserId='"+getuserid+"'",null);
					      resultSet.moveToFirst();
					      int seq_No=resultSet.getInt(0);
					      seq_No++;
					      db.execSQL("UPDATE registrationtable3 SET seqNo ="+seq_No+" WHERE UserId ='"+ getuserid+"'");	
					       JSONObject jobj=new JSONObject();
					      try{ 
						       
						        jobj.accumulate("CUSERNAME", getuserid);
						        jobj.accumulate("LOCATION", msg);
						        jobj.accumulate("SERVICE_ID", "btp");
						        jobj.accumulate("SEQNO",seq_No);
						        digSign ds=new digSign();
						    	String sign=ds.digsn(jobj.toString());
						       jobj.accumulate("SIGNED_REQUEST",sign);
						       //String req=jobj.toString();
						       SendRequestLca lc=new SendRequestLca();
						        lc.execute(jobj.toString());
						        Editor editor = pref.edit();
						    	editor.putString("request",sign);
						    	editor.commit();
						        bluetoothComunication.sendMessageByBluetooth(jobj.toString());
						        Intent it=new Intent("com.example.btp.Final1");
						        startActivity(it);
						        
						       
					      }
					      catch(Exception e)
					      {
					    	  e.printStackTrace();
					      }
				      // }
				      
						
					}else{
						notice.showToast("Enter Your Location");
					}
				}else{
					notice.showToast("bluetoothComunication is null");
				}
			}
		});
		
		btnService.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, BT_TIMER_VISIBLE); 
				startActivityForResult(discoverableIntent, BT_VISIBLE);
			}
		});
		
		btnClient.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
			
progressDialog = ProgressDialog.show(Bluetooth.this, "Searching For Devices", "Loading...");
				
				closeCommunication();
				
				devicesFound.clear(); 
				adaptador.startDiscovery();
				/*Allowed a=new Allowed();
				if(a.canClaim()==true)
				{
				progressDialog = ProgressDialog.show(Bluetooth.this, "Searching For Devices", "Loading...");
				
				closeCommunication();
				
				devicesFound.clear(); 
				adaptador.startDiscovery();
				}
				else
				{
					progressDialog = ProgressDialog.show(Bluetooth.this, "oops!!!", "Sorry, You are not allowed to claim please verify some people...");

				}*/
			}
		});
	}

	public void inicializaBluetooth() {
		adaptador = BluetoothAdapter.getDefaultAdapter(); 
		
		if (adaptador != null) {
			if (!adaptador.isEnabled()) { 
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); 
				startActivityForResult(enableBtIntent, BT_ACTIVATE);
			}
		} else {
			notice.showToast("BluetoothAdapter is null");
			finish();
		}
	}
	
	public void registerFilters(){
		eventsBTReceiver = new EventsBluetoothReceiver(); 

		IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND); 
		IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); 

		registerReceiver(eventsBTReceiver, filter1);
		registerReceiver(eventsBTReceiver, filter2);
	}

	private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            synchronized (msg) {
                switch (msg.what) {
                	case 1:
                		socket = (BluetoothSocket)(msg.obj);
                		
                		if(socket != null){
                			bluetoothComunication = new BluetoothComunication(handler, 3, 2);
                			bluetoothComunication.openComunication(socket);
                		}else{
                			notice.showToast("Socket is null");
                		}
                		break;
            			
                	case 2:
                		String message = (String)(msg.obj);
                		
                		notice.showToast(message);
                		break;
                	
                	case 3:
                		String messageBT = (String)(msg.obj);
                		Intent it=new Intent("com.example.btp.Reply");
                		it.putExtra("request", messageBT);
                		startActivity(it);
                		
                		//historic.add(messageBT);
       				 	//historic.notifyDataSetChanged();
       				 	break;
                }
            }
        };
    };
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(eventsBTReceiver);
		
		closeCommunication();
	}
    
    public void closeCommunication(){
    	try {
    		if(socket != null){
    			socket.close();
    			socket = null;
    		}
    		
    		if(bluetoothServiceTask != null){
				bluetoothServiceTask.closeServerSocket();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case BT_ACTIVATE:
				if (RESULT_OK != resultCode) {
					notice.showToast("Please Activate Your Bluetooth");
					finish(); 
				}
				break;
				
			case BT_VISIBLE:
				if (resultCode == BT_TIMER_VISIBLE) {
					closeCommunication();
					
					bluetoothServiceTask = new BluetoothServiceTask(this, adaptador, handler, 1); 
					bluetoothServiceTask.execute();
				} else {
					notice.showToast("Make Your Bluetooth Visible");
				}
				break;
		}
	}

	private class EventsBluetoothReceiver extends BroadcastReceiver { 
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) { 
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				devicesFound.add(device); 
			} else{
				if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())){
					progressDialog.dismiss();
					exibirDispositivosEncontrados();
				}
			}
		}
	}
	
	private void exibirDispositivosEncontrados() { 

		String[] aparelhos = new String[devicesFound.size()]; 

		for (int i = 0; i < devicesFound.size(); i++){
			aparelhos[i] = devicesFound.get(i).getName();
		}
			
		AlertDialog.Builder dialog = new AlertDialog.Builder(this).setTitle("Available devices are...  ").setItems(aparelhos, new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				
				bluetoothClientTask = new BluetoothClientTask(Bluetooth.this, handler, 1);
				bluetoothClientTask.execute(devicesFound.get(which));

				dialog.dismiss(); 
			}
		});
		dialog.show();
	}
	
}