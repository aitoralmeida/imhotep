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
package piramide.multimodal.client.tester;

import java.util.HashMap;
import java.util.Map;

import piramide.multimodal.client.tester.capabilityTests.AbstractTester;
import piramide.multimodal.client.tester.capabilityTests.FontColourTester;
import piramide.multimodal.client.tester.capabilityTests.FontSizeTester;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TesterActivity extends Activity {
	
    private final Class<?> [] TESTERS = {
    		FontSizeTester.class,
    		FontColourTester.class
    };
    
    private Map<String, Map<String, String>> values = new HashMap<String, Map<String, String>>();
    
    private Button startButton;
    private final ProfileManager profile = new ProfileManager(this);
    private final ApplicationDownloader downloader = new ApplicationDownloader(this);
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.startButton = (Button)findViewById(R.id.start_tests);
        
    }
    
    @Override
    public void onStart(){
    	super.onStart();
        if(this.profile.exists()){
        	this.startButton.setText("Update profile");
        	
        	Map<String, Map<String, String>> loadedValues;
			try {
				loadedValues = this.profile.loadProfile();
				this.values = loadedValues;
			} catch (ProfileManagementException e) {
				e.printStackTrace();
				showDialog("Error loading profile: " + e.getMessage()); 
			}
        }else{
        	this.startButton.setText("Create profile");
        }
    }
    

    
    public void startButtonHandler(View obj){
    	
    	//TestConfig p = new TestConfig();
        //p.put(getString(R.string.font_size), "11");

        //Bundle b = new Bundle();
        //b.putParcelable(getString(R.string.config_parcel), p);
        //Intent intent = new Intent(this, FontSizeTest.class);
        //intent.putExtras(b);
    	
    	//Intent intent = new Intent(this, ActivityDevolviendo.class);
    	//intent.putExtra("mibundle", "bundleliza, bundleliza, bundlelizaci�n");
    	//startActivityForResult(intent, 125);
    	
    	startStatusActivity(0);
    }
    
    public void downloadAndInstallButtonHandler(View obj){
    	try {
			this.downloader.download();
		} catch (DownloaderException e) {
			e.printStackTrace();
			showDialog("Error downloading application: " + e.getMessage());
			return;
		}
    	this.downloader.install();
    }

    public void consumeWebServiceButtonHandler(View obj){
    	WebService ws = new WebService();
    	// ws.getApplicationsList();
    	ws.getDeviceList();
    }
	
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	
    	final int processedRequestCode;
    	if(resultCode == AbstractTester.BACK_BUTTON){
    		if(requestCode == 0 || requestCode == 1)
    			return;
    		else
    			processedRequestCode = requestCode - 2;
    	}else
    		processedRequestCode = requestCode;
    		
    	// If requestCode is even, then the previous request was a Test
    	if(processedRequestCode % 2 == 0){
    		
	    	fillValues(data);
	    	
	    	if((processedRequestCode + 1) / 2 == this.TESTERS.length){
	    		try {
					this.profile.update(this.values);
				} catch (ProfileManagementException e) {
					showDialog("Couldn't create profile: " + e.getMessage());
					return;
				}
	    		showDialog("Profile created");
	    		return;
	    	}
    		
        	startStatusActivity(processedRequestCode);
        	
        // If requestCode is odd, then the previous request was a Status Activity
    	}else{ 

	     	startActivityForResult(new Intent(this, this.TESTERS[processedRequestCode/2]), processedRequestCode + 1);
    	}
    }
    
	void showDialog(String message) {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setMessage(message);
		dialog.setButton("Cancel", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
			
		});
		dialog.setCancelable(true);
		dialog.show();
	}

	private void startStatusActivity(int requestCode) {
		final Intent intent = new Intent(this, StatusActivity.class);
		
		final StringBuffer text = new StringBuffer();
		if(requestCode > 1){
			text.append("Test for ");
			text.append(this.TESTERS[requestCode / 2 - 1].getSimpleName());
			text.append(" ended. ");
		}
		
		text.append("Now launching test ");
		text.append(this.TESTERS[requestCode / 2].getSimpleName());
		
		intent.putExtra("message", text.toString());
		
		startActivityForResult(intent, requestCode + 1);
	}
    
	private void fillValues(Intent data) {
		if(data == null) // If 
			return;
		
		final String category = data.getStringExtra(AbstractTester.CATEGORY);
		final String name     = data.getStringExtra(AbstractTester.NAME);
		final String value    = data.getStringExtra(AbstractTester.VALUE);
		
		if(!this.values.containsKey(category))
			this.values.put(category, new HashMap<String, String>());

		
		this.values.get(category).put(name, value);
	}
}