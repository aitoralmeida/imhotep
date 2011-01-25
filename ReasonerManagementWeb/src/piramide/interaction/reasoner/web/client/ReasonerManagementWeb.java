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
package piramide.interaction.reasoner.web.client;

import piramide.interaction.reasoner.web.client.ui.IMainWindow;
import piramide.interaction.reasoner.web.client.ui.LowerPanel;
import piramide.interaction.reasoner.web.client.ui.TitlePanel;
import piramide.interaction.reasoner.web.client.ui.common.Styles;
import piramide.interaction.reasoner.web.shared.Capabilities;
import piramide.interaction.reasoner.web.shared.model.CompilingConfiguration;
import piramide.interaction.reasoner.web.shared.model.DeviceCapabilities;
import piramide.interaction.reasoner.web.shared.model.FuzzyConfiguration;
import piramide.interaction.reasoner.web.shared.model.GeolocationRegions;
import piramide.interaction.reasoner.web.shared.model.UserCapabilities;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import com.google.gwt.user.client.ui.SuggestOracle.Response;

public class ReasonerManagementWeb implements EntryPoint, IMainWindow {
	
	final ReasonerManagerAsync reasonerManager = GWT.create(ReasonerManager.class);

	private CompilingConfiguration compilingConfiguration = new CompilingConfiguration();
	private FuzzyConfiguration fuzzyConfiguration = new FuzzyConfiguration();
	
	private final LowerPanel lowerPanel = new LowerPanel(this);
	
	public void onModuleLoad() {
		initializeService();
		
		
		final VerticalPanel mainWindow = new VerticalPanel();
		mainWindow.setSpacing(10);
		mainWindow.setWidth("70%");
		mainWindow.add(new TitlePanel());
		
		this.lowerPanel.disable();
		mainWindow.add(this.lowerPanel);

		RootPanel.get("main").add(mainWindow);
	}
	
	private int enabledSteps = 0;
	
	private void incrementEnabledSteps(){
		this.enabledSteps++;
		if(this.enabledSteps == 2){
			this.lowerPanel.enable();
			
			this.reasonerManager.searchDeviceID(new Request("htc",5), new AsyncCallback<Response>() {
				@Override
				public void onFailure(Throwable caught) {
					
				}

				@Override
				public void onSuccess(Response result) {
				}
			});			
		}
	}

	private void initializeService() {
		this.reasonerManager.retrieveGeolocationRegions(new AsyncCallback<String[]>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(String [] result) {
				incrementEnabledSteps();
				GeolocationRegions.clear();
				for(String str : result)
					GeolocationRegions.add(str);
			}
		});
		
		this.reasonerManager.retrieveCapabilities(new AsyncCallback<Capabilities>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Capabilities result) {
				incrementEnabledSteps();
				UserCapabilities.clear();
				for(String capability : result.getUserCapabilities())
					UserCapabilities.add(capability);
				
				DeviceCapabilities.clear();
				for(String capability : result.getDeviceCapabilities())
					DeviceCapabilities.add(capability);
			}
		});
	}
	
	@Override
	public void showError(String message){
		
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Error");
		
		final VerticalPanel dialogContents = new VerticalPanel();
		final Label label = new Label(message);
		label.setStyleName(Styles.error);
		dialogContents.add(label);
		final Button closeButton = new Button("Close");
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		dialogContents.add(closeButton);
		
		dialogBox.setWidget(dialogContents);
		dialogBox.setGlassEnabled(true);
	    dialogBox.setAnimationEnabled(true);
	    
	    dialogBox.center();
	    dialogBox.show();
	}

	@Override
	public void setFuzzyConfiguration(FuzzyConfiguration fuzzyConfiguration) {
		this.fuzzyConfiguration     = fuzzyConfiguration;
	}

	@Override
	public void setProfileConfiguration(CompilingConfiguration compilingConfiguration) {
		this.compilingConfiguration = compilingConfiguration;
	}

	@Override
	public CompilingConfiguration getCompilingConfiguration() {
		return this.compilingConfiguration;
	}

	@Override
	public FuzzyConfiguration getFuzzyConfiguration() {
		return this.fuzzyConfiguration;
	}
}
