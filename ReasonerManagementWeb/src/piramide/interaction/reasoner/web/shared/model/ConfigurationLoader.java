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
package piramide.interaction.reasoner.web.shared.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.impl.DOMParseException;

public class ConfigurationLoader {
	
	public static CompilingConfiguration loadProfileConfiguration(String text) throws ConfigurationException{
		final Document document;
		try {
			document = XMLParser.parse(text);
		} catch (DOMParseException e) {
			throw new ConfigurationException("Invalid XML document: " + e.getMessage(), e);
		}
		
		final Profile profile = parseProfile(document);
		final String mobileDevice = parseMobileDevice(document);
		final String geolocation = parseGeolocation(document);
		
		final CompilingConfiguration configuration = new CompilingConfiguration(profile, mobileDevice, new Geolocation(geolocation));
		
		return configuration;
	}
	
	public static FuzzyConfiguration loadFuzzyConfiguration(String text) throws ConfigurationException{
		final Document document;
		try {
			document = XMLParser.parse(text);
		} catch (DOMParseException e) {
			throw new ConfigurationException("Invalid XML document: " + e.getMessage(), e);
		}
		
		final NodeList fuzzy = document.getElementsByTagName(FuzzyConfiguration.FUZZY);
		if (fuzzy.getLength() != 1) 
			throw new ConfigurationException("A single fuzzy tag was expected");		
		if(!(fuzzy.item(0) instanceof Element))
			throw new ConfigurationException("A fuzzy element was expected");
		
		final FuzzyConfiguration fuzzyConfiguration = parseFuzzyConfiguration(fuzzy.item(0));
		
		return fuzzyConfiguration;
	}

	private static String parseMobileDevice(final Document document) throws ConfigurationException {
		final NodeList mobileDeviceNodeList = document.getElementsByTagName(CompilingConfiguration.MOBILE_DEVICE);
		if((mobileDeviceNodeList.getLength() != 1))
			throw new ConfigurationException("No mobile-device found in the configuration");
		
		return mobileDeviceNodeList.item(0).getChildNodes().item(0).getNodeValue();
	} 
	
	private static String parseGeolocation(final Document document) throws ConfigurationException {
		final NodeList mobileDeviceNodeList = document.getElementsByTagName(CompilingConfiguration.GEOLOCATION);
		if((mobileDeviceNodeList.getLength() != 1))
			throw new ConfigurationException("No geolocation found in the configuration");
		
		final String text = mobileDeviceNodeList.item(0).getChildNodes().item(0).getNodeValue();
		for(String location : GeolocationRegions.values()){
			if(location.equals(text))
				return text;
		}
		throw new ConfigurationException("Invalid geolocation found in the configuration");
	} 
	
	private static Profile parseProfile(final Document document) throws ConfigurationException {
		final NodeList profileList = document.getElementsByTagName(Profile.PROFILE);
		if((profileList.getLength() != 1))
			throw new ConfigurationException("No profile found in the configuration");
		
		final Profile profile = parseProfile(profileList.item(0));
		return profile;
	}

	private static Profile parseProfile(Node node) throws ConfigurationException{
		if(!(node instanceof Element))
			throw new ConfigurationException("Profile element expected");
		
		final Element element = (Element)node;
			
		final Map<String, String> capabilities = new HashMap<String, String>();
		for(String capability : UserCapabilities.values()){
			final NodeList capabilityList =  element.getElementsByTagName(capability);
			if (capabilityList.getLength() > 1) 
				throw new ConfigurationException("A capability cannot have more than one value");
			else if (capabilityList.getLength() == 1){
				final Element capabilityElement = (Element)capabilityList.item(0);
				final String capabilityValue = capabilityElement.getChildNodes().item(0).getNodeValue();
				capabilities.put(capability, capabilityValue);
			} 			
		}
		
		final Profile profile = new Profile(capabilities);
		return profile;
	}
	
	private static FuzzyConfiguration parseFuzzyConfiguration(Node node) throws ConfigurationException{
		final Element fuzzy = (Element)node;
		
		//input
		final NodeList inputList = fuzzy.getElementsByTagName(InputVariablesConfiguration.INPUT);
		if(inputList.getLength() != 1)
			throw new ConfigurationException("A single input tag was expected");
		final Node inputNode = inputList.item(0);		
		if(!(inputNode instanceof Element))
			throw new ConfigurationException("Input element expected");
		final InputVariablesConfiguration inputVariables = parseInputVariables(inputNode);
		
		//output
		final NodeList outputList = fuzzy.getElementsByTagName(OutputVariablesConfiguration.OUTPUT);
		if(outputList.getLength() != 1)
			throw new ConfigurationException("A single output tag was expected");
		final Node outputNode = outputList.item(0);		
		if(!(outputNode instanceof Element))
			throw new ConfigurationException("Output element expected");
		final OutputVariablesConfiguration outputVariables = parseOutputVariables(outputNode);
		
		//rules
		final NodeList ruleList = fuzzy.getElementsByTagName(FuzzyConfiguration.RULES);
		if(ruleList.getLength() != 1)
			throw new ConfigurationException("A single rules tag was expected");
		final Node rulesNode = ruleList.item(0);		
		if(!(rulesNode instanceof Element))
			throw new ConfigurationException("Rules element expected");
		final Element rulesElement = (Element)rulesNode;

		final String rules = rulesElement.getChildNodes().item(0).getNodeValue();
		
		final FuzzyConfiguration fuzzyConfiguration = new FuzzyConfiguration(inputVariables, outputVariables, rules);
		return fuzzyConfiguration;
		
	}
	
	private static InputVariablesConfiguration parseInputVariables(Node node) throws ConfigurationException{
		final Element input = (Element)node;
		
		final NodeList devicesList = input.getElementsByTagName(InputVariablesConfiguration.DEVICES);
		if(devicesList.getLength() != 1)
			throw new ConfigurationException("A single device tag was expected");
		final Node deviceNode = devicesList.item(0);		
		if(!(deviceNode instanceof Element))
			throw new ConfigurationException("Device element expected");
		final List<Variable> deviceVariables = parseDevicesInputVariables(deviceNode);
		
		//User
		final NodeList userList = input.getElementsByTagName(InputVariablesConfiguration.USER);
		if(userList.getLength() != 1)
			throw new ConfigurationException("A single user tag was expected");
		final Node userNode = userList.item(0);		
		if(!(userNode instanceof Element))
			throw new ConfigurationException("User element expected");
		final List<Variable> userVariables = parseUserInputVariables(userNode);
		
		final InputVariablesConfiguration inputVariablesConfiguration = new InputVariablesConfiguration(deviceVariables, userVariables);
		return inputVariablesConfiguration;		
	}
	
	private static List<Variable> parseDevicesInputVariables(Node node) {
		final Element devices = (Element)node;
		NodeList variables = devices.getElementsByTagName(Variable.VARIABLE);		
		final List<Variable> deviceVariables = parseVariables(variables);
				
		return deviceVariables;
	}

	
	private static List<Variable> parseUserInputVariables(Node node){
		final Element devices = (Element)node;
		NodeList variables = devices.getElementsByTagName(Variable.VARIABLE);		
		final List<Variable> userVariables = parseVariables(variables);
				
		return userVariables;		
	}
	
	private static OutputVariablesConfiguration parseOutputVariables(Node node){
		final Element devices = (Element)node;
		NodeList variables = devices.getElementsByTagName(Variable.VARIABLE);		
		final List<Variable> variableList = parseVariables(variables);
		OutputVariablesConfiguration outputConfiguration = new OutputVariablesConfiguration(variableList);
		return outputConfiguration;

	}
	
	private static List<Variable>  parseVariables(NodeList variables) {
		final List<Variable> variableList = new Vector<Variable>();
		for(int i = 0; i < variables.getLength(); i++)
		{
			final Element variableElement = (Element)variables.item(i);
			final String name = variableElement.getAttribute(Variable.NAME);
			
			final NodeList termList = variableElement.getElementsByTagName(Variable.TERM);
			final Vector <String> terms = new Vector<String>();
			for (int j = 0; j < termList.getLength(); j++) {
				Element term = (Element)termList.item(j);
				terms.add(term.getAttribute(Variable.NAME));
			}
			
			final Variable variable = new Variable(name, terms.toArray(new String[terms.size()]));
			variableList.add(variable);			
		}
		
		return variableList;
	}
	
}
