package manager;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class BluetoothClient{
	 
	 private final UUID MEU_UUID_PC = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	 public BluetoothSocket conectedBluetooth(BluetoothDevice device) {
		 BluetoothSocket socket = null;
		 
		 try {
			 socket = device.createRfcommSocketToServiceRecord(MEU_UUID_PC);
			 socket.connect();
			 
		 } catch (IOException e) { 
			 e.printStackTrace();
		 }
		 
		 return socket;
	 }
	 
 }