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
package piramide.interaction.reasoner.web.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import piramide.interaction.reasoner.FuzzyInferredResult;
import piramide.interaction.reasoner.FuzzyReasonerException;
import piramide.interaction.reasoner.FuzzyReasonerWizardFacade;
import piramide.interaction.reasoner.Geolocation;
import piramide.interaction.reasoner.IFuzzyReasonerWizardFacade;
import piramide.interaction.reasoner.creator.WarningStore;
import piramide.interaction.reasoner.db.DeviceCapability;
import piramide.interaction.reasoner.db.MobileDevice;
import piramide.interaction.reasoner.db.UserCapabilities;
import piramide.interaction.reasoner.web.client.ReasonerManager;
import piramide.interaction.reasoner.web.shared.Capabilities;
import piramide.interaction.reasoner.web.shared.DeviceNameSuggestion;
import piramide.interaction.reasoner.web.shared.ReasonerManagerException;
import piramide.interaction.reasoner.web.shared.VariableTrendValues;
import piramide.interaction.reasoner.web.shared.model.CompilingConfiguration;
import piramide.interaction.reasoner.web.shared.model.FuzzyConfiguration;
import piramide.interaction.reasoner.web.shared.model.Variable;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ReasonerManagerImpl extends RemoteServiceServlet implements
		ReasonerManager {
	
	private final IFuzzyReasonerWizardFacade fuzzyReasoner;
	private final String [] geolocationRegions;
	
	public ReasonerManagerImpl() throws FuzzyReasonerException{
		this.fuzzyReasoner = new FuzzyReasonerWizardFacade();
		this.fuzzyReasoner.initializeCacheData();
		this.geolocationRegions = this.fuzzyReasoner.getGeolocationRegions();
	}

	public SuggestOracle.Response searchDeviceID(SuggestOracle.Request request) throws IllegalArgumentException {
		
		final List<MobileDevice> results;
		try {
			results = this.fuzzyReasoner.searchDeviceNames(request.getQuery(), request.getLimit());
		} catch (FuzzyReasonerException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Fail: " + e.getMessage());
		}
		
		
		final List<Suggestion> suggestions = new Vector<Suggestion>();
		for(MobileDevice result : results){
			final String display = generateDisplayName(result.getName(), request);
			final HashMap<String, Number> capabilities = new HashMap<String, Number>();
			for(DeviceCapability capability : DeviceCapability.values())
				capabilities.put(capability.name(), result.getCapabilityValue(capability));
			
			suggestions.add(new DeviceNameSuggestion(result.getName(), display, capabilities, result.getDecayedTrend(), result.getTotalTrend()));
		}
		
		return new SuggestOracle.Response(suggestions);
	}
	
	private final Comparator<String> longestStringComparator = new Comparator<String>() {

		@Override
		public int compare(String o1, String o2) {
			if(o1.length() == o2.length())
				return o1.compareTo(o2);
			
			if(o1.length() > o2.length())
				return -1;
			
			return 1;
		}
	};
	
	private String generateDisplayName(String name, SuggestOracle.Request request){
		
		final List<String> queryParts = new ArrayList<String>();
		
		for(String queryPart : request.getQuery().split(" "))
			if(queryPart.trim().length() > 0)
				queryParts.add(queryPart);
		
		Collections.sort(queryParts, this.longestStringComparator);

		final StringBuilder finalName = new StringBuilder();
		
		for(int i = 0; i < name.length(); ++i){
			final String currentPart = name.substring(i);
			boolean found = false;
			
			for(String queryPart : queryParts){
				if(currentPart.indexOf(queryPart) == 0){
					i += (queryPart.length() - 1);
					finalName.append("<b>");
					finalName.append(queryPart);
					finalName.append("</b>");
					found = true;
					break;
				}
			}
			
			if(!found)
				finalName.append(currentPart.charAt(0));
		}
		
		return finalName.toString();
	}

	@Override
	public Capabilities retrieveCapabilities() {
		final DeviceCapability [] originalDeviceCapabilities = DeviceCapability.values();
		final String [] deviceCapabilities = new String[DeviceCapability.values().length];
		
		for(int i = 0; i < deviceCapabilities.length; ++i)
			deviceCapabilities[i] = originalDeviceCapabilities[i].name();
		
		final UserCapabilities.UserCapability [] originalUserCapabilities = UserCapabilities.UserCapability.values();
		final String [] userCapabilities = new String[UserCapabilities.UserCapability.values().length];
		
		for(int i = 0; i < userCapabilities.length; ++i)
			userCapabilities[i] = originalUserCapabilities[i].name();
		
		return new Capabilities(userCapabilities, deviceCapabilities);
	}

	@Override
	public DeviceNameSuggestion retrieveDeviceID(String deviceID)
			throws IllegalArgumentException {
		final MobileDevice mobileDevice;
		try {
			mobileDevice = this.fuzzyReasoner.retrieveDeviceID(deviceID);
		} catch (FuzzyReasonerException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Fail: " + e.getMessage());
		}
		
		final String display = mobileDevice.getName();
		final HashMap<String, Number> capabilities = new HashMap<String, Number>();
		for(DeviceCapability capability : DeviceCapability.values())
			capabilities.put(capability.name(), mobileDevice.getCapabilityValue(capability));
		
		return new DeviceNameSuggestion(mobileDevice.getName(), display, capabilities, mobileDevice.getDecayedTrend(), mobileDevice.getTotalTrend());
	}

	@Override
	public VariableTrendValues retrieveData(CompilingConfiguration compilingConfiguration, FuzzyConfiguration fuzzyConfiguration) throws ReasonerManagerException {
		
		final String deviceName = compilingConfiguration.getDeviceName();
		
		final DeviceNameSuggestion deviceData = this.retrieveDeviceID(deviceName);
		
		final Map<String, String> unmanagedCapabilities = compilingConfiguration.getProfile().getCapabilities();
		final Map<String, Object> managedCapabilities = manageCapabilityTypes(unmanagedCapabilities);
		final String geolocation = compilingConfiguration.getGeolocation().getGeo();
		
		final String rules = fuzzyConfiguration.getRules();
		final Map<String, String[]> allInputVariables = new HashMap<String, String[]>();
		
		for(Variable var : fuzzyConfiguration.getInput().getDeviceVariables())
			allInputVariables.put(var.getName(), var.getTerms());
		
		for(Variable var : fuzzyConfiguration.getInput().getUserVariables())
			allInputVariables.put(var.getName(), var.getTerms());
		
		final Map<String, String[]> outputVariables = new HashMap<String, String[]>();
		for(Variable var : fuzzyConfiguration.getOutput().getVariables())
			outputVariables.put(var.getName(), var.getTerms());
		

		final WarningStore warningStore = new WarningStore();
		try {
			final FuzzyInferredResult result = this.fuzzyReasoner.getInferredValues(deviceName, warningStore, managedCapabilities, allInputVariables, new Geolocation(geolocation), outputVariables, rules);
			final HashMap<String, LinkedHashMap<String, Double>> values = result.getValues();
			
			final HashMap<String, Double> defuzzifiedValues = result.getDefuzzifiedValues();
			
			final HashMap<String, Double> allVariableValues = new HashMap<String, Double>();
			for(String variable : managedCapabilities.keySet()){
				final Object obj = managedCapabilities.get(variable);
				if(obj instanceof Number){
					final Double d = new Double(((Number)obj).doubleValue());
					allVariableValues.put(variable, d);
				}
			}
			for(String variable : defuzzifiedValues.keySet())
				allVariableValues.put(variable, defuzzifiedValues.get(variable));
			
			for(String deviceVariable : deviceData.getCapabilities().keySet())
				allVariableValues.put(deviceVariable, Double.valueOf(deviceData.getCapabilities().get(deviceVariable).doubleValue()));
			
			return new VariableTrendValues(values, warningStore.getMessages(), allVariableValues);
		} catch (FuzzyReasonerException e) {
			e.printStackTrace();
			throw new ReasonerManagerException("Error inferring values: " + e.getMessage());
		}
	}
	
	private Map<String, Object> manageCapabilityTypes(Map<String, String> objs){
		final Map<String, Object> newCapabilities = new HashMap<String, Object>();
		
		for(String capability : objs.keySet()){
			final String obj = objs.get(capability);
			
			Object value;
			
			try{
				value = Integer.valueOf(obj);
			}catch(NumberFormatException nfe){
				try{
					value = Float.valueOf(obj);
				}catch(NumberFormatException nfe2){
					try{
						value = Double.valueOf(obj);
					}catch(NumberFormatException nfe3){
						value = obj;
					}
				}
			}
			
			newCapabilities.put(capability, value);
		}
		
		return newCapabilities;
	}

	@Override
	public String[] retrieveGeolocationRegions() {
		return this.geolocationRegions;
	}
}
