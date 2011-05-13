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
package piramide.interaction.reasoner.creator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import net.sourceforge.jFuzzyLogic.FIS;
import piramide.interaction.reasoner.RegionDistributionInfo;
import piramide.interaction.reasoner.creator.membershipgenerator.IMembershipFunctionGenerator;
import piramide.interaction.reasoner.creator.membershipgenerator.MembershipFunctionGeneratorFactory;
import piramide.interaction.reasoner.creator.membershipgenerator.MembershipFunctionGeneratorFactory.Generators;
import piramide.interaction.reasoner.db.DeviceCapability;
import piramide.interaction.reasoner.db.MobileDevices;
import piramide.interaction.reasoner.db.UserCapabilities;
import piramide.interaction.reasoner.db.UserCapabilities.UserCapability;
import piramide.interaction.reasoner.db.UserCapabilities.Value;
import piramide.interaction.reasoner.wizard.Variable;

public class FclCreator {
	
	private static Generators GENERATOR = Generators.iterative;
	
	public String createRuleFile(String blockName, Map<DeviceCapability, Variable> deviceInputVariables, Map<UserCapability, Variable> userInputVariables, Map<String, Variable> outputVariables, MobileDevices mobileDevices, String rules, WarningStore warningStore) throws InvalidSyntaxException {
		
		final StringBuilder fclCode = new StringBuilder();
		
		//Function Block Name
		fclCode.append("//Function block\n");
    	fclCode.append("FUNCTION_BLOCK " + blockName + "\n");
    	fclCode.append("\n");
    	
    	//Input Variable
    	fclCode.append("//Input variables\n");
    	fclCode.append("VAR_INPUT\n");
    	for(DeviceCapability inputVar : deviceInputVariables.keySet()){
    		fclCode.append(inputVar.name() + " : REAL;\n");
    	}
    	for(UserCapability inputVar : userInputVariables.keySet()){
    		fclCode.append(inputVar.name() + " : REAL;\n");
    	}
    	fclCode.append("END_VAR\n");
    	fclCode.append("\n");
    	
    	//Output Variable
    	fclCode.append("//Output variables\n");
    	fclCode.append("VAR_OUTPUT\n");
    	for(String outputVarName : outputVariables.keySet()){
    		fclCode.append(outputVarName + " : REAL;\n");
    	}
    	fclCode.append("END_VAR\n");
    	fclCode.append("\n");
    	
    	//Input variables fuzzification
    	fclCode.append("//Variable fuzzification\n");
    	for(DeviceCapability inputVar : deviceInputVariables.keySet()){
    		final String inputVarName = inputVar.name();
    		fclCode.append("//" + inputVarName + " fuzzification\n");
    		fclCode.append("FUZZIFY " + inputVarName + "\n");
    		final Variable variable = deviceInputVariables.get(inputVar);
    		final Map<Number, Double> value2trend = mobileDevices.getValue2trend(DeviceCapability.valueOf(inputVarName));
    		final MembershipFunctionGeneratorFactory factory = new MembershipFunctionGeneratorFactory();
    		final IMembershipFunctionGenerator mfg = factory.create(value2trend, GENERATOR, warningStore);
    		final RegionDistributionInfo[] linguisticTerms = (RegionDistributionInfo[])variable.getTerms().toArray();
    		for(LinguisticTermMembershipFunction func : mfg.createFunctions(linguisticTerms)){
    			fclCode.append(func.toString());
    			fclCode.append("\n");
    		}
    		fclCode.append("END_FUZZIFY\n");
    	}
    	
    	for(UserCapability inputVar : userInputVariables.keySet()){
    		final String inputVarName = inputVar.name();
    		fclCode.append("//" + inputVarName + " fuzzification\n");
    		fclCode.append("FUZZIFY " + inputVarName + "\n");
    		final Variable variable = userInputVariables.get(inputVar);
    		fclCode.append(createUserInputTerms(inputVar, variable.getTerms()));
    		fclCode.append("\n");
    		fclCode.append("END_FUZZIFY\n");
    	}
    	
    	//Output variable defuzzification
    	fclCode.append("\n");
    	fclCode.append("//Variable defuzzification\n");
    	for(String outputVarName : outputVariables.keySet()){
    		fclCode.append("DEFUZZIFY " + outputVarName + "\n");
    		Variable outputVar = outputVariables.get(outputVarName);
    		String outputVarTerms = createOutputTerms(outputVar.getTerms());
    		fclCode.append(outputVarTerms);
    		fclCode.append("\n");
    		fclCode.append("\t//Default defuzzification method is Center of Area\n");
    		fclCode.append("\tMETHOD : COA;\n");
    		fclCode.append("\t// Default value is 0 (if no rule activates defuzzifier)\n");
    		fclCode.append("\tDEFAULT := 0;\n");
    		fclCode.append("END_DEFUZZIFY\n");
    		fclCode.append("\n");    	    
    	}
    	
    	//Rule block
    	fclCode.append("//Rules\n");
    	fclCode.append("RULEBLOCK No1\n");
    	fclCode.append("\t//Default methods\n");
    	fclCode.append("\tAND : MIN;\n");
    	fclCode.append("\tACT : MIN;\n");
    	fclCode.append("\tACCU : MAX;\n");
    	fclCode.append("\n");
    	fclCode.append("\t");
   		fclCode.append(rules);
    	
    	fclCode.append("\n");
    	fclCode.append("END_RULEBLOCK\n");
    	fclCode.append("\n");
    	fclCode.append("END_FUNCTION_BLOCK\n");
    	
    	final String code = fclCode.toString();
    	checkFclCode(code);
    	
    	return code;
		
	}
	
	private void checkFclCode(String fclCode) throws InvalidSyntaxException{
		
		final ByteArrayInputStream fclBytes = new ByteArrayInputStream(fclCode.getBytes());
        
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream ps = new PrintStream(baos);
		final FIS fis = checkFclCodeErrors(fclBytes, ps);
        
        
        String errors;
		try {
			ps.flush();
			baos.flush();
			errors = baos.toString();
		} catch (IOException e1) {
			errors = ""; // and cry
		}
        
        String[] errorLines = errors.split("\n");
        
        if(errors.isEmpty()){
        	try {
        		fis.evaluate();
			} catch (Exception e) {
				throw new InvalidSyntaxException(e);
			}
        } else{
        	final String [] fclLines = fclCode.split("\n");
        	final StringBuilder builder = new StringBuilder();
        	for(String errorLine : errorLines){
        		try{
	        		final String strLineNumber = errorLine.substring(0, errorLine.indexOf(':')).substring("line ".length());
	        		String errorMessage = "Error in character " + errorLine.substring(errorLine.indexOf(':') + 1);
	        		try{
	        			final int lineNumber = Integer.parseInt(strLineNumber);
	        			errorMessage += " in line <" + fclLines[lineNumber - 1] + ">";
	        		}catch(Exception e){
	        			errorMessage += " in line <COULD NOT RETRIEVE LINE>";
	        		}
	        		builder.append(errorMessage + "\n");
        		}catch(Exception e){
        			builder.append("Error message: " + errorLine + "\n");
        		}
        	}
        	throw new InvalidSyntaxException(builder.toString());
        }
	}

	
	/**
	 * 
	 * jfuzzylogic does not report the error messages anywhere than in System.err. Since we are
	 * showing those messages to the user in a remote machine, we have to replace System.err by
	 * other PrintStream, run it, replace it again by the original System.err and return the 
	 * results. This must be synchronized so as to avoid that two threads change the same 
	 * System.err concurrently. 
	 */
	private FIS checkFclCodeErrors(final ByteArrayInputStream fclBytes,
			final PrintStream ps) throws InvalidSyntaxException{
		
		final FIS fis;
		
		synchronized (FclCreator.class) {
			final PrintStream syserr = System.err;
			Field field;
			try {
				final Class<?> klass = Class.forName(System.class.getName());
				field = klass.getDeclaredField("err");
				field.setAccessible(true);
				
				final Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
				
				
				field.set(null, ps);
			} catch (Exception e) {
				System.err.println("Can't show the error messages due to error: " + e.getMessage());
				e.printStackTrace();
				field = null;
			}
	        
			try{
				fis = FIS.load(fclBytes,true);
			}catch(RuntimeException e){
				throw new InvalidSyntaxException("Could not load FIS file: " + e.getMessage(), e);
			}finally{
				if(field != null){
					try {
						field.set(null, syserr);
					} catch (Exception e) {
						// Can't even show in System.err }:-D
						// Worst exception... EVER
					}
				}
			}
		}
		return fis;
	}
	
	private String createOutputTerms(List<RegionDistributionInfo> terms){
		final StringBuilder outputTerms = new StringBuilder();
		for (int i = 0; i < terms.size(); i++) {
			outputTerms.append("\tTERM " + terms.get(i).getName() + "  := ");
			if (i==0){
				outputTerms.append("(0, 1) (1, 0);\n");
			} else if (i == terms.size() - 1){
				outputTerms.append("(" + (terms.size() - 2) + ", 0) ( " + (terms.size() - 1) + ", 1);\n");
			} else{
				outputTerms.append("(" + (i-1) + ", 0) (" + i + ", 1) (" + (i + 1) + ", 0);\n");
			}
		}
		return outputTerms.toString();
	}
	
	private String createUserInputTerms(UserCapability capability, List<RegionDistributionInfo> terms){
		final Value value = UserCapabilities.get(capability);
		
		final double distance = value.getUpperBoundary() - value.getLowerBoundary();
		final double slotSize = distance / (terms.size() - 1);
		
		double current = value.getLowerBoundary();
		
		final StringBuilder outputTerms = new StringBuilder();
		for (int i = 0; i < terms.size(); i++) {
			outputTerms.append("\tTERM " + terms.get(i).getName() + "  := ");
			
			if(i != 0){
				// Only ascending
				outputTerms.append("(");
				outputTerms.append(current);
				outputTerms.append(", 0)");
				
				outputTerms.append("(");
				outputTerms.append(current + slotSize);
				outputTerms.append(", 1) ");
				current += slotSize;
			}
			
			if(i != terms.size() - 1){
				// Only descending
				outputTerms.append("(");
				outputTerms.append(current);
				outputTerms.append(", 1) ");
				
				outputTerms.append("(");
				outputTerms.append(current + slotSize);
				outputTerms.append(", 0)");
			}
			
			outputTerms.append(";\n");
		}
		
		return outputTerms.toString();
	}	
}
