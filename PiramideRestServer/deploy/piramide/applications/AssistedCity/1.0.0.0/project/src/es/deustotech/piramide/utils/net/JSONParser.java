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
 * 		   Pablo Orduña <pablo.orduna@deusto.es>
 *
 */

package es.deustotech.piramide.utils.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import es.deustotech.piramide.utils.constants.Constants;
import es.deustotech.piramide.utils.parcelable.Point;

import android.util.Log;

public class JSONParser {
	
	private static String result;
	private static Vector<Point> points;
	
	public static Vector<Point> parseFromUrl(HttpEntity httpEntity) {
		if (httpEntity != null) {
			InputStream inStream;
			try {
				inStream 					= httpEntity.getContent();
				result 						= convertStreamToString(inStream);
				points 						= new Vector<Point>();
				final JSONObject jsonObj 	= new JSONObject(result);
				
				final JSONObject result 	= jsonObj.getJSONObject(Constants.JSON_RESPONSE_DATA);
				
				//show 8 results
				for (int i=0; i<8; i++){
					final JSONObject resultAux 	= (JSONObject) result.getJSONArray(Constants.JSON_RESULTS).get(i);
//					final JSONObject phoneAux 	= (JSONObject) resultAux.getJSONArray(Constants.JSON_PHONE_NUMBERS).get(0);
					
					final Point point 			= new Point(resultAux.getString(Constants.JSON_TITLE), 
							resultAux.getString(Constants.JSON_STREET), resultAux.getString(Constants.JSON_LAT), 
							resultAux.getString(Constants.JSON_LNG)
							
							/*new PhoneNumbers(phoneAux.getString(Constants.JSON_TYPE), 
							phoneAux.getString(Constants.JSON_NUMBER)), 
							resultAux.getString(Constants.JSON_CITY), 
							resultAux.getString(Constants.JSON_TITLE), 
							resultAux.getString(Constants.JSON_REGION), 
							resultAux.getString(Constants.JSON_TITLE_NO_FORMATTING), 
							resultAux.getString(Constants.JSON_STREET), 
							resultAux.getJSONArray(Constants.JSON_ADDRESS_LINES).getString(0), 
							resultAux.getString(Constants.JSON_COUNTRY), 
							resultAux.getString(Constants.JSON_LAT), 
							resultAux.getString(Constants.JSON_LNG),
							resultAux.getString(Constants.JSON_TO_HERE), 
							resultAux.getString(Constants.JSON_FROM_HERE)*/);
					
					points.add(point);
				}
				
								
//				final JSONObject phoneAux 	= (JSONObject) result.getJSONArray(Constants.JSON_PHONE_NUMBERS).get(0);
				
				
				
				/*
				Log.i(Constants.TAG, "<jsonobject>\n" + jsonObj.toString() + "\n</jsonobject>");

				final JSONArray nameArray 	= jsonObj.names();
				final JSONArray valArray 	= jsonObj.toJSONArray(nameArray);

				for(int i=0; i<valArray.length(); i++) {
					Log.e(Constants.TAG, "<jsonname" + i + ">\n" + nameArray.getString(i) + "\n</jsonname" + i + ">\n" 
							+ "<jsonvalue" + i + ">\n" + valArray.getString(i) + "\n</jsonvalue" + i + ">");
				}
				jsonObj.put(Constants.SAMPLE_KEY, Constants.SAMPLE_VALUE);
				
				Log.i(Constants.TAG, "<jsonobject>\n" + jsonObj.toString() + "\n</jsonobject>");
				 */
				
				inStream.close();
			} catch (IllegalStateException ise) {
				Log.d(Constants.TAG, ise.getMessage());
			} catch (IOException ioe) {
				Log.d(Constants.TAG, ioe.getMessage());
			} catch (JSONException je) {
				Log.d(Constants.TAG, je.getMessage());
			}
		} // If the response does not enclose an entity, there is no need
		// to worry about connection release
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
