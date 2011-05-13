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
import java.util.TreeMap;
import java.util.Vector;

import piramide.interaction.reasoner.RegionDistributionInfo;
import piramide.interaction.reasoner.creator.LinguisticTermMembershipFunction;
import piramide.interaction.reasoner.creator.Point;
import piramide.interaction.reasoner.creator.membershipgenerator.IMembershipFunctionGenerator;


public class RegionAccumulatorMembershipFunctionGenerator extends AbstractRegion implements IMembershipFunctionGenerator {
	
	public RegionAccumulatorMembershipFunctionGenerator(Map<Number, Double> values2trends){
		super(values2trends);
	}
	
	@Override
	public LinguisticTermMembershipFunction [] createFunctions(RegionDistributionInfo ... linguisticTerms){
		final LinguisticTermMembershipFunction [] functions = new LinguisticTermMembershipFunction[linguisticTerms.length];
		
		final List<Subregion> regions = createSubregions(linguisticTerms.length);
		
		for(int i = 0; i < linguisticTerms.length; ++i){
			final RegionDistributionInfo currentLinguisticTerm = linguisticTerms[i];
			
			final List<Point> ascendingPoints; 
			final List<Point> descendingPoints;
			
			if(i == 0)
				ascendingPoints = new Vector<Point>();
			else
				ascendingPoints = regions.get(i - 1).calculateAscendingPoints();
			
			if(i == linguisticTerms.length - 1)
				descendingPoints = new Vector<Point>();
			else
				descendingPoints = regions.get(i).calculateDescendingPoints();
			
			ascendingPoints.addAll(descendingPoints);
			
			final Point firstPoint = ascendingPoints.get(0).copy(0.0);
			final Point lastPoint  = ascendingPoints.get(ascendingPoints.size() - 1).copy(0.0);
			
			ascendingPoints.add(0, firstPoint);
			ascendingPoints.add(lastPoint);
			
			functions[i] = new LinguisticTermMembershipFunction(currentLinguisticTerm.getName(), ascendingPoints.toArray(new Point[]{}));
		}
		
		return functions;
	}
	
	
	
	
	List<Subregion> createSubregions(int numberOfLinguisticTerms){
		final Boundary [] boundaries = this.calculateBoundaries(numberOfLinguisticTerms);
		
		final List<Subregion> sets = new Vector<Subregion>();
		
		for(int i = 0; i < boundaries.length - 1; ++i){
			final Map<Number, Double> currentSet = new TreeMap<Number, Double>();
			
			final Boundary beforeBoundary = boundaries[i];
			final Boundary afterBoundary = boundaries[i + 1];
			
			for(int position = beforeBoundary.getPosAfter(); position <= afterBoundary.getPosBefore(); ++position){
				final double currentKey   = this.orderedKeys.get(position).doubleValue();
				final double currentValue = this.values2trends.get(Double.valueOf(currentKey)).doubleValue();
				
				currentSet.put(Double.valueOf(currentKey), Double.valueOf(currentValue));
			}
			
			sets.add(new Subregion(currentSet));
		}
		
		return sets;
	}
	
	/*
	 * A number of linguistic terms is provided. If this number is 3, this method
	 * will set three boundaries: first, "0", then the point where the 50% of the values
	 * are accumulated, and then the last point. If the number is higher, the number of
	 * boundaries will obviously increase.
	 * 
	 * Therefore, we can establish that X number of linguistic terms will create
	 * X boundaries, and (X - 1) subregions.
	 */
	Boundary [] calculateBoundaries(int numberOfLinguisticTerms){
		final List<Boundary> boundaries = new Vector<Boundary>();
		
		// 
		// We add the first boundary, which is always 0, meaning that it is 
		// the beginning of the universe.
		// 
		boundaries.add(new Boundary(0.0, 0.0, 0, 0));

		double accumulated = 0.0;
		int currentPosition = -1;
		
		// 
		// To model four linguistic terms, we need to split the universe into
		// three subregions
		// 
		final int numberOfSubregions = numberOfLinguisticTerms - 1;
		
		for(int currentSubregionNumber = 0; currentSubregionNumber < numberOfSubregions; ++currentSubregionNumber){
			// 
			// Given a subregion, we calculate the next boundary, since the previous boundary has
			// already been added to "boundaries".
			//
			
			final double currentProportionInSubregion = (currentSubregionNumber + 1) / (double)numberOfSubregions;
			
			final double nextBoundaryTrendValue = currentProportionInSubregion * this.sumOfValues;
			
			while(accumulated <= nextBoundaryTrendValue && currentPosition < this.orderedKeys.size()){
				currentPosition++;
				if(currentPosition < this.orderedKeys.size()){
					final Number currentKey   = this.orderedKeys.get(currentPosition);
					final double currentValue = this.values2trends.get(currentKey).doubleValue(); 
					accumulated += currentValue;
				}else
					break;
			}

			final int firstPositionBefore;
			if(currentPosition == 0)
				firstPositionBefore = currentPosition;
			else if(!isLastPosition(currentPosition) && hasAchievedExactAccumulated(accumulated, nextBoundaryTrendValue))
				firstPositionBefore = currentPosition;
			else
				firstPositionBefore = currentPosition - 1;
			
			
			// If the currentPositionInSubregion is the last position, we take as "position after" the last position, 
			// instead of the current position.
			final int firstPositionAfter;
			if(isLastPosition(currentPosition))
				firstPositionAfter = currentPosition - 1;
			else
				firstPositionAfter = currentPosition;
			
			final double lastBefore = this.orderedKeys.get(firstPositionBefore).doubleValue();
			final double firstAfter = this.orderedKeys.get(firstPositionAfter).doubleValue();

			final Boundary boundary = new Boundary(lastBefore, firstAfter, firstPositionBefore, firstPositionAfter);
			boundaries.add(boundary);
		}
		
		return boundaries.toArray(new Boundary[]{});
	}

	private boolean hasAchievedExactAccumulated(double accumulated,
			final double nextBoundaryTrendValue) {
		return accumulated == nextBoundaryTrendValue;
	}

	private boolean isLastPosition(int currentPosition) {
		return currentPosition == this.orderedKeys.size();
	}
}
