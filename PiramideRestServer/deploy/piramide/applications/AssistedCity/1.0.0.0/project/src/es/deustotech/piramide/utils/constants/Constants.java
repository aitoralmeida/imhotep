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

package es.deustotech.piramide.utils.constants;

import android.widget.Toast;

public class Constants {
	public static final int DELAY_TIME 						= 4000;
	public static final int DELAY_TIME_UPDATE_DISTANCE		= 5000;
	public static final int SUCCESS_RETURN_CODE 			= 1;
	public static final int	SHORT_DURATION 					= Toast.LENGTH_SHORT;
	//Service constants
	public static long UPDATE_INTERVAL 						= 10000;  //10 seconds to retrieve new location
    public static long DELAY_INTERVAL 						= 0;
    public static final String LOCATION_SERVICE_STARTED 	= "LocationService started";
    public static final String LOCATION_SERVICE_STOPPED 	= "LocationService stopped";
    public static final String LOCATION_SERVICE_IEXCEPTION 	= "LocationService InterruptedException";
    public static final int START_NOT_STICK 				= 1;
    //Other constants
    public static final String TAG 							= "AssistedCity";
    public static final String NEXT_LINE 					= "\n";
    public static final String NORTH						= "N";
    public static final String TIMER_STOPPED 				= "Timer stopped...";
    public static final String FLASH_MODE_MESSAGE 			= "Camera flash turned ";
    public static final String SAMPLE_VALUE 				= "sample value";
	public static final String SAMPLE_KEY 					= "sample key";
	public static final String JSON_ADDRESS_LINES 			= "addressLines";
	public static final String JSON_NUMBER 					= "number";
	public static final String JSON_TYPE 					= "type";
	public static final String JSON_PHONE_NUMBERS 			= "phoneNumbers";
	public static final String JSON_TITLE_NO_FORMATTING 	= "titleNoFormatting";
	public static final String JSON_TITLE 					= "title";
	public static final String JSON_REGION 					= "region";
	public static final String JSON_LNG 					= "lng";
	public static final String JSON_LAT 					= "lat";
	public static final String JSON_COUNTRY 				= "country";
	public static final String JSON_CITY 					= "city";
	public static final String JSON_RESULTS 				= "results";
	public static final String JSON_RESPONSE_DATA 			= "responseData";
	public static final String JSON_STREET 					= "streetAddress";
	public static final String JSON_FROM_HERE 				= "ddUrlFromHere";
	public static final String JSON_TO_HERE 				= "ddUrlToHere";
	public static final String GAS_STATION_FORMATTED 		= "Gas%20Stations";
	public static final String GAS_STATION 					= "Gas Stations";
	public static final String LONGITUDE 					= "Longitude";
	public static final String LATITUDE 					= "Latitude";
	public static final String STREET 						= "Street";
	public static final String TITLE 						= "Title";
	public static final String POINT 						= "Point";
	public static final String SELECTED 					= "Ha seleccionado, ";
	public static final String CURRENT_DISTANCE 			= "Current distance: ";
	//Deusto coordinates
	public static final double DEFAULT_LATITUDE 			= 43.270137;
	public static final double DEFAULT_LONGITUDE 			= -2.9410225;
	//Madrid coordinates
	public static final double MADRID_LONGITUDE				= -3.678702;
	public static final double MADRID_LATITUDE				= 40.467349;
	//Names for the units to use
	public static final int KILOMETERS 						= 0;
	public static final int METERS 							= 3;
	public static final String MP3_FILE 					= "voice.mp3";
	public static final String DEFAULT_LANGUAGE 			= "es";
	// TODO: change this URL
	public static final String URL = "http://translate.google.com/translate_tts?tl=";
	public static final String[] URI						= new String[]{
		"http://ajax.googleapis.com/ajax/services/search/local?v=1.0&q=category:+'",
		"'&sll=", 
		",", 
		"&rsz=large&start=1"
	};
    public static final String[] CATEGORIES 				= new String[]{
			"Bares", "Restaurantes", "Cafes",
			"Hoteles", "Museos", "Gasolineras", 
			"Taxis", "Aeropuertos"};
    public static final String[] LIST_MENU_ITEMS 			= new String[]{
			"No Sight Problems", "Sight Problems"};
    public static final String[] SETTINGS_ITEMS				= new String[]{
    		"Camera flash"
    };
    public static final String[] COORDINATES	 			= new String[]{
			"Latitude: ", "Longitude: "};
    public static final CharSequence SCAN_MESSAGE 			= "Scanning enviroment, " +
			"please wait...";
    public static final String HELP_MESSAGE_1 			= "Este programa ha sido diseniado para ayudarle a encontrar " +
	"los puntos que le interesen.";
public static final String HELP_MESSAGE_2 			= "Simplemente vaya seleccionando las opciones deseadas en los " +
	"listados que se le presenten.";
public static final String HELP_MESSAGE_3 			= "Primero, seleccione el tipo de lugar al que desea ir, " +
	"ya sea un bar, un museo, etc.";
public static final String HELP_MESSAGE_4 			= "En segundo lugar, seleccione el lugar concreto " +
	"que le interesa, como el Museo de Bellas Artes. ";
public static final String HELP_MESSAGE_5 			= "Y para finalizar, deje que el programa le guie por su ciudad " +
	"hasta el punto seleccionado.";
	public static final double PI = 3.14;
	public static final int COMPASS_ANGLE_LANDSCAPE_CORRETION = 90;
	public static final double MAX_DISTANCE_BY_GMAPS = 1000.0; // 1 km. TODO: check if this is right with GMaps
}