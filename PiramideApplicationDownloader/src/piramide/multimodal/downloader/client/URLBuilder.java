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

import java.net.URLEncoder;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class URLBuilder {
	
	private final Context context;
	
	URLBuilder(Context context){
		this.context = context;
	}
	
	private String getBaseURL(){
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
		final String baseUrlName    = this.context.getString(R.string.baseurl_pref_name);
		final String baseUrlDefault = this.context.getString(R.string.baseurl_pref_default);
		return preferences.getString(baseUrlName, baseUrlDefault);
	}
	
	String buildApplicationsURL(){
		final StringBuilder builder = new StringBuilder(getBaseURL());
		builder.append("applications/");
		return builder.toString();
	}
	
	String buildVersionsURL(String application){
		final StringBuilder builder = new StringBuilder(getBaseURL());
		builder.append("applications/");
		builder.append(application);
		builder.append("/versions/");
		return builder.toString();
	}
	
	String buildApplicationURL(String application, String version, Map<String, Object> userVariables) {
		// http://localhost:8080/PiramideRestServer/applications/PiramideSimpleSample/versions/1.0.0.0/devices/HTC+Desire/app.apk?piramide.user.name=hola&piramide.user.size=100
		final StringBuilder builder = new StringBuilder(getBaseURL());
		builder.append("applications/");
		builder.append(application);
		builder.append("/versions/");
		builder.append(version);
		builder.append("/devices/HTC+Desire/app.apk?");
		for(String key : userVariables.keySet()){
			builder.append(key);
			builder.append("=");
			final String value = userVariables.get(key).toString();
			final String encodedValue = URLEncoder.encode(value);
			builder.append(encodedValue);
			builder.append("&");
		}
		final String applicationURL = builder.toString();
		return applicationURL;
	}
}
