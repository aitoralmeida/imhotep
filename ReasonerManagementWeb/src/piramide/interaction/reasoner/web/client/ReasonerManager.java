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
import piramide.interaction.reasoner.web.shared.ReasonerManagerException;
import piramide.interaction.reasoner.web.shared.VariableTrendValues;
import piramide.interaction.reasoner.web.shared.DeviceNameSuggestion;
import piramide.interaction.reasoner.web.shared.model.CompilingConfiguration;
import piramide.interaction.reasoner.web.shared.model.FuzzyConfiguration;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("reasoner")
public interface ReasonerManager extends RemoteService {
	
	SuggestOracle.Response searchDeviceID(SuggestOracle.Request request) throws IllegalArgumentException;
	DeviceNameSuggestion retrieveDeviceID(String deviceID) throws IllegalArgumentException;
	
	Capabilities retrieveCapabilities();
	String [] retrieveGeolocationRegions();
	
	VariableTrendValues retrieveData(CompilingConfiguration compilingConfiguration, FuzzyConfiguration fuzzyConfiguration) throws ReasonerManagerException;
}
