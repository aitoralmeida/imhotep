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

package piramide.multimodal.preprocessor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import piramide.multimodal.preprocessor.exceptions.SyntaxErrorException;


public class ParameterScanner {
	
	private static final Pattern variableRegex = Pattern.compile("^[a-zA-Z_]+[a-zA-Z0-9_]*(\\.[a-zA-Z0-9_]+)*$");
	
	public String [] scanVariablesInLine(String line, int lineNumber) throws SyntaxErrorException{
		final String [] splitted = line.split("\\$\\{");
		
		final Set<String> variables = new HashSet<String>();
		for(int i = 1; i < splitted.length; ++i){
			if(splitted[i].indexOf("}") >= 0){
				final String variable = splitted[i].split("}")[0].trim();
				if(variable.length() == 0){
					throw new SyntaxErrorException("Empty variable name in line: " + lineNumber, lineNumber);
				}else{
					if(!variableRegex.matcher(variable).find())
						throw new SyntaxErrorException("Invalid variable name: " + variable + " in line: " + lineNumber, lineNumber);
				}
				variables.add(variable);
			}
		}
		
		return variables.toArray(new String[]{});
	}
	
	public String [] scanVariablesInFile(String fileContent) throws SyntaxErrorException {
		final String [] lines = fileContent.split("\n");
		Set<String> variables = new HashSet<String>();
		for(int lineNumber = 0; lineNumber < lines.length; ++lineNumber){
			final String line = lines[lineNumber];
			final List<String> variablesInLine = Arrays.asList(this.scanVariablesInLine(line, lineNumber));
			variables.addAll(variablesInLine);
		}
		return variables.toArray(new String[]{});
	}
	
	public String replaceVariables(String expression, String dictName, int lineNumber) throws SyntaxErrorException{
		
		final String [] variables = this.scanVariablesInLine(expression, lineNumber);
		
		String replacedExpression = expression;
		
		for(String variable : variables)
			replacedExpression = replacedExpression.replaceAll("\\$\\{\\s*" + variable + "\\s*\\}", dictName + ".get('" + variable + "')");
		
		return replacedExpression;
	}
	
	public String replaceVariables(String expression, Map<String, Object> variables){
		
		String replacedExpression = expression;
		
		for(String variable : variables.keySet())
			replacedExpression = replacedExpression.replaceAll("\\$\\{\\s*" + variable + "\\s*\\}", variables.get(variable).toString());
		
		return replacedExpression;
	}
}

