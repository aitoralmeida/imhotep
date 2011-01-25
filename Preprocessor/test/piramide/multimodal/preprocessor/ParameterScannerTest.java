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

package piramide.multimodal.preprocessor;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import piramide.multimodal.preprocessor.exceptions.SyntaxErrorException;


public class ParameterScannerTest {
	
	private ParameterScanner scanner;
	
	@Before
	public void setUp(){
		this.scanner = new ParameterScanner();
	}
	
	@Test
	public void testScan() throws Exception {
		final String [] actualVariables = this.scanner.scanVariablesInLine("${   variable1 } ${variable2    }", 0);
		final String [] expectedVariables = {"variable1", "variable2"};
		assertArrayEquals(expectedVariables, actualVariables);
	}
	
	@Test
	public void testScanRepeated() throws Exception {
		final String [] actualVariables = this.scanner.scanVariablesInLine("${   variable1 } ${variable2    } ${variable2}", 0);
		final String [] expectedVariables = {"variable1", "variable2"};
		assertArrayEquals(expectedVariables, actualVariables);
	}
	
	@Test
	public void testScanWithInternalBraces() throws Exception {
		final String [] actualVariables = this.scanner.scanVariablesInLine("${   variable1 } $foo { ${variable2    } }", 0);
		final String [] expectedVariables = {"variable1", "variable2"};
		assertArrayEquals(expectedVariables, actualVariables);
	}
	
	@Test
	public void testScanWithNoVariable() throws Exception{
		final String [] actualVariables = this.scanner.scanVariablesInLine("} $foo {}", 0);
		final String [] expectedVariables = {};
		assertArrayEquals(expectedVariables, actualVariables);
	}
	
	@Test
	public void testScanNotFinishedVariable() throws Exception{
		final String [] actualVariables = this.scanner.scanVariablesInLine("} $foo {} ${foo${foo", 0);
		final String [] expectedVariables = {};
		assertArrayEquals(expectedVariables, actualVariables);
	}
	
	@Test
	public void testScanValidVariables() throws Exception {
		final String [] actualVariables = this.scanner.scanVariablesInLine("${   variable1 } ${variable2    } ${piramide.devices09.foo.99.bar} ${something_.with_} ${_foo}", 0);
		
		final String [] expectedVariables = {"variable1", "variable2", "piramide.devices09.foo.99.bar", "something_.with_", "_foo"};
		final String [] expectedOrderedVariables = new HashSet<String>(Arrays.asList(expectedVariables)).toArray(new String[]{});
		assertArrayEquals(expectedOrderedVariables, actualVariables);
	}
	
	@Test
	public void testScanInValidVariables() throws Exception {
		final String [] invalidNames = {
				" ${} ", " ${   } ", 
				" ${ 9variable }", " ${ this..is} ",
				" ${ ^foo} ", " ${ %} "
		};
		for(String invalidName : invalidNames){
			try{
				this.scanner.scanVariablesInLine(invalidName, 0);
				fail( SyntaxErrorException.class.getName() + " expected with invalid name: " + invalidName);
			}catch(SyntaxErrorException see){
				// expected
			}catch(Exception e){
				fail( SyntaxErrorException.class.getName() + " expected with invalid name: " + invalidName + "; got: " + e.getMessage());
			}
		}
	}
	
	@Test
	public void testReplaceVariables() throws Exception{
		final String actualExpression   = this.scanner.replaceVariables("${variable1} ${variable2}", "params", 0);
		final String expectedExpression = "params.get('variable1') params.get('variable2')"; 
		assertEquals(expectedExpression, actualExpression);
	}
	
	@Test
	public void testReplaceVariablesWithSpaces() throws Exception{
		final String actualExpression   = this.scanner.replaceVariables("${   variable1} ${ variable2   }", "params", 0);
		final String expectedExpression = "params.get('variable1') params.get('variable2')"; 
		assertEquals(expectedExpression, actualExpression);
	}
	
	@Test
	public void testReplaceVariables_Map() throws Exception{
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("variable1", "foo");
		variables.put("variable2", 5.6f);
		final String actualExpression   = this.scanner.replaceVariables("${variable1} ${variable2}", variables);
		final String expectedExpression = "foo 5.6"; 
		assertEquals(expectedExpression, actualExpression);
	}
}
