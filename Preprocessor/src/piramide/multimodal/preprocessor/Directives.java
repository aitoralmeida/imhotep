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

public class Directives {
	
    public String DIRECTIVE_START;
	public String IF;
	public String ELSE;
	public String ELIF;
	public String ENDIF;
	                             
	public String IFDEF;
	public String ELIFDEF;
	public String ENDIFDEF;
	                                
	public String USER_DEFINED_ERROR;
	public String HIDDEN;
	                             
	public final static String QUOTE = "\"";
	public final static String SPACE = " ";
	
	public final static String EXPRESION_START 	  =  "\\$\\(";
	public final static char PARENTHESIS_START    =  '(';
	public final static char EXPRESSION_END 	  =  ')';
	
	public Directives(String directiveStart)
	{
        
		this.DIRECTIVE_START = directiveStart;
		this.IF                 = this.DIRECTIVE_START + "if ";     
		this.ELSE               = this.DIRECTIVE_START + "else";    
		this.ELIF               = this.DIRECTIVE_START + "elif ";   
		this.ENDIF              = this.DIRECTIVE_START + "endif";   
		                    
		this.IFDEF              = this.DIRECTIVE_START + "ifdef ";  
		this.ELIFDEF            = this.DIRECTIVE_START + "elifdef ";
		this.ENDIFDEF           = this.DIRECTIVE_START + "endifdef";
		                          
		this.USER_DEFINED_ERROR = this.DIRECTIVE_START + "error ";  
		this.HIDDEN             = this.DIRECTIVE_START + " ";                                                                
		
	}

}
