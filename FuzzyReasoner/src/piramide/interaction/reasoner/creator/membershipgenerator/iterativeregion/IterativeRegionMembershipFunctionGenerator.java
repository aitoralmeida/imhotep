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
import java.util.Map;
import java.util.Vector;

import piramide.interaction.reasoner.RegionDistributionInfo;
import piramide.interaction.reasoner.creator.LinguisticTermMembershipFunction;
import piramide.interaction.reasoner.creator.Point;
import piramide.interaction.reasoner.creator.WarningStore;
import piramide.interaction.reasoner.creator.membershipgenerator.IMembershipFunctionGenerator;

public class IterativeRegionMembershipFunctionGenerator implements
		IMembershipFunctionGenerator {

	private final Map<Number, Double> values2trends;
	private final WarningStore warningStore;
	
	public IterativeRegionMembershipFunctionGenerator(Map<Number, Double> values2trends, WarningStore warningStore) {
		this.values2trends = values2trends;
		this.warningStore  = warningStore;
	}

	@Override
	public LinguisticTermMembershipFunction[] createFunctions(RegionDistributionInfo ... linguisticTerms) {
		
		final Universe universe = new Universe(this.values2trends, linguisticTerms);
		final Universe bestUniverse = findBestUniverse(universe);
		
		
		if (linguisticTerms.length == 1)
			return createUniqueMembershipFunction(universe, linguisticTerms[0]);
		
		final Region [] regions = bestUniverse.getRegions();
		
		final LinguisticTermMembershipFunction [] functions = new LinguisticTermMembershipFunction[regions.length + 1];

		if(regions.length > 0 && linguisticTerms.length > 0)
			functions[0] = new LinguisticTermMembershipFunction(linguisticTerms[0].getName(), regions[0].getDescendingPoints());
		
		for(int i = 0; i < regions.length; ++i){
			final Region currentRegion = regions[i];
			final Point [] ascendingPoints = currentRegion.getAscendingPoints();
			
			final Point [] descendingPoints;
			if(i + 1 < regions.length){
				final Region nextRegion = regions[i + 1];
				descendingPoints = nextRegion.getDescendingPoints();
			}else
				descendingPoints = new Point[]{};
			
			final Point[] totalPoints = mergePoints(ascendingPoints, descendingPoints);
			
			final RegionDistributionInfo linguisticTerm = linguisticTerms[i + 1];
			functions[ i + 1 ] = new LinguisticTermMembershipFunction(linguisticTerm.getName(), totalPoints);
		}
		
		if(bestUniverse.detectRepeatedLinguisticTerms())
			this.warningStore.add("There are repeated linguistic terms in the universe");
		
		return functions;
	}
	
	LinguisticTermMembershipFunction [] createUniqueMembershipFunction(Universe universe, RegionDistributionInfo linguisticTerm){
		final Point points[] = new Point[3];
		
		points[0] = new Point(0.0, 0.0);
	
		final double targetTrend = universe.getSumOfTrends() / 2.0;
		double accumulatedTrend = 0.0;
		final Individual[] individuals = universe.getIndividuals();
		for (int i = 0; i < individuals.length; i++) {
			accumulatedTrend += individuals[i].getTrend();
			if (accumulatedTrend >= targetTrend){
				points[1] = new Point(individuals[i].getIdentityValue(), 1.0);
				break;
			}
		}
		
		points[2] = new Point(individuals[individuals.length-1].getIdentityValue(), 0.0);		
		
		final LinguisticTermMembershipFunction [] functions = new LinguisticTermMembershipFunction[1];
			
		functions[0] = new LinguisticTermMembershipFunction(linguisticTerm.getName(), points);
		return functions;
		
	}

	private Point[] mergePoints(final Point[] ascendingPoints, final Point[] descendingPoints) {
		if(ascendingPoints.length == 0)
			return descendingPoints;
		if(descendingPoints.length == 0)
			return ascendingPoints;
		
		final List<Point> totalPoints = new Vector<Point>();

		for(int i = 0; i < ascendingPoints.length - 1; ++i)
			totalPoints.add(ascendingPoints[i]);
		
		final Point lastAscendingPoint   = ascendingPoints[ascendingPoints.length - 1];
		final Point firstDescendingPoint = descendingPoints[0];
		
		if(lastAscendingPoint.getValue() == firstDescendingPoint.getValue()){
			totalPoints.add(lastAscendingPoint);
			totalPoints.add(firstDescendingPoint);
		}else{
			final double horizontalDistance = lastAscendingPoint.getValue() - firstDescendingPoint.getValue();
			
			if(ascendingPoints.length < 2 || descendingPoints.length < 2){
				totalPoints.add(new Point(lastAscendingPoint.getValue() + horizontalDistance / 2, 1.0));
			}else{
				final double leftTrend  = ascendingPoints[ascendingPoints.length - 2].getTrend();
				final double rightTrend = descendingPoints[1].getTrend();
				
				final double heightSum = leftTrend + rightTrend;
				
				final double proportion = leftTrend / heightSum;
				
				final double position = lastAscendingPoint.getValue() - proportion * horizontalDistance;
				
				totalPoints.add(new Point(position, 1.0));
			}
		}
		
		for(int i = 1; i < descendingPoints.length; ++i)
			totalPoints.add(descendingPoints[i]);
		
		return totalPoints.toArray(new Point[]{});
	}

	Universe findBestUniverse(Universe universe) {
		Universe bestUniverse = universe;
		double bestHeuristic = Double.MAX_VALUE;
		
		for(int i = 0; i < universe.getPermutationsSize(); ++i){
			final Universe currentPermutation = universe.getPermutation(i);
			final double currentHeuristic = currentPermutation.heuristicValue();
			if(currentHeuristic < bestHeuristic){
				bestUniverse  = currentPermutation;
				bestHeuristic = currentHeuristic;
			}
		}
		
		return bestUniverse;
	}

}
