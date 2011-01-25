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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import piramide.multimodal.preprocessor.parser.ConditionNode;
import piramide.multimodal.preprocessor.parser.ElseBranch;
import piramide.multimodal.preprocessor.parser.IfBranch;
import piramide.multimodal.preprocessor.parser.Node;
import piramide.multimodal.preprocessor.parser.TextNode;



public class ParserTest {

	private Parser parser;
	
	@Before
	public void setUp(){
		this.parser = new Parser();
	}
	
	@Test
	public void testParse_simple() throws Exception{
		final Node [] tree = new Node[] {
				new TextNode("foo1\nfoo2 "),
				new ConditionNode(0, 
						new IfBranch(0, " 5 > 4 ",
								new TextNode("foo3 \nfoo4 "),
								new ConditionNode(0, 
										new IfBranch(0, "5 < 4",
												// Since !(5 > 4), this branch will not be evaluated
												new TextNode("foo5 ")
										),
										new ElseBranch(
												new TextNode("foo6 ")
										)
								),
								new TextNode("foo7 \nfoo8 ")
							),
						// Since 5 > 4, this branch will not be evaluated
						new ElseBranch(
								new TextNode("foo9 \nfoo10 ")
							)
				),
				new TextNode("foo11 \nfoo12")
			};
		final String expectedCode =  "foo1\nfoo2 " +
									 "foo3 \nfoo4 " +
									 "foo6 " +
									 "foo7 \nfoo8 " +
									 "foo11 \nfoo12";
		
		final String actualCode = this.parser.parse(Arrays.asList(tree), new HashMap<String, Object>());
		assertEquals(expectedCode, actualCode);
	}
	
	@Test
	public void testParse_elifs() throws Exception{
		final Node [] tree = new Node[] {
				new TextNode("foo1\nfoo2 "),
				new ConditionNode(0, 
						new IfBranch(0, " 5 > 4 ",
								new TextNode("foo3 \nfoo4 ")
							),
						new IfBranch(0, " 5 > 4 ",
								new TextNode("foo5 \nfoo6 ")
							),
						// Since 5 > 4, this branch will not be evaluated
						new ElseBranch(
								new TextNode("foo7 \nfoo8 ")
							)
				),
				new TextNode("foo9 \nfoo10 "),
				new ConditionNode(0, 
						new IfBranch(0, " 5 < 4 ",
								new TextNode("foo11 \nfoo12 ")
							),
						new IfBranch(0, " 5 > 4 ",
								new TextNode("foo13 \nfoo14 ")
							),
						// Since 5 > 4, this branch will not be evaluated
						new ElseBranch(
								new TextNode("foo15 \nfoo16 ")
							)
				),
				new TextNode("foo17 \nfoo18")
			};
		final String expectedCode =  "foo1\nfoo2 " +
									 "foo3 \nfoo4 " +
									 "foo9 \nfoo10 " +
									 "foo13 \nfoo14 " +
									 "foo17 \nfoo18";
		
		final String actualCode = this.parser.parse(Arrays.asList(tree), new HashMap<String, Object>());
		assertEquals(expectedCode, actualCode);
	}
	
	@Test
	public void testParse_ifWithVariable() throws Exception{
		final Node [] tree = new Node[] {
				new TextNode("foo1\nfoo2 "),
				new ConditionNode(0, 
						new IfBranch(0, " ${piramide.devices.screen.x} > 100 ",
								new TextNode("foo3 \nfoo4 ")
							),
						new ElseBranch(
								new TextNode("foo5 \nfoo6 ")
							)
				),
				new TextNode("foo7 \nfoo8"),
				new ConditionNode(0, 
						new IfBranch(0, " lowercase(${piramide.devices.model.name}) == \"htc dream\" ",
								new TextNode("foo9 \nfoo10 ")
							),
						new ElseBranch(
								new TextNode("foo11 \nfoo12 ")
							)
				),
			};
		final String expectedCode =  "foo1\nfoo2 " +
									 "foo3 \nfoo4 " +
									 "foo7 \nfoo8" + 
									 "foo9 \nfoo10 ";
		
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("piramide.devices.screen.x", 150);
		variables.put("piramide.devices.model.name", "HTC Dream");
		final String actualCode = this.parser.parse(Arrays.asList(tree), variables);
		assertEquals(expectedCode, actualCode);
	}
	
}

