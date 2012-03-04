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
	
	// Handler that receives messages from the thread
	  private final class ServiceHandler extends Handler {
	      public ServiceHandler(Looper looper) {
	          super(looper);
	      }
	      @Override
	      public void handleMessage(Message msg) {
	         if(isBluetooth()){
	        	activateBluetooth();
	         } 
	    	  
	    	  if(getPairedDevices().size() > 0){
	    		  BluetoothDevice device = getPairedDevices().get("Brains Mac");
	    		  BluetoothConnectThread bct = new BluetoothConnectThread(device);
	    		  bct.start();
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
	  	// den BroadcastReceiver im System registrieren
	  	
	      // For each start request, send a message to start a job and deliver the
	      // start ID so we know which request we're stopping when we finish the job
	      Message msg = mServiceHandler.obtainMessage();
	      msg.arg1 = startId;
	      mServiceHandler.sendMessage(msg);
	      
	      // If we get killed, after returning from here, restart
	      return START_STICKY;
	  }

	  
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	private boolean isBluetooth(){
		ba = BluetoothAdapter.getDefaultAdapter();
		return ba != null ? true : false;
	}
	
	private void activateBluetooth(){
		if (!ba.isEnabled()) {
		  Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		  startActivity(enableBtIntent);
		}
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
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            // das Bluetooth-Gerät aus dem Intent holen
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            // Hinzufügen des Namens und der Adresse in ein Array
	            devices.put(device.getName(), device.getAddress());
	        }
	    }

		
	};


	
	
}
