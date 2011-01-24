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

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import piramide.interaction.reasoner.creator.Point;

@SuppressWarnings("boxing")
public class RegionTest {

	@Test
	public void testCalculateOriginDestination_1region() {
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);
		
		final Universe universe = new Universe(values2trends, "a", "b");
		
		assertEquals(1, universe.getRegions().length);
		final Region region = universe.getRegions()[0];
		
		assertEquals(0.0, region.calculateOrigin(), 0.000001);
		assertEquals(3.0, region.calculateDestination(), 0.000001);
	}
	
	
	@Test
	public void testCalculateOriginDestination_2regions() {
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);
		
		final Universe universe = new Universe(values2trends, "a", "b", "c");
		
		assertEquals(2, universe.getRegions().length);
		final Region region1 = universe.getRegions()[0];
		assertEquals(0.0 , region1.calculateOrigin(), 0.000001);
		assertEquals(1.5 , region1.calculateDestination(), 0.000001);
		final Region region2 = universe.getRegions()[1];
		assertEquals(1.5 , region2.calculateOrigin(), 0.000001);
		assertEquals(3.0 , region2.calculateDestination(), 0.000001);
		
	}

	@Test
	public void testCalculateOriginDestination_2regions_2() {
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 3.0);
		values2trends.put(1.0, 1.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);
		
		final Universe universe = new Universe(values2trends, "a", "b", "c");
		
		assertEquals(2, universe.getRegions().length);
		final Region region1 = universe.getRegions()[0];
		assertEquals(0.0 , region1.calculateOrigin(), 0.000001);
		assertEquals(4.0 / 3, region1.calculateDestination(), 0.000001);
		final Region region2 = universe.getRegions()[1];
		assertEquals(4.0 / 3 , region2.calculateOrigin(), 0.000001);
		assertEquals(3.0 , region2.calculateDestination(), 0.000001);
		
	}

	@Test
	public void testCalculateOriginDestination_2regions_3() {
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 1.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 3.0);
		values2trends.put(3.0, 4.0);
		
		final Universe universe = new Universe(values2trends, "a", "b", "c");
		
		assertEquals(2, universe.getRegions().length);
		final Region region1 = universe.getRegions()[0];
		assertEquals(0.0, region1.calculateOrigin(), 0.000001);
		assertEquals(2.0, region1.calculateDestination(), 0.000001);
		final Region region2 = universe.getRegions()[1];
		assertEquals(2.0, region2.calculateOrigin(), 0.000001);
		assertEquals(3.0, region2.calculateDestination(), 0.000001);
		
	}
	
	@Test
	public void testCalculateOriginDestination_2regions_4() {
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(3.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(4.0, 2.0);
		values2trends.put(5.0, 2.0);
		
		final Universe universe = new Universe(values2trends, "a", "b", "c");
		
		assertEquals(2, universe.getRegions().length);
		final Region region1 = universe.getRegions()[0];
		assertEquals(0.0 , region1.calculateOrigin(), 0.000001);
		assertEquals(3.5 , region1.calculateDestination(), 0.000001);
		final Region region2 = universe.getRegions()[1];
		assertEquals(3.5 , region2.calculateOrigin(), 0.000001);
		assertEquals(5.0 , region2.calculateDestination(), 0.000001);
		
	}	
	
	@Test
	public void testGetAscendingPoints_1region() {
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);
		
		final Universe universe = new Universe(values2trends, "a", "b");
		
		assertEquals(1, universe.getRegions().length);
		final Region region = universe.getRegions()[0];
		
		assertEquals(0.0, region.calculateOrigin(), 0.000001);
		assertEquals(3.0, region.calculateDestination(), 0.000001);
		
		final Point[] points = region.getAscendingPoints();
		assertEquals(4, points.length);
		assertEquals(0.0, points[0].getValue(), 0.00001);
		assertEquals(0.0, points[0].getTrend(), 0.00001);

		assertEquals(1.0, points[1].getValue(), 0.00001);
		assertEquals(1.0 / 3, points[1].getTrend(), 0.00001);
		
		assertEquals(2.0, points[2].getValue(), 0.00001);
		assertEquals(2.0 / 3, points[2].getTrend(), 0.00001);
		
		assertEquals(3.0, points[3].getValue(), 0.00001);
		assertEquals(1.0, points[3].getTrend(), 0.00001);
	}

	@Test
	public void testGetAscendingPoints_1region_2() {
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);
		
		final Universe universe = new Universe(values2trends, "a", "b", "c");
		
		assertEquals(2, universe.getRegions().length);
		final Region region1 = universe.getRegions()[0];
		
		assertEquals(0.0, region1.calculateOrigin(), 0.000001);
		assertEquals(1.5, region1.calculateDestination(), 0.000001);
		
		final Point[] points1 = region1.getAscendingPoints();
		assertEquals(3, points1.length);
		assertEquals(0.0, points1[0].getValue(), 0.00001);
		assertEquals(0.0, points1[0].getTrend(), 0.00001);

		assertEquals(1.0, points1[1].getValue(), 0.00001);
		assertEquals(1.0, points1[1].getTrend(), 0.00001);
		
		assertEquals(1.5, points1[2].getValue(), 0.00001);
		assertEquals(1.0, points1[2].getTrend(), 0.00001);
		
		final Region region2 = universe.getRegions()[1];
		
		assertEquals(1.5, region2.calculateOrigin(), 0.000001);
		assertEquals(3.0, region2.calculateDestination(), 0.000001);
		
		final Point[] points2 = region2.getAscendingPoints();
		assertEquals(3, points2.length);
		assertEquals(1.5, points2[0].getValue(), 0.00001);
		assertEquals(0.0, points2[0].getTrend(), 0.00001);

		assertEquals(2.0, points2[1].getValue(), 0.00001);
		assertEquals(0.0, points2[1].getTrend(), 0.00001);
		
		assertEquals(3.0, points2[2].getValue(), 0.00001);
		assertEquals(1.0, points2[2].getTrend(), 0.00001);
	}

	@Test
	public void testGetDescendingPoints_1region_1() {
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);
		
		final Universe universe = new Universe(values2trends, "a", "b", "c");
		
		assertEquals(2, universe.getRegions().length);
		final Region region1 = universe.getRegions()[0];
		
		assertEquals(0.0, region1.calculateOrigin(), 0.000001);
		assertEquals(1.5, region1.calculateDestination(), 0.000001);
		
		final Point[] points1 = region1.getDescendingPoints();
		assertEquals(3, points1.length);
		assertEquals(0.0, points1[0].getValue(), 0.00001);
		assertEquals(1.0, points1[0].getTrend(), 0.00001);

		assertEquals(1.0, points1[1].getValue(), 0.00001);
		assertEquals(0.0, points1[1].getTrend(), 0.00001);
		
		assertEquals(1.5, points1[2].getValue(), 0.00001);
		assertEquals(0.0, points1[2].getTrend(), 0.00001);
		
		final Region region2 = universe.getRegions()[1];
		
		assertEquals(1.5, region2.calculateOrigin(), 0.000001);
		assertEquals(3.0, region2.calculateDestination(), 0.000001);
		
		final Point[] points2 = region2.getDescendingPoints();
		assertEquals(3, points2.length);
		assertEquals(1.5, points2[0].getValue(), 0.00001);
		assertEquals(1.0, points2[0].getTrend(), 0.00001);

		assertEquals(2.0, points2[1].getValue(), 0.00001);
		assertEquals(1.0, points2[1].getTrend(), 0.00001);
		
		assertEquals(3.0, points2[2].getValue(), 0.00001);
		assertEquals(0.0, points2[2].getTrend(), 0.00001);
	}

}
