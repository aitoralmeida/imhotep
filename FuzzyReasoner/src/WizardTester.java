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
import java.util.HashMap;
import java.util.Vector;

import piramide.interaction.reasoner.RegionDistributionInfo;
import piramide.interaction.reasoner.wizard.FCreateInputVariables;
import piramide.interaction.reasoner.wizard.FCreateRules;
import piramide.interaction.reasoner.wizard.Variable;


public class WizardTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		tryRuleCreation();
		//tryCompleteWizard();

	}
	
	@SuppressWarnings("unused")
	private static void tryCompleteWizard(){
		FCreateInputVariables fIn = new FCreateInputVariables();
		fIn.setVisible(true);
	}

	private static void tryRuleCreation() {
		Vector<RegionDistributionInfo> terms = new Vector<RegionDistributionInfo>();
		terms.add(new RegionDistributionInfo("term1", 1 / 3.0));
		terms.add(new RegionDistributionInfo("term2", 1 / 3.0));
		terms.add(new RegionDistributionInfo("term3", 1 / 3.0));
		Variable inVar = new Variable("Input", terms);
		Variable outVar = new Variable("Output", terms);
		HashMap<String, Variable> in = new HashMap<String,Variable>();
		HashMap<String, Variable> out = new HashMap<String,Variable>();
		in.put("Input", inVar);
		out.put("Output", outVar);
		
		FCreateRules createRules = new FCreateRules(in, out);
		createRules.setVisible(true);
	}

}
