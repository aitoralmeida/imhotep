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

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Vector;

import android.app.Activity;
import android.app.ProgressDialog;
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
import es.deustotech.piramide.R;
import es.deustotech.piramide.activities.options.Help;
import es.deustotech.piramide.services.LocationService;
import es.deustotech.piramide.utils.constants.Constants;
import es.deustotech.piramide.utils.distancecalc.DistanceCalculator;
import es.deustotech.piramide.utils.parcelable.Point;

public class Points extends Activity implements TextToSpeech.OnInitListener{

	private Vector<Point> pointsVector;
	private static Address currentAddress;
	private static Address selectedAddress;
	private static String selectedPointName;
	private TextToSpeech tts;
	private Vibrator vibrator;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_menu);
        
        Log.d(Constants.TAG, "Launching Points...");
        
        final Bundle extras = getIntent().getExtras();
        pointsVector = new Vector<Point>();
        
        for (int i=0; i< extras.getInt("POINTS_SIZE"); i++){
        	Point point = new Point();
        	point.setStreetAddress(extras.getString("Point"+i+"Street"));
        	point.setTitle(extras.getString("Point"+i+"Title"));
        	point.setLatitude(String.valueOf(extras.getDouble("Point"+i+"Latitude")));
        	point.setLongitude(String.valueOf(extras.getDouble("Point"+i+"Longitude")));
        	
        	pointsVector.add(point);
        }
        
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        
        //TODO: use EncapsulatePoints class
        if (pointsVector.size() > 0) {
        	setResult(Constants.SUCCESS_RETURN_CODE, new Intent());
        	createMenu(pointsVector);
        } else {
        	Log.d(Constants.TAG, "Vector is null");
        }
	}
	
	private void createMenu(final Vector<Point> vector) {
		ArrayAdapter<String> adapter;
		int adapterLayout 	= 0;
		final ListView list 		= (ListView)findViewById(R.id.list_view);
		//#if ${piramide.user.capabilities.problems}
			//#if ${piramide.user.capabilities.problems.sight}
				//#if ${piramide.user.capabilities.problems.sight.diopters} < 15
					adapterLayout = R.layout.list_item_normal;
				//#else
					adapterLayout = R.layout.list_item_sight_disability;
					tts = new TextToSpeech(this, this);
				//#endif
			//#endif
		//#endif
					
		final String[] pointList = new String[vector.size()];
		for (int i=0; i<vector.size(); i++){
			pointList[i] = vector.get(i).getTitle();
		}
		
		final String[] latitudeList = new String[vector.size()];
		for (int i=0; i<vector.size(); i++){
			latitudeList[i] = vector.get(i).getLatitude();
		}
		
		final String[] longitudeList = new String[vector.size()];
		for (int i=0; i<vector.size(); i++){
			longitudeList[i] = vector.get(i).getLongitude();
		}
		
		adapter = new ArrayAdapter<String>(this, adapterLayout, pointList);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener(){
			ProgressDialog dialog = null;
			final Handler dialogHandler = new Handler(){
				
				@Override
				public void handleMessage(Message msg){
					dialog.cancel();
				}
			};
			public void onItemClick(AdapterView<?> adapter, View view, 
					int position, long id) {
				vibrator.vibrate(500);
				selectedPointName = list.getItemAtPosition(position).toString();
				dialog = ProgressDialog.show(Points.this, "", 
                        "Consultando lugares...", true);
				if (tts != null){
					//#if ${piramide.user.capabilities.problems.sight.diopters} > 15
					tts.stop();
					speak(Constants.SELECTED + selectedPointName);
					//#endif
				}
				dialogHandler.sendMessage(dialogHandler.obtainMessage());
				Intent intent = new Intent(Points.this, Categories.getOnLoadActivity());
				//#if ${piramide.user.capabilities.problems}
					//#if ${piramide.user.capabilities.problems.sight}
							selectedAddress = new Address(new Locale("es", "ES"));
							currentAddress 	= new Address(new Locale("es", "ES"));
			
							//TODO: manage if location is null (no GPS signal available or whatever)
							currentAddress.setLatitude(LocationService.getCurrentLatitude());
							currentAddress.setLongitude(LocationService.getCurrentLongitude());
			
							final Point selectedPoint = vector.get(position);
			
							selectedAddress.setLatitude(Double.parseDouble(selectedPoint.getLatitude()));
							selectedAddress.setLongitude(Double.parseDouble(selectedPoint.getLongitude()));
			
							final double distance = DistanceCalculator.calculateDistance(currentAddress, selectedAddress, Constants.METERS);
							intent.putExtra("distance", formatDistance(distance));
					//#endif
							startActivityForResult(intent, 0);
				//#endif
			}

			private String formatDistance(final double distance) {
				final DecimalFormat fmt = new DecimalFormat("##.##");
				final String finalDistance = fmt.format(distance);
				return finalDistance;
			}});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.points_options_menu, menu);
	    
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
		    case R.id.help:
		    	showHelpActivity();
		    	return true;
		    default:
		    	return super.onOptionsItemSelected(item);
	    }
	}
	
	private void showHelpActivity() {
		if (tts != null) {
			tts.stop();
		}
		startActivityForResult(new Intent(Points.this, 
				Help.class), 0);
	}
/*
	private void exit() {
		if (tts != null)
			tts.stop();
		this.finish();
	}
*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onResume() {
		if (tts != null){
			speak("Listando " + Categories.getSelection() + " cercanos");
			for(Point point : pointsVector)
				speak(point.getTitle());
		}
		super.onResume();
	}
	
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

	public static Address getCurrentAddress() {
		return currentAddress;
	}

	public static Address getSelectedAddress() {
		return selectedAddress;
	}

	public static String getSelectedPointName() {
		return selectedPointName;
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
            	speak("Listando " + Categories.getSelection() + " cercanos");
            	
            	for(Point point : pointsVector)
            		speak(point.getTitle());
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