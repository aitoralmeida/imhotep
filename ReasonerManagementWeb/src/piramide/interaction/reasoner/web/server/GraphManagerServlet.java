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
package piramide.interaction.reasoner.web.server;

import javax.servlet.http.HttpServlet;

import piramide.interaction.reasoner.FuzzyReasonerException;
import piramide.interaction.reasoner.FuzzyReasonerWizardFacade;
import piramide.interaction.reasoner.Geolocation;
import piramide.interaction.reasoner.IFuzzyReasonerWizardFacade;
import piramide.interaction.reasoner.web.shared.ImageUrlBuilder;

@SuppressWarnings("serial")
public class GraphManagerServlet extends HttpServlet {
	
	private final static int DEFAULT_WIDTH  = 600;
	private final static int DEFAULT_HEIGHT = 400;
	
	private final IFuzzyReasonerWizardFacade facade;
	
	public GraphManagerServlet() throws FuzzyReasonerException{
		this.facade = new FuzzyReasonerWizardFacade();
	}
	
	@Override
	protected void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) 
		throws javax.servlet.ServletException, java.io.IOException{
		
		final String variableName = req.getParameter(ImageUrlBuilder.VARIABLE);
		final String [] linguisticTerms = req.getParameterValues(ImageUrlBuilder.VALUE);
		
		final String geoName = req.getParameter(ImageUrlBuilder.GEO);
		final Geolocation geo; 
		if(geoName == null)
			geo = Geolocation.ALL;
		else
			geo = new Geolocation(geoName);
		
		final boolean inputOutput = retrieveBooleanParameter(req, true, ImageUrlBuilder.INPUT);
		final boolean devicesUsers = retrieveBooleanParameter(req, true, ImageUrlBuilder.DEVICES);
		
		final int width = retrieveIntegerParameter(req,  DEFAULT_WIDTH, "width");
		final int height = retrieveIntegerParameter(req, DEFAULT_HEIGHT, "height");
		
		try {
			resp.setContentType("image/png");
			this.facade.generateMembershipFunctionGraph(inputOutput, devicesUsers, variableName, linguisticTerms, resp.getOutputStream(), width, height, geo);
		} catch (FuzzyReasonerException e) {
			resp.getWriter().println("failer: " + e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	private int retrieveIntegerParameter( javax.servlet.http.HttpServletRequest req, int defaultValue, String parameterName) {
		final String widthStr = req.getParameter(parameterName);
		if(widthStr == null)
			return defaultValue;
		try {
			return Integer.parseInt(widthStr);
		} catch (NumberFormatException e1) {
			return defaultValue;
		}
	}
	
	private boolean retrieveBooleanParameter( javax.servlet.http.HttpServletRequest req, boolean defaultValue, String parameterName) {
		final String widthStr = req.getParameter(parameterName);
		if(widthStr == null)
			return defaultValue;
		return Boolean.parseBoolean(widthStr);
	}
}
