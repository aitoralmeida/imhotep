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
 * Author: Pablo Orduña <pablo.orduna@deusto.es>
 *
 */

package es.deustotech.piramide.utils.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import es.deustotech.piramide.utils.constants.Constants;
import es.deustotech.piramide.utils.parcelable.Point;

public class GoogleLocalClient {
	
	private final String selection;
	private final double latitude;
	private final double longitude;
	
	private static final int MAX_PAGES_TO_PARSE = 8;
	
	public static final String[] URI						= new String[]{
		"http://ajax.googleapis.com/ajax/services/search/local?v=1.0&q=category:+'",
		"'&sll=", 
		",", 
		"&rsz=large&start=0"
	};

	public static class GoogleLocalException extends Exception{
		private static final long serialVersionUID = -3750604856982935160L;

		public GoogleLocalException() {
			super();
		}

		public GoogleLocalException(String detailMessage, Throwable throwable) {
			super(detailMessage, throwable);
		}

		public GoogleLocalException(String detailMessage) {
			super(detailMessage);
		}

		public GoogleLocalException(Throwable throwable) {
			super(throwable);
		}
	}
	
	public GoogleLocalClient(String selection, double latitude, double longitude){
		if (selection.equalsIgnoreCase(Constants.GAS_STATION)){
			this.selection = Constants.GAS_STATION_FORMATTED;
		} else {
			this.selection = selection;
		}
		this.latitude  = latitude;
		this.longitude = longitude;
	}
	
	public Vector<Point> getPoints() throws GoogleLocalException{
		final Vector<Point> points = new Vector<Point>();
		int position = 0;
		String start = "0";
		JSONArray pages;
		do{
			try {
				final URI baseURI = new URI(URI[0] + selection + URI[1] + String.valueOf(latitude) + URI[2] + String.valueOf(longitude) + URI[3] + start);
				Log.w("GoogleLocalClient", baseURI.toURL().toString());
				final HttpEntity entity = RestClient.connect(baseURI.toURL().toString(), null);
				final String content = convertStreamToString(entity.getContent());
				final JSONObject jsonObj 	  = new JSONObject(content);
				final JSONObject responseData = jsonObj.getJSONObject("responseData");
				final JSONObject cursor       = responseData.getJSONObject("cursor");
				if(!cursor.has("pages"))
					break; // No pages, no results
				
				pages         = cursor.getJSONArray("pages");
				position++;
				if(position < pages.length()){
					final JSONObject nextPage = pages.getJSONObject(position);
					start = nextPage.getString("start");
				}
				
				final JSONArray results = responseData.getJSONArray("results");
				for(int i = 0; i < results.length(); ++i){
					final JSONObject result = results.getJSONObject(i);
					final Point point 			= new Point(result.getString(Constants.JSON_TITLE), 
							result.getString(Constants.JSON_STREET), result.getString(Constants.JSON_LAT), 
							result.getString(Constants.JSON_LNG));
					points.add(point);
				}
				
			} catch (Exception e) {
				throw new GoogleLocalException(e.getMessage(), e);
			}
		}while(position < pages.length() && position < MAX_PAGES_TO_PARSE);
		
		Log.w("GoogleLocalClient", Integer.valueOf(points.size()).toString());
		
		return points;
	}
	
	private static String convertStreamToString(InputStream inStream) {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
		StringBuilder sb 			= new StringBuilder();
		String line 				= null;
		
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + Constants.NEXT_LINE); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}

