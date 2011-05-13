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

import java.util.Map;

import piramide.interaction.reasoner.creator.WarningStore;

public interface IFuzzyReasoner {
	
	/**
	 * Given initial capabilities, the fuzzy reasoner generates new capabilities. Example:
	 * if the initial capabilities are "resolution.width", "resolution.height", "display.width"
	 * and "display.height", with numeric values, it will infer if the resolution is "high", "medium" 
	 * or "low", and it will also infer if the display is "high", "medium" or "low", and will also 
	 * return if "image" is "high" or "low" depending on both variables. Therefore, it will return
	 * a map with the new values "resolution", "display" and "image".
	 * 
	 * @param initialCapabilities the initial capabilities and the values
	 * @param inputVariables the linguistic terms used for each capability
	 * @return the inferred capabilities
	 */
	public Map<String, String> inferNewCapabilities(String deviceName, WarningStore warningStore, Map<String, Object> initialCapabilities, Map<String, RegionDistributionInfo[]> inputVariables) throws FuzzyReasonerException;
	
	/**
	 * Given initial capabilities, the fuzzy reasoner generates new capabilities. Example:
	 * if the initial capabilities are "resolution.width", "resolution.height", "display.width"
	 * and "display.height", with numeric values, it will infer if the resolution is "high", "medium" 
	 * or "low", and it will also infer if the display is "high", "medium" or "low", and will also 
	 * return if "image" is "high" or "low" depending on both variables. Therefore, it will return
	 * a map with the new values "resolution", "display" and "image". 
	 * 
	 * It will take into account the geolocation of the user, so it will consider that a display 
	 * is big if the trends in that place say that it is big. If the geolocation used is not 
	 * supported, it will default to the global settings.
	 * 
	 * @param initialCapabilities the initial capabilities and the values
	 * @param inputVariables the linguistic terms used for each capability
	 * @param geo where will the application be downloaded
	 * @return the inferred capabilities
	 */
	public Map<String, String> inferNewCapabilities(String deviceName, WarningStore warningStore, Map<String, Object> initialCapabilities, Map<String, RegionDistributionInfo[]> inputVariables, Geolocation geo) throws FuzzyReasonerException;
	
	/**
	 * Given initial capabilities, the fuzzy reasoner generates new capabilities. Example:
	 * if the initial capabilities are "resolution.width", "resolution.height", "display.width"
	 * and "display.height", with numeric values, it will infer if the resolution is "high", "medium" 
	 * or "low", and it will also infer if the display is "high", "medium" or "low", and will also 
	 * return if "image" is "high" or "low" depending on both variables. Therefore, it will return
	 * a map with the new values "resolution", "display" and "image". 
	 * 
	 * It will support a set of rules, created using the FCL format of jfuzzylogic, so it can 
	 * infer the output variables.
	 * 
	 * @param initialCapabilities the initial capabilities and the values
	 * @param inputVariables the linguistic terms used for each capability
	 * @param rules the set of rules in FCL format
	 * @return the inferred capabilities
	 */
	public Map<String, String> inferNewCapabilities(String deviceName, WarningStore warningStore, Map<String, Object> initialCapabilities, Map<String, RegionDistributionInfo[]> inputVariables, Map<String, RegionDistributionInfo[]> outputVariables, String rules) throws FuzzyReasonerException;
	
	/**
	 * Given initial capabilities, the fuzzy reasoner generates new capabilities. Example:
	 * if the initial capabilities are "resolution.width", "resolution.height", "display.width"
	 * and "display.height", with numeric values, it will infer if the resolution is "high", "medium" 
	 * or "low", and it will also infer if the display is "high", "medium" or "low", and will also 
	 * return if "image" is "high" or "low" depending on both variables. Therefore, it will return
	 * a map with the new values "resolution", "display" and "image". 
	 * 
	 * It will take into account the geolocation of the user, so it will consider that a display 
	 * is big if the trends in that place say that it is big. If the geolocation used is not 
	 * supported, it will default to the global settings.
	 * 
	 * It will also support a set of rules, created using the FCL format of jfuzzylogic, so it can 
	 * infer the output variables.
	 * 
	 * @param initialCapabilities the initial capabilities and the values
	 * @param inputVariables the linguistic terms used for each capability
	 * @param geo where will the application be downloaded
	 * @param rules the set of rules in FCL format
	 * @return the inferred capabilities
	 */
	public Map<String, String> inferNewCapabilities(String deviceName, WarningStore warningStore, Map<String, Object> initialCapabilities, Map<String, RegionDistributionInfo[]> inputVariables, Geolocation geo, Map<String, RegionDistributionInfo[]> outputVariables, String rules) throws FuzzyReasonerException;
}
