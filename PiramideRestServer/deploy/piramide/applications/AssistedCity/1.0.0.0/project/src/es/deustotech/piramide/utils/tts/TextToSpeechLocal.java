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

package es.deustotech.piramide.utils.tts;

import java.util.Locale;

import es.deustotech.piramide.utils.constants.Constants;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

/**
 * This class uses the default Android TextToSpeech API 
 */
public class TextToSpeechLocal implements TextToSpeech.OnInitListener{

	private TextToSpeech tts;
	
	public TextToSpeechLocal(Context applicationContext) {
		 tts = new TextToSpeech(applicationContext,
		            this  // TextToSpeech.OnInitListener
		            );
	}

	@Override
	public void onInit(int status) {
		 // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // Note that a language may not be available, and the result will indicate this.
            int result = tts.setLanguage(Locale.US);
            // Try this someday for some interesting results.
            // int result tts.setLanguage(Locale.FRANCE);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
               // Lanuage data is missing or the language is not supported.
                Log.e(Constants.TAG, "Language is not available.");
            } else {
                // Check the documentation for other possible result codes.
                // For example, the language may be available for the locale,
                // but not for the specified country and variant.

                // The TTS engine has been successfully initialized.
                // Allow the user to press the button for the app to speak again.
//                button.setEnabled(true);
                // Greet the user.
            }
        } else {
            // Initialization failed.
            Log.e(Constants.TAG, "Could not initialize TextToSpeech.");
        }
	}
	
	public void speak(String message){
		tts.speak(message,
	            TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
	            null);
	}

}