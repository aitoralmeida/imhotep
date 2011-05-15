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
package piramide.interaction.reasoner;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import piramide.interaction.reasoner.creator.WarningStore;
import piramide.interaction.reasoner.db.MobileDevice;
import piramide.interaction.reasoner.db.decay.DecayFunctionFactory.DecayFunctions;

public interface IFuzzyReasonerWizardFacade {
	public void initializeCacheData();
	
	public List<MobileDevice> searchDeviceNames(String query, int max) throws FuzzyReasonerException;
	
	public MobileDevice retrieveDeviceID(String request) throws FuzzyReasonerException;
	
	public void generateMembershipFunctionGraph(boolean inputOutput, boolean devicesUsers, String variableName, RegionDistributionInfo [] linguisticTerms, OutputStream destination, int width, int height, Geolocation geo, DecayFunctions decayFunction) throws FuzzyReasonerException;
	
	public String [] getGeolocationRegions() throws FuzzyReasonerException;
	
	public FuzzyInferredResult getInferredValues(String deviceName, WarningStore warningStore, Map<String, Object> initialCapabilities, Map<String, RegionDistributionInfo[]> inputVariables, Geolocation geo, DecayFunctions decayFunction, Map<String, RegionDistributionInfo[]> outputVariables, String rules) throws FuzzyReasonerException;
}
