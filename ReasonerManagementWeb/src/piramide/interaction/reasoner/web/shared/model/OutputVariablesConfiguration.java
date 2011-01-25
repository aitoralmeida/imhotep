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

public class OutputVariablesConfiguration implements Serializable, IsSerializable {
	
	private static final long serialVersionUID = -8231811296626270028L;
	
	private List<Variable> variables;
	static final String OUTPUT = "output";

	public OutputVariablesConfiguration(){
		this.variables = new ArrayList<Variable>();
	}
	
	public OutputVariablesConfiguration(List<Variable> variables) {
		this.variables = variables;
	}

	public List<Variable> getVariables() {
		return this.variables;
	}

	public boolean isDefault() {
		return this.variables.isEmpty();
	}

	Element createElement(Document document) {
		final Element outputElement = document.createElement(OUTPUT);
		
		for(Variable variable : this.variables){
			outputElement.appendChild(document.createTextNode("\n\t\t"));
			
			final Element variableElement = variable.createElement(document, "\n\t\t");
			outputElement.appendChild(variableElement);
		}
		
		outputElement.appendChild(document.createTextNode("\n\t"));
		
		return outputElement;
	}
}
