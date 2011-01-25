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
 *         Pablo Ordu�a <pablo.orduna@deusto.es>
 *
 */

package piramide.multimodal.fuzzyreasoner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class Reasoner implements IReasoner{
	
	private FIS fuzzyEngine;
	final static private String FILE_NAME = "fcl/tipper.fcl";
	final static String NO_TERM = "NO_TERM";
	final static String FUNCTION_BLOCK_NAME = "variableInferenceRules";
	
	public Reasoner()
	{
		this.loadRules(Reasoner.FILE_NAME);
	}
	
	@Override
	public Map<String, Object> expandVariables (Map<String, Object> initialVariableValues)
	{
		Map<String, Object> finalVariableValues = initialVariableValues;
		if( this.fuzzyEngine != null ) {
			setInputs(initialVariableValues);
			this.fuzzyEngine.evaluate();			
			
			final Map<String, String> inferredVariableValues = this.getAllOutputTerms(Reasoner.FUNCTION_BLOCK_NAME);
			for(Iterator<String> keys = inferredVariableValues.keySet().iterator(); keys.hasNext();){
				String key = keys.next();
				//If something has been inferred
				if (!inferredVariableValues.get(key).equals(Reasoner.NO_TERM))
					finalVariableValues.put(key, inferredVariableValues.get(key));
			}
		}
		return finalVariableValues;
	}
	
	@Override
	public Map<String, Object> expandVariables(	Map<String, Object> initialVariableValues, String location) {
		// TODO to be implemented when geoLocation is finished
		return null;
	}
	
	public void loadRules(String fileName){		
	    this.fuzzyEngine = FIS.load(fileName,true);
	    if( this.fuzzyEngine == null ) { 
	        System.out.println("Invalid file");
	    }		
	}
	
	public void setInputs(Map<String, Object> inputVars){ 			
	    Set<String> keys = inputVars.keySet();
	    for (String varName : keys) {
	    	try {
	    		Double varValue = Double.valueOf(inputVars.get(varName).toString());
	    		this.fuzzyEngine.setVariable(varName, varValue);				
			} catch (NumberFormatException e) {
				System.out.println(varName + "is not a number");
			}			
		}
	}
	
	public String getMostProbableValue(Variable variable)
	{
		String bestTerm = "";
		Double bestValue = 0.0;
	
		//search for the most probable term
		//TODO add some kind of minimal tresshold to the membership value
		for (Iterator<String> terms = variable.iteratorLinguisticTermNames(); terms.hasNext();) {
			String term = terms.next();
			if (variable.getMembership(term) >= bestValue){
				bestTerm = term;
				bestValue = variable.getMembership(term);
			}
		}
		//If the best value is not positive nothing has been inferred for this variable
		if (bestValue == 0.0)
			return Reasoner.NO_TERM;
		else
			return bestTerm;
	}
	
	public Map<String, String> getAllOutputTerms(String functionBlockName)
	{
		HashMap<String, String> res = new HashMap<String,String>();
		HashMap <String, Variable> variables = this.fuzzyEngine.getFunctionBlock(functionBlockName).getVariables();
		for(Iterator<Variable> varValues = variables.values().iterator();varValues.hasNext();){
			Variable variable = varValues.next();
			//we are only interested in output variables
			if (variable.isOutputVarable()){
				res.put(variable.getName(), this.getMostProbableValue(variable));
			}
		}
		return res;
	}

	
	//For debug purposes only
	public void showChart (){
		this.fuzzyEngine.chart();		
	}

	
	
	

}
