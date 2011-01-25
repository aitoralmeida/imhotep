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

package piramide.multimodal.preprocessor.exceptions;

public class EvaluationException extends Exception {

	private static final long serialVersionUID = 5513205879107918949L;
	private final int lineNumber; 

	public EvaluationException(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public EvaluationException(String message, int lineNumber) {
		super(message);
		this.lineNumber = lineNumber;
	}

	public EvaluationException(int lineNumber, Throwable cause) {
		super(cause);
		this.lineNumber = lineNumber;
	}

	public EvaluationException(String message, int lineNumber, Throwable cause) {
		super(message, cause);
		this.lineNumber = lineNumber;
	}
	
	public int getLineNumber(){
		return this.lineNumber;
	}
}
