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
package piramide.interaction.reasoner.db;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserCapabilities{
	
	public static enum UserCapability {
		sight,
		earing
	}
	
	public final static class Value{
		private final double lowerBoundary;
		private final double upperBoundary;
		
		private Value(double lowerBoundary, double upperBoundary) {
			this.lowerBoundary = lowerBoundary;
			this.upperBoundary = upperBoundary;
		}
		
		public double getLowerBoundary() {
			return this.lowerBoundary;
		}
		public double getUpperBoundary() {
			return this.upperBoundary;
		}
	}
	
	private final static Map<UserCapability, Value> values = new HashMap<UserCapability, Value>();
	
	static{
		
		values.put(UserCapability.sight, new Value(0.0, 40.0));
		values.put(UserCapability.earing, new Value(-10.0, 20.0));
		
		
		for(UserCapability capability : UserCapability.values())
			if(!values.containsKey(capability))
				throw new IllegalStateException("User capability: " + capability + " not filled");
	}

	public static Set<UserCapability> keySet(){
		return values.keySet();
	}
	
	public static Collection<Value> values(){
		return values.values();
	}
	
	public static Value get(UserCapability capability){
		return values.get(capability);
	}
	
	public static boolean containsKey(UserCapability capability){
		return values.containsKey(capability);
	}
	
	public static int size(){
		return values.size();
	}
}
