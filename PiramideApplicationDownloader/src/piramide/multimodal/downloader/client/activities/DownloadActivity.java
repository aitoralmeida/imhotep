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
package piramide.multimodal.downloader.client.activities;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import piramide.multimodal.downloader.client.DownloaderException;
import piramide.multimodal.downloader.client.PiramDialog;
import piramide.multimodal.downloader.client.Profiles;
import piramide.multimodal.downloader.client.R;
import piramide.multimodal.downloader.client.RestClient;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

public class DownloadActivity extends Activity {
	
	private final static Executor executor = Executors.newSingleThreadExecutor();
	private volatile String reason;
	private final Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			new PiramDialog(DownloadActivity.this).show(DownloadActivity.this.reason);
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.download);
    	
    	final Intent intent = getIntent();
        final String application = intent.getStringExtra("application");
        final String version     = intent.getStringExtra("version");
        
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String profilePrefName = getString(R.string.profile_pref);
        final String regularUser = getString(R.string.regular_user);
        final String userProfileName = preferences.getString(profilePrefName, regularUser);
        
        final Profiles profiles = new Profiles(this);
        final Map<String, Object> userVariables = profiles.getProfile(userProfileName);
        
        executor.execute(new Runnable() {
			@Override
			public void run() {
		        final RestClient client = new RestClient(DownloadActivity.this);
		        try {
					client.downloadAndInstallApplication(application, version, userVariables);
				} catch (DownloaderException e) {
					e.printStackTrace();
					DownloadActivity.this.reason = "Error downloading or installing application: " + e.getMessage();
					DownloadActivity.this.handler.sendMessage(DownloadActivity.this.handler.obtainMessage());
					return;
				}
			}
		});
    }
}
