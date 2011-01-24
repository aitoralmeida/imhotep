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
 *         Pablo Orduña <pablo.orduna@deusto.es>
 *
 */

package es.deustotech.piramide.services;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import es.deustotech.piramide.activities.location.Categories;
import es.deustotech.piramide.activities.location.Directions;
import es.deustotech.piramide.utils.constants.Constants;

public class LocationService extends Service {

	public static Categories CATEGORIES_ACTIVITY;
	public static Directions DIRECTIONS_ACTIVITY;
	private static volatile String currentLocation;
	private static volatile double currentLatitude = Constants.DEFAULT_LATITUDE;
	private static volatile double currentLongitude = Constants.DEFAULT_LONGITUDE;
	private static volatile float currentAccuracy;
	private static boolean isEmulator;
	private static boolean MOCK = true;
	private static volatile double lastLatitude = Constants.DEFAULT_LATITUDE;
	private static volatile double lastLongitude = Constants.DEFAULT_LONGITUDE;
//	private static volatile double lastLatitude = Constants.MADRID_LATITUDE;
//	private static volatile double lastLongitude = Constants.MADRID_LONGITUDE;

	@Override 
	public void onCreate() {
		super.onCreate();
		setupEmulator();
		if (CATEGORIES_ACTIVITY != null){
			Log.i(getClass().getSimpleName(), "--------------------" + Constants.LOCATION_SERVICE_STARTED);
		}
		Log.i(Constants.TAG, "--------------------Service created");
	}
	
	//For handling subactivities
	public static void setMainActivity(Activity activity) {
		//TODO: chequear esto. Parece que el activity.getTitle() devuelve AssistedCity
		if (activity.getTitle().equals("Categories")){
			CATEGORIES_ACTIVITY = (Categories) activity;
		}
		else if (activity.getTitle().equals("Directions")){
			DIRECTIONS_ACTIVITY = (Directions) activity;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(Constants.TAG, "--------------------LocationService running...");
		final String context 			= Context.LOCATION_SERVICE;
		LocationManager locationManager = (LocationManager) getSystemService(context);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		
		final String provider 	= locationManager.getBestProvider(criteria, true);
		final Location location = locationManager.getLastKnownLocation(provider);
		
		updateWithNewLocation(location);

		locationManager.requestLocationUpdates(provider, 10000, 10,
				locationListener);
		Log.i(getClass().getSimpleName(), "--------------------" + Constants.LOCATION_SERVICE_STARTED);
		
		return Constants.START_NOT_STICK;
	}

	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			Log.i(Constants.TAG, "--------------------Location changed");
			updateWithNewLocation(location);
		}

		public void onProviderDisabled(String provider) {
			Log.i(Constants.TAG, "--------------------Provider disabled");
			updateWithNewLocation(null);
		}

		public void onProviderEnabled(String provider) {
			Log.i(Constants.TAG, "--------------------Provider enabled");
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.i(Constants.TAG, "--------------------Status changed");
		}
	};

	private void updateWithNewLocation(Location location) {
		String addressString = "No address found";
		Log.i(Constants.TAG, "-----------------------Updating Location: " + currentLocation);
		
		if (location != null) {
//			double latitude 		= location.getLatitude();
//			double longitude 		= location.getLongitude();
//			double latitude 		= Constants.MADRID_LATITUDE;
//			double longitude 		= Constants.MADRID_LONGITUDE;
			double latitude 		= Constants.DEFAULT_LATITUDE;
			double longitude 		= Constants.DEFAULT_LONGITUDE;
			currentLatitude         = latitude;
			currentLongitude        = longitude;
			currentAccuracy            = location.getAccuracy();
			currentLocation			= "Lat:" + latitude + Constants.NEXT_LINE + "Long:" + longitude;
			final Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
			
			try {
				final List<Address> addresses = geoCoder.getFromLocation(latitude,
						longitude, 1);
				StringBuilder stringBuilder = new StringBuilder();
				if (addresses.size() > 0) {
					final Address address = addresses.get(0);
					for (int i = 0; i < address.getMaxAddressLineIndex(); i++){
						stringBuilder.append(address.getAddressLine(i)).append(Constants.NEXT_LINE);
					}

					stringBuilder.append(address.getLocality()).append(Constants.NEXT_LINE);
					stringBuilder.append(address.getPostalCode()).append(Constants.NEXT_LINE);
					stringBuilder.append(address.getCountryName());
				}
				addressString = stringBuilder.toString();
			} catch (IOException ioe) {
				Log.e(Constants.TAG, ioe.getMessage());
			}
		} else {
			currentLocation = "No location found";
		}
		
		final Context context = this.getApplicationContext();
//		currentLocation = "Coordenadas actuales:" + Constants.NEXT_LINE + currentLocation
//				+ Constants.NEXT_LINE + addressString;
		currentLocation = addressString;
		
		if (currentLocation.equals("No location found")) {
			Toast.makeText(context, "Unnable to determine your location", Constants.SHORT_DURATION).show();
			Log.i(Constants.TAG, "--------------------No location found...");
		} 
		Log.i(Constants.TAG, "--------------------Location Updated: " + currentLocation);
	}

	@Override 
	public void onDestroy() {
		this.stopSelf();
		if (CATEGORIES_ACTIVITY != null) {
			Log.i(getClass().getSimpleName(), "--------------------" + Constants.LOCATION_SERVICE_STOPPED);
		}
		super.onDestroy();
	}
	
	public static String getLocation(){
		return currentLocation;
	}
	
	private void setupEmulator(){
		if(MOCK){
			final TelephonyManager telmgr 	= (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
			isEmulator 						= "000000000000000".equals(telmgr.getDeviceId());
		}else{
			isEmulator = false;
		}
	}
	
	public static void setLatitude(double latitude){
		lastLatitude = latitude;
	}
	
	public static void setLongitude(double longitude){
		lastLongitude = longitude;
	}
	
	public static double getCurrentLatitude() {
		if(isEmulator){
			return lastLatitude;			
		}
		return currentLatitude;
	}

	public static float getCurrentAccuracy() {
		return currentAccuracy;
	}

	public static double getCurrentLongitude() {
		if(isEmulator){
			return lastLongitude;			
		}
		return currentLongitude;
	}
}