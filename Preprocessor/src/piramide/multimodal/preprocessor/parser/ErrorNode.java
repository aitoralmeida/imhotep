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

import piramide.multimodal.preprocessor.ExpressionEvaluator;
import piramide.multimodal.preprocessor.exceptions.UserDefinedErrorException;

public class ErrorNode extends Node{
	private final String errorText;
	private final int lineNumber;
	
	public ErrorNode(int lineNumber, String errorText){
		this.lineNumber = lineNumber;
		this.errorText  = errorText;
	}

	public String getErrorText() {
		return this.errorText;
	}
	
	@Override
	public boolean equals (Object obj){
		if (!(obj instanceof ErrorNode)){
			return false;
		}
		
		final ErrorNode node = (ErrorNode) obj;
		
		return this.errorText.equals(node.errorText);
	}
	
	@Override
	public String toString(){
		return "<ErrorNode text='" + this.errorText + "' />";
	}

	@Override
	public String generateCode(ExpressionEvaluator evaluator, Map<String, Object> variables)
			throws UserDefinedErrorException {
		throw new UserDefinedErrorException("User defined error: <" + this.errorText + "> in line: " + this.lineNumber, this.lineNumber);
	}
}
