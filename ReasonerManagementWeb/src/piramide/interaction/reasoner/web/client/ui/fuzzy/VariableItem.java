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

import java.util.List;

import piramide.interaction.reasoner.web.client.ui.IMainWindow;
import piramide.interaction.reasoner.web.client.ui.common.i18n.IPiramideMessages;
import piramide.interaction.reasoner.web.shared.ImageUrlBuilder;
import piramide.interaction.reasoner.web.shared.model.Variable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

class VariableItem extends Composite {

	private static VariableItemUiBinder uiBinder = GWT.create(VariableItemUiBinder.class);
	private static IPiramideMessages messages = GWT.create(IPiramideMessages.class);

	interface VariableItemUiBinder extends UiBinder<Widget, VariableItem> {}
	
	@UiField
	Label nameLabel;
	
	@UiField
	Label typeLabel;
	
	@UiField
	Label userDeviceTypeLabel;
	
	@UiField
	HorizontalPanel userOrDevicePanel;
	
	private IMainWindow mainWindow; 
	private final Variable variable;
	private final LinguisticTermsManagerPanel linguisticPanel;
	
	VariableItem(IMainWindow mainWindow, String name, boolean type, boolean deviceUser, Variable variable, LinguisticTermsManagerPanel linguisticPanel) {
		this.mainWindow = mainWindow;
		this.linguisticPanel = linguisticPanel;
		this.variable = variable;
		
		initWidget(uiBinder.createAndBindUi(this));
		
		this.setName(name);
		this.setType(type);
		this.setInputType(deviceUser);
	}
	
	void setInputType(boolean deviceUser) {
		this.userOrDevicePanel.setVisible(this.isInput());
		this.userDeviceTypeLabel.setText(deviceUser?messages.device():messages.user());
	}

	void setType(boolean type){
		this.typeLabel.setText(type?messages.input():messages.output());
	}
	
	void setName(String name){
		this.nameLabel.setText(name);
		this.variable.setText(name);
	}
	
	void setTerms(String [] terms){
		this.variable.setTerms(terms);
	}
	
	boolean isDeviceOrUser(){
		return this.userDeviceTypeLabel.getText().equals(messages.device());
	}
	
	String getName(){
		return this.nameLabel.getText();
	}
	
	boolean isInput(){
		return this.typeLabel.getText().equals(messages.input());
	}
	
	Variable getVariable(){
		return this.variable;
	}
	
	@UiHandler("viewButton")
	void onViewButtonClicked(ClickEvent event){
		final String variableName = this.nameLabel.getText();
		final String variableType = this.typeLabel.getText();
		final String variableScope = this.userDeviceTypeLabel.getText();
		
		String[] terms = new String[]{};
		final boolean devices;
		final boolean input;
		if (variableType.equals(messages.input()) && variableScope.equals(messages.device())){	
			final List<Variable> deviceVariables = this.mainWindow.getFuzzyConfiguration().getInput().getDeviceVariables();
			terms = findVariableTerms(variableName, deviceVariables);
			devices = true;
			input = true;
		} else if (variableType.equals(messages.input())){	
			final List<Variable> userVariables = this.mainWindow.getFuzzyConfiguration().getInput().getUserVariables();
			terms = findVariableTerms(variableName, userVariables);
			devices = false;
			input = true;
		} else {
			final List<Variable> outputVariables = this.mainWindow.getFuzzyConfiguration().getOutput().getVariables();
			terms = findVariableTerms(variableName, outputVariables);
			devices = false;
			input = false;
		}
		
		
		final ImageUrlBuilder builder = new ImageUrlBuilder(variableName, null, devices, input, terms);
		
		final DialogBox dialogBox = new DialogBox();
		VerticalPanel variableImagePanel = new VerticalPanel();
		final Label loadingMessage = new Label(messages.loading());
		variableImagePanel.add(loadingMessage);
		final Image image = new Image(builder.toString());
		image.setVisible(false);
		variableImagePanel.add(image);
		image.addLoadHandler(new LoadHandler() {
			
			@Override
			public void onLoad(LoadEvent event) {
				image.setVisible(true);
				loadingMessage.setVisible(false);
			}
		});
		
		Button closeButton = new Button(messages.cancel());
		closeButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				
			}
		});
		
		variableImagePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		variableImagePanel.add(closeButton);
		
		dialogBox.setWidget(variableImagePanel);
		
		dialogBox.setWidth("500px");
		dialogBox.setText(this.getName());
		
		dialogBox.setGlassEnabled(true);
	    dialogBox.center();
	    dialogBox.show();
		
		
	}

	private String[] findVariableTerms(final String variableName, final List<Variable> variables) {
		for(Variable variable : variables){
			if(variable.getName().equals(variableName))
				return variable.getTerms();
				
		}
		throw new IllegalArgumentException("Variable not found");
	}
	
	@UiHandler("deleteButton")
	void onDeleteButtonClicked(ClickEvent event){
		this.linguisticPanel.delete(this);
	}
	
	@UiHandler("editButton")
	void onEditButtonClicked(ClickEvent event){
		final DialogBox dialogBox = new DialogBox();
		final AddEditLinguisticTermPanel term = new AddEditLinguisticTermPanel(this.mainWindow, dialogBox, this);
		
		dialogBox.setWidget(term);
		
		dialogBox.setWidth("500px");
		dialogBox.setText(messages.edit());
		
		dialogBox.setGlassEnabled(true);
	    dialogBox.center();
	    dialogBox.show();
	}
}
