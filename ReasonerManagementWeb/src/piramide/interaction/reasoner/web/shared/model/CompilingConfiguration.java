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
import com.google.gwt.xml.client.Text;
import com.google.gwt.xml.client.XMLParser;


public class CompilingConfiguration implements Serializable, IsSerializable {
	
	private static final long serialVersionUID = -4838229070179454240L;
	
	static final String COMPILING_CONFIGURATION = "compiling-configuration";
	static final String MOBILE_DEVICE = "mobile-device";
	static final String GEOLOCATION = "geolocation";


	public static String SAMPLE = "<!-- This is a sample compiling configuration, replace it with the real one -->\n" +
								  "<compiling-configuration>\n" +
								  "\t<profile>\n" +
								  "\t\t<sight>10.0</sight>\n" +
								  "\t\t<earing>15.0</earing>\n" +
								  "\t</profile>\n" +
								  "\t<mobile-device>nokia 6630</mobile-device>\n" +
								  "\t<geolocation>es</geolocation>\n" +
								  "</compiling-configuration>";
	
	private Profile profile;
	private String deviceName;
	private Geolocation geo;

	
	public CompilingConfiguration(){
		this.profile    = new Profile();
		this.deviceName = "";
		this.geo        = new Geolocation();
	}
	
	public boolean isDefault(){
		return this.deviceName.equals("") && this.profile.isDefault() && this.geo.isDefault();
	}
	
	public CompilingConfiguration(Profile profile, String deviceName, Geolocation geo) {
		this.profile = profile;
		this.deviceName = deviceName;
		this.geo = geo;
	}

	public Profile getProfile() {
		return this.profile;
	}

	public String getDeviceName() {
		return this.deviceName;
	}
	
	public Geolocation getGeolocation(){
		return this.geo;
	}
	
	public void setGeolocation(Geolocation geo){
		this.geo = geo;
	}
	
	public void setDeviceName(String deviceName){
		this.deviceName = deviceName;
	}
	
	public String toXml(){
		final Document document = XMLParser.createDocument();
		
		final Element rootElement = document.createElement(COMPILING_CONFIGURATION);
		document.appendChild(rootElement);
		
		final Element element = this.profile.createElement(document);
		rootElement.appendChild(document.createTextNode("\n\t"));
		rootElement.appendChild(element);
		rootElement.appendChild(document.createTextNode("\n\t"));
		
		final Element mobileDevice = document.createElement(MOBILE_DEVICE);
		rootElement.appendChild(mobileDevice);
		rootElement.appendChild(document.createTextNode("\n\t"));
		
		final Text mobileDeviceContents = document.createTextNode(this.deviceName);
		mobileDevice.appendChild(mobileDeviceContents);
		
		final Element geolocation = document.createElement(GEOLOCATION);
		rootElement.appendChild(geolocation);
		rootElement.appendChild(document.createTextNode("\n"));
		
		final Text geoContents = document.createTextNode(this.geo.getGeo());
		geolocation.appendChild(geoContents);
		
		return document.toString().trim();
	}
}
