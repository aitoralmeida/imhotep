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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;

public class InputVariablesConfiguration implements Serializable, IsSerializable {
	
	private static final long serialVersionUID = 2101784245088131348L;
	
	private List<Variable> deviceVariables;
	private List<Variable> userVariables;
	static final String USER = "user";
	static final String DEVICES = "devices";
	static final String INPUT = "input";
	
	public InputVariablesConfiguration(){
		this.deviceVariables = new ArrayList<Variable>();
		this.userVariables   = new ArrayList<Variable>();
	}
	
	public InputVariablesConfiguration(List<Variable> deviceVariables,
			List<Variable> userVariables) {
		this.deviceVariables = deviceVariables;
		this.userVariables = userVariables;
	}
	
	public List<Variable> getDeviceVariables() {
		return this.deviceVariables;
	}
	
	public void setDeviceVariables(List<Variable> deviceVariables) {
		this.deviceVariables = deviceVariables;
	}
	
	public List<Variable> getUserVariables() {
		return this.userVariables;
	}
	
	public void setUserVariables(List<Variable> userVariables) {
		this.userVariables = userVariables;
	}

	public boolean isDefault() {
		return this.deviceVariables.isEmpty() && this.userVariables.isEmpty();
	}

	Element createElement(Document document) {
		final Element outputElement = document.createElement(INPUT);
		
		outputElement.appendChild(document.createTextNode("\n\t\t"));
		
		final Element devicesElement = document.createElement(DEVICES);
		outputElement.appendChild(devicesElement);
		
		outputElement.appendChild(document.createTextNode("\n\t\t"));
		final Element userElement = document.createElement(USER);
		outputElement.appendChild(userElement);

		for(Variable variable : this.deviceVariables){
			devicesElement.appendChild(document.createTextNode("\n\t\t\t"));
			
			final Element variableElement = variable.createElement(document, "\n\t\t\t");
			devicesElement.appendChild(variableElement);
		}
		devicesElement.appendChild(document.createTextNode("\n\t\t"));
		
		for(Variable variable : this.userVariables){
			userElement.appendChild(document.createTextNode("\n\t\t\t"));
			
			final Element variableElement = variable.createElement(document, "\n\t\t\t");
			userElement.appendChild(variableElement);
		}
		userElement.appendChild(document.createTextNode("\n\t\t"));
		
		
		outputElement.appendChild(document.createTextNode("\n\t"));
		
		return outputElement;
	}
}
