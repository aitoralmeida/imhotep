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

package piramide.multimodal.preprocessor.parser;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

import piramide.multimodal.preprocessor.ExpressionEvaluator;
import piramide.multimodal.preprocessor.exceptions.SyntaxErrorException;


public class HiddenNodeTest {
	
	final private ExpressionEvaluator evaluator = new ExpressionEvaluator();
	
	@Test
	public void testHiddenNodeGenerateCode_ExpressionOnly() throws Exception {
		final HiddenNode node = new HiddenNode(0, " foo $( 5  + 5 ) ");
		final String actual = node.generateCode(this.evaluator, new HashMap<String, Object>());
		final String expected = " foo 10 ";
		assertEquals(expected, actual);
	}

	@Test(expected=SyntaxErrorException.class)
	public void testHiddenNodeGenerateCode_SyntaxErrorFindingClosingParenthesis() throws Exception {
		final HiddenNode node = new HiddenNode(0, " foo $( 5  + 5 ");
		node.generateCode(this.evaluator, new HashMap<String, Object>());
	}

	@Test
	public void testHiddenNodeGenerateCode_ExpressionWithNestedParenthesis() throws Exception {
		final HiddenNode node = new HiddenNode(0, " foo $( (2  + 3) + (5) ) )");
		final String actual = node.generateCode(this.evaluator, new HashMap<String, Object>());
		final String expected = " foo 10 )";
		assertEquals(expected, actual);
	}

	@Test
	public void testHiddenNodeGenerateCode_VariableOnly() throws Exception {
		final HiddenNode node = new HiddenNode(0, " foo ${piramide.devices.model.name}");
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("piramide.devices.model.name", "bar");
		final String actual = node.generateCode(this.evaluator, variables);
		final String expected = " foo bar";
		assertEquals(expected, actual);
	}

	@Test
	public void testHiddenNodeGenerateCode_VariableInTheMiddleOnly() throws Exception {
		final HiddenNode node = new HiddenNode(0, " foo $( 5 ) ${piramide.devices.model.name} $( 6) ${piramide.devices.model.name}");
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("piramide.devices.model.name", "bar");
		final String actual = node.generateCode(this.evaluator, variables);
		final String expected = " foo 5 bar 6 bar";
		assertEquals(expected, actual);
	}

	@Test
	public void testHiddenNodeGenerateCode_WithExpressionWithVariable() throws Exception {
		final HiddenNode node = new HiddenNode(0, " foo $( 5  + ${piramide.devices.screen.x} )");
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("piramide.devices.screen.x", 5);
		final String actual = node.generateCode(this.evaluator, variables);
		final String expected = " foo 10";
		assertEquals(expected, actual);
	}
}
