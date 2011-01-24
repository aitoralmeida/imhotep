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

import org.json.JSONArray;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

public class PiramApplicationsResource extends Resource {

	private final DirectoriesManager directoriesManager;
	
	public PiramApplicationsResource(Context context, Request request, Response response) {
		super(context, request, response);
		
        this.directoriesManager = new DirectoriesManager();
        
        getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		this.directoriesManager.loadGlobalStructure();
		final JSONArray arr = new JSONArray();
		
		for(String application : this.directoriesManager.listApplications())
			arr.put(application);
		
		return new StringRepresentation(arr.toString());
	}
	
}
