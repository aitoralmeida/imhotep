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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

class DirectoriesManager {
	
	private static final String BASE_DIRECTORY = ".piramide";
	private static final String APPLICATIONS   = "applications";
	private static final String PROJECT        = "project";
	private static final String COMPILATIONS   = "compilations";
	private static final String CONFIGURATION  = "configuration";
	private static final String SCRIPT_FILE    = "build.xml";
	private static final String BINARY_FILE    = "app.apk";
	private static final String VARIABLES      = "variables.txt";
	private static final String SOURCE_CODE    = "src";
	private static final String GENERATED_CODE = "generated";
	
	private final String application;
	private final String version;
	private final Map<String, String> userVariables;
	
	private String applicationsDirectoryName;
	private String applicationDirectoryName;
	private String applicationVersionDirectoryName;
	private String projectDirectoryName;
	private String antScriptFileName;
	private String compilationsDirectoryName;
	private String configurationDirectoryName;
	private String variablesTxtFileName;
	private String sourceCodeDirectoryName;
	private String generatedCodeDirectoryName;
	
	DirectoriesManager(){
		this.application = null;
		this.version = null;
		this.userVariables = null;
	}
	
	DirectoriesManager(String application){
		this.application = application;
		this.version = null;
		this.userVariables = null;
	}
	
	DirectoriesManager(String application, String version){
		this.application = application;
		this.version = version;
		this.userVariables = null;
	}
	
	DirectoriesManager(String application, String version, Map<String, String> userVariables){
		this.application = application;
		this.version = version;
		this.userVariables = userVariables;
	}
	
	String getProjectDirectoryName(){
		return this.projectDirectoryName;
	}
	
	String getApplicationVersionDirectoryName(){
		return this.applicationVersionDirectoryName;
	}
	
	String getAntScriptFileName(){
		return this.antScriptFileName;
	}
	
	String getCompilationsDirectoryName(){
		return this.compilationsDirectoryName;
	}
	
	String getCompilationDirectoryName(){
		final String identifier = createIdentifier(this.userVariables);
		return this.compilationsDirectoryName + File.separator + identifier;
	}
	
	String getBinaryFileName(){
		return getCompilationDirectoryName() + File.separator + BINARY_FILE;
	}
	
	String getConfigurationDirectoryName(){
		return this.configurationDirectoryName;
	}
	
	String getVariablesTxtFileName(){
		return this.variablesTxtFileName;
	}
	
	String getSourceCodeDirectoryName(){
		return this.sourceCodeDirectoryName;
	}
	
	String getGeneratedCodeDirectoryName(){
		return this.generatedCodeDirectoryName;
	}
	
	String [] listApplications(){
		final File applicationsDirectory = new File(this.applicationsDirectoryName);
		final String [] apps = applicationsDirectory.list();
		final List<String> applications = new Vector<String>(apps.length);
		for(String app : apps)
			if(!app.equals(".svn"))
				applications.add(app);
		return applications.toArray(new String[]{});
	}
	
	String [] listVersions(){
		final File applicationDirectory = new File(this.applicationDirectoryName);
		final String [] vers = applicationDirectory.list();
		final List<String> versions = new Vector<String>(vers.length);
		for(String ver : vers)
			if(!ver.equals(".svn"))
				versions.add(ver);
		return versions.toArray(new String[]{});
	}
	
	boolean binaryFileExists(){
		final File binaryFile = new File(getBinaryFileName());
		return binaryFile.exists();
	}

	void createCompilationDirectory() throws ResourceException {
		final String compilationDirectoryName = getCompilationDirectoryName();
		final File compilationDirectory = new File(compilationDirectoryName);
		if(compilationDirectory.mkdir()){ // Only if I have created the directory
			final File projectDirectory = new File(this.projectDirectoryName);
			try {
				FileUtils.copyDirectory(projectDirectory, compilationDirectory);
			} catch (IOException e) {
				throw new ResourceException(e);
			}
		}
	}
	
	private String createIdentifier(Map<String, String> userVariables){
		final StringBuilder arguments = new StringBuilder();
		final List<String> keys = new Vector<String>(this.userVariables.keySet());
		Collections.sort(keys);
		
		for(String key : keys){
			arguments.append("_");
			arguments.append(key);
			arguments.append("-");
			arguments.append(userVariables.get(key));
		}
		
		final StringBuilder builder = new StringBuilder("compilation");
		
		final String arg = arguments.toString();
		if(needsToBeEncoded(arg))
			builder.append(encode(arg));
		else
			builder.append(arg);
		
		return builder.toString();
	}
	
	private String encode(String arg){
		try {
			final MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(arg.getBytes());
			byte[] md5sum = digest.digest();
			final BigInteger bigInt = new BigInteger(1, md5sum);
			final String output = bigInt.toString(16);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("MD5 required: " + e.getMessage(), e);
		}
	}

	private boolean needsToBeEncoded(final String arg) {
		boolean needsToBeEncoded = false;
		for(char c : arg.toCharArray())
			if(!Character.isLetterOrDigit(c) && c != '.' && c != '_' && c != '-')
				needsToBeEncoded = true;
		return needsToBeEncoded;
	}
	

	void loadGlobalStructure() throws ResourceException {
    	// .piramide exists
        final File baseDirectory = new File(BASE_DIRECTORY);
        if(!baseDirectory.exists())
        	representError("PIRAmIDE base directory not found; expected: " + baseDirectory.getAbsolutePath());
        
    	// .piramide/configuration exists
    	
    	final StringBuilder configurationPath = new StringBuilder(BASE_DIRECTORY);
    	configurationPath.append(File.separator);
    	configurationPath.append(CONFIGURATION);
    	final File configurationDirectory = new File(configurationPath.toString());
    	if(!configurationDirectory.exists())
    		representError("PIRAmIDE configuration directory not found; expected: " + configurationDirectory.getAbsolutePath());
    	
    	this.configurationDirectoryName = configurationDirectory.getAbsolutePath();
    	
    	// .piramide/applications exists
        
    	final StringBuilder path = new StringBuilder(BASE_DIRECTORY);
    	path.append(File.separator);
    	path.append(APPLICATIONS);
    	
    	final File applicationsDir = new File(path.toString());
    	if(!applicationsDir.exists())
    		representError("PIRAmIDE applications directory not found; expected: " + applicationsDir.getAbsolutePath());
    	
    	this.applicationsDirectoryName = applicationsDir.getAbsolutePath();
	}
	
	void loadApplication() throws ResourceException {
		if(this.application == null)
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Calling loadApplication without providing an application");
		
		loadGlobalStructure();
		
		final StringBuilder path = new StringBuilder(this.applicationsDirectoryName);
    	// .piramide/applications/{application} exists
        
    	path.append(File.separator);
    	path.append(this.application);
    	final File applicationDir = new File(path.toString());
    	if(!applicationDir.exists())
    		representError("PIRAmIDE concrete application directory not found; expected: " + applicationDir.getAbsolutePath());
    	this.applicationDirectoryName = applicationDir.getAbsolutePath();
	}
	
	private void loadVersion() throws ResourceException {
		if(this.version == null)
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Calling loadVersion without providing an version");
		
		loadApplication();
		
		final StringBuilder path = new StringBuilder(this.applicationDirectoryName);
    	// .piramide/applications/{application}/{version} exists
		
    	path.append(File.separator);
    	path.append(this.version);
    	final File applicationVersionDir = new File(path.toString());
    	if(!applicationVersionDir.exists())
    		representError("PIRAmIDE concrete application version directory not found; expected: " + applicationVersionDir.getAbsolutePath());
    	
    	this.applicationVersionDirectoryName = applicationVersionDir.getAbsolutePath();

    	// .piramide/applications/{application}/{version}/project exists
    	
    	final StringBuilder projectDirectoryName = new StringBuilder(path.toString()); 
    	projectDirectoryName.append(File.separator);
    	projectDirectoryName.append(PROJECT);
    	final File projectDir = new File(projectDirectoryName.toString());
    	if(!projectDir.exists())
    		representError("PIRAmIDE concrete application version project directory not found; expected: " + projectDir.getAbsolutePath());

    	this.projectDirectoryName = projectDir.getAbsolutePath();

    	// .piramide/applications/{application}/{version}/compilations exists and is writable
    	
    	final StringBuilder compilationsDirectoryName = new StringBuilder(path.toString());
    	compilationsDirectoryName.append(File.separator);
    	compilationsDirectoryName.append(COMPILATIONS);
    	final File compilationsDir = new File(compilationsDirectoryName.toString());
    	if(!compilationsDir.exists())
    		representError("PIRAmIDE concrete application compilations directory not found; expected: " + compilationsDir.getAbsolutePath());
    	
    	if(!compilationsDir.canWrite())
    		representError("PIRAmIDE concrete application compilations directory not writable in: " + compilationsDir.getAbsolutePath());
    	
    	this.compilationsDirectoryName = compilationsDir.getAbsolutePath();
    	
    	// .piramide/applications/{application}/{version}/project/build.xml exists
    	
    	projectDirectoryName.append(File.separator);
    	projectDirectoryName.append(SCRIPT_FILE);
    	final File scriptFile = new File(projectDirectoryName.toString());
    	if(!scriptFile.exists())
    		representError("PIRAmIDE concrete application script file not found; expected: " + scriptFile.getAbsolutePath());
    	
    	this.antScriptFileName = scriptFile.getAbsolutePath();
 	}
	
	void loadCompilation() throws ResourceException {
		if(this.userVariables == null)
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Calling loadCompilation without providing userVariables");
		
		loadVersion();
    	
    	final String identifier = createIdentifier(this.userVariables);
    	createCompilationDirectory();
    	
    	final String compilationDirectoryName = compilationsDirectoryName + File.separator + identifier + File.separator; 

    	// .piramide/applications/{application}/{version}/compilations/{compilation}/variables.txt; it doesn't matter if it exists or not, we're going to overwrite ti
       	
    	final StringBuilder variablesFileName = new StringBuilder(compilationDirectoryName);
    	variablesFileName.append(File.separator);
    	variablesFileName.append(VARIABLES);
    	final File variablesFile = new File(variablesFileName.toString());
    	this.variablesTxtFileName = variablesFile.getAbsolutePath();
    
    	
    	// .piramide/applications/{application}/{version}/src exists
    	
    	final StringBuilder sourceCodeDirectoryName = new StringBuilder(compilationDirectoryName);
    	sourceCodeDirectoryName.append(File.separator);
    	sourceCodeDirectoryName.append(SOURCE_CODE);
    	final File sourceCodeDirectory = new File(sourceCodeDirectoryName.toString());
    	if(!sourceCodeDirectory.exists())
    		representError("PIRAmIDE concrete application source directory not found; expected: " + sourceCodeDirectory.getAbsolutePath());
    	this.sourceCodeDirectoryName = sourceCodeDirectory.getAbsolutePath();
    	
    	// .piramide/applications/{application}/{version}/src exists
    	
    	final StringBuilder generatedCodeDirectoryName = new StringBuilder(compilationDirectoryName);
    	generatedCodeDirectoryName.append(File.separator);
    	generatedCodeDirectoryName.append(GENERATED_CODE);
    	final File generatedCodeDirectory = new File(generatedCodeDirectoryName.toString());
    	generatedCodeDirectory.mkdir();
    	this.generatedCodeDirectoryName = generatedCodeDirectory.getAbsolutePath();
	}
	
    private void representError(String message) throws ResourceException{
    	throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, message);
    }
}
