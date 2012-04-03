package instantambient.ambientclient.activities;

import instantambeint.ambientclient.R;
import instantambient.ambientclient.db.AmbientDB;
import instantambient.ambientclient.db.AmbientData;
import instantambient.ambientclient.db.RoomAmbientDB;
import instantambient.ambientclient.service.BluetoothService;
import instantambient.ambientclient.util.NumberPicker;
import instantambient.ambientclient.util.RoomAmbientListAdapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class AmbientActivity extends Activity {

	boolean newAmbient;
	String ambientState;
	EditText ambientName;
	String roomId;
	Button saveButton;
	
	Button ligthButton;
	Button temperatureButton;
	int light;
	double temperature;
	ListView ambientList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ambient);
		 saveButton = (Button)findViewById(R.id.saveConfigButton);
		 newAmbient = getIntent().getBooleanExtra("NEW_AMBIENT", false);
		 ambientState = getIntent().getStringExtra("AMBIENT_STATE");
		 roomId = getIntent().getStringExtra("ROOM_NAME");
		 ambientList = (ListView)findViewById(R.id.room_ambient);

			ambientList.setOnItemClickListener(new OnItemClickListener() {
				
				public void onItemClick(AdapterView arg0, View arg1, int position, long id) {
					Object obj = ambientList.getItemAtPosition(position);
					if (obj instanceof RoomAmbientDB) {
						Intent na = new Intent(AmbientActivity.this,RoomAmbientActivity.class);
						na.putExtra("NEW_AMBIENT", false);
						na.putExtra("ROOM_NAME", ((RoomAmbientDB)obj).getAmbientName());
						na.putExtra("AMBIENT_ID", roomId);
						startActivity(na);
					}
				}
			});
		 
		 
		 
		 ambientName = (EditText)findViewById(R.id.nameAmbient);
	        if(newAmbient){
	        	setTitle(getString(R.string.title_new_ambient_activity));
	        	saveButton.setText(getResources().getString(R.string.button_save_config));
	        } else {
	        	if(AmbientData.getInstance().getAmbient(roomId) != null)setTitle(AmbientData.getInstance().getAmbient(roomId).getAmbientId());
	        	saveButton.setText(getResources().getString(R.string.button_update_config));
	        	if(AmbientData.getInstance().getAmbient(roomId) != null)ambientName.setText(AmbientData.getInstance().getAmbient(roomId).getAmbientId());
	        	ambientName.setFocusable(false);
	        }
	        
	        ligthButton = (Button)findViewById(R.id.lightButton);
	        temperatureButton = (Button)findViewById(R.id.temperatureButton);
	       
	        
	        ligthButton.setOnClickListener(new android.view.View.OnClickListener() {
				public void onClick(View v) {
					createAlertDialog(getResources().getString(R.string.txt_light), getLigthPicker());
					
				}
			});
	        
	        temperatureButton.setOnClickListener(new android.view.View.OnClickListener() {
				public void onClick(View v) {
					createAlertDialog(getResources().getString(R.string.txt_temperature), getTemperaturePicker());
	
				}
			});
	        
	        AmbientData ad = AmbientData.getInstance();
	        if(newAmbient) {
	        	light = ad.getGenerealAmbient().getLight();
	        	temperature = ad.getGenerealAmbient().getTemperature();
	        } else {
	        	if(AmbientData.getInstance().getAmbient(roomId) != null)light = ad.getAmbient(roomId).getLight();
	        	if(AmbientData.getInstance().getAmbient(roomId) != null)temperature = ad.getAmbient(roomId).getTemperature();
	        }
	        StringBuffer lig = new StringBuffer();
	        lig.append(light).append(" %");
	        ligthButton.setText(lig);
	        StringBuffer temp = new StringBuffer();
	        temp.append(temperature).append(" Grad");
	        temperatureButton.setText(temp.toString());
	        
	        initSave();
	        
	        
	}
	
	
	@Override
	protected void onResume() {
		ArrayList<RoomAmbientDB> data =  new ArrayList<RoomAmbientDB>();
		for (RoomAmbientDB ra : AmbientData.getInstance().getRoomAmbients()){
			if(ra.getRefAmbiendId() != null && ra.getRefAmbiendId().equals(roomId)) {
				data.add(ra);
			}
		}
		RoomAmbientListAdapter listAdapter = new RoomAmbientListAdapter(this);
		listAdapter.setData(data);
		ambientList.setAdapter(listAdapter);
		super.onResume();
	}
	
	private LinearLayout getLigthPicker() {
    	LinearLayout lightLayout = new LinearLayout(this);
    	lightLayout.setGravity(Gravity.CENTER);
    	NumberPicker np = new NumberPicker(this);
    	np.setRange(0, 100);
    	np.setCurrent(light);
    	
    	lightLayout.addView(np, 0);
    	return lightLayout;
    }
    
    private LinearLayout getTemperaturePicker() {
    	NumberPicker firstNumber = new NumberPicker(this);
    	firstNumber.setRange(0, 50);
    	firstNumber.setCurrent((int)temperature);
    	NumberPicker secondNumber = new NumberPicker(this);
    	secondNumber.setRange(0, 9);
    	secondNumber.setCurrent((int)((temperature *10) % 10));
    	LinearLayout temperaturLayout = new LinearLayout(this);
    	temperaturLayout.setGravity(Gravity.CENTER);
    	temperaturLayout.addView(firstNumber, 0);
    	temperaturLayout.addView(secondNumber, 1);
    	return temperaturLayout;
    }
	
	
	private void createAlertDialog(String title, final LinearLayout layout){
    	Builder alertDialog =  new AlertDialog.Builder(this);
    	alertDialog.setTitle(title);
    	if(layout != null)alertDialog.setView(layout);
    	alertDialog.setPositiveButton(R.string.button_save,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	if(layout.getChildCount() > 1) {
	                		NumberPicker firstTemperaturPicker = (NumberPicker)layout.getChildAt(0);
	                		NumberPicker secondTemperaturPicker = (NumberPicker)layout.getChildAt(1);
	                		StringBuffer sb = new StringBuffer();
	                		sb.append(firstTemperaturPicker.getCurrent()).append(".");
	                		sb.append(secondTemperaturPicker.getCurrent());
	                		temperature = new Double(sb.toString());
	                		sb.append(" Grad");
	                		temperatureButton.setText(sb.toString());
                		
                	} else {
                		NumberPicker lightPicker = (NumberPicker)layout.getChildAt(0);
                		light = lightPicker.getCurrent();
                		StringBuffer sb = new StringBuffer();
                		sb.append(lightPicker.getCurrent()).append(" %");
                		ligthButton.setText(sb.toString());
                	}

                }
            });
          alertDialog.setNegativeButton(R.string.button_cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	dialog.dismiss();
                    }
                });
          alertDialog.show();
    }

	 @Override
		public final boolean onCreateOptionsMenu(final Menu menu) {
			final MenuInflater inflater = this.getMenuInflater();
			if(!newAmbient) {
				inflater.inflate(R.menu.ambient_menu, menu);
			}
			return super.onCreateOptionsMenu(menu);
		}

		@Override
		public final boolean onMenuItemSelected(final int featureId,
				final MenuItem item) {
			switch (item.getItemId()) {
			case R.id.add_ambient:
				Intent na = new Intent(AmbientActivity.this,RoomAmbientActivity.class);
				na.putExtra("NEW_AMBIENT", true);
				na.putExtra("AMBIENT_ID", roomId);
				startActivity(na);
			return true;
		
			}
			return super.onMenuItemSelected(featureId, item);
		}
	
		private void initSave(){
			saveButton.setOnClickListener(new android.view.View.OnClickListener() {
				
				public void onClick(View v) {
					AmbientDB ra;
					if(newAmbient) {
						ra =  new AmbientDB();
					} else {
						ra = AmbientData.getInstance().getAmbient(roomId);
					}
					ra.setLight(light);
					ra.setTemperature(temperature);
					ra.setAmbientId(ambientName.getText().toString());
					
					if(newAmbient) {
						AmbientData.getInstance().addAmbient(ra);
					} else {
						AmbientData.getInstance().updateAmbient(ra);
					}
					finish();
				}
			});
		}
	
}
