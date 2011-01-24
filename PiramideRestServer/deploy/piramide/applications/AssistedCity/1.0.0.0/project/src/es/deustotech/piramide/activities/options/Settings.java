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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import es.deustotech.piramide.R;
import es.deustotech.piramide.utils.constants.Constants;

public class Settings extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        Log.d(Constants.TAG, "Launching Settings...");
        
        createMenu();
        setResult(Constants.SUCCESS_RETURN_CODE, new Intent());
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void createMenu() {
		ListView list = (ListView)findViewById(R.id.list_view); 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, Constants.SETTINGS_ITEMS); 
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> adapter, View view, 
					int position, long id) {
				
				if (position == 0){
					Toast.makeText(getApplicationContext(), Constants.FLASH_MODE_MESSAGE, Constants.SHORT_DURATION).show();
				} else {
					Toast.makeText(getApplicationContext(), "Not implemented yet!", Constants.SHORT_DURATION).show();
				}
			}
			
		});
	}
	
	@Override 
	protected void onDestroy() {
		super.onDestroy();
	}
}
