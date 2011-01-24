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
package piramide.interaction.reasoner.creator.membershipgenerator.regionaccumulator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

abstract class AbstractRegion {
	protected final Map<Number, Double> values2trends;
	protected final List<Number> orderedKeys;
	protected final double sumOfValues;
	
	AbstractRegion(Map<Number, Double> values2trends) {
		this.values2trends = values2trends;
		this.orderedKeys = Arrays.asList(values2trends.keySet().toArray(new Number[]{}));
		Collections.sort(this.orderedKeys, new Comparator<Number>() {
			@Override
			public int compare(Number number1, Number number2) {
				return new Double(number1.doubleValue()).compareTo(Double.valueOf(number2.doubleValue()));
			}
		});
		this.sumOfValues = calculateSumOfValues();
	}

	Map<Number, Double> getResults2trends() {
		return this.values2trends;
	}

	private double calculateSumOfValues() {
		double total = 0.0;
		
		for(double value : this.values2trends.values()){
			total += value;
		}
		return total;
	}
	
}
