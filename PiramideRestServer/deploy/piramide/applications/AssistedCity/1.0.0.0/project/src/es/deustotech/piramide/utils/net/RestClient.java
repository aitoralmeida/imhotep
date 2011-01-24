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

package es.deustotech.piramide.utils.net;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.util.Log;
import es.deustotech.piramide.utils.constants.Constants;

public class RestClient {
	public static HttpEntity connect(String url, HttpEntity httpEntity) {
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpParams params = new BasicHttpParams();
		params.setParameter("http.protocol.content-charset", "UTF-8");
		httpClient.setParams(params);
		final HttpGet httpGet 		= new HttpGet(url); //request object
		HttpResponse response		= null;

		try {
			response = httpClient.execute(httpGet);
		} catch (ClientProtocolException cpe) {
			Log.d(Constants.TAG, cpe.getMessage());
		} catch (IOException ioe) {
			Log.d(Constants.TAG, ioe.getMessage());
		}
		return httpEntity = response.getEntity();
	}
}