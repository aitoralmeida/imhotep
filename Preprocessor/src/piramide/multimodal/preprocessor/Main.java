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
 * Author: FILLME <FILLME@FILLME>
 *
 */

package piramide.multimodal.preprocessor;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class Main {
	
	public static final String DEFAULT_INPUT_DIRECTORY_NAME = "samplecode";
	public static final String DEFAULT_VARIABLES_FILE_NAME = "samplecode/variables.txt";
	public static final String DEFAULT_EXTENSION_NAME = ".java";
	public static final String DEFAULT_OUTPUT_DIRECTORY_NAME = "output";
	
	public static void main(String [] args) throws Exception{

		final String variablesFileName;
		final String inputDirectoryName;
		final String outputDirectoryName;
		final String extensionName;
		final String codeLanguage;

		if(args.length == 4){
			inputDirectoryName  = args[0];
			variablesFileName   = args[1];
			extensionName       = args[2];
			outputDirectoryName = args[3];
			codeLanguage        = DirectoryPreprocessor.LANG_JAVA; //es lo que hay :-P
		}else{
			inputDirectoryName  = "samplecode";
			variablesFileName   = "samplecode/variables.txt";
			extensionName       = ".java";
			codeLanguage        = DirectoryPreprocessor.LANG_JAVA;
			outputDirectoryName = "output";
			
			System.err.println("Usage: preprocessor inputDirectory variablesFileName extensionName outputDirectoryName");
			System.err.println("\tTaking default values: ");
			System.err.println("\tpreprocessor " + inputDirectoryName + " " + variablesFileName + " " + extensionName + " " + outputDirectoryName);
		}
		
		
		if(new File(outputDirectoryName).exists())
			FileUtils.deleteDirectory(new File(outputDirectoryName));
		new File(outputDirectoryName).mkdir();
		
		DirectoryPreprocessor dp = new DirectoryPreprocessor(new File(variablesFileName));
		dp.preprocess(inputDirectoryName, outputDirectoryName, extensionName, codeLanguage);
	}
}
