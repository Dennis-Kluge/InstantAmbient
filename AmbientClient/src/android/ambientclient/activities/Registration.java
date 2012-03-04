package android.ambientclient.activities;

import android.ambientclient.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//import android.widget.NumberPicker;

public class Registration extends Activity {
=======
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button; 
import android.widget.LinearLayout;
import android.ambientclient.util.NumberPicker;

public class Registration extends Activity {
	
	Button ligthButton;
	Button temperatureButton;
	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        ligthButton = (Button)findViewById(R.id.lightButton);
        temperatureButton = (Button)findViewById(R.id.temperatureButton);
        Button saveButton = (Button)findViewById(R.id.saveConfigButton);

        
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
        
        saveButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(Registration.this,MainActivity.class);
				startActivity(i);

//				finish();

			}
		});
        
        
    }
    
    

    private LinearLayout getLigthPicker() {
    	LinearLayout lightLayout = new LinearLayout(this);
    	lightLayout.setGravity(Gravity.CENTER);
    	NumberPicker np = new NumberPicker(this);
    	np.setRange(0, 100);
    	np.setCurrent(80);
    	
    	lightLayout.addView(np, 0);
    	return lightLayout;
    }
    
    private LinearLayout getTemperaturePicker() {
    	NumberPicker firstNumber = new NumberPicker(this);
    	firstNumber.setRange(0, 50);
    	firstNumber.setCurrent(22);
    	NumberPicker secondNumber = new NumberPicker(this);
    	secondNumber.setRange(0, 9);
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
                		sb.append(firstTemperaturPicker.getCurrent()).append(",");
                		sb.append(secondTemperaturPicker.getCurrent()).append(" Grad");
                		temperatureButton.setText(sb.toString());
                	} else {
                		NumberPicker lightPicker = (NumberPicker)layout.getChildAt(0);
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
//            alertDialog.create();
          alertDialog.show();
    }
}