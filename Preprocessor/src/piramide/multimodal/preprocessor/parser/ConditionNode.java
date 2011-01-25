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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import piramide.multimodal.preprocessor.ExpressionEvaluator;
import piramide.multimodal.preprocessor.exceptions.EvaluationException;
import piramide.multimodal.preprocessor.exceptions.InvalidTypeException;
import piramide.multimodal.preprocessor.exceptions.SyntaxErrorException;
import piramide.multimodal.preprocessor.exceptions.UserDefinedErrorException;

public class ConditionNode extends Node {
	private final List<Branch> branches;
	private final int lineNumber;
	
	public ConditionNode(int lineNumber, List<Branch> branches) throws SyntaxErrorException{
		if(branches.size() < 1){
			throw new SyntaxErrorException("There should be at least one branches: an 'if' in line: " + lineNumber, lineNumber);
		}
		
		for(int i = 0; i < branches.size() - 1; ++i)
			if(!(branches.get(i) instanceof IfBranch)){
				throw new SyntaxErrorException("Every branch must be an IfBranch (except for the last one) in line: " + lineNumber, lineNumber);
			}
					
		this.lineNumber = lineNumber;
		this.branches   = branches;
	}
	
	public ConditionNode(int lineNumber, Branch ... branches) throws SyntaxErrorException{
		this(lineNumber, Arrays.asList(branches));
	}
	
	public List<Branch> getBranches(){
		return this.branches;
	}
	
	@Override
	public String generateCode(ExpressionEvaluator evaluator, Map<String, Object> variables)
			throws UserDefinedErrorException, EvaluationException {
		for(Branch branch : this.branches){
			if(branch instanceof IfBranch){
				final IfBranch ifBranch = (IfBranch)branch;
				final String condition = ifBranch.getCondition();
				
				final boolean conditionSucceeded = evaluateCondition(evaluator, ifBranch.getLineNumber(), condition, variables);
				
				if(conditionSucceeded)
					return processBranch(evaluator, branch, variables);
			}else{
				return processBranch(evaluator, branch, variables);
			}
		}
		return "";
	}

	private boolean evaluateCondition(ExpressionEvaluator evaluator, int line, String condition, Map<String, Object> variables) throws EvaluationException,
			InvalidTypeException {
		final Object evaluatedCondition = evaluator.eval(line, condition, variables);
		final boolean conditionSucceeded;
		if(evaluatedCondition instanceof Boolean)
			conditionSucceeded = ((Boolean)evaluatedCondition).booleanValue();
		else
			throw new InvalidTypeException("Boolean expression expected, found: " + evaluatedCondition.getClass().getName() + " in line: " + this.lineNumber, this.lineNumber);
		return conditionSucceeded;
	}

	private String processBranch(ExpressionEvaluator evaluator, Branch branch, Map<String, Object> variables)
			throws UserDefinedErrorException, EvaluationException {
		final StringBuilder resultingText = new StringBuilder();
		for(Node node : branch.getNodes()){
			resultingText.append(node.generateCode(evaluator, variables));
		}
		return resultingText.toString();
	}
	
	@Override
	public boolean equals (Object obj){
		if (!(obj instanceof ConditionNode)){
			return false;
		}
		
		final ConditionNode node = (ConditionNode) obj;
		
		if (this.branches.size() != node.branches.size())
			return false;
		
		for (int i = 0; i < this.branches.size(); i++)
			if (!this.branches.get(i).equals(node.branches.get(i)))
				return false;
			
		return true;
	}
	
	@Override
	public String toString(){
		String conditionNode = "<ConditionNode>\n";
		for(Branch branch : this.branches)
			conditionNode += "\t" + branch.toString() + "\n";
		return conditionNode + "</ConditionNode>";
	}
}
