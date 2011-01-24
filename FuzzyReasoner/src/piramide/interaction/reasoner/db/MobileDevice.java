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
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MobileDevice implements Serializable {
	
	private static final long serialVersionUID = -2495118951093453957L;
	
	private final String name;
	private final Map<DeviceCapability, Number> capabilities;
	private final List<Trend> trends;
	private final QueryInformation queryInformation;
	
	
	public MobileDevice(String name, Map<DeviceCapability, Number> capabilities, List<Trend> trends, QueryInformation queryInformation) {
		super();
		this.name = name;
		this.capabilities = capabilities;
		this.trends = trends;
		this.queryInformation = queryInformation;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return "<MobileDevices name ='" + this.name + "'/>";
	}
	
	public List<Trend> getAllTrends() {
		return this.trends;
	}
	
	List<Trend> getValidTrends() {
		final List<Trend> validTrends = new Vector<Trend>();
		final int maxDate = this.queryInformation.getMaxYear() * 12 + this.queryInformation.getMaxMonth();
		
		for(Trend trend : this.trends){
			final int currentDate = trend.getYear() * 12 + trend.getMonth();
			if(currentDate <= maxDate)
				validTrends.add(trend);
		}
		
		return validTrends;
	}
	
	public Map<DeviceCapability, Number> getCapabilities() {
		return this.capabilities;
	}
	
	public Number getCapabilityValue(DeviceCapability capability){
		return this.capabilities.get(capability);
	}

	public double getTotalTrend (){
		double totalTrend = 0.0;
		
		for (Trend trend : this.trends)
			totalTrend = totalTrend + trend.getValue();
		
		return totalTrend;
	}
	
	public double getDecayedTrend(){
		double total = 0.0;
		
		List<Trend> trends = calculateDecay();
		
		for(Trend trend : trends)
			total += trend.getValue();
		
		return total;
	}
	
	List<Trend> calculateDecay () {
		final Calendar cal = CalendarFactory.now();  
		final int actualMonth = cal.get(Calendar.MONTH);
		final int actualYear = cal.get(Calendar.YEAR);
		
		final int actualMonths = ((actualYear - 1) * 12) + actualMonth;
		final Vector<Trend> trends = new Vector<Trend>();
		for (Trend trend : this.getValidTrends()) {
			final int trendMonths = ((trend.getYear() -1) * 12) + trend.getMonth();
			final int monthsPassed = actualMonths - trendMonths;
			final double decay = getDecay(monthsPassed);
			final Trend t = new Trend(trend.getYear(), trend.getMonth(), trend.getValue() * decay);
			trends.add(t);
		}
		
		return trends;
	}	
	
	private double getDecay(int monthsPassed) {
		double decay;
		if (monthsPassed <= 15){  //1st year
			decay = 1.0;
		} else if (monthsPassed > 15 & monthsPassed <= 24){ // 2nd sear
			decay = 0.9;
		} else if (monthsPassed > 24 & monthsPassed <= 36){ //3rd year
			decay = 0.4;
		} else if (monthsPassed > 36 & monthsPassed <= 60){ //4rd and 5th year
			decay = 0.1;
		}else{ //the rest
			decay = 0.05;
		}
		return decay;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.capabilities == null) ? 0 : this.capabilities.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.trends == null) ? 0 : this.trends.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MobileDevice other = (MobileDevice) obj;
		if (this.capabilities == null) {
			if (other.capabilities != null)
				return false;
		} else if (!this.capabilities.equals(other.capabilities))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		if (this.trends == null) {
			if (other.trends != null)
				return false;
		} else if (!this.trends.equals(other.trends))
			return false;
		return true;
	}
}
