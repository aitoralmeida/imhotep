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

import java.io.Serializable;

public class QueryInformation implements Serializable {
	
	private static final long serialVersionUID = -3592260652384312131L;
	
	private int maxMonth;
	private int maxYear;
	
	public int getMaxMonth() {
		return this.maxMonth;
	}
	
	public void setMaxMonth(int maxMonth) {
		this.maxMonth = maxMonth;
	}
	
	public int getMaxYear() {
		return this.maxYear;
	}
	
	public void setMaxYear(int maxYear) {
		this.maxYear = maxYear;
	}
}
