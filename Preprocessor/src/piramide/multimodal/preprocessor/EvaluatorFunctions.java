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
 *
 */

package piramide.multimodal.preprocessor;

import org.python.core.Py;
import org.python.core.PyException;
import org.python.core.PyFloat;
import org.python.core.PyObject;
import org.python.core.PyInteger;
import org.python.core.PyString;

public class EvaluatorFunctions{

	private static final class Numeric{}; //Chapuhack to support numeric (PyInteger & PyFloat) parameters in functions
	
	/*
	 * Functions exported to the Evaluator. These methods are the only ones supported by the Evaluator, and
	 * they are automatically registered.
	 */
	
	@SuppressWarnings("unused")
	public static Object lowercase(PyObject [] objs, String[] args){
		assertArguments(objs, PyString.class);
		return objs[0].toString().toLowerCase();
	}
	
	@SuppressWarnings("unused")
	public static Object uppercase(PyObject [] objs, String [] args){
		assertArguments(objs, PyString.class);
		return objs[0].toString().toUpperCase();
	}
	
	@SuppressWarnings("unused")
	public static Object trim(PyObject [] objs, String [] args){
		assertArguments(objs, PyString.class);
		return objs[0].toString().trim();
	}
	
	@SuppressWarnings("unused")
	public static Object ceil(PyObject [] objs, String [] args){
		assertArguments(objs, PyFloat.class);
		return Math.ceil(((PyFloat)objs[0]).asDouble());
	}
	
	@SuppressWarnings("unused")
	public static Object floor(PyObject [] objs, String [] args){
		assertArguments(objs, PyFloat.class);
		return Math.floor(((PyFloat)objs[0]).asDouble());
	}
	
	@SuppressWarnings("unused")
	public static Object round(PyObject [] objs, String [] args){
		assertArguments(objs, PyFloat.class);
		return Math.round(((PyFloat)objs[0]).asDouble());
	}
	
	@SuppressWarnings("unused")
	public static Object defined(PyObject [] objs, String [] args){
		assertArguments(objs, PyObject.class);
		return new Boolean(objs[0] != Py.None);
	}
	
	@SuppressWarnings("unused")
	public static Object sqrt(PyObject [] objs, String [] args){
		assertArguments(objs, Numeric.class);
		final Number number = parseNumeric(objs[0]);
		return Math.sqrt(number.doubleValue());
	}
	
	@SuppressWarnings("unused")
	public static Object log(PyObject [] objs, String [] args){
		assertArguments(objs, Numeric.class);
		final Number number = parseNumeric(objs[0]);
		return Math.log(number.doubleValue());
	}
	
	@SuppressWarnings("unused")
	public static Object pow(PyObject [] objs, String [] args){
		assertArguments(objs, Numeric.class, Numeric.class);
		final Number number1 = parseNumeric(objs[0]);
		final Number number2 = parseNumeric(objs[1]);
		return Math.pow(number1.doubleValue(),number2.doubleValue());
	}
	
	private static boolean isNumeric(PyObject obj){
		boolean isInteger = PyInteger.class.isInstance(obj);
		boolean isFloat = PyFloat.class.isInstance(obj);
		return (isInteger | isFloat);
	}
	
	private static Number parseNumeric(PyObject num){
		if (PyInteger.class.isInstance(num)){
			return new Integer(((PyInteger)num).getValue());
		}else{
			return new Float(((PyFloat)num).getValue());
		}
	}
	
	@SuppressWarnings("unused")
	public static Object contains(PyObject [] objs, String [] args){
		assertArguments(objs, PyString.class, PyString.class);
		return new Boolean(objs[0].toString().contains(objs[1].toString()));
	}
	
	/**
	 * Checks that the number of objects is the number expected, and that they match the types expected.
	 * 
	 * @param objs: arguments received as parameters.
	 * @param expected: the types of these arguments.
	 */
	private static void assertArguments(PyObject [] objs, Class<?> ... expected){
		if(objs.length != expected.length)
			throw new PyException(new PyString("Invalid number of arguments: expected " + expected.length + "; found: " + objs.length));
		for(int i = 0; i < objs.length; ++i)
			if (expected[i] == Numeric.class){
				if (!isNumeric(objs[0]))
					throw new PyException(new PyString("Invalid argument type: expected type: " + expected[i].getName() + "; found: " + objs[i].getClass().getName()));
			} else if(!expected[i].isInstance(objs[i]))
				throw new PyException(new PyString("Invalid argument type: expected type: " + expected[i].getName() + "; found: " + objs[i].getClass().getName()));
	}
}

