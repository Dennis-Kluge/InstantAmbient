package android.ambientclient.service;

import java.util.HashMap;
import java.util.Set;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class BluetoothService extends Service{

	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	private BluetoothAdapter ba;
	private HashMap<String, BluetoothDevice> devices;
	private BluetoothDevice bd;
	private BluetoothConnectThread bct;
	
	// Handler that receives messages from the thread
	  private final class ServiceHandler extends Handler {
	      public ServiceHandler(Looper looper) {
	          super(looper);
	      }
	      @Override
	      public void handleMessage(Message msg) {
	    	
	    	  if(getPairedDevices().size() > 0){
	    		  if(isCorrectDevice()) {
	    			  System.out.println("Ausgabe: start thread pair");
	    			  startDataThread();
	    		  } else {
	    			  System.out.println("Ausgabe: start suche wenn nicht dabei ohnr pair");
	    			  ba.startDiscovery();
	    		  }
	    	  } else {
	    		  System.out.println("Ausgabe: start ohne pair");
	    		  ba.startDiscovery();
	    	  }
	    	  
	          // Stop the service using the startId, so that we don't stop
	          // the service in the middle of handling another job
	          stopSelf(msg.arg1);
	      }
	  }

	
	  @Override
	  public void onCreate() {
		  
		devices = new HashMap<String, BluetoothDevice>();
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		registerReceiver(mReceiver, filter);
	    // Start up the thread running the service.  Note that we create a
	    // separate thread because the service normally runs in the process's
	    // main thread, which we don't want to block.  We also make it
	    // background priority so CPU-intensive work will not disrupt our UI.
	    HandlerThread thread = new HandlerThread("BluetoothService");
	    thread.start();
	    
	    // Get the HandlerThread's Looper and use it for our Handler 
	    mServiceLooper = thread.getLooper();
	    mServiceHandler = new ServiceHandler(mServiceLooper);
	  }

	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	      Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
	      ba = BluetoothAdapter.getDefaultAdapter();
	      // For each start request, send a message to start a job and deliver the
	      // start ID so we know which request we're stopping when we finish the job
	      Message msg = mServiceHandler.obtainMessage();
	      msg.arg1 = startId;
	      mServiceHandler.sendMessage(msg);
	      
	      // If we get killed, after returning from here, restart
	      return START_STICKY;
	  }

	  
	@Override
	public void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}  
	  
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	private HashMap<String, BluetoothDevice> getPairedDevices(){
		Set<BluetoothDevice> pairedDevices = ba.getBondedDevices();
		// wenn gepaarte Geräte existieren
		if (pairedDevices.size() > 0) {
		    // Durchgehen der gepaarten Geräte
		    for (BluetoothDevice device : pairedDevices) {
		                // einem Array die Addresse und den Namen der Geräte hinzufügen
		        devices.put(device.getName(), device);
		    }
		}
		return devices;
	}
	
	// BroadcastReceiver für ACTION_FOUND
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    @Override
		public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        // wenn durch die Suche ein Gerät gefunden wurde
	        System.out.println("Ausgabe: action found: " + action);
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            // das Bluetooth-Gerät aus dem Intent holen
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            if(isCorrectDevice()) {
	            	startDataThread();
	            }
	        }
	        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
	        	writeData("Hier kommen die richtigen daten rein");
	        }
	    }
		
	};
	
	private void startDataThread(){
		if(bd == null) return;
		bct = new BluetoothConnectThread(bd, ba);
		bct.start();
	}

	public void writeData(String data) {
		if(bct != null) {
			bct.writeData(data);
		}
	}

	private boolean isCorrectDevice(){
		bd = null;
		if(getPairedDevices().get("BrainsMac") != null || getPairedDevices().get("Swens MacBook Pro") != null) {
			
		}
		BluetoothDevice device = getPairedDevices().get("BrainsMac");
//		BluetoothDevice device = getPairedDevices().get("Swens MacBook Pro");
		if(device != null) {
			bd = device;
			return true;
		} else {
			return false;
		}
	}
	
}
