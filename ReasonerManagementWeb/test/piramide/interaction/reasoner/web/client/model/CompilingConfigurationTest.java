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
package piramide.interaction.reasoner.web.client.model;

import piramide.interaction.reasoner.web.shared.model.CompilingConfiguration;
import piramide.interaction.reasoner.web.shared.model.ConfigurationLoader;
import piramide.interaction.reasoner.web.shared.model.GeolocationRegions;
import piramide.interaction.reasoner.web.shared.model.UserCapabilities;

import com.google.gwt.junit.client.GWTTestCase;

public class CompilingConfigurationTest extends GWTTestCase {

	
	public void testCompilingConfigurationToXml() throws Exception {
		UserCapabilities.clear();
		UserCapabilities.add("sight");
		UserCapabilities.add("earing");
		
		GeolocationRegions.clear();
		GeolocationRegions.add("jp");
		GeolocationRegions.add("es");
		GeolocationRegions.add("all");
		
		final CompilingConfiguration compilingConfiguration = ConfigurationLoader.loadProfileConfiguration(CompilingConfiguration.SAMPLE);
		final String xml = compilingConfiguration.toXml();
		
		final String endOfComment = "-->";
		final String sampleWithoutComments = CompilingConfiguration.SAMPLE.substring(CompilingConfiguration.SAMPLE.indexOf(endOfComment) + endOfComment.length());
		
		assertEquals(sampleWithoutComments.trim(), xml);
	}
	
	@Override
	public String getModuleName() {
		return "piramide.interaction.reasoner.web.ReasonerManagementWeb";
	}

}
