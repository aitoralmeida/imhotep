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

import java.util.Arrays;
import java.util.List;

public class ElseBranch extends Branch {

	public ElseBranch(List<Node> nodes){
		super(nodes);
	}

	public ElseBranch(Node ... nodes){
		super(Arrays.asList(nodes));
	}

	@Override
	public boolean equals (Object obj){
		if (!(obj instanceof ElseBranch)){
			return false;
		}
		
		final ElseBranch branch = (ElseBranch) obj;
		
		if (this.getNodes().size() == branch.getNodes().size()){
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
		String ifBranch = "<ElseBranch>\n";
		for(Node node : this.getNodes())
			ifBranch += "\t" + node.toString() + "\n";
		
		return ifBranch + "</ElseBranch>";
	}
}
