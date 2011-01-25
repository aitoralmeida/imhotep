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

package piramide.multimodal.preprocessor;

import java.util.List;
import java.util.Map;

import piramide.multimodal.preprocessor.exceptions.EvaluationException;
import piramide.multimodal.preprocessor.exceptions.UserDefinedErrorException;
import piramide.multimodal.preprocessor.parser.Node;

public class Parser {
	final ExpressionEvaluator evaluator = new ExpressionEvaluator();
	
	public String parse(List<Node> nodes, Map<String, Object> variables) throws UserDefinedErrorException, EvaluationException {
		final StringBuilder builder = new StringBuilder();
		for(Node node : nodes){
			builder.append(node.generateCode(this.evaluator, variables));
		}
		return builder.toString();
	}
}
