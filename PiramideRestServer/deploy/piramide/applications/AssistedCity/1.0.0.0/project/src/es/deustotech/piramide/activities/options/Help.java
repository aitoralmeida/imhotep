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

package es.deustotech.piramide.activities.options;

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
import es.deustotech.piramide.R;
import es.deustotech.piramide.utils.constants.Constants;
import es.deustotech.piramide.utils.tts.TextToSpeechManager;
import es.deustotech.piramide.utils.tts.TextToSpeechWeb;

public class Help extends Activity {
	private Vibrator vibrator;
	private TextToSpeechManager tts;
	private boolean firstCall = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		configureItems();
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		//#if ${piramide.user.capabilities.problems.sight.diopters} > 15
			setContentView(R.layout.help_sight_disability);
		//#else
			setContentView(R.layout.help);
		//#endif
		Log.d(Constants.TAG, "Launching Help...");

		//#if ${piramide.user.capabilities.problems.sight.diopters} > 15
		if (firstCall){
			tts = new TextToSpeechWeb();
			tts.init(getApplicationContext(), Constants.DEFAULT_LANGUAGE);
			createHelpList();
		}
		//#endif
		firstCall = false;
		setResult(Constants.SUCCESS_RETURN_CODE, new Intent());
	}
	
	@Override
	protected void onResume() {
		//#if ${piramide.user.capabilities.problems.sight.diopters} > 15
			tts.speech("Ha seleccionado, ayuda");
			readHelpMessage();
		//#endif
		super.onResume();
	}

	private void readHelpMessage(){
		if (tts != null){
			tts.speech(Constants.HELP_MESSAGE_1);
			tts.speech(Constants.HELP_MESSAGE_2);
			tts.speech(Constants.HELP_MESSAGE_3);
			tts.speech(Constants.HELP_MESSAGE_4);
			tts.speech(Constants.HELP_MESSAGE_5);
		}
	}
	
	private void createHelpList(){
		final ListView list 		= (ListView)findViewById(R.id.help_list_view);
		final int adapterLayout 	= R.layout.list_item_sight_disability;
		final String[] 	HELP_ITEMS 	= configureItems();
		final ArrayAdapter<String> adapter 	= new ArrayAdapter<String>(this, adapterLayout, HELP_ITEMS);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> adapter, View view, 
					int position, long id) {
				vibrator.vibrate(500);
				final String selection = list.getItemAtPosition(position).toString();
				tts.speech(selection);
			}
		});
	}
	
	private String[] configureItems() {
		return new String[]{
				(String) getResources().getText(R.string.help_message_title),
				(String) getResources().getText(R.string.help_message_body),
				(String) getResources().getText(R.string.help_message_footer)
		};
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override 
	protected void onDestroy() {
		if (tts != null){
			tts.stop();
		}
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		if (tts != null){
			tts.stop();
		}
		this.finish();
		super.onBackPressed();
	}
}