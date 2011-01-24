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

import static org.junit.Assert.*;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

@SuppressWarnings("boxing")
public class UniverseTest {

	@Test
	public void testCopy() {
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);
		
		final Universe universe  = new Universe(values2trends, "a", "b");
		final Universe universe2 = universe.copy();
		assertEquals(universe.getLinguisticTermNumber(), universe2.getLinguisticTermNumber());
	}

	@Test
	public void testInitializedToLeft_1region(){
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);
		
		final Universe universe = new Universe(values2trends, "a", "b");
		
		assertEquals(1, universe.getRegions().length);
		final Region region = universe.getRegions()[0];
		assertEquals(0, region.getLeftBoundary().getPos());
		assertEquals(3, region.getRightBoundary().getPos());
		
		assertEquals(4, region.size());
		assertEquals(0.0, region.get(0).getIdentity());
		assertEquals(1.0, region.get(1).getIdentity());
		assertEquals(2.0, region.get(2).getIdentity());
		assertEquals(3.0, region.get(3).getIdentity());
	}
	
	@Test
	public void testInitializedToLeft_1region_2(){
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 0.1);
		values2trends.put(1.0, 0.1);
		values2trends.put(2.0, 0.1);
		values2trends.put(3.0, 0.1);
		values2trends.put(4.0, 0.1);
		values2trends.put(5.0, 0.1);
		values2trends.put(6.0, 0.1);
		values2trends.put(7.0, 0.1);
		values2trends.put(8.0, 0.1);
		values2trends.put(9.0, 0.1);
		
		final Universe universe = new Universe(values2trends, "a", "b");
		
		assertEquals(1, universe.getRegions().length);
		final Region region = universe.getRegions()[0];
		assertEquals(0, region.getLeftBoundary().getPos());
		assertEquals(9, region.getRightBoundary().getPos());
	}
	
	@Test
	public void testInitializedToLeft_2region_1(){
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);
		
		final Universe universe = new Universe(values2trends, "a", "b", "c");
		
		assertEquals(2, universe.getRegions().length);
		final Region region1 = universe.getRegions()[0];
		assertEquals(0, region1.getLeftBoundary().getPos());
		assertEquals(1, region1.getRightBoundary().getPos());
		
		assertEquals(2, region1.size());
		assertEquals(0.0, region1.get(0).getIdentity());
		assertEquals(1.0, region1.get(1).getIdentity());
		
		final Region region2 = universe.getRegions()[1];
		assertEquals(2, region2.getLeftBoundary().getPos());
		assertEquals(3, region2.getRightBoundary().getPos());
		
		assertEquals(2, region2.size());
		assertEquals(2.0, region2.get(0).getIdentity());
		assertEquals(3.0, region2.get(1).getIdentity());
	}
	
	@Test
	public void testInitializedToLeft_2region_2(){
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 1.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 3.0);
		values2trends.put(3.0, 4.0);
		
		final Universe universe = new Universe(values2trends, "a", "b", "c");
		
		assertEquals(2, universe.getRegions().length);
		final Region region1 = universe.getRegions()[0];
		assertEquals(0, region1.getLeftBoundary().getPos());
		assertEquals(2, region1.getRightBoundary().getPos());
		
		assertEquals(3, region1.size());
		assertEquals(0.0, region1.get(0).getIdentity());
		assertEquals(1.0, region1.get(1).getIdentity());
		assertEquals(2.0, region1.get(2).getIdentity());
		
		final Region region2 = universe.getRegions()[1];
		assertEquals(2, region2.getLeftBoundary().getPos());
		assertEquals(3, region2.getRightBoundary().getPos());
		
		assertEquals(2, region2.size());
		assertEquals(2.0, region2.get(0).getIdentity());
		assertEquals(3.0, region2.get(1).getIdentity());
	}
	
	@Test
	public void testInitializedToLeft_3region_1(){
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);
		
		final Universe universe = new Universe(values2trends, "a", "b", "c", "d");
		
		assertEquals(3, universe.getRegions().length);
		final Region region1 = universe.getRegions()[0];
		assertEquals(0, region1.getLeftBoundary().getPos());
		assertEquals(1, region1.getRightBoundary().getPos());
		
		assertEquals(2, region1.size());
		assertEquals(0.0, region1.get(0).getIdentity());
		assertEquals(1.0, region1.get(1).getIdentity());
		
		final Region region2 = universe.getRegions()[1];
		assertEquals(1, region2.getLeftBoundary().getPos());
		assertEquals(2, region2.getRightBoundary().getPos());
		
		assertEquals(2, region2.size());
		assertEquals(1.0, region2.get(0).getIdentity());
		assertEquals(2.0, region2.get(1).getIdentity());
		
		final Region region3 = universe.getRegions()[2];
		assertEquals(2, region3.getLeftBoundary().getPos());
		assertEquals(3, region3.getRightBoundary().getPos());
		
		assertEquals(2, region3.size());
		assertEquals(2.0, region3.get(0).getIdentity());
		assertEquals(3.0, region3.get(1).getIdentity());
	}

	
	@Test
	public void testPermutations_2region(){
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);
		
		final Universe universe = new Universe(values2trends, "a", "b", "c"); // 2 regions
		assertEquals(8, universe.getPermutationsSize()); // 4 ** 2 / 2 = 8
		
		
		final Universe universe0 = universe.getPermutation(0);
		assertEquals(0, universe0.getRegions()[0].getLeftBoundary().getPos());
		assertEquals(1, universe0.getRegions()[0].getRightBoundary().getPos());
		assertEquals(2, universe0.getRegions()[1].getLeftBoundary().getPos());
		assertEquals(3, universe0.getRegions()[1].getRightBoundary().getPos());
		
		// This one makes no sense, since position 0 is not listed in any region. However, the heuristic function will penalize it
		final Universe universe1 = universe.getPermutation(1);
		assertEquals(1, universe1.getRegions()[0].getLeftBoundary().getPos());
		assertEquals(1, universe1.getRegions()[0].getRightBoundary().getPos());
		assertEquals(2, universe1.getRegions()[1].getLeftBoundary().getPos());
		assertEquals(3, universe1.getRegions()[1].getRightBoundary().getPos());
		assertEquals(Double.MAX_VALUE, universe1.heuristicValue(), 0.0001);
		
		final Universe universe2 = universe.getPermutation(2);
		assertEquals(0, universe2.getRegions()[0].getLeftBoundary().getPos());
		assertEquals(2, universe2.getRegions()[0].getRightBoundary().getPos());
		assertEquals(2, universe2.getRegions()[1].getLeftBoundary().getPos());
		assertEquals(3, universe2.getRegions()[1].getRightBoundary().getPos());
		
		// This one makes no sense, since position 0 is not listed in any region. However, the heuristic function will penalize it
		final Universe universe3 = universe.getPermutation(3);
		assertEquals(1, universe3.getRegions()[0].getLeftBoundary().getPos());
		assertEquals(2, universe3.getRegions()[0].getRightBoundary().getPos());
		assertEquals(2, universe3.getRegions()[1].getLeftBoundary().getPos());
		assertEquals(3, universe3.getRegions()[1].getRightBoundary().getPos());
		assertEquals(Double.MAX_VALUE, universe3.heuristicValue(), 0.0001);
		
		// This one makes no sense, since position 2 is not listed in any region. However, the heuristic function will penalize it
		final Universe universe4 = universe.getPermutation(4);
		assertEquals(0, universe4.getRegions()[0].getLeftBoundary().getPos());
		assertEquals(1, universe4.getRegions()[0].getRightBoundary().getPos());
		assertEquals(3, universe4.getRegions()[1].getLeftBoundary().getPos());
		assertEquals(3, universe4.getRegions()[1].getRightBoundary().getPos());
		assertEquals(Double.MAX_VALUE, universe4.heuristicValue(), 0.0001);
		
		// This one makes no sense, since position 2 is not listed in any region. However, the heuristic function will penalize it
		final Universe universe5 = universe.getPermutation(5);
		assertEquals(1, universe5.getRegions()[0].getLeftBoundary().getPos());
		assertEquals(1, universe5.getRegions()[0].getRightBoundary().getPos());
		assertEquals(3, universe5.getRegions()[1].getLeftBoundary().getPos());
		assertEquals(3, universe5.getRegions()[1].getRightBoundary().getPos());
		assertEquals(Double.MAX_VALUE, universe5.heuristicValue(), 0.0001);
		
		final Universe universe6 = universe.getPermutation(6);
		assertEquals(0, universe6.getRegions()[0].getLeftBoundary().getPos());
		assertEquals(2, universe6.getRegions()[0].getRightBoundary().getPos());
		assertEquals(3, universe6.getRegions()[1].getLeftBoundary().getPos());
		assertEquals(3, universe6.getRegions()[1].getRightBoundary().getPos());
		
		// This one makes no sense, since position 0 is not listed in any region. However, the heuristic function will penalize it
		final Universe universe7 = universe.getPermutation(7);
		assertEquals(1, universe7.getRegions()[0].getLeftBoundary().getPos());
		assertEquals(2, universe7.getRegions()[0].getRightBoundary().getPos());
		assertEquals(3, universe7.getRegions()[1].getLeftBoundary().getPos());
		assertEquals(3, universe7.getRegions()[1].getRightBoundary().getPos());
		assertEquals(Double.MAX_VALUE, universe7.heuristicValue(), 0.0001);
	}
}
