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
 * Author: Aitor Almeida <aitor.almeida@deusto.es>
 *         Pablo Orduña <pablo.orduna@deusto.es>
 *         Eduardo Castillejo <eduardo.castillejo@deusto.es>
 *
 */

package es.deustotech.piramide.activities.location;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import es.deustotech.piramide.R;
import es.deustotech.piramide.services.LocationService;
import es.deustotech.piramide.utils.constants.Constants;
import es.deustotech.piramide.utils.tts.TextToSpeechManager;
import es.deustotech.piramide.utils.tts.TextToSpeechWeb;

public class CurrentLocation extends Activity {
	
	private TextView location;
	private TextToSpeechManager tts;
	private Vibrator vibrator;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //#if ${piramide.user.capabilities.problems}
			//#if ${piramide.user.capabilities.problems.sight}
				//#if ${piramide.user.capabilities.problems.sight.diopters} < 15
        			setContentView(R.layout.location_normal);
        		//#else
        			setContentView(R.layout.location_sight_disability);
        			tts = new TextToSpeechWeb();
		        	tts.init(getApplicationContext(), Constants.DEFAULT_LANGUAGE);
		        	vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        		//#endif
        	//#endif
        //#endif
        Log.d(Constants.TAG, "Launching CurrentLocation...");
        createCurrentLocationList();
        
        location = (TextView) findViewById(R.id.location);
        location.setText(LocationService.getLocation());
        
        //#if ${piramide.user.capabilities.problems.sight.diopters} > 15
        	tts.speech((String)location.getText());
        //#endif
        
        setResult(Constants.SUCCESS_RETURN_CODE, new Intent());
	}
	
	private void createCurrentLocationList() {
			final ListView list 				= (ListView)findViewById(R.id.location_list_view);
			int adapterLayout;
			//#if ${piramide.user.capabilities.problems.sight.diopters} > 15
				adapterLayout = R.layout.location_sight_disability;
			//#else
				adapterLayout = R.layout.location_normal;
			//#endif
				String[] currentLocation = {LocationService.getLocation()};
				
			final ArrayAdapter<String> adapter 	= new ArrayAdapter<String>(this, adapterLayout, currentLocation);
			list.setAdapter(adapter);
			
			list.setOnItemClickListener(new OnItemClickListener(){
				public void onItemClick(AdapterView<?> adapter, View view, 
						int position, long id) {
					//#if ${piramide.user.capabilities.problems.sight.diopters} > 15
						vibrator.vibrate(500);
						String selection = list.getItemAtPosition(position).toString();
						tts.speech(selection);
					//#endif
				}
			});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onBackPressed() {
		if (tts != null) {
			tts.stop();			
		}
		this.finish();
		super.onBackPressed();
	}
	
	@Override 
	protected void onDestroy() {
		if (tts != null) {
			tts.stop();			
		}
		super.onDestroy();
	}
}
