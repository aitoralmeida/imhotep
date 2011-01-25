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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;

public class ImageUrlBuilder {
	
	private final List<String> values = new ArrayList<String>();
	private final String variableName;
	private final boolean devices;
	private final boolean input;
	private final String geo;
	
	public final static String GRAPH_MANAGER = "graph-manager";
	public final static String VARIABLE = "variable"; 
	public final static String VALUE = "value"; 
	public final static String DEVICES = "devices"; 
	public final static String INPUT = "input"; 
	public final static String GEO   = "geo";
	
	public ImageUrlBuilder(String variableName, String geo, boolean devices, boolean input, String ... values){
		this.variableName = variableName;
		this.geo     = geo;
		this.devices = devices;
		this.input = input;
		for(String value : values)
			this.values.add(value);
	}
	
	public void addValue(String value){
		this.values.add(value);
	}
	
	@Override
	public String toString(){
		final StringBuffer targetURL = new StringBuffer(GWT.getModuleBaseURL());
		targetURL.append(GRAPH_MANAGER);
		targetURL.append("?");
		targetURL.append(VARIABLE);
		targetURL.append("=");
		targetURL.append(this.variableName);
		
		if(this.geo != null){
			targetURL.append("&");
			targetURL.append(GEO);
			targetURL.append("=");
			targetURL.append(this.geo);
		}
		
		for (String value : this.values){
			targetURL.append("&");
			targetURL.append(VALUE);
			targetURL.append("=");
			targetURL.append(value);
		}
		
		targetURL.append("&");
		targetURL.append(DEVICES);
		targetURL.append("=");
		targetURL.append((this.devices?"true":"false"));
		
		targetURL.append("&");
		targetURL.append(INPUT);
		targetURL.append("=");
		targetURL.append((this.input?"true":"false"));
		
		return targetURL.toString();
	}
}
