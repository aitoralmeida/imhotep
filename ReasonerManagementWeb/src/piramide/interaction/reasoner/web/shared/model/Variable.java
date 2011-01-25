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

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;

public class Variable implements Serializable, IsSerializable {
	
	private static final long serialVersionUID = -4635782997038486852L;
	
	static final String NAME = "name";
	static final String TERM = "term";
	static final String VARIABLE = "variable";
	
	private String name;
	private String [] terms;
	
	public Variable(){
		this.name = "";
		this.terms = new String [] {};
	}
	
	public Variable(String name, String[] terms) {
		this.name = name;
		this.terms = terms;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String[] getTerms() {
		return this.terms;
	}

	Element createElement(Document document, String tabs) {
		
		final Element variableElement = document.createElement(VARIABLE);
		
		variableElement.setAttribute(NAME, this.name);
		
		for(String term : this.terms){
			variableElement.appendChild(document.createTextNode(tabs + "\t"));
			
			final Element currentTerm = document.createElement(TERM);
			currentTerm.setAttribute(NAME, term);
			variableElement.appendChild(currentTerm);
		}
		
		variableElement.appendChild(document.createTextNode(tabs));
		
		return variableElement;
	}

	public void setText(String name) {
		this.name = name;
	}
	
	public void setTerms(String [] terms){
		this.terms = terms;
	}
}
