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
 *
 */

package piramide.multimodal.preprocessor.parser;

import java.util.Map;

import piramide.multimodal.preprocessor.ExpressionEvaluator;
import piramide.multimodal.preprocessor.exceptions.UserDefinedErrorException;

public class TextNode extends Node {
	private final String text;
	
	public TextNode(String text){
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
	
	@Override
	public boolean equals (Object obj){
		
		if (!(obj instanceof TextNode)){
			return false;
		}
		
		final TextNode node = (TextNode) obj;
		return this.text.equals(node.text);
	}
	
	@Override
	public String toString(){
		return "<TextNode text='" + this.text + "'/>";
	}

	@Override
	public String generateCode(ExpressionEvaluator evaluator, Map<String, Object> variables)
			throws UserDefinedErrorException {
		return this.text;
	}
}
