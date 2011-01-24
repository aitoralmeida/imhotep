/*
 * Copyright (C) 2010 PIRAmIDE-SP3 authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Eduardo Castillejo <eduardo.castillejo@deusto.es>
 *
 */

package es.deustotech.piramide.activities.location;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import es.deustotech.piramide.R;
import es.deustotech.piramide.activities.options.Help;
import es.deustotech.piramide.utils.constants.Constants;
import es.deustotech.piramide.utils.distancecalc.DistanceCalculator;

public class Directions extends Activity implements TextToSpeech.OnInitListener{
	
	private static final String[] 	DIRECTIONS_ITEMS = new String[]{
		"Gire a la derecha (58 m)", "Gire a la izquierda (10 m)", 
		"Siga recto (30 m)", "Ha llegado a su destino!"
		};
	private volatile String distance = "0 metros";
	private TextView txtDistance;
	private TextToSpeech tts;
	private Vibrator vibrator;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			txtDistance.setText(distance  + " metros");
		}
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directions_list_item);
        distance 	= getIntent().getExtras().getString("distance");
        txtDistance = (TextView) findViewById(R.id.txt_distance);
		txtDistance.setText(distance + " metros");
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		createDirectionsList();
        
        Log.d(Constants.TAG, "Launching Directions...");
        
      //#if ${piramide.user.capabilities.problems.sight.diopters} > 15
       tts = new TextToSpeech(this, this);
      //#endif
        setResult(Constants.SUCCESS_RETURN_CODE, new Intent());
	}
	
	public void onStart(){
		super.onStart();
		Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				distance = updateCurrentDistance();
				speak(distance);
				handler.sendMessage(handler.obtainMessage());
			}
		};
        timer.schedule(timerTask, 0, Constants.DELAY_TIME_UPDATE_DISTANCE);
	}
	
	public void onStop(){
		super.onStop();
	}
	
	private String updateCurrentDistance() {
		final Address currentAddress 	= Points.getCurrentAddress();
		final Address selectedAddress 	= Points.getSelectedAddress();
		final double updatedDistance 	= DistanceCalculator.calculateDistance(currentAddress, selectedAddress, Constants.METERS);
		
		return String.valueOf(Math.round(updatedDistance));
	}
	
	private void createDirectionsList() {
		final ListView list 				= (ListView)findViewById(R.id.list_view);
		final int adapterLayout 			= R.layout.list_item_sight_disability;
		final ArrayAdapter<String> adapter 	= new ArrayAdapter<String>(this, adapterLayout, DIRECTIONS_ITEMS);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> adapter, View view, 
					int position, long id) {
				//#if ${piramide.user.capabilities.problems.sight.diopters} > 15
					vibrator.vibrate(500);
					String selection = list.getItemAtPosition(position).toString();
					if (tts!=null){
						tts.stop();
						speak(selection);
					}
				//#endif
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onResume() {
		if (tts != null){
			speak("Listando indicaciones");
			speak("Distancia al punto, " + (String)txtDistance.getText());
			for(String indication : DIRECTIONS_ITEMS)
				speak(indication);
		}
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.directions_options_menu, menu);
	    
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
		    case R.id.help:
		    	showHelpActivity();
		    	return true;
//		    case R.id.location:
//		    	showLocationActivity();
//		    	return true;
		    default:
		    	return super.onOptionsItemSelected(item);
	    }
	}
	/*
	private void showLocationActivity() {
		if (tts != null) {
			tts.stop();
		}
		if (LocationService.getLocation().equals("No location found")) {
			Toast.makeText(getApplicationContext(), "Unnable to determine your location", Constants.SHORT_DURATION).show();
		} else {
			startActivityForResult(new Intent(Directions.this, 
					CurrentLocation.class), 0);
		}
	}
	*/
	
	private void showHelpActivity() {
		if (tts != null) {
			tts.stop();
		}
		startActivityForResult(new Intent(Directions.this, 
				Help.class), 0);
	}
	/*
	private void exit() {
		this.finish();
	}
	*/
	
	@Override
	public void onBackPressed() {
		if (tts != null){
			tts.stop();
		}
		super.onBackPressed();
	}
	
	@Override 
	protected void onDestroy() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public void onInit(int status) {
		// status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // Note that a language may not be available, and the result will indicate this.
            int result = tts.setLanguage(Locale.US);
            // Try this someday for some interesting results.
            // int result tts.setLanguage(Locale.FRANCE);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
               // Lanuage data is missing or the language is not supported.
                Log.e(Constants.TAG, "Language is not available.");
            } else {
                // Check the documentation for other possible result codes.
                // For example, the language may be available for the locale,
                // but not for the specified country and variant.

                // The TTS engine has been successfully initialized.
                // Allow the user to press the button for the app to speak again.
//                button.setEnabled(true);
                // Greet the user.
            	speak("Listando indicaciones");
            	
            	speak("Distancia al punto, " + (String)txtDistance.getText());
            	
            	for(String indication : DIRECTIONS_ITEMS)
            		speak(indication);
            }
        } else {
            // Initialization failed.
            Log.e(Constants.TAG, "Could not initialize TextToSpeech.");
        }
	}
	
	private void speak(String message){
        tts.speak(message,
            TextToSpeech.QUEUE_ADD,  // Drop all pending entries in the playback queue.
            null);
	}
}