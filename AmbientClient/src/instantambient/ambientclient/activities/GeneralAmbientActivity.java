package instantambient.ambientclient.activities;

import instantambeint.ambientclient.R;
import instantambient.ambientclient.db.AmbientData;
import instantambient.ambientclient.util.NumberPicker;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class GeneralAmbientActivity extends Activity{

	Button ligthButton;
	Button temperatureButton;
	
	int light;
	double temperature;
	boolean startFromMain = false;

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_ambient);
        Button saveButton = (Button)findViewById(R.id.saveConfigButton);
        if(AmbientData.getInstance().getGenerealAmbient().isFirstStart()){
        	setTitle(getString(R.string.title_registration));
        	saveButton.setText(getResources().getString(R.string.button_save_config));
        } else {
        	setTitle(getString(R.string.title_general_ambient_activity));
        	saveButton.setText(getResources().getString(R.string.button_update_config));
        }
        ligthButton = (Button)findViewById(R.id.lightButton);
        temperatureButton = (Button)findViewById(R.id.temperatureButton);
        

        startFromMain =  getIntent().getBooleanExtra("START_FROM_MAIN", false);
        
        ligthButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				createAlertDialog(getResources().getString(R.string.txt_light), getLigthPicker());
				
			}
		});
        
        temperatureButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				createAlertDialog(getResources().getString(R.string.txt_temperature), getTemperaturePicker());

			}
		});
        
        AmbientData ad = AmbientData.getInstance();
        
        StringBuffer lig = new StringBuffer();
        light = ad.getGenerealAmbient().getLight();
        lig.append(light).append(" %");
        ligthButton.setText(lig);
        StringBuffer temp = new StringBuffer();
        temperature = ad.getGenerealAmbient().getTemperature();
        temp.append(temperature).append(" Grad");
        temperatureButton.setText(temp.toString());
        
        
        
        saveButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				AmbientData.getInstance().getGenerealAmbient().setLight(light);
				AmbientData.getInstance().getGenerealAmbient().setTemperature(temperature);
				if(AmbientData.getInstance().getGenerealAmbient().isFirstStart()) {
					AmbientData.getInstance().getGenerealAmbient().setFirstStart(false);
					AmbientData.getInstance().updateGeneralAmbient();
					Intent i = new Intent(GeneralAmbientActivity.this,MainActivity.class);
					startActivity(i);
				}
				AmbientData.getInstance().updateGeneralAmbient();
				
				finish();
			}
		});
    }
    
    @Override
    protected void onResume() {
    	if(!AmbientData.getInstance().getGenerealAmbient().isFirstStart() && !startFromMain) {
    		Intent i = new Intent(GeneralAmbientActivity.this,MainActivity.class);
			startActivity(i);
    		finish();
    	}
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
	
}
