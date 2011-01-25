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
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.Test;

import piramide.multimodal.preprocessor.exceptions.IllegalCodeException;
import piramide.multimodal.preprocessor.exceptions.UserDefinedErrorException;


public class PreprocessorTest 
{

	//********************* condition without variables******************************************************
	private final String inputIf = 			"//#if 5 > 0 \n" +
											"System.out.println(\"5\");\n" + 
											"//#endif";
	
	private final String outputIf = 		"System.out.println(\"5\");\n";
	
	private final String inputElse = 		"//#if 5 < 0 \n" +
											"System.out.println(\"5\");\n" + 
											"//#else\n"+
											"System.out.println(\"else\");\n" +										
											"//#endif";
	
	private final String outputElse = 		"System.out.println(\"else\");\n";
	
	private final String inputElif = 		"//#if 5 < 0 \n" +
											"System.out.println(\"5\");\n" + 
											"//#elif 6 > 0\n" +
											"System.out.println(\"6\");\n" +								
											"//#endif";
	
	private final String outputElif = 		"System.out.println(\"6\");\n";
	
	private final String inputElifElse = 	"//#if 5 < 0 \n" +
											"System.out.println(\"5\");\n" + 
											"//#elif 6 < 0\n" +
											"System.out.println(\"6\");\n" +
											"//#else\n"+
											"System.out.println(\"else\");\n" +	
											"//#endif";
	
	private final String outputElifElse = 		"System.out.println(\"else\");\n";
	
	
	//**************************Conditions with variables****************************************************************
	
	private final String inputIfWithVariables = 		"//#if ${piramide.device.screen.height} > 0 \n" +
														"System.out.println(\"5\");\n" + 
														"//#endif";

	private final String outputIfWithVariables = 		"System.out.println(\"5\");\n";
	
	private final String inputElseWithVariables = 		"//#if ${piramide.device.screen.height} < 0 \n" +
														"System.out.println(\"5\");\n" + 
														"//#else\n"+
														"System.out.println(\"else\");\n" +										
														"//#endif";
	
	private final String outputElseWithVariables = 		"System.out.println(\"else\");\n";
	
	private final String inputElifWithVariables = 		"//#if ${piramide.device.screen.height} < 0 \n" +
														"System.out.println(\"5\");\n" + 
														"//#elif ${piramide.device.screen.height} > 0\n" +
														"System.out.println(\"6\");\n" +								
														"//#endif";
	
	private final String outputElifWithVariables = 		"System.out.println(\"6\");\n";
	
	private final String inputElifElseWithVariables = 	"//#if ${piramide.device.screen.height} < 0 \n" +
														"System.out.println(\"5\");\n" + 
														"//#elif ${piramide.device.screen.height} < 0\n" +
														"System.out.println(\"6\");\n" +
														"//#else\n"+
														"System.out.println(\"else\");\n" +	
														"//#endif";
	
	private final String outputElifElseWithVariables = 	"System.out.println(\"else\");\n";
	
	//********************************comments****************************************************
	
	private final String inputComment = 	"//#if 1 > 0 \n" +
											"//# return 1;\n" + 
											"//#else\n"+
											"return 0;\n" +										
											"//#endif";

	private final String outputComment = 	"return 1;";
	
	//******************************Variable definition********************************************
	
	private final String inputVariableDef = "private int var = //# ${piramide.device.screen.height};";
	
	private final String outputVariableDef = "private int var = 12;";
	
	private final String existingVariable = "piramide.device.screen.height";
	private final String screenHeight = "piramide.device.screen.height";
		
	
	//*********************************** Null and empty code tests******************************************************************
	
	@Test (expected=NullPointerException.class)
	public void testNullCodeInGetUsedVariables() throws Exception {
		
		final IPreprocessor preprocessor = new Preprocessor();
		
		preprocessor.getUsedVariables(null);
	}
	
	@Test
	public void testEmptyCodeInGetUsedVariables() throws Exception {
		
		final IPreprocessor preprocessor = new Preprocessor();
		
		final String [] variables = preprocessor.getUsedVariables("");
		assertEquals(0, variables.length);
	}
	
	@Test (expected=NullPointerException.class)
	public void testNullCodeInPreprocess() throws Exception {
		
		final IPreprocessor preprocessor = new Preprocessor();
		final HashMap<String, Object> variableValues = new HashMap<String, Object>();
		
		preprocessor.preprocess("file.java", DirectoryPreprocessor.DIRECTIVE_START_SLASH, null, variableValues);
	}
	
	@Test
	public void testEmptyCodeInPreprocess() throws Exception {
		
		final IPreprocessor preprocessor = new Preprocessor();
		final HashMap<String, Object> variableValues = new HashMap<String, Object>();
		
		final PreprocessorResult result = preprocessor.preprocess("file.java", DirectoryPreprocessor.DIRECTIVE_START_SLASH, "", variableValues);
		assertEquals("", result.getPreprocessedCode());
	}
	
	
	//************************************************Conditions*****************************************************************************
	
	@Test
	public void testPreprocessIf() throws Exception {
		
		final IPreprocessor preprocessor = new Preprocessor();
		final HashMap<String, Object> variableValues = new HashMap<String, Object>();
		variableValues.put(this.existingVariable, new Integer(12));
		
		final PreprocessorResult result = preprocessor.preprocess("file.java", DirectoryPreprocessor.DIRECTIVE_START_SLASH, this.inputIf, variableValues);
		
		assertEquals(this.outputIf, result.getPreprocessedCode());
	}
	
	@Test
	public void testPreprocessElse() throws Exception {
		
		final IPreprocessor preprocessor = new Preprocessor();
		final HashMap<String, Object> variableValues = new HashMap<String, Object>();
		variableValues.put(this.existingVariable, new Integer(12));
		
		final PreprocessorResult result = preprocessor.preprocess("file.java", DirectoryPreprocessor.DIRECTIVE_START_SLASH, this.inputElse, variableValues);
		
		assertEquals(this.outputElse, result.getPreprocessedCode());
	}

	@Test
	public void testPreprocessElif() throws Exception {
		
		final IPreprocessor preprocessor = new Preprocessor();
		final HashMap<String, Object> variableValues = new HashMap<String, Object>();
		variableValues.put(this.existingVariable, new Integer(12));
		
		final PreprocessorResult result = preprocessor.preprocess("file.java", DirectoryPreprocessor.DIRECTIVE_START_SLASH, this.inputElif, variableValues);
		
		assertEquals(this.outputElif, result.getPreprocessedCode());
	}
	
	@Test
	public void testPreprocessElifElse() throws Exception {
		
		final IPreprocessor preprocessor = new Preprocessor();
		final HashMap<String, Object> variableValues = new HashMap<String, Object>();
		variableValues.put(this.existingVariable, new Integer(12));
		
		final PreprocessorResult result = preprocessor.preprocess("file.java", DirectoryPreprocessor.DIRECTIVE_START_SLASH, this.inputElifElse, variableValues);
		assertEquals(this.outputElifElse, result.getPreprocessedCode());
	}
	
	
	//******************************************Conditions with variables**************************************************************
	
	@Test
	public void testPreprocessIfWithVariables() throws Exception {
		
		final IPreprocessor preprocessor = new Preprocessor();
		final HashMap<String, Object> variableValues = new HashMap<String, Object>();
		variableValues.put(this.screenHeight, new Integer(12));
		
		final PreprocessorResult result = preprocessor.preprocess("file.java", DirectoryPreprocessor.DIRECTIVE_START_SLASH, this.inputIfWithVariables, variableValues);
		
		assertEquals(this.outputIfWithVariables, result.getPreprocessedCode());
	}
	
	@Test
	public void testPreprocessElseWithVariables() throws Exception {
		
		final IPreprocessor preprocessor = new Preprocessor();
		final HashMap<String, Object> variableValues = new HashMap<String, Object>();
		variableValues.put(this.screenHeight, new Integer(12));
		
		final PreprocessorResult result = preprocessor.preprocess("file.java", DirectoryPreprocessor.DIRECTIVE_START_SLASH, this.inputElseWithVariables, variableValues);
		
		assertEquals(this.outputElseWithVariables, result.getPreprocessedCode());
	}
	
	@Test
	public void testPreprocessElifWithVariables() throws Exception {
		
		final IPreprocessor preprocessor = new Preprocessor();
		final HashMap<String, Object> variableValues = new HashMap<String, Object>();
		variableValues.put(this.screenHeight, new Integer(12));
		
		final PreprocessorResult result = preprocessor.preprocess("file.java", DirectoryPreprocessor.DIRECTIVE_START_SLASH, this.inputElifWithVariables, variableValues);
		
		assertEquals(this.outputElifWithVariables, result.getPreprocessedCode());
	}
	
	@Test
	public void testPreprocessElifElseWithVariables() throws Exception {
		
		final IPreprocessor preprocessor = new Preprocessor();
		final HashMap<String, Object> variableValues = new HashMap<String, Object>();
		variableValues.put(this.screenHeight, new Integer(12));
		
		final PreprocessorResult result = preprocessor.preprocess("file.java", DirectoryPreprocessor.DIRECTIVE_START_SLASH, this.inputElifElseWithVariables, variableValues);
		
		assertEquals(this.outputElifElseWithVariables, result.getPreprocessedCode());
	}
	
	//*****************************************************Comments*********************************************************************
	@Test
	public void testComments() throws Exception {
		
		final IPreprocessor preprocessor = new Preprocessor();
		final HashMap<String, Object> variableValues = new HashMap<String, Object>();
		variableValues.put(this.screenHeight, new Integer(12));
		
		final PreprocessorResult result = preprocessor.preprocess("file.java", DirectoryPreprocessor.DIRECTIVE_START_SLASH, this.inputComment, variableValues);
		
		assertEquals(this.outputComment, result.getPreprocessedCode());
	}
	
	//******************************************************Variable definitions*************************************************************
	@Test
	public void testVariableDefinition() throws Exception {
		
		final IPreprocessor preprocessor = new Preprocessor();
		final HashMap<String, Object> variableValues = new HashMap<String, Object>();
		variableValues.put(this.screenHeight, new Integer(12));
		
		final PreprocessorResult result = preprocessor.preprocess("file.java", DirectoryPreprocessor.DIRECTIVE_START_SLASH, this.inputVariableDef, variableValues);
		
		assertEquals(this.outputVariableDef, result.getPreprocessedCode());
	}

	@Test
	public void testIsVariableDefinedDefined() throws Exception{
		final IPreprocessor preprocessor = new Preprocessor();
		final HashMap<String, Object> variableValues = new HashMap<String, Object>();
		final String code = "//#if defined(${piramide.devices.height.x}) \n" +
							"foo\n" +
							"//#endif\n" +
							"//#if defined(${piramide.devices.height.y})\n" +
							"foo2\n" +
							"//#else\n" +
							"foo3\n" +
							"//#endif";
		final String expected = "foo\nfoo3\n";
		variableValues.put("piramide.devices.height.x", 100);
		final PreprocessorResult actual = preprocessor.preprocess("file.java", DirectoryPreprocessor.DIRECTIVE_START_SLASH, code, variableValues);
		assertEquals(expected, actual.getPreprocessedCode());
	}
	
	@Test
	public void testUserExceptionIsThrown() throws Exception{
		final IPreprocessor preprocessor = new Preprocessor();
		final HashMap<String, Object> variableValues = new HashMap<String, Object>();
		final String code = "//#if 5>0\n" +
							"//#error text in exception\n" +
							"//#endif\n";
		try{
			preprocessor.preprocess("file.java", DirectoryPreprocessor.DIRECTIVE_START_SLASH, code, variableValues);
			fail(UserDefinedErrorException.class.getName() + " expected");
		}catch(UserDefinedErrorException userException){
			assertEquals("User defined error: <text in exception> in line: " + 1, userException.getMessage());
		}
	}
	
	@Test
	public void testUserExceptionIsNotThrown() throws Exception{
		final IPreprocessor preprocessor = new Preprocessor();
		final HashMap<String, Object> variableValues = new HashMap<String, Object>();
		final String code = "//#if 5<0\n" +
							"//#error text in exception\n" +
							"//#endif\n";
		final PreprocessorResult actual = preprocessor.preprocess("file.java", DirectoryPreprocessor.DIRECTIVE_START_SLASH, code, variableValues);
		assertEquals("", actual.getPreprocessedCode());
	}
	
	@Test
	public void testLineNumber() throws Exception{
		final IPreprocessor preprocessor = new Preprocessor();
		final HashMap<String, Object> variableValues = new HashMap<String, Object>();
		final String code = "//#if 5 < 0\n" +
							"//#error text in exception\n" +
							"//#endif\n" +
							"asdf\n" +
							"//#if fail\n" +
							"//#endif";
		try{
			preprocessor.preprocess("file.java", DirectoryPreprocessor.DIRECTIVE_START_SLASH, code, variableValues);
			fail(IllegalCodeException.class.getName() + " expected");
		}catch(IllegalCodeException ice){
			assertEquals(5, ice.getLineNumber());
		}
	}
	
}
