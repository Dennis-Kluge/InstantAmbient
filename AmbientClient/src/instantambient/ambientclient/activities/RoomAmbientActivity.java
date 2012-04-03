package instantambient.ambientclient.activities;

import instantambeint.ambientclient.R;
import instantambient.ambientclient.db.AmbientData;
import instantambient.ambientclient.db.RoomAmbientDB;
import instantambient.ambientclient.util.NumberPicker;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class RoomAmbientActivity extends Activity {
	
	static final int TEMP = 0;
	static final int RED = 1;
	static final int GREEN = 2;
	static final int BLUE = 3;
	
	
	Button ligthButton;
	Button temperatureButton;
	Button redLigthButton;
	Button greenLigthButton;
	Button blueLigthButton;
	LinearLayout lightLayout;
	
	static int clickedButton;
	
	int light;
	double temperature;
	double redLight;
	double greenLight;
	double blueLight;
	boolean newAmbient;
	String ambientState;
	EditText ambientName;
	String roomId;
	String roomType;
	String ambientId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room_ambient);
		 Button saveButton = (Button)findViewById(R.id.saveConfigButton);
		 newAmbient = getIntent().getBooleanExtra("NEW_AMBIENT", false);
		 ambientState = getIntent().getStringExtra("AMBIENT_STATE");
		 roomId = getIntent().getStringExtra("ROOM_NAME");
		 ambientId = getIntent().getStringExtra("AMBIENT_ID");
		 ambientName = (EditText)findViewById(R.id.nameAmbient);
	        if(newAmbient){
	        	setTitle(getString(R.string.title_new_ambient_activity));
	        	saveButton.setText(getResources().getString(R.string.button_save_config));
	        } else {
	        	if(AmbientData.getInstance().getRoomAmbient(roomId) != null)setTitle(AmbientData.getInstance().getRoomAmbient(roomId).getAmbientName());
	        	saveButton.setText(getResources().getString(R.string.button_update_config));
	        	if(AmbientData.getInstance().getRoomAmbient(roomId) != null)ambientName.setText(AmbientData.getInstance().getRoomAmbient(roomId).getAmbientName());
	        	ambientName.setFocusable(false);
	        }
			ligthButton = (Button)findViewById(R.id.lightButton);
	        temperatureButton = (Button)findViewById(R.id.temperatureButton);
	        redLigthButton = (Button)findViewById(R.id.redLightButton);
	        greenLigthButton = (Button)findViewById(R.id.greenLightButton);
	        blueLigthButton = (Button)findViewById(R.id.blueLightButton);
	        lightLayout = (LinearLayout)findViewById(R.id.lightLayout);
	        
	        if(ambientState != null && ambientState.equals(AmbientData.BEDROOM)) {
	        	lightLayout.setVisibility(View.GONE);
	        } else {
	        	lightLayout.setVisibility(View.VISIBLE);
	        }
	        
	        
	        ligthButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					createAlertDialog(getResources().getString(R.string.txt_light), getLigthPicker());
					
				}
			});
	        
	        temperatureButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					clickedButton = TEMP;
					createAlertDialog(getResources().getString(R.string.txt_temperature), getTemperaturePicker());

				}
			});
		
	        redLigthButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					clickedButton = RED;
					createAlertDialog(getResources().getString(R.string.txt_light), getColorPicker(RED));
					
				}
			});
	        
	        greenLigthButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					clickedButton = GREEN;
					createAlertDialog(getResources().getString(R.string.txt_light), getColorPicker(GREEN));
					
				}
			});
	        
	        blueLigthButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					clickedButton = BLUE;
					createAlertDialog(getResources().getString(R.string.txt_light), getColorPicker(BLUE));
				}
			});
	        
	        AmbientData ad = AmbientData.getInstance();
	        if(newAmbient) {
	        	light = ad.getGenerealAmbient().getLight();
	        	temperature = ad.getGenerealAmbient().getTemperature();
	        } else {
	        	light = ad.getRoomAmbient(roomId).getLight();
	        	temperature = ad.getRoomAmbient(roomId).getTemperature();
	        }
	        
	        StringBuffer lig = new StringBuffer();
	        StringBuffer temp = new StringBuffer();
	        
	        lig.append(light).append(" %");
	        ligthButton.setText(lig);
	        
	        temp.append(temperature).append(" Grad");
	        temperatureButton.setText(temp.toString());
	        
	        RoomAmbientDB ra = ad.getRoomAmbient(roomId);
	        
	        
	        if(ra != null) {
	        	roomType = ra.getRoomType();
	        	redLight = ra.getRedLight();
	        	greenLight = ra.getGreenLight();
	        	blueLight = ra.getBlueLight();
	        }
	        
	        redLigthButton.setText(""+redLight);
	        greenLigthButton.setText(""+greenLight);
	        blueLigthButton.setText(""+blueLight);
	        
	        saveButton.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					RoomAmbientDB ra;
					if(newAmbient) {
						ra =  new RoomAmbientDB();
					} else {
						ra = AmbientData.getInstance().getRoomAmbient(roomId);
					}
					ra.setLight(light);
					ra.setTemperature(temperature);
					ra.setRedLight(redLight);
					ra.setGreenLight(greenLight);
					ra.setBlueLight(blueLight);
					ra.setAmbientName(ambientName.getText().toString());
					ra.setRoomType(roomType);
					ra.setRefAmbiendId(ambientId);
					if(newAmbient) {
						AmbientData.getInstance().addRoomAmbient(ra);
					} else {
						AmbientData.getInstance().updateRoomAmbient(ra);
					}
					finish();
				}
			});
	        
	        
	        Spinner s = (Spinner) findViewById(R.id.spinner1);
	        ArrayAdapter adapter = ArrayAdapter.createFromResource(
	                this, R.array.roomambients, android.R.layout.simple_spinner_item);
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        s.setAdapter(adapter);
	        
	        s.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parent, View view,
						int pos, long id) {
					Object item = parent.getItemAtPosition(pos);
					if(item.equals(AmbientData.BEDROOM_SPINNER)) {
						roomType = AmbientData.BEDROOM;
						lightLayout.setVisibility(View.GONE);
					} else {
						roomType = AmbientData.LIVINGROOM;
						lightLayout.setVisibility(View.VISIBLE);
					}
				}

				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
	        
	        
	        if(roomType != null && roomType.equals(AmbientData.BEDROOM)) {
	        	s.setSelection(1);
	        } else {
	        	s.setSelection(0);
	        }
	        
		
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
	    	firstNumber.setRange(0, 38);
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
	    
	    private LinearLayout getColorPicker(int color) {
	    	double temp = 0;
	    	switch (color) {
			case RoomAmbientActivity.RED:
				temp = redLight;
				break;
			case RoomAmbientActivity.GREEN:
				temp = greenLight;
				break;
			case RoomAmbientActivity.BLUE:
				temp = blueLight;
				break;

			default:
				break;
			}
	    	NumberPicker firstNumber = new NumberPicker(this);
	    	firstNumber.setRange(0, 1);
	    	firstNumber.setCurrent((int)temp);
	    	NumberPicker secondNumber = new NumberPicker(this);
	    	secondNumber.setRange(0, 9);
	    	secondNumber.setCurrent((int)((temp *10) % 10));
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
	                		if(clickedButton == TEMP) {
		                		NumberPicker firstTemperaturPicker = (NumberPicker)layout.getChildAt(0);
		                		NumberPicker secondTemperaturPicker = (NumberPicker)layout.getChildAt(1);
		                		StringBuffer sb = new StringBuffer();
		                		sb.append(firstTemperaturPicker.getCurrent()).append(".");
		                		sb.append(secondTemperaturPicker.getCurrent());
		                		temperature = new Double(sb.toString());
		                		sb.append(" Grad");
		                		temperatureButton.setText(sb.toString());
	                		} else {
	                			NumberPicker firstColorPicker = (NumberPicker)layout.getChildAt(0);
		                		NumberPicker secondColorPicker = (NumberPicker)layout.getChildAt(1);
		                		StringBuffer sbC = new StringBuffer();
		                		sbC.append(firstColorPicker.getCurrent()).append(".");
		                		sbC.append(secondColorPicker.getCurrent());
		                		switch (RoomAmbientActivity.clickedButton) {
		                		case RoomAmbientActivity.RED:
		            				redLight = new Double(sbC.toString());
		            				redLigthButton.setText(sbC.toString());
		            				break;
		            			case RoomAmbientActivity.GREEN:
		            				greenLight = new Double(sbC.toString());
		            				greenLigthButton.setText(sbC.toString());
		            				break;
		            			case RoomAmbientActivity.BLUE:
		            				blueLight = new Double(sbC.toString());
		            				blueLigthButton.setText(sbC.toString());
		            				break;

		            			default:
		            				break;
		            			}
	                		}
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
	    
	  
}
