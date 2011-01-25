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
package piramide.multimodal.client.tester.capabilityTests;

import piramide.multimodal.client.tester.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FontSizeTester extends AbstractTester {
	
	public static final String CATEGORY_VALUE = "visual";
	public static final String NAME_VALUE     = "fontSize";
	
	public static final int INITIAL_SIZE = 20;
	public static final int STEP = 2;
	
	
	private int minimumVisible = FontSizeTester.INITIAL_SIZE;	
	
	private TextView testText;
	private Button buttonYes;
	private Button buttonNo;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.font_size_test);
        
        //TestConfig configuration = getConfiguration();         
        //String fontSize = configuration.get(getString(R.string.font_size));
        
        this.buttonYes = (Button)findViewById(R.id.buttonYes);
        this.buttonYes.setTextSize(FontSizeTester.INITIAL_SIZE);        
        
        this.buttonNo = (Button)findViewById(R.id.buttonNo);
        this.buttonNo.setTextSize(FontSizeTester.INITIAL_SIZE);
        
        this.testText = (TextView)findViewById(R.id.testText);
        this.testText.setTextSize(FontSizeTester.INITIAL_SIZE);        
        this.testText.setText(getString(R.string.font_test));      
    }
    
    
    public void onClickYes(View v)
    {
    	this.minimumVisible -= FontSizeTester.STEP;
    	if(this.minimumVisible == 0){
    		returnValue();
    	}else
    		this.testText.setTextSize(this.minimumVisible); 
    }
    
    
    public void onClickNo(View v)
    {
    	 returnValue();    	
    }
    
	private void returnValue() {
		final Intent intent = new Intent();
		final String result = (this.minimumVisible == FontSizeTester.INITIAL_SIZE)?getString(R.string.blind):Integer.toString(this.minimumVisible + STEP);
		final String value = "<" + NAME_VALUE +">" + result + "</" + FontSizeTester.NAME_VALUE +">";
		
		intent.putExtra(AbstractTester.CATEGORY, FontSizeTester.CATEGORY_VALUE);
		intent.putExtra(AbstractTester.NAME,     FontSizeTester.NAME_VALUE);
		intent.putExtra(AbstractTester.VALUE,    value);
		
		setResult(RESULT_OK, intent);
		this.finish();
	}
    
/*
	private TestConfig getConfiguration() {
		Bundle bundle = getIntent().getExtras();        
        TestConfig configuration = bundle.getParcelable(getString(R.string.config_parcel));
		return configuration;
	}
*/
}
