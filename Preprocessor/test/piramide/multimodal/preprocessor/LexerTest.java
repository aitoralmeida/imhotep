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

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import piramide.multimodal.preprocessor.exceptions.SyntaxErrorException;
import piramide.multimodal.preprocessor.parser.ConditionNode;
import piramide.multimodal.preprocessor.parser.ElseBranch;
import piramide.multimodal.preprocessor.parser.ErrorNode;
import piramide.multimodal.preprocessor.parser.HiddenNode;
import piramide.multimodal.preprocessor.parser.IfBranch;
import piramide.multimodal.preprocessor.parser.Node;
import piramide.multimodal.preprocessor.parser.TextNode;

public class LexerTest {

	private Lexer lexer;
	
	@Before
	public void setUp(){
		this.lexer = new Lexer(DirectoryPreprocessor.DIRECTIVE_START_SLASH);
	}
	
	@Test
	public void testParse_Simpletext() throws Exception {
		final String text = "foo";
		final Node [] resultingNodes = this.lexer.lex("file.java", text);
		
		final Node [] expectedNodes = new Node[] {
			new TextNode("foo")
		};
		assertArrayEquals(expectedNodes, resultingNodes);
	}

	@Test
	public void testParse_SimpletextEndingInNewLine() throws Exception {
		final String text = "foo\naa\n\n\n\n";
		final Node [] resultingNodes = this.lexer.lex("file.java", text);
		
		final Node [] expectedNodes = new Node[] {
			new TextNode("foo\naa\n\n\n\n")
		};
		assertArrayEquals(expectedNodes, resultingNodes);
	}

	@Test
	public void testParse_IfWithoutAnythingElseAtTheEnd() throws Exception {
		final String text = "//#if condition\n" +
							"foo\n" +
							"//#endif";
		final Node [] resultingNodes = this.lexer.lex("file.java", text);
		
		final Node [] expectedNodes = new Node[] {
			new ConditionNode(0, new IfBranch(0, "condition", new TextNode("foo\n")))
		};
		assertArrayEquals(expectedNodes, resultingNodes);
	}

	@Test
	public void testParse_IfWithTextAtTheBeginning() throws Exception {
		final String text = "\n" +
							"//#if condition\n" +
							"foo\n" +
							"//#endif";
		final Node [] resultingNodes = this.lexer.lex("file.java", text);
		
		final Node [] expectedNodes = new Node[] {
			new TextNode("\n"),
			new ConditionNode(0, new IfBranch(0, "condition", new TextNode("foo\n")))
		};
		assertArrayEquals(expectedNodes, resultingNodes);
	}

	@Test
	public void testParse_HiddenWithoutAnythingElseAtTheEnd() throws Exception {
		final String text = "//# foo";
		final Node [] resultingNodes = this.lexer.lex("file.java", text);
		
		final Node [] expectedNodes = new Node[] {
			new HiddenNode(0, "foo")
		};
		assertArrayEquals(expectedNodes, resultingNodes);
	}

	@Test
	public void testParse_ErrorWithoutAnythingElseAtTheEnd() throws Exception {
		final String text = "//#error foo";
		final Node [] resultingNodes = this.lexer.lex("file.java", text);
		
		final Node [] expectedNodes = new Node[] {
			new ErrorNode(0, "foo")
		};
		assertArrayEquals(expectedNodes, resultingNodes);
	}

	@Test
	public void testParse_Conditions() throws Exception {
		final String text = "foo1\n" +
							"foo2 //#if condition\n" +
							"foo3 \n" +
							"foo4 \n" +
							"foo5 //#else \n" +
							"foo6 \n" +
							"foo7 //#endif \n" +
							"foo8 \n" +
							"foo9";
		final Node [] resultingNodes = this.lexer.lex("file.java", text);
		
		final Node [] expectedNodes = new Node[] {
			new TextNode("foo1\nfoo2 "),
			new ConditionNode(0, 
					new IfBranch(0, "condition",
							new TextNode("foo3 \nfoo4 \nfoo5 ")
						),
					new ElseBranch(
							new TextNode("foo6 \nfoo7 ")
						)
			),
			new TextNode("foo8 \nfoo9")
		};
		assertArrayEquals(expectedNodes, resultingNodes);
	}
	
	@Test
	public void testParse_NestedConditions() throws Exception {
		final String text = "foo1\n" +
							"foo2 //#if condition\n" +
							"foo3 \n" +
							"foo4 //#if othercondition\n" +
							"foo5 //#else \n" +
							"foo6 //#endif \n" +
							"foo7 \n" +
							"foo8 //#else \n" +
							"foo9 \n" +
							"foo10 //#endif \n" +
							"foo11 \n" +
							"foo12";
		final Node [] resultingNodes = this.lexer.lex("file.java", text);
		
		final Node [] expectedNodes = new Node[] {
			new TextNode("foo1\nfoo2 "),
			new ConditionNode(0, 
					new IfBranch(0, "condition",
							new TextNode("foo3 \nfoo4 "),
							new ConditionNode(0, 
									new IfBranch(0, "othercondition",
											new TextNode("foo5 ")
									),
									new ElseBranch(
											new TextNode("foo6 ")
									)
							),
							new TextNode("foo7 \nfoo8 ")
						),
					new ElseBranch(
							new TextNode("foo9 \nfoo10 ")
						)
			),
			new TextNode("foo11 \nfoo12")
		};
		assertArrayEquals(expectedNodes, resultingNodes);
	}

	@Test
	public void testParse_HiddenAndError() throws Exception {
		final String text = "foo1\n" +
							"foo2 //# foo bar\n" +
							"foo3 \n" +
							"foo4 \n" +
							"foo5 //#error foo2 bar2\n" +
							"foo6 \n" +
							"foo7";
		final Node [] resultingNodes = this.lexer.lex("file.java", text);
		
		final Node [] expectedNodes = new Node[] {
			new TextNode("foo1\nfoo2 "),
			new HiddenNode(0, "foo bar"),
			new TextNode("foo3 \nfoo4 \nfoo5 "),
			new ErrorNode(0, "foo2 bar2"),
			new TextNode("foo6 \nfoo7")
		};
		assertArrayEquals(expectedNodes, resultingNodes);
	}
	
	@Test
	public void testParse_ConditionWithoutElse() throws Exception {
		final String text = "foo1\n" +
				"foo2 //#if condition\n" +
				"foo3 \n" +
				"foo4 \n" +
				"foo5 //#endif \n" +
				"foo6 \n" +
				"foo7";
		final Node [] resultingNodes = this.lexer.lex("file.java", text);
		
		final Node [] expectedNodes = new Node[] {
		new TextNode("foo1\nfoo2 "),
		new ConditionNode(0, 
			new IfBranch(0, "condition",
					new TextNode("foo3 \nfoo4 \nfoo5 ")
				)
		),
		new TextNode("foo6 \nfoo7")
		};
		assertArrayEquals(expectedNodes, resultingNodes);
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testParse_ConditionWithTwoElses_Failing() throws Exception {
		final String text = "foo1\n" +
				"foo2 //#if condition\n" +
				"foo3 \n" +
				"foo2 //#else\n" +
				"foo4 \n" +
				"foo2 //#else\n" +
				"foo4 \n" +
				"foo5 //#endif \n" +
				"foo6 \n" +
				"foo7";
		this.lexer.lex("file.java", text);
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testParse_ConditionUnfinished_Failing() throws Exception {
		final String text = "foo1\n" +
				"foo2 //#if condition\n" +
				"foo3 \n" +
				"foo7";
		this.lexer.lex("file.java", text);
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testParse_ConditionUnexpectedElse_Failing() throws Exception {
		final String text = "foo1\n" +
				"foo2 //#else\n" +
				"foo3 \n" +
				"foo7";
		this.lexer.lex("file.java", text);
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testParse_ConditionUnexpectedEndif_Failing() throws Exception {
		final String text = "foo1\n" +
				"foo2 //#endif\n" +
				"foo3 \n" +
				"foo7";
		this.lexer.lex("file.java", text);
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testParse_ConditionUnexpectedElif_Failing() throws Exception {
		final String text = "foo1\n" +
				"foo2 //#elif condition\n" +
				"foo3 \n" +
				"foo7";
		this.lexer.lex("file.java", text);
	}
	
	@Test(expected=SyntaxErrorException.class)
	public void testParse_ConditionUnexpectedToken_Failing() throws Exception {
		final String text = "foo1\n" +
				"foo2 //#foo\n" +
				"foo3 \n" +
				"foo7";
		this.lexer.lex("file.java", text);
	}
}
