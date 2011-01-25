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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.python.core.Py;
import org.python.core.PyBoolean;
import org.python.core.PyDictionary;
import org.python.core.PyFloat;
import org.python.core.PyInteger;
import org.python.core.PyLong;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySyntaxError;
import org.python.util.PythonInterpreter;

import piramide.multimodal.preprocessor.exceptions.EvaluationException;
import piramide.multimodal.preprocessor.exceptions.FunctionsNotSupportedException;
import piramide.multimodal.preprocessor.exceptions.IllegalCodeException;
import piramide.multimodal.preprocessor.exceptions.InvalidTypeException;
import piramide.multimodal.preprocessor.exceptions.SyntaxErrorException;

public class ExpressionEvaluator {
	
	private static final String PARAMS = "params";
	private final String [] reservedWords = {"not", "and", "or"};
	private final String [] allowedWords;
	private final PythonInterpreter interpreter;
	private final Pattern pattern = Pattern.compile("([a-zA-Z0-9_]+)\\s*\\(");
	
	public ExpressionEvaluator(){
		this.interpreter = new PythonInterpreter();
		
		final List<String> listOfAllowedWords = new Vector<String>();
		
		for(String reservedWord : this.reservedWords)
			listOfAllowedWords.add(reservedWord);
		
		for(Method method : EvaluatorFunctions.class.getDeclaredMethods())
			if(Modifier.isPublic(method.getModifiers())){
				final String methodName = method.getName();
				final PyObject funcObj = Py.newJavaFunc(EvaluatorFunctions.class, methodName);
				this.interpreter.set(methodName, funcObj);
				listOfAllowedWords.add(methodName);
			}
		this.allowedWords = listOfAllowedWords.toArray(new String[]{});
	}
	
	public Object eval(int lineNumber, String expression) throws EvaluationException {
		return this.eval(lineNumber, expression, new HashMap<String, Object>());
	}
	
	public Object eval(int lineNumber, String expression, Map<String, Object> parameters) throws EvaluationException{
		if(!this.checkExpression(expression))
			throw new FunctionsNotSupportedException("Function calls are not supported: " + expression + " in line: " + lineNumber, lineNumber);
		
		final PyDictionary pyParameters = buildPythonParameters(parameters, lineNumber);
		final String serializedParameters = pyParameters.toString();
		
		final ParameterScanner scanner = new ParameterScanner();
		final String expressionWithParametersReplaced = scanner.replaceVariables(expression, PARAMS, lineNumber);
		
		final String finalExpression = "(lambda " + PARAMS + " : " + expressionWithParametersReplaced + ") (" + serializedParameters + ")";
		
		final PyObject pyObj;
		try {
			pyObj = this.interpreter.eval(finalExpression);
		} catch (PySyntaxError e){
			throw new SyntaxErrorException("Syntax error: " + e.getMessage() + " in line: " + lineNumber, lineNumber, e);
		} catch (Exception e) {
			throw new IllegalCodeException("General exception: " + e.getMessage() + " in line: " + lineNumber, lineNumber, e);
		}
		
		return this.extractJavaValue(pyObj, lineNumber);
	}

	private Object extractJavaValue(PyObject pyObj, int lineNumber) throws InvalidTypeException {
		
		if(pyObj instanceof PyBoolean){
			return new Boolean(pyObj.toString());		
		} else if(pyObj instanceof PyString){
			return pyObj.toString();
		} else if(pyObj instanceof PyInteger){
			return new Integer(pyObj.toString());
		} else if(pyObj instanceof PyFloat){
			return new Float(pyObj.toString());
		} else if(pyObj instanceof PyLong){
			return new Long(pyObj.toString().substring(0, pyObj.toString().length() - 1));
		} else {				
			throw new InvalidTypeException("The following type can not be used in the preprocess sentences: " + pyObj.getClass().getName() + " in line: " + lineNumber, lineNumber);
		}
	}

	private PyDictionary buildPythonParameters(Map<String, Object> parameters, int lineNumber) throws InvalidTypeException {
		
		final Map<PyObject, PyObject> pyParameters = new HashMap<PyObject, PyObject>();
		for(String key : parameters.keySet()){
			Object value = parameters.get(key);
			final PyObject pyValue;
			if(value instanceof Boolean)
				pyValue = new PyBoolean(((Boolean) value).booleanValue());
			else if(value instanceof String)
				pyValue = new PyString((String)value);
			else if(value instanceof Integer)
				pyValue = new PyInteger(((Integer) value).intValue());
			else if(value instanceof Long)
				pyValue = new PyLong(((Long)value).longValue());
			else if(value instanceof Float)
				pyValue = new PyFloat(((Float) value).floatValue());
			else if(value instanceof Double)
				pyValue = new PyFloat(((Double) value).floatValue());
			else
				throw new InvalidTypeException("Unsupported parameter type: " + value + " in line: " + lineNumber, lineNumber);
			pyParameters.put(new PyString(key), pyValue);
		}
		return new PyDictionary(pyParameters);
	}

	/**
	 * 
	 * A malicious user could try to call a function or import a module. We simply deny any function call.
	 * The problem is that we will not support strings containing functions, such as "foo()", but we don't 
	 * expect the developers to use them in that way. However, in the future we could implement some security
	 * policies to avoid these problems in a better way. 
	 * 
	 * @param expression
	 * @return whether we accept the expression or not
	 */
	private boolean checkExpression (String expression)
	{
		final Matcher matcher = this.pattern.matcher(expression);
		if(matcher.find()){
			for(int i = 0; i < matcher.groupCount(); ++i){
				String possibleMethodName = matcher.group(i);
				if(possibleMethodName.length() <= 1)
					continue;
				
				possibleMethodName = possibleMethodName.substring(0, possibleMethodName.length() - 1).trim();
					
				boolean found = false;
				for(String allowedWord : this.allowedWords)
					if(allowedWord.equals(possibleMethodName)){
						found = true;
						break;
					}
				if(!found)
					return false;
			}
		}
		return true;
	}
}
