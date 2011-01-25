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
import java.util.Map;

import piramide.multimodal.preprocessor.exceptions.EvaluationException;
import piramide.multimodal.preprocessor.exceptions.SyntaxErrorException;
import piramide.multimodal.preprocessor.exceptions.UserDefinedErrorException;
import piramide.multimodal.preprocessor.parser.Node;

public class Preprocessor implements IPreprocessor {
	
	//Parses the code-tree, returning the adapted source code.
	final static Parser parser = new Parser();
	//Infers new variables from the initial variables
//	final static Reasoner reasoner = new Reasoner();
	

	/* (non-Javadoc)
	 * @see piramide.multimodal.preprocessor.IPreprocessor#getUsedVariables(java.lang.String)
	 */
	@Override
	public String [] getUsedVariables (String code) throws SyntaxErrorException
	{
		final ParameterScanner scanner = new ParameterScanner();
		return scanner.scanVariablesInFile(code);
	}
	
	@Override
	@Deprecated
	public PreprocessorResult preprocess (String directiveStart, String code, Map<String, Object> variableValues) throws UserDefinedErrorException, EvaluationException {
		return this.preprocess("<not provided>", directiveStart, code, variableValues);
	}
	
	/* (non-Javadoc)
	 * @see piramide.multimodal.preprocessor.IPreprocessor#preprocess(java.lang.String, java.util.Map)
	 */
	@Override
	public PreprocessorResult preprocess (String filename, String directiveStart, String code, Map<String, Object> variableValues) throws UserDefinedErrorException, EvaluationException
	{
//		Map<String, Object> expandedVariableValues = reasoner.expandVariables(variableValues);
		//Creates the tree representing the code to be parsed
		final Lexer lexer = new Lexer(directiveStart);
		final Node[] nodes  = lexer.lex(filename, code);
		final String parsedCode = parser.parse(Arrays.asList(nodes), variableValues);
		
		return new PreprocessorResult(parsedCode, variableValues);
	}

}
