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
import com.google.gwt.xml.client.XMLParser;

public class FuzzyConfiguration implements Serializable, IsSerializable {
	
	private static final long serialVersionUID = -46474361068508017L;

	public static final String SAMPLE = "<!-- This is a sample fuzzy configuration, replace it with the real one -->\n" +
										"<fuzzy>\n" +
										"\t<input>\n" +
										"\t\t<devices>\n" +
										"\t\t\t<variable name=\"reso_size\">\n" +
										"\t\t\t\t<term name=\"small\"/>\n" +
										"\t\t\t\t<term name=\"normal\"/>\n" +
										"\t\t\t\t<term name=\"big\"/>\n" +
										"\t\t\t</variable>\n" +
										"\t\t</devices>\n" +
										"\t\t<user>\n" +
										"\t\t\t<variable name=\"earing\">\n" +
										"\t\t\t\t<term name=\"good\"/>\n" +
										"\t\t\t\t<term name=\"regular\"/>\n" +
										"\t\t\t\t<term name=\"bad\"/>\n" +
										"\t\t\t</variable>\n" +
										"\t\t</user>\n" +
										"\t</input>\n" +
										"\t<output>\n" +
										"\t\t<variable name=\"video\">\n" +
										"\t\t\t<term name=\"good\"/>\n" +
										"\t\t\t<term name=\"bad\"/>\n" +
										"\t\t</variable>\n" +
										"\t</output>\n" +
										"\t<rules>RULE RULE1: IF reso_size IS big OR reso_size IS normal THEN video IS good;\n\nRULE RULE2: IF reso_size IS small THEN video IS bad;</rules>\n" +
										"</fuzzy>";
	
	private InputVariablesConfiguration input;
	private OutputVariablesConfiguration output;
	private String rules;

	static final String RULES = "rules";
	static final String FUZZY = "fuzzy";
	
	public FuzzyConfiguration(){
		this.input  = new InputVariablesConfiguration();
		this.output = new OutputVariablesConfiguration();
		this.rules  = "";
	}
	
	public boolean isDefault(){
		return this.rules.equals("") && this.input.isDefault() && this.output.isDefault();
	}
	
	public FuzzyConfiguration(InputVariablesConfiguration input, OutputVariablesConfiguration output, String rules) {
		this.input = input;
		this.output = output;
		this.rules = rules;
	}

	public InputVariablesConfiguration getInput() {
		return this.input;
	}

	public OutputVariablesConfiguration getOutput() {
		return this.output;
	}

	public String getRules() {
		return this.rules;
	}
	
	public void setRules(String rules){
		this.rules = rules;
	}
	
	public String toXml(){
		final Document document = XMLParser.createDocument();
		
		final Element rootElement = document.createElement(FUZZY);
		document.appendChild(rootElement);
		
		rootElement.appendChild(document.createTextNode("\n\t"));
		
		final Element inputElement = this.input.createElement(document);
		rootElement.appendChild(inputElement);
		
		rootElement.appendChild(document.createTextNode("\n\t"));
		
		final Element outputElement = this.output.createElement(document);
		rootElement.appendChild(outputElement);
		
		rootElement.appendChild(document.createTextNode("\n\t"));
		
		final Element rulesElement = document.createElement(RULES);
		rulesElement.appendChild(document.createTextNode(this.rules));
		rootElement.appendChild(rulesElement);
		
		rootElement.appendChild(document.createTextNode("\n"));
		
		return document.toString();
	}
}
