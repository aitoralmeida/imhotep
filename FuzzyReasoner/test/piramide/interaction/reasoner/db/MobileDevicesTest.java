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

import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("boxing")
public class MobileDevicesTest {
	
	private MobileDevices mobileDevices;
	
	@Before
	public void setUp(){
		// "Today" is March 1, 2010
		final Calendar today = Calendar.getInstance();
		today.set(Calendar.YEAR, 2010);
		today.set(Calendar.MONTH, 3 - 1);
		today.set(Calendar.DAY_OF_MONTH, 1);
		CalendarFactory.setFakeCalendar(today);
		
		final MobileDevice device1 = MobileDeviceTest.createMobileDevice1();
		final MobileDevice device2 = MobileDeviceTest.createMobileDevice2();
		final MobileDevice device3 = MobileDeviceTest.createMobileDevice3();
		this.mobileDevices = new MobileDevices(Arrays.asList(device1, device2, device3));
	}
	
	@After
	public void tearDown(){
		CalendarFactory.returnToRealCalendar();
	}
	
	@Test
	public void testValue2trend(){
		final Map<Number, Double> value2trend = this.mobileDevices.getValue2trend(DeviceCapability.reso_size);
		assertEquals(2, value2trend.keySet().size());
		
		final double expectedTrend = 10 * 0.05 + 12 * 0.1 + 16 * 0.1 + 17 * 0.4 + 15 * 0.9 + 20 * 1.0 + 30 * 1.0 + 10 * 1.0;
		assertEquals(10.0 + 10.0, value2trend.get(10000), 0.001);
		assertEquals(expectedTrend, value2trend.get(96000), 0.001);
	}
}
