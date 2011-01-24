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

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;


public final class ViewfinderView extends View {

  private final CanvasIconDrawer drawer;
  
  // This constructor is used when the class is built from an XML resource.
  public ViewfinderView(Context context, AttributeSet attrs) {
    super(context, attrs);
    
    this.drawer = new CanvasIconDrawer(this);
  }
  
  @Override
  public void onDraw(Canvas canvas) {
	  this.drawer.draw(canvas);
  }
  
  public CanvasIconDrawer getDrawer(){
	  return this.drawer;
  }
  
  public void drawViewfinder() {
	  invalidate();
  }
}
