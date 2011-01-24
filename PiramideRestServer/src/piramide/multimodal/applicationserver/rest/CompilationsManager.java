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
 *         Eduardo Castillejo <eduardo.castillejo@deusto.es>
 *
 */
package piramide.multimodal.applicationserver.rest;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import piramide.multimodal.preprocessor.DirectoryPreprocessor;
import piramide.multimodal.preprocessor.exceptions.EvaluationException;
import piramide.multimodal.preprocessor.exceptions.UserDefinedErrorException;

class CompilationsManager {
	
	private final boolean DEBUG = true;
	private final static String [] LAUNCH_SCRIPT_ARGS = new String [] {"ant"};
	private final static ConcurrentMap<String, Object> locks = new ConcurrentHashMap<String, Object>();
	private final DirectoriesManager directoriesManager;
	private final Map<String, String> userVariables;
	
	CompilationsManager(DirectoriesManager directoriesManager, Map<String, String> userVariables) {
		this.directoriesManager = directoriesManager;
		this.userVariables      = userVariables;
	}

	void performCompilation() throws ResourceException {
		final String binaryFilename = this.directoriesManager.getBinaryFileName();
		final Object lock = new Object();
		Object currentLock = locks.putIfAbsent(binaryFilename, lock);
		if(currentLock == null)
			currentLock = lock;
		
		synchronized(currentLock) {
			if(this.directoriesManager.binaryFileExists())
				return;
			
			generateVariablesFile();
			
			// Preprocess
			preprocess();
			
			// Compile
			compile();
			
			// Exit
			locks.remove(binaryFilename);
		}
	}

	private void generateVariablesFile() throws ResourceException {
		final File variablesFile = new File(this.directoriesManager.getVariablesTxtFileName());
		final JSONObject variables = new JSONObject();
		try{
			for(String key : this.userVariables.keySet()){
				final String value = this.userVariables.get(key);
				try{
					final int intValue = Integer.parseInt(value);
					variables.put(key, intValue);
					continue;
				}catch(NumberFormatException nfe){}
				
				try {
					final double doubleValue = Double.parseDouble(value);
					variables.put(key, doubleValue);
					continue;
				} catch (NumberFormatException e) {}
				
				if(value.equals("true") || value.equals("false"))
					variables.put(key, Boolean.valueOf(value));
				else
					variables.put(key, value);
			}
		}catch (JSONException e) {
			e.printStackTrace();
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Couldn't generate variables file: " + e.getMessage(), e);
		}
		
		try {
			FileUtils.writeStringToFile(variablesFile, variables.toString());
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Couldn't generate variables file: " + e.getMessage(), e);
		}
	}

	private void preprocess() throws ResourceException {
		try {
			final DirectoryPreprocessor preprocessor = new DirectoryPreprocessor(new File(this.directoriesManager.getVariablesTxtFileName()));
			preprocessor.preprocess(this.directoriesManager.getSourceCodeDirectoryName(), this.directoriesManager.getGeneratedCodeDirectoryName(), ".java", DirectoryPreprocessor.LANG_JAVA);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Couldn't preprocess file due to " + IOException.class.getCanonicalName() + ": " + e.getMessage(), e);
		} catch (UserDefinedErrorException e) {
			e.printStackTrace();
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Couldn't preprocess file due to " + UserDefinedErrorException.class.getCanonicalName() + ": " + e.getMessage(), e);
		} catch (EvaluationException e) {
			e.printStackTrace();
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Couldn't preprocess file due to " + EvaluationException.class.getCanonicalName() + ": " + e.getMessage(), e);
		}
	}
	
	private void compile() throws ResourceException{
		final boolean USE_PROCESS_BUILDER = System.getProperty("os.name").toLowerCase() == "linux";
		
		final File configurationDirectory = new File(this.directoriesManager.getConfigurationDirectoryName());
		final File compilationDirectory = new File(this.directoriesManager.getCompilationDirectoryName());
		try {
			FileUtils.copyDirectory(configurationDirectory, compilationDirectory);

			final Process process;
			if(USE_PROCESS_BUILDER){
				process = new ProcessBuilder(LAUNCH_SCRIPT_ARGS).directory(compilationDirectory).start();
			}else{
				final Runtime rt = Runtime.getRuntime();
				process = rt.exec(LAUNCH_SCRIPT_ARGS, new String[]{}, compilationDirectory);
			}
			
			final int returnCode = process.waitFor();
			final String inputStream = IOUtils.toString(process.getInputStream());
			final String errorStream = IOUtils.toString(process.getErrorStream());
			
			if(returnCode != 0 || DEBUG){
				System.out.println(inputStream);
				System.out.println(errorStream);
			}
			if(returnCode != 0){
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Couldn't compile file! " + inputStream.replaceAll("\n", "<br>") + "; errors: " + errorStream.replaceAll("\n", "<br>"));
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Couldn't find ant script!", e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Compiling process interrupted!", e);
		}
	}
}
