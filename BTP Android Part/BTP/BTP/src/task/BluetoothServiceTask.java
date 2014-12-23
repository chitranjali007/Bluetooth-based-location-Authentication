package task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import manager.BluetoothService;

public class BluetoothServiceTask extends AsyncTask<Void, Void, BluetoothSocket>{

	private Handler handler;
	private Message message;
	
	private Activity activity;
	private ProgressDialog progressDialog;	
	
	private BluetoothService bluetoothService;

	public BluetoothServiceTask(Activity activity, BluetoothAdapter adapter, Handler handler, int what){
		this.activity = activity;
		this.handler = handler;
		
		message = new Message();
		message.what = what;
	
		bluetoothService = new BluetoothService(adapter);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = ProgressDialog.show(activity, "Listening to connection", "Establishing connection... ");
	}
	
	@Override
	protected BluetoothSocket doInBackground(Void... arg0) {
		
		return bluetoothService.scanBluetooth();
	}
	
	@Override
	protected void onPostExecute(BluetoothSocket result) {
		super.onPostExecute(result);
		
		closeDialog();
		
		message.obj = result;
		handler.dispatchMessage(message);
	}
	
	public void closeServerSocket(){
		bluetoothService.closeServerSocket();
	}
	
	private void closeDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

}	