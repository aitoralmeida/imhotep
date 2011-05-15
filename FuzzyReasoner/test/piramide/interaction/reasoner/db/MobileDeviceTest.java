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
package piramide.interaction.reasoner.db;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import piramide.interaction.reasoner.db.decay.DecayFunctionFactory.DecayFunctions;

@SuppressWarnings("boxing")
public class MobileDeviceTest {

	private MobileDevice mobileDevice;
	
	@Before
	public void setUp(){
		// "Today" is March 1, 2010
		final Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, 2010);
		today.set(Calendar.MONTH, 3 - 1);
		today.set(Calendar.DAY_OF_MONTH, 1);
		CalendarFactory.setFakeCalendar(today);
		
		this.mobileDevice = createMobileDevice1(); 
	}

	static MobileDevice createMobileDevice1() {
		// htc touch diamond2
		final Map<DeviceCapability, Number> capabilities = new HashMap<DeviceCapability, Number>();
		capabilities.put(DeviceCapability.real_size, 4000);
		capabilities.put(DeviceCapability.reso_size, 96000);
		
		// Only take into account months <= December 2009
		final QueryInformation queryInformation = new QueryInformation();
		queryInformation.setMaxMonth(12);
		queryInformation.setMaxYear(2009);
		
		final List<Trend> trends = new Vector<Trend>();
		// A trend which is trend > 60
		trends.add(new Trend(2004, 2,  10));
		
		// A trend which is 60 >= trend > 36
		trends.add(new Trend(2005, 2,  12));
		
		// A trend which is 36 >= trend > 24
		trends.add(new Trend(2006, 2,  16));
		
		// A trend which is 36 >= trend > 24
		trends.add(new Trend(2007, 2,  17));
		
		// A trend which is 24 >= trend > 15
		trends.add(new Trend(2008, 6,  15));
		
		// Some trends of the last 15 months
		trends.add(new Trend(2009, 1,  20));
		trends.add(new Trend(2009, 6,  30));
		trends.add(new Trend(2009, 12, 10));
		
		// A trend after the queryInformation
		trends.add(new Trend(2010, 1, 40));
		
		return new MobileDevice("muzzy", capabilities, trends, queryInformation, DecayFunctions.model);
	}
	
	static MobileDevice createMobileDevice2() {
		final Map<DeviceCapability, Number> capabilities = new HashMap<DeviceCapability, Number>();
		capabilities.put(DeviceCapability.real_size, 5000);
		capabilities.put(DeviceCapability.reso_size, 10000);
		
		// Only take into account months <= December 2009
		final QueryInformation queryInformation = new QueryInformation();
		queryInformation.setMaxMonth(12);
		queryInformation.setMaxYear(2009);
		
		final List<Trend> trends = new Vector<Trend>();
		// A trend which is trend > 60
		trends.add(new Trend(2009, 12, 10));
		
		return new MobileDevice("muzzy2", capabilities, trends, queryInformation, DecayFunctions.model);
	}
		
	static MobileDevice createMobileDevice3() {
		final Map<DeviceCapability, Number> capabilities = new HashMap<DeviceCapability, Number>();
		capabilities.put(DeviceCapability.real_size, 5000);
		capabilities.put(DeviceCapability.reso_size, 10000);
		
		// Only take into account months <= December 2009
		final QueryInformation queryInformation = new QueryInformation();
		queryInformation.setMaxMonth(12);
		queryInformation.setMaxYear(2009);
		
		final List<Trend> trends = new Vector<Trend>();
		// A trend which is trend > 60
		trends.add(new Trend(2009, 12, 10));
		
		return new MobileDevice("muzzy2", capabilities, trends, queryInformation, DecayFunctions.model);
	}
		
	
	@After
	public void tearDown(){
		CalendarFactory.returnToRealCalendar();
	}
	
	@Test
	public void testGetValidTrends() {
		final int allSize = this.mobileDevice.getAllTrends().size();
		final int validTrendsSize = this.mobileDevice.getValidTrends().size();
		
		// There is 1 trend after what the queryInformation said
		assertEquals(allSize - 1, validTrendsSize);
	}

	@Test
	public void testCalculateDecayModel() {
		final List<Trend> calculatedTrends = this.mobileDevice.calculateDecay();
		
		assertEquals(8, calculatedTrends.size());
		
		assertEquals(new Trend(2004,  2, 10 * 0.05), calculatedTrends.get(0));
		assertEquals(new Trend(2005,  2, 12 * 0.1 ), calculatedTrends.get(1));
		assertEquals(new Trend(2006,  2, 16 * 0.1 ), calculatedTrends.get(2));
		assertEquals(new Trend(2007,  2, 17 * 0.4 ), calculatedTrends.get(3));
		assertEquals(new Trend(2008,  6, 15 * 0.9 ), calculatedTrends.get(4));
		assertEquals(new Trend(2009,  1, 20 * 1.0 ), calculatedTrends.get(5));
		assertEquals(new Trend(2009,  6, 30 * 1.0 ), calculatedTrends.get(6));
		assertEquals(new Trend(2009, 12, 10 * 1.0 ), calculatedTrends.get(7));
	}

	@Test
	public void testGetDecayedTrend() {
		final double expectedTrend = 10 * 0.05 + 12 * 0.1 + 16 * 0.1 + 17 * 0.4 + 15 * 0.9 + 20 * 1.0 + 30 * 1.0 + 10 * 1.0;
		final double actualTrend   = this.mobileDevice.getDecayedTrend();
		
		assertEquals(expectedTrend, actualTrend, 0.0001);
	}

}
