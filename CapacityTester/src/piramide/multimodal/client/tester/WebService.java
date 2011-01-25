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
package piramide.multimodal.client.tester;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebService {
	
	void getApplicationsList() {
		final String NAMESPACE   = "http://localhost/";
    	final String METHOD_NAME = "getApplicationsList";
    	final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
    	final String URL         = "http://192.168.59.6:8080/IDownloadApplication/services/IDownloadApplicationSOAP";
    	
    	try{
    	
	    	SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	    	// Debería ser addProperty!!!
	    	
	    	//request.addProperty("model", "nokia_xpressmusic");
	    	// // genera: <n0:getApplicationsList id="o0" c:root="1" xmlns:n0="http://localhost/"><model i:type="d:string">nokia_xpressmusic</model></n0:getApplicationsList>
	    	// // devuelve: <soapenv:Fault><faultcode>soapenv:Server.userException</faultcode><faultstring>org.xml.sax.SAXException: Invalid element in localhost.IDownloadApplication.GetApplicationsListRequest - model</faultstring><detail><ns1:hostname xmlns:ns1="http://xml.apache.org/axis/">nctrun-laptop</ns1:hostname></detail></soapenv:Fault>
	    	
	    	//request.addAttribute("model", "nokia_xpressmusic");
	    	// // genera: <n0:getApplicationsList id="o0" c:root="1" model="nokia_xpressmusic" xmlns:n0="http://localhost/" />
	    	// // devuelve bien cero resultados (asume que estás llamando a getApplicationsList en minúsculas)
	    	
	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        envelope.setOutputSoapObject(request);
	
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	
	        androidHttpTransport.call(SOAP_ACTION, envelope);
	
	        final Object result = envelope.getResponse();
	        
	        System.out.println(result);
	        System.out.println(result.getClass().getName());
	        
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
	
		
	
	void getDeviceList() {
		final String NAMESPACE   = "http://localhost/";
    	final String METHOD_NAME = "getDevicesList";
    	final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
    	final String URL         = "http://192.168.59.6:8080/IDownloadApplication/services/IDownloadApplicationSOAP";
    	
    	try{
    	
	    	SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	    	//request.addProperty("prop1", "myprop");
	    	
	
	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        envelope.setOutputSoapObject(request);
	
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	
	        androidHttpTransport.call(SOAP_ACTION, envelope);
	
	        final Object result = envelope.getResponse();
	        
	        System.out.println(result);
	        
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
	
	
}
