package android.ambientclient.activities;

import java.util.ArrayList;

import android.ambientclient.R;
import android.ambientclient.service.BluetoothService;
import android.ambientclient.util.AmbientListAdapter;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends ListActivity{
	
	private final int REQUEST_ENABLE_BT = 1;
	BluetoothAdapter ba;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main_view);
		
		AmbientListAdapter listAdapter = new AmbientListAdapter(this);
		
		ArrayList<String> data =  new ArrayList<String>();
		for (int i = 0; i < 20; i++){
			data.add("Hier stehen die ganzen verschiedenen Umgebungen drin");
		}
		
		
		listAdapter.setData(data);
		
		setListAdapter(listAdapter);
		
		
		if(isBluetooth()) {
			activateBluetooth();
		} 
		
		
	}
	
	private boolean isBluetooth(){
		ba = BluetoothAdapter.getDefaultAdapter();
		return ba != null ? true : false;
	}
	
	private void activateBluetooth(){
		if (!ba.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			startService();
		}
	}
	
	private void startService(){
		Intent service = new Intent(MainActivity.this, BluetoothService.class);
		startService(service);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_ENABLE_BT) {
			if(ba.isEnabled()) {
				startService();
			} else {
				AlertDialog.Builder ad = new AlertDialog.Builder(this);
				ad.setMessage(R.string.txt_enable_bluetooth);
				ad.setPositiveButton(getResources().getString(R.string.button_ok), new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				ad.show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
