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

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import piramide.interaction.reasoner.creator.membershipgenerator.regionaccumulator.Boundary;
import piramide.interaction.reasoner.creator.membershipgenerator.regionaccumulator.RegionAccumulatorMembershipFunctionGenerator;
import piramide.interaction.reasoner.creator.membershipgenerator.regionaccumulator.Subregion;
import piramide.interaction.reasoner.db.DeviceCapability;
import piramide.interaction.reasoner.db.IDatabaseManager;
import piramide.interaction.reasoner.db.MobileDevices;
import piramide.interaction.reasoner.db.WrappedDatabaseManager;

@SuppressWarnings("boxing")
public class MembershipFunctionGeneratorTest {
	
	
	@Test
	public void test2subregions() throws Exception {
		// 3 boundaries => 2 subregions
		final int BOUNDARIES = 3;
		final int SUBREGIONS = BOUNDARIES - 1;
		
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);
		
		final RegionAccumulatorMembershipFunctionGenerator generator = new RegionAccumulatorMembershipFunctionGenerator(values2trends);
		final List<Subregion> regions = generator.createSubregions(BOUNDARIES);
		
		assertEquals(SUBREGIONS, regions.size());
		
		final Subregion subregion1 = regions.get(0);
		assertEquals(2, subregion1.orderedKeys.size());
		assertEquals(0.0, subregion1.orderedKeys.get(0));
		assertEquals(1.0, subregion1.orderedKeys.get(1));
		
		final Subregion subregion2 = regions.get(1);
		assertEquals(2, subregion2.orderedKeys.size());
		assertEquals(2.0, subregion2.orderedKeys.get(0));
		assertEquals(3.0, subregion2.orderedKeys.get(1));
	}
	
//	@Test
//	public void test3subregions() throws Exception {
//		// 4 boundaries => 3 subregions
//		final int BOUNDARIES = 4;
//		final int SUBREGIONS = BOUNDARIES - 1;
//		
//		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
//		values2trends.put(0.0, 2.0);
//		values2trends.put(1.0, 2.0);
//		values2trends.put(2.0, 2.0);
//		values2trends.put(3.0, 2.0);
//		
//		final RegionAccumulatorMembershipFunctionGenerator generator = new RegionAccumulatorMembershipFunctionGenerator(values2trends);
//		final List<Subregion> regions = generator.createSubregions(BOUNDARIES);
//		
//		assertEquals(SUBREGIONS, regions.size());
//		
//		final Subregion subregion1 = regions.get(0);
//		assertEquals(2, subregion1.orderedKeys.size());
//		assertEquals(0.0, subregion1.orderedKeys.get(0));
//		assertEquals(1.0, subregion1.orderedKeys.get(1));
//		
//		final Subregion subregion2 = regions.get(1);
//		assertEquals(2, subregion2.orderedKeys.size());
//		assertEquals(1.0, subregion2.orderedKeys.get(0));
//		assertEquals(2.0, subregion2.orderedKeys.get(1));
//		
//		final Subregion subregion3 = regions.get(2);
//		assertEquals(2, subregion3.orderedKeys.size());
//		assertEquals(2.0, subregion3.orderedKeys.get(0));
//		assertEquals(3.0, subregion3.orderedKeys.get(1));
//	}
	
//	@Test
//	public void testSubregions() throws Exception {
//		// 4 boundaries => 3 subregions
//		final int BOUNDARIES = 6;
//		final int SUBREGIONS = BOUNDARIES - 1;
//		
//		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
//		values2trends.put(0.0,  10.0);
//		values2trends.put(6.0,  27.0);
//		values2trends.put(8.0,  12.0);
//		values2trends.put(10.0, 99.0);
//		
//		final MembershipFunctionGenerator generator = new MembershipFunctionGenerator(values2trends);
//		final List<Subregion> regions = generator.createSubregions(BOUNDARIES);
//		
//		assertEquals(SUBREGIONS, regions.size());
//		
//		final Subregion subregion1 = regions.get(0);
//		assertEquals(0.0, subregion1.orderedKeys.get(0));
//		assertEquals(2, subregion1.orderedKeys.size());
//		assertEquals(6.0, subregion1.orderedKeys.get(1));
//	}

    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
	/////////////////  TESTING CUT     /////////////////
    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
	
	@Test
	public void testCalculateBoundaries() throws Exception {
		final IDatabaseManager db = new WrappedDatabaseManager();
		final MobileDevices mobileDevices = db.getResults();
		final Map<Number, Double> values2trends = mobileDevices.getValue2trend(DeviceCapability.reso_size);
		
		// 1 boundaries => 0 subregions
		final int BOUNDARIES = 3;
		
		final RegionAccumulatorMembershipFunctionGenerator generator = new RegionAccumulatorMembershipFunctionGenerator(values2trends);
		final Boundary [] boundaries = generator.calculateBoundaries(BOUNDARIES);
		
		assertEquals(BOUNDARIES, boundaries.length);
	}	
	
	@Test
	public void testCalculateBoundariesInto1() throws Exception {
		// 1 boundaries => 0 subregions
		final int BOUNDARIES = 1;
		
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0,  10.0);
		values2trends.put(6.0,  27.0);
		values2trends.put(8.0,  12.0);
		values2trends.put(10.0, 99.0);
		
		final RegionAccumulatorMembershipFunctionGenerator generator = new RegionAccumulatorMembershipFunctionGenerator(values2trends);
		final Boundary [] boundaries = generator.calculateBoundaries(BOUNDARIES);
		
		assertEquals(BOUNDARIES, boundaries.length);
		assertEquals(0.0, boundaries[0].getBefore(), 0.01);
		assertEquals(0.0, boundaries[0].getAfter(), 0.01);
		
		assertEquals(0, boundaries[0].getPosBefore());
		assertEquals(0, boundaries[0].getPosAfter()); //TODO: this one might be modified in the future
	}
	
	@Test
	public void testCalculateBoundariesInto2() throws Exception {
		// 2 boundaries => 1 subregions
		final int BOUNDARIES = 2;
		
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);
		
		final RegionAccumulatorMembershipFunctionGenerator generator = new RegionAccumulatorMembershipFunctionGenerator(values2trends);
		final Boundary [] boundaries = generator.calculateBoundaries(BOUNDARIES);
		
		assertEquals(BOUNDARIES, boundaries.length);
		
		assertEquals(0.0, boundaries[0].getBefore(), 0.01);
		assertEquals(0.0, boundaries[0].getAfter(), 0.01);		
		assertEquals(0, boundaries[0].getPosBefore());
		assertEquals(0, boundaries[0].getPosAfter());
		
		assertEquals(3.0, boundaries[1].getBefore(), 0.01);
		assertEquals(3.0, boundaries[1].getAfter(), 0.01);		
		assertEquals(3, boundaries[1].getPosBefore());
		assertEquals(3, boundaries[1].getPosAfter());
	}
	
	@Test
	public void testCalculateBoundariesInto3() throws Exception {
		// 3 boundaries => 2 subregions
		final int BOUNDARIES = 3;
		
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0, 2.0);
		values2trends.put(1.0, 2.0);
		values2trends.put(2.0, 2.0);
		values2trends.put(3.0, 2.0);
		
		final RegionAccumulatorMembershipFunctionGenerator generator = new RegionAccumulatorMembershipFunctionGenerator(values2trends);
		final Boundary [] boundaries = generator.calculateBoundaries(BOUNDARIES);
		
		assertEquals(BOUNDARIES, boundaries.length);
		
		assertEquals(0.0, boundaries[0].getBefore(), 0.01);
		assertEquals(0.0, boundaries[0].getAfter(), 0.01);		
		assertEquals(0, boundaries[0].getPosBefore());
		assertEquals(0, boundaries[0].getPosAfter());
		
		assertEquals(1.0, boundaries[1].getBefore(), 0.01);
		assertEquals(2.0, boundaries[1].getAfter(), 0.01);		
		assertEquals(1, boundaries[1].getPosBefore());
		assertEquals(2, boundaries[1].getPosAfter());
		
		assertEquals(3.0, boundaries[2].getBefore(), 0.01);
		assertEquals(3.0, boundaries[2].getAfter(), 0.01);		
		assertEquals(3, boundaries[2].getPosBefore());
		assertEquals(3, boundaries[2].getPosAfter());
	}
	
	@Test
	public void testCalculateBoundariesInto3real() throws Exception {
		// 3 boundaries => 2 subregions
		final int BOUNDARIES = 3;
		
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0,  10.0);
		values2trends.put(6.0,  27.0);
		values2trends.put(8.0,  12.0);
		values2trends.put(10.0, 99.0);
		
		final RegionAccumulatorMembershipFunctionGenerator generator = new RegionAccumulatorMembershipFunctionGenerator(values2trends);
		final Boundary [] boundaries = generator.calculateBoundaries(BOUNDARIES);
		
		assertEquals(BOUNDARIES, boundaries.length);
		
		assertEquals(0.0, boundaries[0].getBefore(), 0.01);
		assertEquals(0.0, boundaries[0].getAfter(), 0.01);		
		assertEquals(0, boundaries[0].getPosBefore());
		assertEquals(0, boundaries[0].getPosAfter());
		
		assertEquals(8.0, boundaries[1].getBefore(), 0.01);
		assertEquals(10.0, boundaries[1].getAfter(), 0.01);		
		assertEquals(2, boundaries[1].getPosBefore());
		assertEquals(3, boundaries[1].getPosAfter());
		
		assertEquals(10.0, boundaries[2].getBefore(), 0.01);
		assertEquals(10.0, boundaries[2].getAfter(), 0.01);		
		assertEquals(3, boundaries[2].getPosBefore());
		assertEquals(3, boundaries[2].getPosAfter());
	}
	
//	@Test
//	public void testCalculateBoundariesInto4() throws Exception {
//		// 4 boundaries => 3 subregions
//		final int BOUNDARIES = 4;
//		
//		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
//		values2trends.put(0.0, 2.0);
//		values2trends.put(1.0, 2.0);
//		values2trends.put(2.0, 2.0);
//		values2trends.put(3.0, 2.0);
//		
//		final RegionAccumulatorMembershipFunctionGenerator generator = new RegionAccumulatorMembershipFunctionGenerator(values2trends);
//		final Boundary [] boundaries = generator.calculateBoundaries(BOUNDARIES);
//		
//		assertEquals(BOUNDARIES, boundaries.length);
//		
//		assertEquals(0.0, boundaries[0].getBefore(), 0.01);
//		assertEquals(0.0, boundaries[0].getAfter(), 0.01);		
//		assertEquals(0, boundaries[0].getPosBefore());
//		assertEquals(0, boundaries[0].getPosAfter());
//		
//		assertEquals(1.0, boundaries[1].getBefore(), 0.01);
//		assertEquals(1.0, boundaries[1].getAfter(), 0.01);		
//		assertEquals(1, boundaries[1].getPosBefore());
//		assertEquals(1, boundaries[1].getPosAfter());
//		
//		assertEquals(2.0, boundaries[2].getBefore(), 0.01);
//		assertEquals(2.0, boundaries[2].getAfter(), 0.01);		
//		assertEquals(2, boundaries[2].getPosBefore());
//		assertEquals(2, boundaries[2].getPosAfter());
//		
//		assertEquals(3.0, boundaries[3].getBefore(), 0.01);
//		assertEquals(3.0, boundaries[3].getAfter(), 0.01);		
//		assertEquals(3, boundaries[3].getPosBefore());
//		assertEquals(3, boundaries[3].getPosAfter());
//	}	
	
	@Test
	public void testCalculateBoundariesInto4real() throws Exception {
		// 4 boundaries => 3 subregions
		final int BOUNDARIES = 4;
		
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0,  10.0);
		values2trends.put(6.0,  27.0);
		values2trends.put(8.0,  12.0);
		values2trends.put(10.0, 99.0);
		
		final RegionAccumulatorMembershipFunctionGenerator generator = new RegionAccumulatorMembershipFunctionGenerator(values2trends);
		final Boundary [] boundaries = generator.calculateBoundaries(BOUNDARIES);
		
		assertEquals(BOUNDARIES, boundaries.length);
		
		assertEquals(0.0, boundaries[0].getBefore(), 0.01);
		assertEquals(0.0, boundaries[0].getAfter(), 0.01);		
		assertEquals(0, boundaries[0].getPosBefore());
		assertEquals(0, boundaries[0].getPosAfter());
		
		assertEquals(8.0, boundaries[1].getBefore(), 0.01);
		assertEquals(10.0, boundaries[1].getAfter(), 0.01);		
		assertEquals(2, boundaries[1].getPosBefore());
		assertEquals(3, boundaries[1].getPosAfter());
		
		assertEquals(8.0, boundaries[2].getBefore(), 0.01);
		assertEquals(10.0, boundaries[2].getAfter(), 0.01);		
		assertEquals(2, boundaries[2].getPosBefore());
		assertEquals(3, boundaries[2].getPosAfter());
		
		assertEquals(10.0, boundaries[3].getBefore(), 0.01);
		assertEquals(10.0, boundaries[3].getAfter(), 0.01);		
		assertEquals(3, boundaries[3].getPosBefore());
		assertEquals(3, boundaries[3].getPosAfter());
	}
	
	@Test
	public void testCalculateBoundariesInto5real() throws Exception {
		// 5 boundaries => 4 subregions
		final int BOUNDARIES = 5;
		
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0,  10.0);
		values2trends.put(6.0,  27.0);
		values2trends.put(8.0,  12.0);
		values2trends.put(10.0, 99.0);
		
		final RegionAccumulatorMembershipFunctionGenerator generator = new RegionAccumulatorMembershipFunctionGenerator(values2trends);
		final Boundary [] boundaries = generator.calculateBoundaries(BOUNDARIES);
		
		assertEquals(BOUNDARIES, boundaries.length);
		
		assertEquals(0.0, boundaries[0].getBefore(), 0.01);
		assertEquals(0.0, boundaries[0].getAfter(), 0.01);		
		assertEquals(0, boundaries[0].getPosBefore());
		assertEquals(0, boundaries[0].getPosAfter());
		
		assertEquals(6.0, boundaries[1].getBefore(), 0.01);
		assertEquals(8.0, boundaries[1].getAfter(), 0.01);		
		assertEquals(1, boundaries[1].getPosBefore());
		assertEquals(2, boundaries[1].getPosAfter());
		
		assertEquals(8.0, boundaries[2].getBefore(), 0.01);
		assertEquals(10.0, boundaries[2].getAfter(), 0.01);		
		assertEquals(2, boundaries[2].getPosBefore());
		assertEquals(3, boundaries[2].getPosAfter());
		
		assertEquals(8.0, boundaries[3].getBefore(), 0.01);
		assertEquals(10.0, boundaries[3].getAfter(), 0.01);		
		assertEquals(2, boundaries[3].getPosBefore());
		assertEquals(3, boundaries[3].getPosAfter());
		
		assertEquals(10.0, boundaries[4].getBefore(), 0.01);
		assertEquals(10.0, boundaries[4].getAfter(), 0.01);		
		assertEquals(3, boundaries[4].getPosBefore());
		assertEquals(3, boundaries[4].getPosAfter());
	}
	
	@Test
	public void testCalculateBoundariesInto6real() throws Exception {
		// 6 boundaries => 5 subregions
		final int BOUNDARIES = 6;
		
		final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
		values2trends.put(0.0,  10.0);
		values2trends.put(6.0,  27.0);
		values2trends.put(8.0,  12.0);
		values2trends.put(10.0, 99.0);
		
		final RegionAccumulatorMembershipFunctionGenerator generator = new RegionAccumulatorMembershipFunctionGenerator(values2trends);
		final Boundary [] boundaries = generator.calculateBoundaries(BOUNDARIES);
		
		assertEquals(BOUNDARIES, boundaries.length);
		
		assertEquals(0.0, boundaries[0].getBefore(), 0.01);
		assertEquals(0.0, boundaries[0].getAfter(), 0.01);		
		assertEquals(0, boundaries[0].getPosBefore());
		assertEquals(0, boundaries[0].getPosAfter());
		
		assertEquals(0.0, boundaries[1].getBefore(), 0.01);
		assertEquals(6.0, boundaries[1].getAfter(), 0.01);		
		assertEquals(0, boundaries[1].getPosBefore());
		assertEquals(1, boundaries[1].getPosAfter());
		
		assertEquals(8.0, boundaries[2].getBefore(), 0.01);
		assertEquals(10.0, boundaries[2].getAfter(), 0.01);		
		assertEquals(2, boundaries[2].getPosBefore());
		assertEquals(3, boundaries[2].getPosAfter());
		
		assertEquals(8.0, boundaries[3].getBefore(), 0.01);
		assertEquals(10.0, boundaries[3].getAfter(), 0.01);		
		assertEquals(2, boundaries[3].getPosBefore());
		assertEquals(3, boundaries[3].getPosAfter());
		
		assertEquals(8.0, boundaries[4].getBefore(), 0.01);
		assertEquals(10.0, boundaries[4].getAfter(), 0.01);		
		assertEquals(2, boundaries[4].getPosBefore());
		assertEquals(3, boundaries[4].getPosAfter());
		
		assertEquals(10.0, boundaries[5].getBefore(), 0.01);
		assertEquals(10.0, boundaries[5].getAfter(), 0.01);		
		assertEquals(3, boundaries[5].getPosBefore());
		assertEquals(3, boundaries[5].getPosAfter());
	}
}
