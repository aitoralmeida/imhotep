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

public class Trend implements Serializable {
	
	private static final long serialVersionUID = -633439421600852144L;

	private final int year;
	private final int month;
	private final double value;
	
	public Trend(int year, int month, double value) {
		super();
		this.year = year;
		this.month = month;
		this.value = value;
	}
	public int getYear() {
		return this.year;
	}
	public int getMonth() {
		return this.month;
	}
	public double getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return "Trend [month=" + this.month + ", value=" + this.value + ", year=" + this.year + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.month;
		long temp;
		temp = Double.doubleToLongBits(this.value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + this.year;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trend other = (Trend) obj;
		if (this.month != other.month)
			return false;
		if (Double.doubleToLongBits(this.value) != Double
				.doubleToLongBits(other.value))
			return false;
		if (this.year != other.year)
			return false;
		return true;
	}
}
