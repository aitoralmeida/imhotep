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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import piramide.interaction.reasoner.creator.membershipgenerator.regionaccumulator.AbstractRegion;

@SuppressWarnings("boxing")
public class AbstractRegionTest {

	@Test
	public void testGetResults2trends() {
		// Same data as MobileDevicesTest
		final double expectedTrend = 10 * 0.05 + 12 * 0.1 + 16 * 0.1 + 17 * 0.4 + 15 * 0.9 + 20 * 1.0 + 30 * 1.0 + 10 * 1.0;
		final Map<Number, Double> values2trends = new HashMap<Number, Double>();
		values2trends.put(10000, 10.0 + 10.0);
		values2trends.put(96000, expectedTrend);
		
		final AbstractRegion region = new AbstractRegion(values2trends) {};
		
		assertEquals(expectedTrend + 10.0 + 10.0, region.sumOfValues, 0.001);
		
		assertEquals(10000, region.orderedKeys.get(0));
		assertEquals(96000, region.orderedKeys.get(1));
		assertEquals(2, region.orderedKeys.size());
	}

}
