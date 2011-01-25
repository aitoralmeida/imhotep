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
package piramide.interaction.reasoner.web.shared;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public final class DeviceNameSuggestion implements Suggestion, IsSerializable {
	
	private String replacement;
	private String display;
	private HashMap<String, Number> capabilities;
	private double decayedTrend;
	private double totalTrend;
	
	public DeviceNameSuggestion(){
		this.replacement = "";
		this.display = "";
		this.capabilities = new HashMap<String, Number>();
		this.decayedTrend = 0.0;
		this.totalTrend = 0.0;
	}
	
	public DeviceNameSuggestion(String replacement, String display, HashMap<String, Number> capabilities, double decayedTrend, double totalTrend){
		this.replacement  = replacement;
		this.display      = display;
		this.capabilities = capabilities;
		this.decayedTrend = decayedTrend;
		this.totalTrend   = totalTrend;
	}
	
	public Map<String, Number> getCapabilities() {
		return this.capabilities;
	}

	public double getDecayedTrend() {
		return this.decayedTrend;
	}

	public double getTotalTrend() {
		return this.totalTrend;
	}

	@Override
	public String getReplacementString() {
		return this.replacement;
	}

	@Override
	public String getDisplayString() {
		return this.display;
	}
}