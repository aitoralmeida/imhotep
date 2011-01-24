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
package piramide.interaction.reasoner.creator.membershipgenerator;

import java.util.Map;

import piramide.interaction.reasoner.creator.WarningStore;
import piramide.interaction.reasoner.creator.membershipgenerator.iterativeregion.IterativeRegionMembershipFunctionGenerator;
import piramide.interaction.reasoner.creator.membershipgenerator.regionaccumulator.RegionAccumulatorMembershipFunctionGenerator;

public class MembershipFunctionGeneratorFactory {
	
	public static enum Generators{
		regionAccumulator,
		iterative
	}
	
	public IMembershipFunctionGenerator create(Map<Number, Double> values2trends, Generators generator, WarningStore warningStore){
		switch(generator){
			case regionAccumulator: return new RegionAccumulatorMembershipFunctionGenerator(values2trends);
			case iterative: return new IterativeRegionMembershipFunctionGenerator(values2trends, warningStore);
		}
		throw new IllegalArgumentException("generator not supported: " + generator);
	}
}
