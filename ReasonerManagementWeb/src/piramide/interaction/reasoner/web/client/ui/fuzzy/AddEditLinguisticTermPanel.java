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

import java.util.ArrayList;
import java.util.List;

import piramide.interaction.reasoner.web.client.ui.IMainWindow;
import piramide.interaction.reasoner.web.client.ui.common.i18n.IPiramideMessages;
import piramide.interaction.reasoner.web.shared.model.DeviceCapabilities;
import piramide.interaction.reasoner.web.shared.model.FuzzyConfiguration;
import piramide.interaction.reasoner.web.shared.model.UserCapabilities;
import piramide.interaction.reasoner.web.shared.model.Variable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

class AddEditLinguisticTermPanel extends Composite {

	private static IPiramideMessages messages = GWT.create(IPiramideMessages.class);
	
	private static AddEditLinguisticTermPanelUiBinder uiBinder = GWT
			.create(AddEditLinguisticTermPanelUiBinder.class);

	interface AddEditLinguisticTermPanelUiBinder extends UiBinder<Widget, AddEditLinguisticTermPanel> {}

	@UiField
	ListBox inputOutputBox;
	
	@UiField
	HorizontalPanel inputSelectionPanel;
	
	@UiField
	ListBox deviceUserBox;
	
	@UiField
	TextBox nameTextBox;
	
	@UiField
	ListBox deviceNameListBox;
	
	@UiField
	ListBox userNameListBox;
	
	@UiField
	ListBox linguisticTermsListBox;
	
	@UiField
	Button addLinguisticTermButton;
	
	private final IMainWindow mainWindow;
	private final DialogBox dialogBox;
	private VariableItem variableItem;
	private LinguisticTermsManagerPanel linguisticTermsManagerPanel;
	
	// Editing
	AddEditLinguisticTermPanel(IMainWindow mainWindow, DialogBox dialogBox, VariableItem variableItem) {
		this(mainWindow, dialogBox);
		
		this.variableItem = variableItem;
		
		for(String term : this.variableItem.getVariable().getTerms())
			this.linguisticTermsListBox.addItem(term);
		
		this.updateUserInterface(this.variableItem.isInput(), variableItem.isDeviceOrUser());
		
		this.nameTextBox.setText(this.variableItem.getName());
		selectVariableName(this.variableItem.getName(), this.deviceNameListBox);
		selectVariableName(this.variableItem.getName(), this.userNameListBox);
		
		selectVariableName(this.variableItem.isDeviceOrUser()?messages.device():messages.user(), this.deviceUserBox);
	}
	
	private void selectVariableName(final String name, final ListBox box) {
		final int pos = findPosition(box, name);
		box.setSelectedIndex(pos);
	}

	private int findPosition(ListBox box, String name) {
		for(int i = 0; i < box.getItemCount(); ++i)
			if(box.getItemText(i).equals(name))
				return i;
		
		return 0;
	}
	
	AddEditLinguisticTermPanel(IMainWindow mainWindow, DialogBox dialogBox, LinguisticTermsManagerPanel linguisticTermsManagerPanel){
		this(mainWindow, dialogBox);
		
		this.linguisticTermsManagerPanel = linguisticTermsManagerPanel;
	}
	
	private AddEditLinguisticTermPanel(IMainWindow mainWindow, DialogBox dialogBox){
		this.mainWindow = mainWindow;
		this.dialogBox = dialogBox;
		
		initWidget(uiBinder.createAndBindUi(this));
		
		this.inputOutputBox.addItem(messages.input());
		this.inputOutputBox.addItem(messages.output());
		
		this.deviceUserBox.addItem(messages.device());
		this.deviceUserBox.addItem(messages.user());
		
		for(String deviceCapability : DeviceCapabilities.values())
			this.deviceNameListBox.addItem(deviceCapability);
		
		for(String userCapability : UserCapabilities.values())
			this.userNameListBox.addItem(userCapability);
	}
	
	@UiHandler("cancelButton")
	void onCancelButtonClicked(ClickEvent event){
		this.dialogBox.hide();
	}
	
	@UiHandler("inputOutputBox")
	void onInputOutputBoxChanged(ChangeEvent event){
		updateUserInterface(isInputSelected(), isDeviceSelected());
	}

	private void updateUserInterface(boolean input, boolean device) {
		if(!input){
			this.inputOutputBox.setSelectedIndex(1);
			this.userNameListBox.setVisible(false);
			this.deviceNameListBox.setVisible(false);
			
			this.nameTextBox.setVisible(true);
			
			this.inputSelectionPanel.setVisible(false);
		}else{
			this.inputOutputBox.setSelectedIndex(0);
			this.inputSelectionPanel.setVisible(true);
			this.nameTextBox.setVisible(false);
			
			if(device){
				this.userNameListBox.setVisible(false);
				this.deviceNameListBox.setVisible(true);
			}else{
				this.userNameListBox.setVisible(true);
				this.deviceNameListBox.setVisible(false);
			}
		}
	}

	@UiHandler("deviceUserBox")
	void onDeviceNameListBoxChanged(ChangeEvent event){
		if(isDeviceSelected()){
			this.userNameListBox.setVisible(false);
			this.deviceNameListBox.setVisible(true);
		}else{
			this.userNameListBox.setVisible(true);
			this.deviceNameListBox.setVisible(false);
		}
	}
	
	@UiHandler("saveButton")
	void onSaveButtonClicked(ClickEvent event){
		this.dialogBox.hide();

		if(this.variableItem != null){ // If it is editing
			this.variableItem.setName(this.getName());
			this.variableItem.setTerms(this.getTerms());
			this.variableItem.setType(this.isInputSelected());
			this.variableItem.setInputType(this.isDeviceSelected());
		}else{ // It is adding
			final Variable var = new Variable(this.getName(), this.getTerms());
			this.linguisticTermsManagerPanel.add(var, this.isInputSelected(), isDeviceSelected());
			
			final FuzzyConfiguration config = this.mainWindow.getFuzzyConfiguration();
			if(this.isInputSelected()){
				if(isDeviceSelected()){
					config.getInput().getDeviceVariables().add(var);
				}else
					config.getInput().getUserVariables().add(var);
			}else
				config.getOutput().getVariables().add(var);
		}
	}
		
	
	private boolean isInputSelected() {
		final boolean inputSelected = this.inputOutputBox.getSelectedIndex() == 0;
		return inputSelected;
	}

	private boolean isDeviceSelected() {
		final boolean deviceSelected = this.deviceUserBox.getSelectedIndex() == 0;
		return deviceSelected;
	}
	
	private String getName(){
		if(!isInputSelected())
			return this.nameTextBox.getText();
		
		if(isDeviceSelected())
			return this.deviceNameListBox.getItemText(this.deviceNameListBox.getSelectedIndex());
		
		return this.userNameListBox.getItemText(this.userNameListBox.getSelectedIndex());
	}
	
	private String [] getTerms(){
		final List<String> terms = new ArrayList<String>();
		for(int i = 0; i < this.linguisticTermsListBox.getItemCount(); ++i)
			terms.add(this.linguisticTermsListBox.getItemText(i));
		return terms.toArray(new String[]{});
	}	
	
	@UiHandler("addLinguisticTermButton")
	void onAddLinguisticTermButtonClicked(ClickEvent event){
		final DialogBox addDialogBox = new DialogBox();
		addDialogBox.setGlassEnabled(true);
		
		addDialogBox.setText(messages.add());
		
		final VerticalPanel vpanel = new VerticalPanel();
		vpanel.add(new Label(messages.addLinguisticTerm()));
		final TextBox textbox = new TextBox();
		textbox.setVisibleLength(10);
		vpanel.add(textbox);
		
		final HorizontalPanel hpanel = new HorizontalPanel();
		final Button cancelButton = new Button(messages.cancel());
		final Button addButton = new Button(messages.add());
		
		hpanel.add(addButton);
		hpanel.add(cancelButton);
		vpanel.add(hpanel);
		
		addDialogBox.setWidget(vpanel);
		
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addDialogBox.hide();
			}
		});
		
		final ClickHandler handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addDialogBox.hide();
				AddEditLinguisticTermPanel.this.linguisticTermsListBox.addItem(textbox.getText());
				AddEditLinguisticTermPanel.this.addLinguisticTermButton.setFocus(true);
			}
		};
		
		final KeyPressHandler keyboardHandler = new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getCharCode() == KeyCodes.KEY_ENTER)
					handler.onClick(null);
			}
		};
		
		textbox.addKeyPressHandler(keyboardHandler);
		addButton.addClickHandler(handler);
		
		addDialogBox.center();
		addDialogBox.show();
		
		textbox.setFocus(true);
	}
	
	@UiHandler("removeLinguisticTermButton")
	void onRemoveLinguisticTermButtonClicked(ClickEvent event){
		
		for(int i = 0; i < this.linguisticTermsListBox.getItemCount(); ++i)
			if(this.linguisticTermsListBox.isItemSelected(i)){
				this.linguisticTermsListBox.removeItem(i);
				--i;
			}
	}
}
