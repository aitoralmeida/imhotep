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

package es.deustotech.piramide.utils.distancecalc;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

import android.location.Address;
import android.location.Location;

public class DistanceCalculator {
	
	private static double calculateArc(double pointALatitude, double pointALongitude, double pointBLatitude, double pointBLongitude){
		final GeodeticCalculator geoCalc = new GeodeticCalculator();
		final Ellipsoid reference = Ellipsoid.WGS84;
		
		final GlobalCoordinates coordinatesA = new GlobalCoordinates(pointALatitude, pointALongitude);
		final GlobalCoordinates coordinatesB = new GlobalCoordinates(pointBLatitude, pointBLongitude);
		
		final GeodeticCurve geoCurve = geoCalc.calculateGeodeticCurve(
		                             reference, 
		                             coordinatesA, 
		                             coordinatesB
		                  );
		
		return geoCurve.getEllipsoidalDistance();
	}
	
	public static double calculateDistance(Address pointA, Address pointB, int units) {
		return calculateArc(pointA.getLatitude(),
				pointA.getLongitude(),
				pointB.getLatitude(),
				pointB.getLongitude()) 
				;//* Constants.EARTHS_RADIUS[units]; 
	}

	public static double calculateDistance(Location pointA, Location pointB, int units) {
		return calculateArc(pointA.getLatitude(),
				pointA.getLongitude(),
				pointB.getLatitude(),
				pointB.getLongitude()) 
				;//* Constants.EARTHS_RADIUS[units]; 
	}
	
	public static double calculateDistance(double latitudeA, double longitudeA, double latitudeB, double longitudeB){
		return calculateArc(latitudeA, longitudeA, latitudeB, longitudeB);
	}
	
	/*
	 * 			PointA (latitudeA, longitudeA)
	 * 			   		/|
	 * 			   	   / |
	 * 	   distance   /  |
	 * 	     		 /   |
	 * 				/____|
	 * 		PointB (latitudeB, longitudeB)
	 */
	public static double calculatePointsAngle(double latitudeA, double longitudeA, double latitudeB, double longitudeB){
		final double distance 		= calculateDistance(latitudeA, longitudeA, latitudeB, longitudeB);
		final double triangleSide	= calculateDistance(latitudeB, longitudeB, latitudeB, longitudeA);
		final double senAlpha		= triangleSide / distance;
		
		return 90 + Math.toDegrees(Math.asin(senAlpha));
	}
}