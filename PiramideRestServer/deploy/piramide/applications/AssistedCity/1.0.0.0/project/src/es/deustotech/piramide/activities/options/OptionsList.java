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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import es.deustotech.piramide.R;
import es.deustotech.piramide.activities.location.Categories;
import es.deustotech.piramide.utils.constants.Constants;

public class OptionsList extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_menu);
        
        Log.d(Constants.TAG, "Launching OptionsList...");
        
        createMenu();
        setResult(Constants.SUCCESS_RETURN_CODE, new Intent());
//        finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void createMenu() {
		ListView list = (ListView)findViewById(R.id.list_view); 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, Constants.LIST_MENU_ITEMS); 
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> adapter, View view, 
					int position, long id) {
				
				if (position == 0){
					startActivityForResult(new Intent(OptionsList.this, 
							Categories.class), 0);
				} else {
					Toast.makeText(getApplicationContext(), "Not implemented yet!", Constants.SHORT_DURATION).show();
				}
			}
			
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.options_menu_simple, menu);
	    
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
		    case R.id.exit:
		        exit();
		        return true;
		    case R.id.help:
		        showHelpSimpleActivity();
		        return true;
		    case R.id.settings:
		       showSettingsSimpleActivity();
		        return true;
		    default:
		    	return super.onOptionsItemSelected(item);
	    }
	}
	
	private void showSettingsSimpleActivity() {
		startActivityForResult(new Intent(OptionsList.this, 
				SettingsSimple.class), 0);
	}

	private void showHelpSimpleActivity() {
		startActivityForResult(new Intent(OptionsList.this, 
				HelpSimple.class), 0);
	}

	private void exit() {
		//exit and close the application
//		stopService(AssistedCity.getLocationService());
		this.finish();
	}
	
	@Override 
	protected void onDestroy() {
		super.onDestroy();
	}
}