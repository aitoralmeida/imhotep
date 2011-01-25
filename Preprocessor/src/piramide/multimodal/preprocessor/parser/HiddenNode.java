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

package piramide.multimodal.preprocessor.parser;

import java.util.Map;

import piramide.multimodal.preprocessor.Directives;
import piramide.multimodal.preprocessor.ExpressionEvaluator;
import piramide.multimodal.preprocessor.ParameterScanner;
import piramide.multimodal.preprocessor.exceptions.EvaluationException;
import piramide.multimodal.preprocessor.exceptions.SyntaxErrorException;
import piramide.multimodal.preprocessor.exceptions.UserDefinedErrorException;

/*
 * Represents something like:
 *  //# private String foo = "bar";
 *  
 */
public class HiddenNode extends Node{
	private final String hiddenText;
	private final int lineNumber;
	
	public HiddenNode(int lineNumber, String hiddenText){
		this.lineNumber = lineNumber;
		this.hiddenText = hiddenText;
	}

	public String getHiddenText() {
		return this.hiddenText;
	}
	
	@Override
	public boolean equals (Object obj){
		if (!(obj instanceof HiddenNode)){
			return false;
		}
		
		final HiddenNode node = (HiddenNode) obj;
		
		return this.hiddenText.equals(node.hiddenText);
	}

	@Override
	public String generateCode(ExpressionEvaluator evaluator, Map<String, Object> variables)
			throws UserDefinedErrorException, EvaluationException {
		
		final String [] tokens = this.hiddenText.split(Directives.EXPRESION_START);
		
		final ParameterScanner scanner = new ParameterScanner();
		final StringBuilder resultingCode = new StringBuilder();
		resultingCode.append(scanner.replaceVariables(tokens[0], variables));
		
		for(int i = 1; i < tokens.length; ++i){
			final int closingParenthesisPos = this.findClosingParenthesis(tokens[i]);
			final String expression = tokens[i].substring(0, closingParenthesisPos);
			final Object evaluated  = evaluator.eval(this.lineNumber, expression, variables);
			resultingCode.append(evaluated.toString());
			
			final String rest = tokens[i].substring(closingParenthesisPos + 1);
			resultingCode.append(scanner.replaceVariables(rest, variables));
		}
		
		return resultingCode.toString();
	}
	
	private int findClosingParenthesis(String token) throws SyntaxErrorException{
		int numberOfOpeningParenthesis = 0;
		for(int i = 0; i < token.length(); ++i){
			if(token.charAt(i) == Directives.EXPRESSION_END){
				if(numberOfOpeningParenthesis == 0)
					return i;
				else
					numberOfOpeningParenthesis--;
			}else if(token.charAt(i) == Directives.PARENTHESIS_START)
				numberOfOpeningParenthesis++;
		}
		throw new SyntaxErrorException("Missing ')' in expression: " + token + " in line: " + this.lineNumber, this.lineNumber);
	}
	
}
