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
  
import java.util.HashMap;
import java.util.Map;

import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
  
public class CompilationResource extends Resource {  
  
	private final static String USER_VARIABLE_PREFIX = "piramide.user.";
	
	private final String application;
	private final String deviceModel;
	private final String version;
	
	private final Map<String, String> userVariables = new HashMap<String, String>();
	private final DirectoriesManager directoriesManager;
	private final CompilationsManager compilationsManager;
	
	public CompilationResource(Context context, Request request, Response response) {  
        super(context, request, response);
        
        this.application = (String)request.getAttributes().get("application");
        this.deviceModel = (String)request.getAttributes().get("devicemodel");
        this.version     = (String)request.getAttributes().get("version");
        
        final Form form = request.getResourceRef().getQueryAsForm();
        for(Parameter param : form)
        	if(param.getName().startsWith(USER_VARIABLE_PREFIX))
        		this.userVariables.put(param.getName(), param.getValue());
        
        this.directoriesManager = new DirectoriesManager(this.application, this.version, this.userVariables);
        this.compilationsManager = new CompilationsManager(this.directoriesManager, this.userVariables);
        
        // This representation has only one type of representation.  
        getVariants().add(new Variant(MediaType.TEXT_PLAIN));
    }  
  
    @Override  
    public Representation represent(Variant variant) throws ResourceException {
    	this.directoriesManager.loadCompilation(); // Checks the directories, throws exceptions if any directory is missing
    	
    	displayMessage();
    	
    	this.compilationsManager.performCompilation();
    	
    	return new FileRepresentation(this.directoriesManager.getBinaryFileName(), MediaType.APPLICATION_ZIP);
    }

	private void displayMessage() {
		final StringBuilder builder = new StringBuilder("Hi, I have to compile for the version " + this.version + " of the application " + this.application + " and the device model " + this.deviceModel + " with the following user variables: ");
    	for(String key : this.userVariables.keySet())
    		builder.append("\n" + key + ": " + this.userVariables.get(key));
    	System.out.println(builder.toString());
	}
}  