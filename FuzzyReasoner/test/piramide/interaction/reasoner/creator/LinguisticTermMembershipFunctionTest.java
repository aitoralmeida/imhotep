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

import static org.junit.Assert.*;

import org.junit.Test;



public class LinguisticTermMembershipFunctionTest {

	@Test
	public void testToString() {
		final Point [] points = new Point[]{
			new Point(1.0, 10.0),
			new Point(1.5, 20.0)
		};
		final LinguisticTermMembershipFunction func = new LinguisticTermMembershipFunction("", points);
		final String funcString = func.toString();
		
		final String expectedString = "\tTERM  := (1.00000000, 10.00000000) (1.50000000, 20.00000000) ;";
		
		assertEquals(expectedString, funcString);
	}
	
}
