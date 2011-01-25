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

import java.util.List;
import java.util.Vector;

import piramide.multimodal.client.tester.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FontColourTester extends AbstractTester {
	
	public static final String CATEGORY_VALUE = "visual";
	public static final String NAME_VALUE     = "colours";
	public static final String COMBINATION    = "combination";
	public static final String BACKGROUND     = "background";
	public static final String FOREGROUND     = "foreground";
	
	private final int [] COLOURS = {
			0xFFFFFF, // white
			0x000000, // black
			0xFF0000, // red 
			0x00FF00, // green
		//	0x0000FF, // blue
		//	0xFFCD00, // yellow
		//	0xFF7F00, // orange
		//	0x964B00, // brown
		//	0xEE82EE, // violet web
		//	0x8F00FF, // violet spectral
	};

	private TextView testText;
	private Button buttonYes;
	private Button buttonNo;
	
	private final List<ColourCombination> visibleCombinations = new Vector<ColourCombination>();
	
	private int currentBackground;
	private int currentForeground;
	private int position = -1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.visibleCombinations.clear();
        
        setContentView(R.layout.font_colour_test);

        this.buttonYes = (Button)findViewById(R.id.buttonYes);
        this.buttonYes.setTextSize(FontSizeTester.INITIAL_SIZE);        
        
        this.buttonNo = (Button)findViewById(R.id.buttonNo);
        this.buttonNo.setTextSize(FontSizeTester.INITIAL_SIZE);
        
        this.testText = (TextView)findViewById(R.id.font_colour_test_text);
        this.testText.setTextSize(FontSizeTester.INITIAL_SIZE);        
        this.testText.setText(getString(R.string.font_test));
        
        setNextColour();
    }
	
	private int getForegroundColour(){
		return this.COLOURS[this.position / this.COLOURS.length];
	}
	
	private int getBackgroundColour(){
		return this.COLOURS[this.position % this.COLOURS.length];
	}
	
	private int getLength(){
		return this.COLOURS.length * this.COLOURS.length; 
	}
	
	private boolean next(){
		this.position++;
		if(this.getForegroundColour() == this.getBackgroundColour())
			this.position++;
		if(this.position >= this.getLength())
			return false;
		return true;
	}
	
	public void onClickYes(View v)
    {
		this.visibleCombinations.add(new ColourCombination(this.currentBackground, this.currentForeground));
		setNextColour();
    }
	
	private void setNextColour(){
		if(next()){
			this.currentBackground = getBackgroundColour();
			this.currentForeground = getForegroundColour();
		
			this.testText.setBackgroundColor(0xFF000000 | this.currentBackground);
			this.testText.setTextColor(0xFF000000 | this.currentForeground);
		}else{
			returnValue();
		}
	}
	    
	    
    public void onClickNo(View v)
    {
		setNextColour();
    }
	    
    private void returnValue() {
    	
		final Intent intent = new Intent();
		final StringBuffer buf = new StringBuffer("<");
		buf.append(NAME_VALUE);
		buf.append(">");
		
		
    	for (ColourCombination combination : this.visibleCombinations){
    		buf.append("<");
    		buf.append(COMBINATION);
    		buf.append(">");
    		
    		buf.append("<");
    		buf.append(BACKGROUND);
    		buf.append(">#");
    		
    		final String background = Integer.toHexString(combination.getBackgroundColour());
    		
    		for(int i = 0; i < (6 - background.length()); ++i)
    			buf.append("0");
    		buf.append(background);
    		
    		buf.append("</");
    		buf.append(BACKGROUND);
    		buf.append(">");
    		
    		final String foreground = Integer.toHexString(combination.getForegroundColour());
    		
    		buf.append("<");
    		buf.append(FOREGROUND);
    		buf.append(">#");
    		
    		for(int i = 0; i < (6 - foreground.length()); ++i)
    			buf.append("0");

    		buf.append(foreground);
    		buf.append("</");
    		buf.append(FOREGROUND);
    		buf.append(">");
    		
    		buf.append("</");
    		buf.append(COMBINATION);
    		buf.append(">");
    	}
		buf.append("</");
		buf.append(NAME_VALUE);
		buf.append(">");
    	
		intent.putExtra(AbstractTester.CATEGORY, FontColourTester.CATEGORY_VALUE);
		intent.putExtra(AbstractTester.NAME,     FontColourTester.NAME_VALUE);
		intent.putExtra(AbstractTester.VALUE,    buf.toString());
		
		setResult(RESULT_OK, intent);
		this.finish();
	}

}
