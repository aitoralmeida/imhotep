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
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Text;

public class Profile implements Serializable, IsSerializable{
	
	private static final long serialVersionUID = -366377618672882080L;

	static final String PROFILE = "profile";
	
	private Map<String, String> capabilities;

	public Profile(){
		this.capabilities = new TreeMap<String, String>();
	}
	
	public Profile(Map<String, String> capabilities) {
		this.capabilities = capabilities;
	}

	public Map<String, String> getCapabilities() {
		return this.capabilities;
	}

	public boolean isDefault() {
		return this.capabilities.size() == 0;
	}

	Element createElement(Document document) {
		final Element element = document.createElement(Profile.PROFILE);
		
		for(String capability : this.capabilities.keySet()){
			element.appendChild(document.createTextNode("\n\t\t"));
			final Element capabilityElement = document.createElement(capability);
			final Text capabilityValue = document.createTextNode(this.capabilities.get(capability));
			capabilityElement.appendChild(capabilityValue);
			element.appendChild(capabilityElement);
		}
		
		element.appendChild(document.createTextNode("\n\t"));
		
		return element;
	}
}
