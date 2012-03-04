package android.ambientclient.activities;

import android.ambientclient.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//import android.widget.NumberPicker;

public class Registration extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        
        Button ligthButton = (Button)findViewById(R.id.lightButton);
        Button saveButton = (Button)findViewById(R.id.saveConfigButton);
        Button temperatureButton = (Button)findViewById(R.id.temperatureButton);
        
        
        ligthButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				createAlertDialog(getResources().getString(R.string.txt_light));
				
			}
		});
        
        temperatureButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				createAlertDialog(getResources().getString(R.string.txt_temperature));
			}
		});
        
        saveButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(Registration.this,MainActivity.class);
				startActivity(i);
				finish();
			}
		});
        
        
    }
    
    
    private void createAlertDialog(String title){
    	
    	
    	Builder alertDialog =  new AlertDialog.Builder(this);
    	alertDialog.setTitle(title);
//    	alertDialog.setView(npView);
    	alertDialog.setPositiveButton(R.string.button_save,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

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