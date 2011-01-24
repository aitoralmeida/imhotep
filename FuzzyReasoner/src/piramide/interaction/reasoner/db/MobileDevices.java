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

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobileDevices implements Serializable {
	
	private static final long serialVersionUID = 6415573891726956486L;
	
	private final List<MobileDevice> mobileDevices;
	
	public MobileDevices(List<MobileDevice> mobileDevices){
		this.mobileDevices = mobileDevices;
	}

	public List<MobileDevice> getMobileDevices() {
		return this.mobileDevices;
	}
	
	public Map<Number, Double> getValue2trend(DeviceCapability capability){
		final Map<Number, Double> value2trend = new HashMap<Number, Double>();
		
		for(MobileDevice device : this.mobileDevices){
			final Number value = device.getCapabilityValue(capability);
			if(value2trend.containsKey(value)){
				final double previousTrend = value2trend.get(value).doubleValue();
				value2trend.put(value, Double.valueOf(previousTrend + device.getDecayedTrend()));
			}else
				value2trend.put(value, Double.valueOf(device.getDecayedTrend()));
		}
		
		return value2trend;
	}
}
