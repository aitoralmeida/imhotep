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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import piramide.interaction.reasoner.RegionDistributionInfo;
import piramide.interaction.reasoner.creator.LinguisticTermMembershipFunction;
import piramide.interaction.reasoner.creator.Point;
import piramide.interaction.reasoner.creator.WarningStore;

@SuppressWarnings("boxing")
public class IterativeRegionMembershipFunctionGeneratorTest {

	
	@Test
	public void testCreateUniqueMembershipFunction(){
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);
		
		final RegionDistributionInfo linguisticTerm = new RegionDistributionInfo("a", 0.0);
		final IterativeRegionMembershipFunctionGenerator generator = new IterativeRegionMembershipFunctionGenerator(values2trends, new WarningStore());
		final Universe universe = new Universe(values2trends, linguisticTerm);
		
		final Point expectedPoints[] = new Point[3];
		
		expectedPoints[0] = new Point(0.0, 0.0);
		expectedPoints[1] = new Point(1.0, 1.0);
		expectedPoints[2] = new Point(3.0, 0.0);	
		
		final LinguisticTermMembershipFunction expected = new LinguisticTermMembershipFunction(linguisticTerm.getName(), expectedPoints);
		
		final LinguisticTermMembershipFunction [] generated = generator.createUniqueMembershipFunction(universe, linguisticTerm);
		assertEquals(expected.getName(), generated[0].getName());
		final Point[] generatedPoints = generated[0].getPoints();
		
		assertEquals(expectedPoints.length, generatedPoints.length);
		for (int i = 0; i < expectedPoints.length; i++) {
			assertEquals(expectedPoints[i].getValue(), generatedPoints[i].getValue(),0000.1);
			assertEquals(expectedPoints[i].getTrend(), generatedPoints[i].getTrend(),0000.1);			
		}

	}
	
	
	@Test
	public void testCreateFunctions() {
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);
		
		final IterativeRegionMembershipFunctionGenerator generator = new IterativeRegionMembershipFunctionGenerator(values2trends, new WarningStore());
		final LinguisticTermMembershipFunction [] functions = generator.createFunctions(new RegionDistributionInfo("a", 1.0), new RegionDistributionInfo("b", 1.0));
		assertEquals(2, functions.length);
		
		
		assertEquals("a", functions[0].getName());
		assertEquals(4, functions[0].getPoints().length);
		
		assertEquals(0.0, functions[0].getPoints()[0].getValue(), 0.0001);
		assertEquals(1.0, functions[0].getPoints()[1].getValue(), 0.0001);
		assertEquals(2.0, functions[0].getPoints()[2].getValue(), 0.0001);
		assertEquals(3.0, functions[0].getPoints()[3].getValue(), 0.0001);
		
		assertEquals(1.0, functions[0].getPoints()[0].getTrend(), 0.0001);
		assertEquals(2.0 / 3, functions[0].getPoints()[1].getTrend(), 0.0001);
		assertEquals(1.0 / 3, functions[0].getPoints()[2].getTrend(), 0.0001);
		assertEquals(0.0, functions[0].getPoints()[3].getTrend(), 0.0001);
		
		
		assertEquals("b", functions[1].getName());
		assertEquals(4, functions[1].getPoints().length);
		
		assertEquals(0.0, functions[0].getPoints()[0].getValue(), 0.0001);
		assertEquals(1.0, functions[0].getPoints()[1].getValue(), 0.0001);
		assertEquals(2.0, functions[0].getPoints()[2].getValue(), 0.0001);
		assertEquals(3.0, functions[0].getPoints()[3].getValue(), 0.0001);
		
		assertEquals(0.0, functions[1].getPoints()[0].getTrend(), 0.0001);
		assertEquals(1.0 / 3, functions[1].getPoints()[1].getTrend(), 0.0001);
		assertEquals(2.0 / 3, functions[1].getPoints()[2].getTrend(), 0.0001);
		assertEquals(1.0, functions[1].getPoints()[3].getTrend(), 0.0001);
	}
	
	@Test
	public void testCreateFunctions_2regions() {
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);
		
		final IterativeRegionMembershipFunctionGenerator generator = new IterativeRegionMembershipFunctionGenerator(values2trends, new WarningStore());
		final LinguisticTermMembershipFunction [] functions = generator.createFunctions(new RegionDistributionInfo("a", 1 / 2.0), new RegionDistributionInfo("b", 1 / 2.0), new RegionDistributionInfo("c", 1 / 2.0));
		assertEquals(3, functions.length);
		
		
		assertEquals("a", functions[0].getName());
		assertEquals(3, functions[0].getPoints().length);
		
		assertEquals(0.0, functions[0].getPoints()[0].getValue(), 0.0001);
		assertEquals(1.0, functions[0].getPoints()[1].getValue(), 0.0001);
		assertEquals(1.5, functions[0].getPoints()[2].getValue(), 0.0001);
		
		assertEquals(1.0, functions[0].getPoints()[0].getTrend(), 0.0001);
		assertEquals(0.0, functions[0].getPoints()[1].getTrend(), 0.0001);
		assertEquals(0.0, functions[0].getPoints()[2].getTrend(), 0.0001);
		
		assertEquals("b", functions[1].getName());
		assertEquals(6, functions[1].getPoints().length);
		
		assertEquals(0.0, functions[1].getPoints()[0].getValue(), 0.0001);
		assertEquals(1.0, functions[1].getPoints()[1].getValue(), 0.0001);
		assertEquals(1.5, functions[1].getPoints()[2].getValue(), 0.0001);
		assertEquals(1.5, functions[1].getPoints()[3].getValue(), 0.0001);
		assertEquals(2.0, functions[1].getPoints()[4].getValue(), 0.0001);
		assertEquals(3.0, functions[1].getPoints()[5].getValue(), 0.0001);
		
		assertEquals(0.0, functions[1].getPoints()[0].getTrend(), 0.0001);
		assertEquals(1.0, functions[1].getPoints()[1].getTrend(), 0.0001);
		assertEquals(1.0, functions[1].getPoints()[2].getTrend(), 0.0001);
		assertEquals(1.0, functions[1].getPoints()[3].getTrend(), 0.0001);
		assertEquals(1.0, functions[1].getPoints()[4].getTrend(), 0.0001);
		assertEquals(0.0, functions[1].getPoints()[5].getTrend(), 0.0001);
		
		
		assertEquals("c", functions[2].getName());
		assertEquals(3, functions[2].getPoints().length);
		
		assertEquals(1.5, functions[2].getPoints()[0].getValue(), 0.0001);
		assertEquals(2.0, functions[2].getPoints()[1].getValue(), 0.0001);
		assertEquals(3.0, functions[2].getPoints()[2].getValue(), 0.0001);
		
		assertEquals(0.0, functions[2].getPoints()[0].getTrend(), 0.0001);
		assertEquals(0.0, functions[2].getPoints()[1].getTrend(), 0.0001);
		assertEquals(1.0, functions[2].getPoints()[2].getTrend(), 0.0001);
	}
	
	@Test
	public void testFindBestUniverse() {
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);

		final Universe universe = new Universe(values2trends, "a", "b", "c");
		
		final IterativeRegionMembershipFunctionGenerator generator = new IterativeRegionMembershipFunctionGenerator(values2trends, new WarningStore());
		final Universe bestUniverse = generator.findBestUniverse(universe);
		assertEquals(2, bestUniverse.getRegions().length);
		
		assertEquals(0, bestUniverse.getRegions()[0].getLeftBoundary().getPos());
		assertEquals(1, bestUniverse.getRegions()[0].getRightBoundary().getPos());
		
		assertEquals(2, bestUniverse.getRegions()[1].getLeftBoundary().getPos());
		assertEquals(3, bestUniverse.getRegions()[1].getRightBoundary().getPos());
	}

	@Test
	public void testFindBestUniverse_manyRegions() {
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 200.0);
		values2trends.put(3.0, 2.0);

		final Universe universe = new Universe(values2trends, "a", "b", "c", "d", "e");
		
		final IterativeRegionMembershipFunctionGenerator generator = new IterativeRegionMembershipFunctionGenerator(values2trends, new WarningStore());
		final Universe bestUniverse = generator.findBestUniverse(universe);
		assertEquals(4, bestUniverse.getRegions().length);
		
		assertEquals(0, bestUniverse.getRegions()[0].getLeftBoundary().getPos());
		assertEquals(1, bestUniverse.getRegions()[0].getRightBoundary().getPos());
		
		assertEquals(1, bestUniverse.getRegions()[1].getLeftBoundary().getPos());
		assertEquals(2, bestUniverse.getRegions()[1].getRightBoundary().getPos());
		
		assertEquals(1, bestUniverse.getRegions()[2].getLeftBoundary().getPos());
		assertEquals(2, bestUniverse.getRegions()[2].getRightBoundary().getPos());
		
		assertEquals(1, bestUniverse.getRegions()[3].getLeftBoundary().getPos());
		assertEquals(2, bestUniverse.getRegions()[3].getRightBoundary().getPos());
		
		assertTrue(bestUniverse.detectRepeatedLinguisticTerms());
	}

	@Test
	public void testFindBestUniverse_3regions() {
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);

		final Universe universe = new Universe(values2trends, "a", "b", "c", "d");
		
		final IterativeRegionMembershipFunctionGenerator generator = new IterativeRegionMembershipFunctionGenerator(values2trends, new WarningStore());
		final Universe bestUniverse = generator.findBestUniverse(universe);
		assertEquals(3, bestUniverse.getRegions().length);
		
		assertEquals(0, bestUniverse.getRegions()[0].getLeftBoundary().getPos());
		assertEquals(1, bestUniverse.getRegions()[0].getRightBoundary().getPos());
		
		assertEquals(1, bestUniverse.getRegions()[1].getLeftBoundary().getPos());
		assertEquals(2, bestUniverse.getRegions()[1].getRightBoundary().getPos());
		
		assertEquals(2, bestUniverse.getRegions()[2].getLeftBoundary().getPos());
		assertEquals(3, bestUniverse.getRegions()[2].getRightBoundary().getPos());
	}

	@Test
	public void testFindBestUniverse_2() {
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 1.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 1.0);
		values2trends.put(3.0, 2.0);

		final Universe universe = new Universe(values2trends, "a", "b", "c", "d");
		
		final IterativeRegionMembershipFunctionGenerator generator = new IterativeRegionMembershipFunctionGenerator(values2trends, new WarningStore());
		final Universe bestUniverse = generator.findBestUniverse(universe);
		assertEquals(3, bestUniverse.getRegions().length);
		
		assertEquals(0, bestUniverse.getRegions()[0].getLeftBoundary().getPos());
		assertEquals(1, bestUniverse.getRegions()[0].getRightBoundary().getPos());
		
		assertEquals(1, bestUniverse.getRegions()[1].getLeftBoundary().getPos());
		assertEquals(2, bestUniverse.getRegions()[1].getRightBoundary().getPos());
		
		assertEquals(3, bestUniverse.getRegions()[2].getLeftBoundary().getPos());
		assertEquals(3, bestUniverse.getRegions()[2].getRightBoundary().getPos());
	}

	@Test
	public void testFindBestUniverse_3() {
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 1.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 1.0);

		final Universe universe = new Universe(values2trends, "a", "b", "c", "d");
		
		final IterativeRegionMembershipFunctionGenerator generator = new IterativeRegionMembershipFunctionGenerator(values2trends, new WarningStore());
		final Universe bestUniverse = generator.findBestUniverse(universe);
		assertEquals(3, bestUniverse.getRegions().length);
		
		assertEquals(0, bestUniverse.getRegions()[0].getLeftBoundary().getPos());
		assertEquals(1, bestUniverse.getRegions()[0].getRightBoundary().getPos());
		
		assertEquals(1, bestUniverse.getRegions()[1].getLeftBoundary().getPos());
		assertEquals(2, bestUniverse.getRegions()[1].getRightBoundary().getPos());
		
		assertEquals(2, bestUniverse.getRegions()[2].getLeftBoundary().getPos());
		assertEquals(3, bestUniverse.getRegions()[2].getRightBoundary().getPos());
	}

}
