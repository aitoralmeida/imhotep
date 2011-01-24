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

package es.deustotech.piramide.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.google.zxing.client.android.camera.CameraManager;

import es.deustotech.piramide.R;

public class CanvasIconDrawer {
	
	  private final Paint paint;
	  private volatile int xAxisPerThousand 	= 500; // value 0..1000 being 0 = left and 1000 = right; other value will not be printed
	  private volatile int yAxisPerThousand 	= 500; // value 0..1000 being 0 = left and 1000 = right; other value will not be printed
	  private volatile int iconPerThousand      = 500; // value 0..1000 being 0 = close and 1000 = far; other value will not be printed
	  private volatile boolean showUpArrow 		= false;
	  private volatile boolean showDownArrow 	= false;
	  private volatile boolean showLeftArrow 	= false;
	  private volatile boolean showRightArrow 	= false;
	  
	  private final Bitmap placesIcon;
	  private final Bitmap leftArrow;
	  private final Bitmap rightArrow;
	  private final Bitmap downArrow;
	  private final Bitmap upArrow;
	  
	  /**
	   * How many levels of icon resolutions will be available. The higher the number is, 
	   * the more drawings there will be, so the more memory will be required but the better
	   * resolutions will be available and the better effect you will get.
	   */
	  private final int RESOLUTION = 64;
	  /**
	   * Minimum height in pixels; must be higher than placesIcon.getHeight();
	   */
	  private final int MINIMUM_HEIGHT;
	  private final int MINIMUM_WIDTH;
	  private final Bitmap [] resizedPlacesIcons = new Bitmap[RESOLUTION];
	  
	  private final View view;

	  public CanvasIconDrawer(View view){
		  this.view = view;
		  
		  this.paint 			= new Paint();
		  
		  this.placesIcon 	 	= BitmapFactory.decodeResource(view.getResources(), R.drawable.places_icon);
		  this.leftArrow 	 	= BitmapFactory.decodeResource(view.getResources(), R.drawable.left_arrow);
		  this.rightArrow 	 	= BitmapFactory.decodeResource(view.getResources(), R.drawable.right_arrow);
		  this.downArrow 	 	= BitmapFactory.decodeResource(view.getResources(), R.drawable.down_arrow);
		  this.upArrow 		    = BitmapFactory.decodeResource(view.getResources(), R.drawable.up_arrow);
		  
		  this.MINIMUM_HEIGHT 	= 10;
		  this.MINIMUM_WIDTH 	= placesIcon.getWidth() * MINIMUM_HEIGHT / placesIcon.getHeight();
		  
		  loadResizedPlacedIcons();
	  }
	  
	  private void loadResizedPlacedIcons(){
		  for(int i = 0; i < RESOLUTION; ++i){
			  int newWidth  = MINIMUM_WIDTH  + (placesIcon.getWidth() - MINIMUM_WIDTH) * i / RESOLUTION; 
			  if(newWidth == 0)
				  newWidth = 1;
			  
			  int newHeight = MINIMUM_HEIGHT + (placesIcon.getHeight() - MINIMUM_HEIGHT) * i / RESOLUTION; 
			  if(newHeight == 0)
				  newHeight = 1;
			  
			  if(i > 0 && newHeight == this.resizedPlacesIcons[i - 1].getHeight() && newWidth == this.resizedPlacesIcons[i - 1].getWidth()){
				  this.resizedPlacesIcons[i] = this.resizedPlacesIcons[i - 1];
			  }else{
				  final Bitmap resized = Bitmap.createScaledBitmap(placesIcon, newWidth, newHeight, false);
				  this.resizedPlacesIcons[i] = resized;
			  }
		  }
	  }
	  
	  private Bitmap getResizedPlacesIcon(double iconProportion){
		  assert iconProportion >= 0 && iconProportion <= 1.0;
		  final int position = (int)Math.round(iconProportion * (RESOLUTION - 1));
		  return this.resizedPlacesIcons[position];
	  }
	  
	  public void setPointPerThousand(int x, int y){
		  this.xAxisPerThousand = x;
		  this.yAxisPerThousand = y;
	  }
	  
	  public void setArrows(boolean up, boolean down, boolean left, boolean right){
		  this.showUpArrow    = up;
		  this.showDownArrow  = down;
		  this.showLeftArrow  = left;
		  this.showRightArrow = right;
	  }
	  
	  public void setPointPerThousand(int x, int y, int iconPerThousand){
		  this.xAxisPerThousand = x;
		  this.yAxisPerThousand = y;
		  this.iconPerThousand = iconPerThousand;
	  }
	  
	  public void draw(Canvas canvas) {
	      Rect frame = CameraManager.get().getFramingRect();
		  if(this.xAxisPerThousand >= 0    && this.xAxisPerThousand <= 1000 
			    	&& this.yAxisPerThousand >= 0  && this.yAxisPerThousand <= 1000 
			    	&& this.iconPerThousand >= 0   && this.iconPerThousand <= 1000) {
			    	  
			    	  final double iconProportion = 1.0 * (1000 - this.iconPerThousand) / 1000;
			    	  final Bitmap resizedPlacesIcon = this.getResizedPlacesIcon(iconProportion);
				      canvas.drawBitmap(resizedPlacesIcon, this.xAxisPerThousand * canvas.getWidth() / 1000, this.yAxisPerThousand * canvas.getHeight() / 1000, paint);
			      }
		  
		  if(this.showLeftArrow) {
			  canvas.drawBitmap(leftArrow, 0, 
					  canvas.getHeight() / 2.0f - leftArrow.getHeight() / 2, paint);
		  }
		  if(this.showRightArrow) {
			  canvas.drawBitmap(rightArrow, canvas.getWidth() - rightArrow.getWidth(), 
					  canvas.getHeight() / 2.0f - rightArrow.getHeight() / 2, paint);
		  }
		  if(this.showUpArrow) {
			  canvas.drawBitmap(upArrow, canvas.getWidth() / 2.0f - upArrow.getWidth() / 2, 
					  40 , paint);
		  }
		  if(this.showDownArrow) {
			  canvas.drawBitmap(downArrow, canvas.getWidth() / 2.0f - downArrow.getWidth() / 2, 
					  canvas.getHeight() - downArrow.getHeight(), paint);
		  }
		  
		  // Request another update at the animation interval, but only repaint the laser line,
		  // not the entire viewfinder mask.
		  this.view.postInvalidateDelayed(0, frame.left, frame.top, frame.right, frame.bottom);
	  }
	  
}
