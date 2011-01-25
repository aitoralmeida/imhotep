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
package piramide.interaction.reasoner.web.client.ui.fuzzy;

import piramide.interaction.reasoner.web.client.ui.IMainWindow;
import piramide.interaction.reasoner.web.client.ui.common.i18n.IPiramideMessages;
import piramide.interaction.reasoner.web.shared.model.FuzzyConfiguration;
import piramide.interaction.reasoner.web.shared.model.Variable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LinguisticTermsManagerPanel extends Composite {

	private static IPiramideMessages messages = GWT.create(IPiramideMessages.class);
	
	private static LinguisticTermsManagerPanelUiBinder uiBinder = GWT
			.create(LinguisticTermsManagerPanelUiBinder.class);

	interface LinguisticTermsManagerPanelUiBinder extends UiBinder<Widget, LinguisticTermsManagerPanel> {
	}

	@UiField
	VerticalPanel mainPanel;
	
	private final IMainWindow mainWindow;
	private int counter = 0;
	
	public LinguisticTermsManagerPanel(IMainWindow mainWindow){
		this.mainWindow = mainWindow;
		
		initWidget(uiBinder.createAndBindUi(this));
		
		final FuzzyConfiguration fuzzyConfiguration = this.mainWindow.getFuzzyConfiguration();
		
		for(Variable var : fuzzyConfiguration.getInput().getDeviceVariables())
			this.add(var, true, true);
		
		for(Variable var : fuzzyConfiguration.getInput().getUserVariables())
			this.add(var, true, false);
		
		for(Variable var : fuzzyConfiguration.getOutput().getVariables())
			this.add(var, false, true);
		
		checkEmpty();
	}
	
	private boolean isEmpty(){
		return this.counter == 0;
	}
	
	private void checkEmpty(){
		if(isEmpty()){
			final Label emptyLabel = new Label("<empty>");
			emptyLabel.setStyleName("error");
			this.mainPanel.add(emptyLabel);
		}
	}
	
	@UiHandler("addVariableButton")
	void onAddVariableButtonClicked(ClickEvent event){
		final DialogBox dialogBox = new DialogBox();
		final AddEditLinguisticTermPanel term = new AddEditLinguisticTermPanel(this.mainWindow, dialogBox, this);
		
		dialogBox.setWidget(term);
		
		dialogBox.setWidth("500px");
		dialogBox.setText(messages.edit());
		
		dialogBox.setGlassEnabled(true);
	    dialogBox.center();
	    dialogBox.show();
	}
	
	void delete(VariableItem variableItem){
		final FuzzyConfiguration fuzzyConfiguration = this.mainWindow.getFuzzyConfiguration();
		if(variableItem.isInput()){
			if(variableItem.isDeviceOrUser())
				fuzzyConfiguration.getInput().getDeviceVariables().remove(variableItem.getVariable());
			else
				fuzzyConfiguration.getInput().getUserVariables().remove(variableItem.getVariable());
		}else{
			fuzzyConfiguration.getOutput().getVariables().remove(variableItem.getVariable());
		}
		this.mainPanel.remove(variableItem);
		this.counter--;
		
		checkEmpty();
	}
	
	void add(Variable var, boolean input, boolean deviceUser){
		if(isEmpty())
			this.mainPanel.clear();
		
		this.mainPanel.add(new VariableItem(this.mainWindow, var.getName(), input, deviceUser, var, this));
		this.counter++;
	}
}
