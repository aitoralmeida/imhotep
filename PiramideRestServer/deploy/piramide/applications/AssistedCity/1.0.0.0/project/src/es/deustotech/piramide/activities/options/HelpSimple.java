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
import es.deustotech.piramide.R;
import es.deustotech.piramide.utils.constants.Constants;

public class HelpSimple extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_simple);
        
        Log.d(Constants.TAG, "Launching HelpSimple...");
        
        setResult(Constants.SUCCESS_RETURN_CODE, new Intent());
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override 
	protected void onDestroy() {
		super.onDestroy();
	}
}
