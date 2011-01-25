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
package piramide.interaction.reasoner.web.client;

import piramide.interaction.reasoner.web.shared.Capabilities;
import piramide.interaction.reasoner.web.shared.VariableTrendValues;
import piramide.interaction.reasoner.web.shared.DeviceNameSuggestion;
import piramide.interaction.reasoner.web.shared.model.CompilingConfiguration;
import piramide.interaction.reasoner.web.shared.model.FuzzyConfiguration;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

public interface ReasonerManagerAsync {
	void searchDeviceID(SuggestOracle.Request request, AsyncCallback<SuggestOracle.Response> callback) throws IllegalArgumentException;
	
	void retrieveCapabilities(AsyncCallback<Capabilities> callback);
	
	void retrieveDeviceID(String deviceID, AsyncCallback<DeviceNameSuggestion> callback) throws IllegalArgumentException;

	void retrieveData(CompilingConfiguration compilingConfiguration, FuzzyConfiguration fuzzyConfiguration,
			AsyncCallback<VariableTrendValues> callback);

	void retrieveGeolocationRegions(AsyncCallback<String[]> callback);
}
