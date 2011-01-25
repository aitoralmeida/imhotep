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

package piramide.multimodal.preprocessor.exceptions;

public class IllegalCodeException extends EvaluationException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7546520472037722141L;

	/**
	 * @param arg0
	 */
	public IllegalCodeException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public IllegalCodeException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public IllegalCodeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}


}
