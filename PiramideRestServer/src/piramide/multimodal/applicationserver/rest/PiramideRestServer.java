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
  
import org.restlet.Application;  
import org.restlet.Restlet;  
import org.restlet.Router;  
  
public class PiramideRestServer extends Application {  
  
	final static String APPLICATIONS_PATTERN = "/applications/";
	final static String VERSIONS_PATTERN     = "/applications/{application}/versions/";
	final static String COMPILATION_PATTERN  = "/applications/{application}/versions/{version}/devices/{devicemodel}/app.apk";
	
    /** 
     * Creates a root Restlet that will receive all incoming calls. 
     */  
    @Override  
    public Restlet createRoot() {  
        // Create a router Restlet that routes each call to a  
        // new instance of HelloWorldResource.  
        Router router = new Router(getContext());  
  
        // Defines only one route  
        router.attachDefault(Presentation.class);
        router.attach(APPLICATIONS_PATTERN, PiramApplicationsResource.class);
        router.attach(VERSIONS_PATTERN, PiramApplicationVersionsResource.class);
        router.attach(COMPILATION_PATTERN, CompilationResource.class);
  
        return router;  
    }  
}  