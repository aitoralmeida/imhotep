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
package piramide.interaction.reasoner.web.client.ui.profile;

import java.util.HashMap;
import java.util.LinkedHashMap;

import piramide.interaction.reasoner.web.client.ReasonerManager;
import piramide.interaction.reasoner.web.client.ReasonerManagerAsync;
import piramide.interaction.reasoner.web.client.ui.IMainWindow;
import piramide.interaction.reasoner.web.client.ui.common.Styles;
import piramide.interaction.reasoner.web.shared.VariableTrendValues;
import piramide.interaction.reasoner.web.shared.model.CompilingConfiguration;
import piramide.interaction.reasoner.web.shared.model.FuzzyConfiguration;
import piramide.interaction.reasoner.web.shared.model.Variable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TestProfilePanel extends Composite {

	private static TestProfilePanelUiBinder uiBinder = GWT
			.create(TestProfilePanelUiBinder.class);
	
	private static ReasonerManagerAsync manager = GWT.create(ReasonerManager.class);

	interface TestProfilePanelUiBinder extends
			UiBinder<Widget, TestProfilePanel> {
	}

	private final IMainWindow mainWindow;
	
	@UiField
	Label loadingResultsLabel;
	
	@UiField
	VerticalPanel warningsPanel;
	
	@UiField
	VerticalPanel variablesPanel;
	
	public TestProfilePanel(IMainWindow mainWindow) {
		
		this.mainWindow = mainWindow;
		
		initWidget(uiBinder.createAndBindUi(this));
		
		initialize();
	}
	
	private void initialize(){
		final FuzzyConfiguration fuzzyConfig = this.mainWindow.getFuzzyConfiguration();
		final CompilingConfiguration compilingConfig = this.mainWindow.getCompilingConfiguration();
		
		manager.retrieveData(compilingConfig, fuzzyConfig, new AsyncCallback<VariableTrendValues>() {
			
			@Override
			public void onSuccess(VariableTrendValues result) {
				
				TestProfilePanel.this.loadingResultsLabel.setVisible(false);
				
				if(result.getWarningMessages().length > 0){
					TestProfilePanel.this.warningsPanel.setVisible(true);
					for(String warning : result.getWarningMessages()){
						final Label warningLabel = new Label(warning);
						warningLabel.setStyleName(Styles.error);
						TestProfilePanel.this.warningsPanel.add(warningLabel);
					}
				}
				
				final HashMap<String, LinkedHashMap<String, Double>> values = result.getValues();
				for(String variableName : values.keySet()){
					final HashMap<String, Double> term2value = values.get(variableName);
					
					final Double dValue = result.getInitialValues().get(variableName);
					final double value;
					if(dValue == null)
						value = 0.0;
					else
						value = dValue.doubleValue();
					
					boolean input = true;
					
					for(Variable var : fuzzyConfig.getOutput().getVariables()){
						if(var.getName().equals(variableName))
							input = false;
					}
					
					boolean devices = false;
					
					if(input){
						for(Variable var : fuzzyConfig.getInput().getDeviceVariables()){
							if(var.getName().equals(variableName))
								devices = true;
						}
					}
					
					final VariableWithValuesPanel panel = new VariableWithValuesPanel(variableName, compilingConfig.getGeolocation(), devices, input, term2value, value);
					TestProfilePanel.this.variablesPanel.add(panel);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				TestProfilePanel.this.loadingResultsLabel.setText(caught.getMessage());
				caught.printStackTrace();
			}
		});
	}
	
}
