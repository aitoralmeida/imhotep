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

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sourceforge.jFuzzyLogic.FIS;
import piramide.interaction.reasoner.creator.FclCreator;
import piramide.interaction.reasoner.creator.InvalidSyntaxException;
import piramide.interaction.reasoner.creator.WarningStore;
import piramide.interaction.reasoner.db.CalendarFactory;
import piramide.interaction.reasoner.db.DatabaseException;
import piramide.interaction.reasoner.db.DatabaseManager;
import piramide.interaction.reasoner.db.DeviceCapability;
import piramide.interaction.reasoner.db.IDatabaseManager;
import piramide.interaction.reasoner.db.MobileDevice;
import piramide.interaction.reasoner.db.MobileDevices;
import piramide.interaction.reasoner.db.UserCapabilities.UserCapability;
import piramide.interaction.reasoner.db.decay.DecayFunctionFactory.DecayFunctions;
import piramide.interaction.reasoner.wizard.Variable;

public class FuzzyReasoner implements IFuzzyReasoner {

	private static final int TIMEOUT = 3600 * 1000;
	
	private final FclCreator creator = new FclCreator();
	private final IDatabaseManager databaseManager;
	private final CacheFactory<IDatabaseManager> cacheFactory = new CacheFactory<IDatabaseManager>(IDatabaseManager.class);
	
	public FuzzyReasoner() throws FuzzyReasonerException{
		// this.databaseManager = this.cacheFactory.create(new DatabaseManager(), TIMEOUT);
		this.databaseManager = new DatabaseManager();
	}
	
	IDatabaseManager getDatabaseManager(){
		return this.databaseManager;
	}
	
	@Override
	public Map<String, String> inferNewCapabilities(
			String deviceName, 
			WarningStore warningStore,
			Map<String, Object> initialCapabilities,
			Map<String, RegionDistributionInfo[]> inputVariables, Geolocation geo, DecayFunctions decayFunction, Calendar when,
			Map<String, RegionDistributionInfo[]> outputVariables,
			String rules) throws FuzzyReasonerException {

		// TODO: initialCapabilities is still used, and there are problems not considered:
		// - Is getResults() fast enough? Should a cache be used?
		 //TODO: HAcer test
		final FIS fis = generateFISobject(deviceName, warningStore,
				initialCapabilities, inputVariables, geo, decayFunction, when, outputVariables,
				rules);
		
		Map<String, String> inferred = new HashMap<String, String>();
		
		for (Iterator<String> iterator = outputVariables.keySet().iterator(); iterator.hasNext();) {
			final String variableName = iterator.next();
			final net.sourceforge.jFuzzyLogic.rule.Variable variable = fis.getVariable(variableName);
	        final String maxTerm = Collections.max(variable.getLinguisticTerms().keySet(), new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return Double.compare(variable.getMembership(o1), variable.getMembership(o2));
				}
			});
	        
	        inferred.put(variableName, maxTerm);
			
		}
				
		return inferred;
	}

	FIS generateFISobject(String deviceName, WarningStore warningStore,
			Map<String, Object> initialCapabilities,
			Map<String, RegionDistributionInfo[]> inputVariables, Geolocation geo, DecayFunctions decayFunction, Calendar when, 
			Map<String, RegionDistributionInfo[]> outputVariables, String rules)
			throws DatabaseException, InvalidSyntaxException,
			FuzzyReasonerException {
		final MobileDevices mobileDevices = this.databaseManager.getResults(geo, decayFunction, when);
		final MobileDevice device = this.getMobileDevice(mobileDevices, deviceName);		
		
		final String ruleFileContent = generateRuleFileContent(inputVariables, outputVariables, rules, mobileDevices, warningStore);
			
		final ByteArrayInputStream fclBytes = new ByteArrayInputStream(ruleFileContent.getBytes());
		final FIS fis = FIS.load(fclBytes, false);
		
		fillInputVariables(initialCapabilities, inputVariables.keySet(), device, fis);
		
		fis.evaluate();
		return fis;
	}

	private void fillInputVariables(Map<String, Object> initialCapabilities,
			final Set<String> inputVariables, final MobileDevice device,
			final FIS fis) throws FuzzyReasonerException {

		for (Iterator<String> iterator = inputVariables.iterator(); iterator.hasNext();) {
			final String capability = iterator.next();
			Number value;
			try{
				value = device.getCapabilityValue(DeviceCapability.valueOf(capability));
			}catch(IllegalArgumentException e){
				value = null;
			}
			
			if (value != null){
				fis.setVariable(capability, value.doubleValue());
			}else{
				Object capabilityValue = initialCapabilities.get(capability);
				if (capabilityValue == null){
					throw new FuzzyReasonerException("Rule uses an invalid variable: " + capability);
				}
				if (capabilityValue instanceof Number){
					fis.setVariable(capability, ((Number)capabilityValue).doubleValue());
				} else {
					throw new FuzzyReasonerException("Rule uses an non-numeric variable: " + capability);
				}
			}			
		}
	}
	
	
	private MobileDevice getMobileDevice (MobileDevices devices, String name) throws DatabaseException{
		for(MobileDevice device : devices.getMobileDevices()){
			if (device.getName().equals(name)){
				return device;
			}
		}
		throw new DatabaseException("Mobile device not found");
	}
	
	Set<String> extractInputVariables(String rules){
		final Set<String> extractedInputVariables = new HashSet<String>();
		
		final String[] ruleContent = rules.split("IF ");
		
		for (int i = 0; i < ruleContent.length; i++) {
			if (ruleContent[i].contains(" THEN ")) {
				final String[] ruleParts = ruleContent[i].split(" THEN ");
				if (ruleParts[0].contains(" OR ")) {
					final String [] conditions = ruleParts[0].split(" OR ");
					for (int j = 0; j < conditions.length; j++) {
						extractedInputVariables.add(this.getVariable(conditions[i]));
					}
				} else if (ruleParts[0].contains(" AND ")) {
					final String [] conditions = ruleParts[0].split(" AND ");
					for (int j = 0; j < conditions.length; j++) {
						extractedInputVariables.add(this.getVariable(conditions[i]));
					}					
				} else{
					extractedInputVariables.add(this.getVariable(ruleParts[0]));
				}
			}
		}	
		
		return extractedInputVariables;	
	}
	
	private String getVariable(String condition){
		final String[] tokens = condition.split("IS");
		return tokens[0].trim();
	}

	private String generateRuleFileContent(
			Map<String, RegionDistributionInfo[]> inputVariables,
			Map<String, RegionDistributionInfo[]> outputVariables, String rules,
			final MobileDevices mobileDevices, WarningStore warningStore) throws InvalidSyntaxException {
		final Map<DeviceCapability, Variable> deviceInputVariables = new HashMap<DeviceCapability, Variable>();
		final Map<UserCapability, Variable> userInputVariables = new HashMap<UserCapability, Variable>();
		final Map<String, Variable> outVariables    = new HashMap<String, Variable>();
		
		for(String key : inputVariables.keySet()){
			final RegionDistributionInfo [] possibleValues = inputVariables.get(key);
			final Variable variable = new Variable(key, Arrays.asList(possibleValues));
			
			boolean assigned = false;
			
			for(DeviceCapability currentCapability : DeviceCapability.values()){
				if(currentCapability.name().equals(key)){
					deviceInputVariables.put(currentCapability, variable);
					assigned = true;
					break;
				}
			}
			
			if(assigned)
				continue;
			
			for(UserCapability currentCapability : UserCapability.values()){
				if(currentCapability.name().equals(key)){
					userInputVariables.put(currentCapability, variable);
					assigned = true;
					break;
				}
			}
			
			if(assigned)
				continue;
			throw new IllegalArgumentException("Input variable: " + key + " unrecognized for input variables");
		}
		
		for(String key : outputVariables.keySet()){
			final RegionDistributionInfo [] possibleValues = outputVariables.get(key);
			final Variable variable = new Variable(key, Arrays.asList(possibleValues));
			outVariables.put(key, variable);
		}
		
		final String ruleFileContent = this.creator.createRuleFile("block", deviceInputVariables, userInputVariables, outVariables, mobileDevices, rules, warningStore);
		return ruleFileContent;
	}

	@Override
	public Map<String, String> inferNewCapabilities(
			String deviceName, 
			WarningStore warningStore, Map<String, Object> initialCapabilities,
			Map<String, RegionDistributionInfo[]> inputVariables) throws FuzzyReasonerException {
		return this.inferNewCapabilities(deviceName, warningStore, initialCapabilities, inputVariables, Geolocation.ALL, DecayFunctions.model, CalendarFactory.now(), new HashMap<String, RegionDistributionInfo[]>(), "");
	}

	@Override
	public Map<String, String> inferNewCapabilities(
			String deviceName, 
			WarningStore warningStore, Map<String, Object> initialCapabilities,
			Map<String, RegionDistributionInfo[]> inputVariables, Geolocation geo, DecayFunctions decayFunction, Calendar when) throws FuzzyReasonerException {
		return this.inferNewCapabilities(deviceName, warningStore, initialCapabilities, inputVariables, geo, decayFunction, when, new HashMap<String, RegionDistributionInfo[]>(), "");
	}

	@Override
	public Map<String, String> inferNewCapabilities(
			String deviceName, 
			WarningStore warningStore, Map<String, Object> initialCapabilities,
			Map<String, RegionDistributionInfo[]> inputVariables, 
			Map<String, RegionDistributionInfo[]> outputVariables,
			String rules) throws FuzzyReasonerException {
		return this.inferNewCapabilities(deviceName, warningStore, initialCapabilities, inputVariables, Geolocation.ALL, DecayFunctions.model, CalendarFactory.now(), outputVariables, rules);
	}
}
