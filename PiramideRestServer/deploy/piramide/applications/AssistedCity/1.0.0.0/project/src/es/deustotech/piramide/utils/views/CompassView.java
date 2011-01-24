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

package es.deustotech.piramide.utils.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import es.deustotech.piramide.activities.location.Points;
import es.deustotech.piramide.utils.constants.Constants;
import es.deustotech.piramide.utils.distancecalc.DistanceCalculator;

public class CompassView extends View {

	private float direction 	= 0;
	private Paint paint 		= new Paint(Paint.ANTI_ALIAS_FLAG);
//	private Bitmap smallArrow = BitmapFactory.decodeResource(getResources(), R.drawable.compass_arrow_small);
	private boolean firstDraw;

	public CompassView(Context context) {
		super(context);
		initializeView();
	}

	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView();
	}

	public CompassView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView();
	}

	private void initializeView() {
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);//line thickness
		paint.setTextSize(20);
		firstDraw = true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
				MeasureSpec.getSize(heightMeasureSpec)/4);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		final float xAxisPoint = getMeasuredWidth() / 8.0f;
		final float yAxisPoint = getMeasuredHeight() / 1.2f;
 		float radiusCompass;

		if (xAxisPoint > yAxisPoint) {
			radiusCompass = (float) (yAxisPoint * 0.9 / 2.0f);
		} else {
			radiusCompass = (float) ((xAxisPoint) * 0.9 / 2.0f);
		}
		
		paint.setColor(Color.LTGRAY);
		canvas.drawCircle(xAxisPoint, yAxisPoint , radiusCompass, paint);

		if (!firstDraw) {
//			canvas.drawBitmap(smallArrow, xAxisPoint, yAxisPoint, paint);
			canvas.drawLine(xAxisPoint, yAxisPoint,
					(float) (xAxisPoint + radiusCompass
							* Math.sin((double) (-direction) * Constants.PI / 180)),
					(float) (yAxisPoint - radiusCompass
							* Math.cos((double) (-direction) * Constants.PI / 180)), paint);
			//Draw the NORTH letter ('N') over the compass
			canvas.drawText("N", (float) (xAxisPoint + radiusCompass
					* Math.sin((double) (-direction) * Constants.PI / 180)), (float) (yAxisPoint - radiusCompass
							* Math.cos((double) (-direction) * Constants.PI / 180)), paint);
//			canvas.drawText(String.valueOf(direction), cxCompass, cyCompass, paint);
		}
	}

	public void updateDirection(float dir) {
		firstDraw = false;
		final double latitudeA  = Points.getCurrentAddress().getLatitude();
		final double longitudeA = Points.getCurrentAddress().getLongitude();
		final double latitudeB  = Points.getSelectedAddress().getLatitude();
		final double longitudeB = Points.getSelectedAddress().getLongitude();
//		direction = dir + 90;
		// - 90 degrees because of landscape orientation
		direction = (float) (dir + DistanceCalculator.calculatePointsAngle(latitudeA, 
				longitudeA, latitudeB, longitudeB)) - Constants.COMPASS_ANGLE_LANDSCAPE_CORRETION;
		invalidate();				
	}
}