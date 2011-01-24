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

package es.deustotech.piramide.activities;

import java.util.List;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.camera.CameraManager;

import es.deustotech.piramide.R;
import es.deustotech.piramide.activities.location.Points;
import es.deustotech.piramide.services.LocationService;
import es.deustotech.piramide.utils.constants.Constants;
import es.deustotech.piramide.utils.views.CompassView;
import es.deustotech.piramide.views.ViewfinderView;

public class PiramideCaptureActivity extends CaptureActivity implements SensorEventListener {
	
	private ViewfinderView viewfinderView;
	private static SensorManager sensorManager;
	private CompassView compassView;

	private GeodeticCurve geoCurve;
	private static volatile int distanceToPoint = 0;
	private TextView distanceTextView;
	
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		
		distanceTextView 	= (TextView) findViewById(R.id.distance_text_view);
		distanceTextView.setTextColor(Color.WHITE);
		
		viewfinderView 		= (ViewfinderView) findViewById(R.id.viewfinder_view);
		sensorManager  		= (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		compassView			= (CompassView) findViewById(R.id.compass_view);
		//		gridView			= (es.deustotech.samples.GridView) findViewById(R.id.grid_view);
		final List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_NORMAL, new Handler());

		generateCoordinates();
	}

	private void generateCoordinates() {
		final GeodeticCalculator geoCalc = new GeodeticCalculator();
		final Ellipsoid reference = Ellipsoid.WGS84;

		final GlobalCoordinates currentCoordinates = new GlobalCoordinates(LocationService.getCurrentLatitude(),
				LocationService.getCurrentLongitude());
		final GlobalCoordinates pointCoordinates = new GlobalCoordinates(Points.getSelectedAddress().getLatitude(),  
				Points.getSelectedAddress().getLongitude());

		this.geoCurve = geoCalc.calculateGeodeticCurve(
				reference, 
				currentCoordinates, 
				pointCoordinates);
	}
	
	ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	@Override
	public void onSensorChanged(SensorEvent event) { 

		compassView.updateDirection((float)event.values[0]);

		final float myAngleFromNorth = (event.values[0] + 90) % 360;
		final float y = event.values[1];
		final float z = event.values[2];
		final double distance = this.geoCurve.getEllipsoidalDistance();
		generateCoordinates();

		double targetAngleFromNorth = this.geoCurve.getAzimuth();
		final double targetAngleFromMe;

		if(targetAngleFromNorth >= myAngleFromNorth) {
			targetAngleFromMe = targetAngleFromNorth - myAngleFromNorth;
		} else {
			targetAngleFromMe = 360 + targetAngleFromNorth - myAngleFromNorth;
		}

		// VERTICAL <- Horizontal since we're in landscape mode
		final float VERTICAL_FOCUS_ANGLE = CameraManager.get().getHorizontalViewAngle();
		// HORIZONTAL <- Vertical since we're in landscape mode
		final float HORIZONTAL_FOCUS_ANGLE = CameraManager.get().getVerticalViewAngle();

		// Left - right operations
		final double hRange = ((targetAngleFromMe + 360 + HORIZONTAL_FOCUS_ANGLE / 2) % 360) / HORIZONTAL_FOCUS_ANGLE;
		final boolean hVisible = hRange < 1.0;
		final boolean left, right;

		if(hVisible){
			left = right = false;
		}else{
			final double maxHRange = (360.0 / HORIZONTAL_FOCUS_ANGLE);
			final double extreme = (maxHRange - 1.0) / 2.0 + 0.5;
			left  = hRange > extreme;
			right = !left;
		}

		// Up - down operations

		final boolean lookingUp;
		if(Math.abs(y) < 90)
			lookingUp = false;
		else
			lookingUp = true;

		final boolean vVisible = z > (90 - VERTICAL_FOCUS_ANGLE / 2);
		final boolean up, down;
		if(vVisible){
			up = down = false;
		}else{
			up = !lookingUp;
			down = !up;
		}

		final double maxValue = VERTICAL_FOCUS_ANGLE / 2;
		final double relativeDistanceToCenter = (z - (90 - VERTICAL_FOCUS_ANGLE / 2)) / maxValue;

		final double fixedRelativeDistanceToCenter;
		if(lookingUp){
			fixedRelativeDistanceToCenter = (1 - relativeDistanceToCenter) / 2 + 0.5;
		}else{
			fixedRelativeDistanceToCenter = relativeDistanceToCenter / 2;
		}
		final double vRange = fixedRelativeDistanceToCenter;

		final double proportion = calculateProportion(distance);
		
		this.viewfinderView.getDrawer().setPointPerThousand((int)Math.round(hRange * 1000), (int)Math.round(vRange * 1000), (int)Math.round(proportion * 1000));
		this.viewfinderView.getDrawer().setArrows(up, down, left, right);
		// this.viewfinderView.setArrows(up, down, left, right);
		this.viewfinderView.invalidate();
		distanceToPoint = (int)Math.round(this.geoCurve.getEllipsoidalDistance());
		final float accuracy = LocationService.getCurrentAccuracy();
		distanceTextView.setText(distanceToPoint + " [~" + accuracy + "] metros a " + Points.getSelectedPointName());
		//				Points.getSelectedPointName() + " a " + distanceToPoint + " metros");

		/*
		 * text for testing
		distanceTextView.setText("distance: " + this.geoCurve.getEllipsoidalDistance() + "; pos= " + "; z=" 
			+ z + "; proportion: " + proportion
			//+ ";\n Degrees to North from me = " + myAngleFromNorth + "; \n y = " + y + "; \n z = " + z 
			//+ "; \n Degrees to North: " + targetAngleFromNorth 
			//+ "; \n " + (vVisible?"":(up?"up!":"down!")) + " " + (hVisible?"":(left?"left!":"right!"))
			+ ";\n Degrees to point from me: " + targetAngleFromMe);
		 */
	}

	public static double getDistanceToPoint() {
		return distanceToPoint;
	}

	private double calculateProportion(final double distance) {
		// 1-(x-1)^2
		final double directProportion = distance / Constants.MAX_DISTANCE_BY_GMAPS;
		// 1-(directProportion-1)^2
		final int POWER = 2; // the higher the number is, the faster the image will go away. Always use even numbers
		final double curveProportion = 1 - Math.pow(directProportion - 1, POWER);
		return curveProportion;
	}
	
	@Override
	protected void resetStatusView() {
		viewfinderView.setVisibility(View.VISIBLE);
	}

	@Override
	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
