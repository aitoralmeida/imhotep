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
import java.util.Collections;
import java.util.Comparator;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.Variable;

/**
 * Test parsing an FCL file
 * @author pcingola@users.sourceforge.net
 */
public class Screen {
    public static void main(String[] args) throws Exception {
        // Load from 'FCL' file
        String fileName = "fcl/screen.fcl";
        FIS fis = FIS.load(fileName,true);
        // Error while loading?
        if( fis == null ) { 
            System.err.println("Can't load file: '" 
                                   + fileName + "'");
            return;
        }

        // Show 
        fis.chart();

        // iPad
        // fis.setVariable("screensize", 30000.0);
        // fis.setVariable("resolution", 786432.0);
        
        // HTC Magic
        //fis.setVariable("screensize", 5000.0);
        //fis.setVariable("resolution", 153600.0);
        
        // MaruteMovil (Nokia 5130)
        fis.setVariable("screensize", 1200.0);
        fis.setVariable("resolution", 76800.0);

        // Evaluate
        fis.evaluate();
        
        System.out.println(fis.getVariable("resolution").getUniverseMax());
        

        // Show output variable's chart 
        // fis.getVariable("screensize").chartDefuzzifier(true);

        // Print ruleSet
        final Variable variable = fis.getVariable("video");
        final String maxTerm = Collections.max(variable.getLinguisticTerms().keySet(), new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return Double.compare(variable.getMembership(o1), variable.getMembership(o2));
			}
		});
        System.out.println("Es : " + maxTerm);
        System.out.println(variable.defuzzify());
    }
}
