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

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.junit.Test;

import piramide.multimodal.preprocessor.exceptions.FunctionsNotSupportedException;
import piramide.multimodal.preprocessor.exceptions.IllegalCodeException;
import piramide.multimodal.preprocessor.exceptions.InvalidTypeException;
import piramide.multimodal.preprocessor.exceptions.SyntaxErrorException;

public class ExpressionEvaluatorTest {
	
	@Test
	public void testAddInt() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "2 + 2");
		assertTrue("Expected Integer", res instanceof Integer);
		assertEquals(4, res);
	}
	
	@Test
	public void testAddStrings() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "'hola' + 'hola'");
		assertTrue("Expected String", res instanceof String);
		assertEquals("holahola", res);
	}
	
	@Test
	public void testAddFloat() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "2.0 + 2.0");
		assertTrue("Expected Float", res instanceof Float);
		assertEquals(4.0f, res);
	}
	
	@Test
	public void testAddLong() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, (Long.MAX_VALUE -4) + "+ 1");
		assertTrue("Expected Long", res instanceof Long);
		assertEquals(Long.MAX_VALUE-3, res);
	}

	@Test
	public void testCompareStrings() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "'hola' == 'hola'");
		assertTrue("Expected Boolean", res instanceof Boolean);
		assertEquals(true, res);
	}
	
	@Test
	public void testCompareInt() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "1 == 1");
		assertTrue("Expected Boolean", res instanceof Boolean);
		assertEquals(true, res);
	}
	
	@Test
	public void testCompareFloat() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "4.0 == 4.0");
		assertTrue("Expected Boolean", res instanceof Boolean);
		assertEquals(true, res);
	}
	
	@Test
	public void testAndTrue1() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "True and True");
		assertTrue("Expected Boolean", res instanceof Boolean);
		assertEquals(true, res);
	}
	
	@Test
	public void testAndTrue2() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "True and (True and True)");
		assertTrue("Expected Boolean", res instanceof Boolean);
		assertEquals(true, res);
	}
	
	@Test
	public void testAndFalse() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "True and False");
		assertTrue("Expected Boolean", res instanceof Boolean);
		assertEquals(false, res);
	}
	
	@Test
	public void testNot1() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "not True");
		assertTrue("Expected Boolean", res instanceof Boolean);
		assertEquals(false, res);
	}
	
	@Test
	public void testNot2() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "not (True)");
		assertTrue("Expected Boolean", res instanceof Boolean);
		assertEquals(false, res);
	}
	
	@Test
	public void testOrTrueTrue() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "True or True");
		assertTrue("Expected Boolean", res instanceof Boolean);
		assertEquals(true, res);
	}
	
	@Test
	public void testOrTrueFalse() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "True or False");
		assertTrue("Expected Boolean", res instanceof Boolean);
		assertEquals(true, res);
	}
	
	@Test
	public void testOrFalseFalse() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "False or False");
		assertTrue("Expected Boolean", res instanceof Boolean);
		assertEquals(false, res);
	}	
	
	@Test(expected=SyntaxErrorException.class)
	public void testSyntaxError() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
        evaluator.eval(0, "( ");
	}
	
	@Test(expected=IllegalCodeException.class)
	public void testGibberish() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
        evaluator.eval(0, "fdgfdfdgfdfdfdg");
	}
	
	@Test(expected=InvalidTypeException.class)
	public void testComplexTypes() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		evaluator.eval(0, "5+5j");
	}
	
	@Test
	public void testParameter_Boolean() throws Exception{
		final ExpressionEvaluator evaluator = new ExpressionEvaluator();
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("boolean1", true);
		variables.put("boolean2", false);
		assertTrue(  (Boolean)  evaluator.eval(0, "${boolean1} == True" , variables ) );
		assertFalse(  (Boolean) evaluator.eval(0, "${boolean1} == False" , variables ) );
		assertTrue( (Boolean)   evaluator.eval(0, "${boolean2} == False", variables ) );
		assertFalse( (Boolean)  evaluator.eval(0, "${boolean2} == True", variables ) );
	}
	
	@Test
	public void testParameter_String() throws Exception{
		final ExpressionEvaluator evaluator = new ExpressionEvaluator();
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("string", "foo");
		assertTrue(  (Boolean)  evaluator.eval(0, "${string} == 'foo'" , variables ) );
	}
	
	@Test
	public void testParameter_Integer() throws Exception{
		final ExpressionEvaluator evaluator = new ExpressionEvaluator();
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("i", 10);
		assertTrue(  (Boolean)  evaluator.eval(0, "${i} == 10" , variables ) );
	}
	
	@Test
	public void testParameter_Long() throws Exception{
		final ExpressionEvaluator evaluator = new ExpressionEvaluator();
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("l", 10L);
		assertTrue(  (Boolean)  evaluator.eval(0, "${l} == 10" , variables ) );
	}
	
	@Test
	public void testParameter_Float() throws Exception{
		final ExpressionEvaluator evaluator = new ExpressionEvaluator();
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("f", 5.0f);
		assertTrue(  (Boolean)  evaluator.eval(0, "${f} == 5.0" , variables ) );
	}
	
	@Test
	public void testParameter_Double() throws Exception{
		final ExpressionEvaluator evaluator = new ExpressionEvaluator();
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("d", 5.0);
		assertTrue(  (Boolean)  evaluator.eval(0, "${d} == 5.0" , variables ) );
	}
	
	@Test(expected=InvalidTypeException.class)
	public void testParameter_InvalidType() throws Exception{
		final ExpressionEvaluator evaluator = new ExpressionEvaluator();
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("param", new Vector<String>());
		evaluator.eval(0, "${d} == 5.0" , variables );
	}
	
	@Test
	public void testLowercase() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "lowercase('AbCd')");
		assertTrue(res instanceof String);
		assertEquals("abcd", res);
	}
	
	@Test
	public void testUppercase() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "uppercase('AbCd')");
		assertTrue(res instanceof String);
		assertEquals("ABCD", res);
	}
	
	@Test
	public void testTrim() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "trim(' AbCd\t')");
		assertTrue(res instanceof String);
		assertEquals("AbCd", res);
	}
	
	@Test
	public void testFloor() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "floor(5.5)");
		assertTrue(res instanceof Float);
		assertEquals(5.0f, res);
	}
	
	@Test
	public void testCeil() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "ceil(5.5)");
		assertTrue(res instanceof Float);
		assertEquals(6.0f, res);
	}
	
	@Test
	public void testCeilWithDouble() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("mydouble", 5.5);
		Object res = evaluator.eval(0, "ceil(${mydouble})", variables);
		assertTrue(res instanceof Float);
		assertEquals(6.0f, res);
	}
	
	@Test
	public void testRound() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "round(5.8)");
		assertTrue(res instanceof Long);
		assertEquals(6L, res);
	}
	
	@Test
	public void testDefined_exists() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("something", 5.5);
		Object res = evaluator.eval(0, "defined(${something})", variables);
		assertTrue(res instanceof Boolean);
		assertTrue((Boolean)res);
	}
	
	@Test
	public void testDefined_does_not_exists1() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "defined(${something})");
		assertTrue(res instanceof Boolean);
		assertFalse((Boolean)res);
	}
	
	@Test
	public void testDefined_does_not_exist2() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "defined(None)");
		assertTrue(res instanceof Boolean);
		assertFalse((Boolean)res);
	}
	
	@Test
	public void testContains() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "contains('aitor, pablo, eduardo', 'eduardo')");
		assertTrue(res instanceof Boolean);
		assertTrue((Boolean)res);
	}

	@Test
	public void testSqrt() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "sqrt(10)");
		assertEquals("3.1622777", res.toString());
	}
	
	@Test
	public void testLog() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "log(10)");
		assertEquals("2.3025851", res.toString());
	}
	
	@Test
	public void testPow() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "pow(6, 6)");
		assertEquals("46656.0", res.toString());
	}
	
	@Test
	public void testEvaluatingWithUnrequiredParameter() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("foo", "foo");
		Object res = evaluator.eval(0, "5", variables);
		assertTrue(res instanceof Integer);
		assertEquals(5, res);
	}
	
	@Test
	public void testMod() throws Exception{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();		
		Object res = evaluator.eval(0, "7%3");
		assertTrue(res instanceof Integer);
		assertEquals("1", res.toString());
	}
		
	@Test
	public void testInjection(){
		assertTrue(this.checkExpression("5 + 5"));
		assertTrue(this.checkExpression("(5 + 5)"));
		assertTrue(this.checkExpression("( (5 + 5) )"));
		assertTrue(this.checkExpression(" 5 * ( (5 + 5) )"));
		assertTrue(this.checkExpression(" ( (5 + 5) )"));
		assertTrue(this.checkExpression(" 8f + (5 + 5) "));
		assertFalse(this.checkExpression("foo()"));
		assertFalse(this.checkExpression("foo ()"));
		assertFalse(this.checkExpression("foo_ ()"));
		assertFalse(this.checkExpression("foo8 ()"));
		assertFalse(this.checkExpression("foo_()"));
		assertFalse(this.checkExpression("foo8()"));
		assertFalse(this.checkExpression(" foo8()"));
		assertFalse(this.checkExpression("+foo8()"));
		assertFalse(this.checkExpression("-foo8()"));
		assertFalse(this.checkExpression("(foo8()"));
		assertFalse(this.checkExpression(")foo8()"));
		assertFalse(this.checkExpression("(5 + 5); foo8()"));
		assertFalse(this.checkExpression("__import__('urllib2')"));
		assertFalse(this.checkExpression("open('/etc/passwd')"));
		assertTrue(this.checkExpression("not()"));
		assertTrue(this.checkExpression("not() not()"));
		assertTrue(this.checkExpression("not() and()"));
	}
	
	private boolean checkExpression(String expression){
		final ExpressionEvaluator evaluator = new ExpressionEvaluator();
		try{
			evaluator.eval(0, expression);
			return true;
		}catch(FunctionsNotSupportedException e){
			return false;
		}catch(Exception e){
			return true;
		}
	}

}
