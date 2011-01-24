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
package piramide.interaction.reasoner.creator;

import java.util.Locale;



public class LinguisticTermMembershipFunction {
	private final Point [] points;
	private final String name;
	
	public LinguisticTermMembershipFunction(String name, Point [] points){
		this.points = points;
		this.name   = name;
	}
	
	public Point[] getPoints() {
		return this.points;
	}
	
	public String getName() {
		return this.name;
	}

	@Override
	public String toString(){
		// TERM small := (0, 1) (320.0, 0.99594583074159093) 
		final StringBuffer buffer = new StringBuffer();
		
		buffer.append("\tTERM ");
		buffer.append(this.name);
		buffer.append(" := ");
		for(Point point : this.points){
			buffer.append("(");
			buffer.append(String.format(Locale.US, "%.8f", Double.valueOf(point.getValue())));
			buffer.append(", ");
			buffer.append(String.format(Locale.US, "%.8f", Double.valueOf(point.getTrend())));
			buffer.append(") ");
		}
		buffer.append(";");
		return buffer.toString();
	}
	
}
