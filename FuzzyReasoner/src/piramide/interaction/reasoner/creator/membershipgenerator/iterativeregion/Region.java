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
package piramide.interaction.reasoner.creator.membershipgenerator.iterativeregion;

import java.util.List;
import java.util.Vector;

import piramide.interaction.reasoner.creator.Point;


class Region {
	private final Boundary leftBoundary;
	private final Boundary rightBoundary;
	private final Universe universe;
	private final int regionPosition;
	
	Region(Boundary leftBoundary, Boundary rightBoundary, Universe universe, int regionPosition){
		this.leftBoundary   = leftBoundary;
		this.rightBoundary  = rightBoundary;
		this.universe       = universe;
		this.regionPosition = regionPosition;
	}

	Region copy(Universe universe) {
		return new Region(this.leftBoundary.copy(), this.rightBoundary.copy(), universe, this.regionPosition);
	}
	
	Region copy(boolean leftRequiresChange, boolean rightRequiresChange) {
		return new Region(this.leftBoundary.copy(leftRequiresChange), this.rightBoundary.copy(rightRequiresChange), this.universe, this.regionPosition);
	}
	
	int size(){
		return 1 + this.rightBoundary.getPos() - this.leftBoundary.getPos();
	}
	
	Individual get(int pos){
		return this.universe.getIndividuals()[pos + this.leftBoundary.getPos()];
	}
	
	Individual get(Boundary boundary){
		return this.universe.getIndividuals()[boundary.getPos()];
	}
	
	
	double getTrendSum(){
		double totalTrend = 0.0;
		for(int i = 0; i < size(); ++i){
			final Individual current = get(i);
			totalTrend += current.getTrend();
		}
		return totalTrend;
	}
	
	Boundary getLeftBoundary(){
		return this.leftBoundary;
	}
	
	Boundary getRightBoundary(){
		return this.rightBoundary;
	}
	
	private Region getLeftRegion(){
		return this.universe.getRegions()[this.regionPosition - 1];
	}
	
	private Region getRightRegion(){
		return this.universe.getRegions()[this.regionPosition + 1];
	}
	
	private boolean isLastRegion(){
		return this.universe.getRegions().length == this.regionPosition + 1;
	}
	
	private boolean isFirstRegion(){
		return this.regionPosition == 0;
	}
	
	Point [] getAscendingPoints(){
		final double origin = calculateOrigin();
		final double destination = calculateDestination();
		
		final double totalSum = getTrendSum();
		
		final List<Point> points = new Vector<Point>();
		double accumulatedLow  = 0.0;
		double accumulatedHigh = 0.0;
		
		if(this.size() < 1)
			throw new IllegalStateException("Any region must have at least one point");
		
		
		final double firstPoint = this.get(0).getIdentityValue();
		final double lastPoint  = this.get(this.size() - 1).getIdentityValue();
		final double horizontalDistance = lastPoint - firstPoint;
		
		points.add(new Point(origin, 0.0)); // it always starts in 0.0, the minimun membership value
		for (int i = 0; i < size(); ++i) {
			final double identityValue = this.get(i).getIdentityValue();
			
			accumulatedHigh += this.get(i).getTrend();
			
			final double drawnProportion  = (identityValue - firstPoint) / horizontalDistance;
			final double verticalDistance = accumulatedHigh - accumulatedLow;
			final double regressionValue = accumulatedLow + verticalDistance * drawnProportion;
			
			if(identityValue != origin && identityValue != destination){
				points.add(new Point( identityValue, regressionValue / totalSum ));
			}
			
			accumulatedLow = accumulatedHigh;
		}

		points.add(new Point(destination, 1.0)); //it always ends in 1.0, the maximun membership value
		
		return points.toArray(new Point[]{});
	}

	Point[] getDescendingPoints() {
		final Point [] ascendingPoints = this.getAscendingPoints();
		for(Point point : ascendingPoints)
			point.reverse();
		return ascendingPoints;
	}
	
	double calculateOrigin() {
		if(isFirstRegion()){
			return 0.0;
		}else if(this.leftBoundary.getPos() <= getLeftRegion().getRightBoundary().getPos()){
			return get(this.leftBoundary).getIdentityValue();
		}else{
			final Individual myLeft = get(this.leftBoundary);
			
			final Region leftRegion = getLeftRegion();
			final Individual previousRight = leftRegion.get(leftRegion.getRightBoundary());
			
			final double identityDifference = myLeft.getIdentityValue() - previousRight.getIdentityValue();
			final double heightSum = myLeft.getTrend() + previousRight.getTrend();
			
			final double proportion = myLeft.getTrend() / heightSum;
			
			return myLeft.getIdentityValue() - identityDifference * proportion;
		}
	}

	double calculateDestination() {
		if(isLastRegion()){
			return get(getRightBoundary()).getIdentityValue();
		}else if(this.rightBoundary.getPos() >= getRightRegion().getLeftBoundary().getPos()){
			return get(getRightBoundary()).getIdentityValue();
		}else{
			final Individual myRight = get(this.rightBoundary);
			
			final Region rightRegion = getRightRegion();
			final Individual nextLeft = rightRegion.get(rightRegion.getLeftBoundary());
			
			final double identityDifference = nextLeft.getIdentityValue() - myRight.getIdentityValue();
			final double heightSum = myRight.getTrend() + nextLeft.getTrend();
			
			final double proportion = myRight.getTrend() / heightSum;
			return myRight.getIdentityValue() + identityDifference * proportion;
		}
	}
}
