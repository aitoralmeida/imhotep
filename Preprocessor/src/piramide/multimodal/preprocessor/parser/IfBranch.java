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

public class IfBranch extends Branch {
	
	private final String condition;
	private final int lineNumber;
	
	public IfBranch(int lineNumber, String condition, List<Node> nodes){
		super(nodes);
		this.lineNumber = lineNumber;
		this.condition  = condition;
	}

	public IfBranch(int lineNumber, String condition, Node ... nodes){
		this(lineNumber, condition, Arrays.asList(nodes));
	}

	int getLineNumber(){
		return this.lineNumber;
	}
	
	public String getCondition() {
		return this.condition;
	}

	@Override
	public boolean equals (Object obj){
		if (!(obj instanceof IfBranch)){
			return false;
		}
		
		final IfBranch branch = (IfBranch) obj;
		
		if (this.condition.equals(branch.condition) && this.getNodes().size() == branch.getNodes().size()){
			for (int i = 0; i < this.getNodes().size(); i++) {
				if (!this.getNodes().get(i).equals(branch.getNodes().get(i))){
					return false;
				}
			}
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public String toString(){
		String ifBranch = "<IfBranch condition='" + this.condition + "'>\n";
		for(Node node : this.getNodes())
			ifBranch += "\t" + node.toString() + "\n";
		
		return ifBranch + "</IfBranch>";
	}
}
