package android.ambientclient.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

public class BluetoothConnectThread extends Thread{
	static byte[] uuid = {(byte) 1101};
	private static final UUID MY_UUID = UUID.nameUUIDFromBytes(uuid);
	private BluetoothSocket bs;
	private final BluetoothDevice bd;
	private final BluetoothAdapter ba;
	private BluetoothConnectedThread bct;

    public BluetoothConnectThread(BluetoothDevice device, BluetoothAdapter adapter) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        bd = device;
        ba = adapter;
        // Get a BluetoothSocket to connect with the given BluetoothDevice.
        try {
        	
		 Method m = bd.getClass().getMethod("createRfcommSocket",
		            new Class[] { int.class });
		        tmp = (BluetoothSocket)m.invoke(bd, Integer.valueOf(1));
        	
            // MY_UUID is the app’s UUID string, also used by the server code.
//            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
           
           
        	        
        } catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        bs = tmp;
    }

    public void run() {
        // Cancel discovery because it will slow down the connection.
    	ba.cancelDiscovery();
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception.
            bs.connect();
        } catch (IOException connectException) {
        	System.out.println("Ausgabe: fehler beim verbinden: " + connectException);
            // Unable to connect; close the socket and get out.
        	manageConnectedSocket(bs);
            try {
                bs.close();
            } catch (IOException closeException) { }
            return;
        }

        // Do work to manage the connection (in a separate thread).
        manageConnectedSocket(bs);
    }

    private void manageConnectedSocket(BluetoothSocket bs) {
		bct = new BluetoothConnectedThread(bs);
		bct.start();
		writeData("Hallo");
	}

	/** Will cancel an in-progress connection, and close the socket. */
    public void cancel() {
        
        try {
			bs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    }
    
    public void writeData(String data){
    	String jString = "";
		try {
		StringBuffer sb = new StringBuffer();
		JSONObject jb = new JSONObject();
		for(int i= 0;i<20;i++){
			jb.put("id"+i, 1);
			jb.put("light"+i, 60);
		}
		sb.append(jb.toString());
		sb.append("\n");
		jString = sb.toString();
		System.out.println("Ausgabe: jsonstring: " + jString); 
		} catch (JSONException e) {
			
		}
//		String string = "Hallo ihr da draußen!";
		if(bct != null)bct.write(jString.getBytes());
    }

}
