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
package piramide.multimodal.downloader.client;

import piramide.multimodal.downloader.client.activities.ApplicationsActivity;
import piramide.multimodal.downloader.client.activities.PiramidePreferenceActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ApplicationDownloaderActivity extends Activity {
	
	private static String [] FIELDS = new String[]{ "Applications", "Settings" };
	private static Class<?> [] HANDLERS = new Class<?> []{ ApplicationsActivity.class, PiramidePreferenceActivity.class };
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final ListView list = (ListView)findViewById(R.id.front_list_view);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, FIELDS);
        list.setAdapter(adapter);
        
        list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		        final Intent intent = new Intent(ApplicationDownloaderActivity.this, HANDLERS[position]);
				startActivityForResult(intent, 0);
			}
        });
    }    
}