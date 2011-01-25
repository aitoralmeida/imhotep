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
import piramide.interaction.reasoner.web.shared.model.ConfigurationException;
import piramide.interaction.reasoner.web.shared.model.ConfigurationLoader;
import piramide.interaction.reasoner.web.shared.model.FuzzyConfiguration;
import piramide.interaction.reasoner.web.shared.model.GeolocationRegions;
import piramide.interaction.reasoner.web.shared.model.InputVariablesConfiguration;
import piramide.interaction.reasoner.web.shared.model.OutputVariablesConfiguration;
import piramide.interaction.reasoner.web.shared.model.Profile;
import piramide.interaction.reasoner.web.shared.model.UserCapabilities;
import piramide.interaction.reasoner.web.shared.model.Variable;

import com.google.gwt.junit.client.GWTTestCase;

public class ConfigurationLoaderTest extends GWTTestCase {

	public void testEmptyFile() throws Exception {
		final String EMPTY_FILE = "";
		
		try{
			ConfigurationLoader.loadProfileConfiguration(EMPTY_FILE);
			fail(ConfigurationException.class.getName() + " expected");
		}catch(ConfigurationException ce){
			// ok
		}
	}
	
	public void testCorrectProfileConfigurationFile() throws Exception {
		UserCapabilities.clear();
		UserCapabilities.add("earing");
		UserCapabilities.add("sight");
		
		GeolocationRegions.clear();
		GeolocationRegions.add("jp");
		GeolocationRegions.add("es");
		GeolocationRegions.add("all");
		
		final CompilingConfiguration compilingConfiguration = ConfigurationLoader.loadProfileConfiguration(CompilingConfiguration.SAMPLE);
		final Profile profile = compilingConfiguration.getProfile();
		assertEquals(2, profile.getCapabilities().size());
		assertEquals("10.0", profile.getCapabilities().get("sight"));
		assertEquals("15.0", profile.getCapabilities().get("earing"));
		
		assertEquals("nokia 6630", compilingConfiguration.getDeviceName());
	}
	
	public void testCorrectFuzzyConfigurationFile() throws Exception {
		final FuzzyConfiguration fuzzyConfiguration = ConfigurationLoader.loadFuzzyConfiguration(FuzzyConfiguration.SAMPLE);
		
		final InputVariablesConfiguration inputConfig = fuzzyConfiguration.getInput();
		assertEquals(1, inputConfig.getDeviceVariables().size());
		
		final Variable deviceInputVariable = inputConfig.getDeviceVariables().get(0);
		assertEquals("reso_size", deviceInputVariable.getName());
		assertEquals(3, deviceInputVariable.getTerms().length);
		assertEquals("small", deviceInputVariable.getTerms()[0]);
		assertEquals("normal", deviceInputVariable.getTerms()[1]);
		assertEquals("big", deviceInputVariable.getTerms()[2]);
		
		assertEquals(1, inputConfig.getUserVariables().size());
		
		final Variable userInputVariable = inputConfig.getUserVariables().get(0);
		assertEquals("earing", userInputVariable.getName());
		assertEquals(3, userInputVariable.getTerms().length);
		assertEquals("good", userInputVariable.getTerms()[0]);
		assertEquals("regular", userInputVariable.getTerms()[1]);
		assertEquals("bad", userInputVariable.getTerms()[2]);
		
		final OutputVariablesConfiguration outputConfig = fuzzyConfiguration.getOutput();
		assertEquals(1, outputConfig.getVariables().size());
		
		final Variable outputVariable = outputConfig.getVariables().get(0);
		assertEquals("video", outputVariable.getName());
		assertEquals(2, outputVariable.getTerms().length);
		assertEquals("good", outputVariable.getTerms()[0]);
		assertEquals("bad", outputVariable.getTerms()[1]);
		
		assertEquals("RULE RULE1: IF reso_size IS big OR reso_size IS normal THEN video IS good;\n\nRULE RULE2: IF reso_size IS small THEN video IS bad;", fuzzyConfiguration.getRules());
	}
	
	
	
	
	
	@Override
	public String getModuleName() {
		return "piramide.interaction.reasoner.web.ReasonerManagementWeb";
	}

}
