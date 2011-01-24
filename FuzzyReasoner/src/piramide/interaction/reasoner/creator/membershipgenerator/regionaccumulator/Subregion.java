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
 * Author: Aitor Almeida <aitor.almeida@deusto.es>
 *         Pablo Orduña <pablo.orduna@deusto.es>
 *         Eduardo Castillejo <eduardo.castillejo@deusto.es>
 *
 */
package piramide.interaction.reasoner.creator.membershipgenerator.regionaccumulator;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import piramide.interaction.reasoner.creator.Point;



class Subregion extends AbstractRegion {

	Subregion(Map<Number, Double> values2trends) {
		super(values2trends);
	}

	/*
	 * Take as an example the following data:
	 * 
	 *   value 0.0 -> trends 2.0
	 *   value 1.0 -> trends 2.0
	 *   value 2.0 -> trends 2.0
	 *   value 3.0 -> trends 2.0
	 *   
	 * In proportions, this means:
	 * 
	 *   value 0.0 -> trends 25%
	 *   value 1.0 -> trends 25%
	 *   value 2.0 -> trends 25%
	 *   value 3.0 -> trends 25%
	 *   
	 * Calculating the accumulation of the trend values, we can get:
	 * 
	 *   value 0.0 -> accumulated 25%
	 *   value 1.0 -> accumulated 50%
	 *   value 2.0 -> accumulated 75%
	 *   value 3.0 -> accumulated 100%
	 *
	 * However, we need to represent that with value 0.0, the membership 
	 * function is 0.0. We can therefore use the following trend values: 
	 * 
	 *   value 0.0 -> accumulated 0%
	 *   value 1.0 -> accumulated 25%
	 *   value 2.0 -> accumulated 50%
	 *   value 3.0 -> accumulated 75%
	 *
	 * This approach is also not valid, since we want that with value 3.0, the
	 * membership function to be 100%.
	 * 
	 * In order to fix this problem, we take both points, calling them 
	 * "higher point" and "lower point":
	 * 
	 *   value 0.0 -> lower  0%, higher  25%
	 *   value 1.0 -> lower 25%, higher  50%
	 *   value 2.0 -> lower 50%, higher  75%
	 *   value 3.0 -> lower 75%, higher 100%
	 *
	 * Given any value, it will be in a position between the maximum value (3.0)
	 * and the minimum value (0.0). This position represents a percent of the 
	 * distance covered by the maximum and the minimum value. For instance, 1.0 is
	 * in the 33% of the distance covered by 0.0 and 3.0.
	 * 
	 * Given this proportion inside the X axis (being X = value; Y = regression 
	 * trend value we are looking for), we know that the point we are looking for
	 * is exactly in that proportion between the "lower point" and the "higher point":
	 * 
	 *   value 0.0 -> proportion   0%. lower  0%, higher  25%. regression:   0.0%
	 *   value 1.0 -> proportion  33%. lower 25%, higher  50%. regression:  33.3%
	 *   value 2.0 -> proportion  66%. lower 50%, higher  75%. regression:  66.6%
	 *   value 3.0 -> proportion 100%. lower 75%, higher 100%. regression: 100.0%
	 *
	 * Being 
	 * 
	 *    regression = lower + proportion * (higher - lower) 
	 *
	 * If the function is a curve, it doesn't matter, since both curves are equidistant. 
	 * 
	 */
	List<Point> calculateAscendingPoints(){
		final List<Point> points = new Vector<Point>();
		
		if(this.orderedKeys.size() == 0)
			return points;
		
		final double firstPoint           = this.orderedKeys.get(0).doubleValue();
		final double lastPoint            = this.orderedKeys.get(this.orderedKeys.size() - 1).doubleValue();
		final double horizontalDistance   = lastPoint - firstPoint;
		
		if(firstPoint == lastPoint){
			points.add(new Point( firstPoint, 0.0 ));
			points.add(new Point( firstPoint, 1.0 ));
			return points;
		}
		
		double higherAccumulated = 0.0;
		double lowerAccumulated  = 0.0;
		
		for(Number currentKeyNumber : this.orderedKeys){
			final double currentKey = currentKeyNumber.doubleValue();
			final double currentTrend = this.values2trends.get(Double.valueOf(currentKey)).doubleValue();
			
			higherAccumulated += currentTrend;

			final double drawnProportion  = (currentKey - firstPoint) / horizontalDistance;
			final double verticalDistance = higherAccumulated - lowerAccumulated;
			final double regressionValue = lowerAccumulated + verticalDistance * drawnProportion;
			points.add(new Point( currentKey, regressionValue / this.sumOfValues ));
			
			lowerAccumulated  += currentTrend;
		}
		
		return points;
	}
	
	List<Point> calculateDescendingPoints(){
		final List<Point> points = this.calculateAscendingPoints();
		for(Point point : points)
			point.reverse();
		return points;
	}
	
}
