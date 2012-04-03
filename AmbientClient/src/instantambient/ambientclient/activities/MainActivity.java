package instantambient.ambientclient.activities;


import java.util.ArrayList;

import instantambeint.ambientclient.R;
import instantambient.ambientclient.App;
import instantambient.ambientclient.db.AmbientDB;
import instantambient.ambientclient.db.AmbientData;
import instantambient.ambientclient.db.RoomAmbientDB;
import instantambient.ambientclient.service.BluetoothService;
import instantambient.ambientclient.util.AmbientListAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity{
	
	private final int REQUEST_ENABLE_BT = 1;
	BluetoothAdapter ba;
	ListView ambientList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		ambientList = (ListView)findViewById(R.id.ambient_list);
		ambientList.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView arg0, View arg1, int position, long id) {
				Object obj = ambientList.getItemAtPosition(position);
				if (obj instanceof AmbientDB) {
					Intent na = new Intent(MainActivity.this,AmbientActivity.class);
					na.putExtra("NEW_AMBIENT", false);
					na.putExtra("ROOM_NAME", ((AmbientDB)obj).getAmbientId());
					AmbientData.curentId = ((AmbientDB)obj).getAmbientId();
					startActivity(na);
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		if(isBluetooth()) {
			activateBluetooth();
		} 
		ArrayList<AmbientDB> data =  new ArrayList<AmbientDB>();
		for (AmbientDB ra : AmbientData.getInstance().getAmbients()){
			data.add(ra);
		}
		AmbientListAdapter listAdapter = new AmbientListAdapter(this);
		listAdapter.setData(data);
		ambientList.setAdapter(listAdapter);
		super.onResume();
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
	
	@Override
	public final boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public final boolean onMenuItemSelected(final int featureId,
			final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_ambient:
			AlertDialog.Builder ab =  new AlertDialog.Builder(MainActivity.this);
			ab.setMessage(App.getInstance().getResources().getString(R.string.txt_select_ambient));
			ab.setPositiveButton(getResources().getString(R.string.ambient_home), new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent na = new Intent(MainActivity.this,AmbientActivity.class);
					na.putExtra("NEW_AMBIENT", true);
					startActivity(na);
				}
			});
			ab.setNegativeButton(getResources().getString(R.string.ambient_car), new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent na = new Intent(MainActivity.this,AmbientActivity.class);
					na.putExtra("NEW_AMBIENT", true);
					startActivity(na);
				}
			});
			ab.show();
			return true;
		case R.id.general_ambient:
			Intent ga = new Intent(MainActivity.this,GeneralAmbientActivity.class);
			ga.putExtra("START_FROM_MAIN", true);
			startActivity(ga);
			
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

}
