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
package piramide.interaction.reasoner.web.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class VariableTrendValues implements Serializable, IsSerializable {
	
	private static final long serialVersionUID = 5788199606654189720L;
	
	private HashMap<String, LinkedHashMap<String, Double>> values; // variable => trend => value 
	private String [] warningMessages;
	private HashMap<String, Double> initialValues; // variable => value

	public VariableTrendValues() {
		this.values = new HashMap<String, LinkedHashMap<String, Double>>();
		this.warningMessages = new String[]{};
		this.initialValues = new HashMap<String, Double>();
	}
	
	public VariableTrendValues(HashMap<String, LinkedHashMap<String, Double>> values, String [] warnings, HashMap<String, Double> initialValues) {
		this.values = values;
		this.warningMessages = warnings;
		this.initialValues = initialValues;
	}

	public HashMap<String, LinkedHashMap<String, Double>> getValues() {
		return this.values;
	}
	
	public String [] getWarningMessages() {
		return this.warningMessages;
	}

	public HashMap<String, Double> getInitialValues() {
		return this.initialValues;
	}
}
