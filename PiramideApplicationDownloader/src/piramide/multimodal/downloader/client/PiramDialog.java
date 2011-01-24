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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class PiramDialog {
	
	private final Context context;
	
	public PiramDialog(Context context){
		this.context = context;
	}
	
	public void show(String message) {
		final AlertDialog dialog = new AlertDialog.Builder(this.context).create();
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
}
