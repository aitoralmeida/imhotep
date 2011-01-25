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

import java.util.Map;

import piramide.interaction.reasoner.web.client.ui.IMainWindow;
import piramide.interaction.reasoner.web.client.ui.common.Styles;
import piramide.interaction.reasoner.web.client.ui.common.i18n.IPiramideMessages;
import piramide.interaction.reasoner.web.shared.model.UserCapabilities;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ManageUserProfilePanel extends Composite {

	private static ManageUserProfilePanelUiBinder uiBinder = GWT
			.create(ManageUserProfilePanelUiBinder.class);
	
	private static IPiramideMessages messages = GWT.create(IPiramideMessages.class);

	interface ManageUserProfilePanelUiBinder extends
			UiBinder<Widget, ManageUserProfilePanel> {
	}
	
	private final IMainWindow mainWindow;
	private final int CAPABILITY_COLUMN_NUMBER = 2;
	
	@UiField
	FlexTable profileTable;
	
	@UiField
	HorizontalPanel addCapabilityPanel;
	
	@UiField
	ListBox capabilityNameListBox;
	
	@UiField
	TextBox valueTextbox;

	public ManageUserProfilePanel(IMainWindow mainWindow) {
		
		this.mainWindow = mainWindow;
		
		initWidget(uiBinder.createAndBindUi(this));
		
		final Map<String, String> capabilities = this.mainWindow.getCompilingConfiguration().getProfile().getCapabilities();
		
		int counter = 0;
		
		for(final String capability : capabilities.keySet()){
			addRow(capabilities, counter, capability);
			counter++;
		}
		
		showAddButton();
	}

	
	private void addRow(final Map<String, String> capabilities, int counter, final String capability) {
		final Label capabilityNameLabel = new Label(messages.capability() + ":");
		capabilityNameLabel.setStyleName(Styles.important);
		this.profileTable.setWidget(counter, 1, capabilityNameLabel);
		
		final Label capabilityNameValue = new Label(capability);
		this.profileTable.setWidget(counter, this.CAPABILITY_COLUMN_NUMBER, capabilityNameValue);
		
		final Label capabilityValueLabel = new Label(messages.capability() + ":");
		capabilityValueLabel.setStyleName(Styles.important);
		this.profileTable.setWidget(counter, 3, capabilityValueLabel);
		
		final Label capabilityValueValue = new Label(capabilities.get(capability));
		this.profileTable.setWidget(counter, 4, capabilityValueValue);
		
		final Button delete = new Button(messages.delete());
		this.profileTable.setWidget(counter, 5, delete);
		
		delete.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				capabilities.remove(capability);
				removeCapability(capability);
				showAddButton();
			}
		});
	}
	
	@UiHandler("addButton")
	void onAddButtonClicked(ClickEvent event){
		final String item  = this.capabilityNameListBox.getItemText(this.capabilityNameListBox.getSelectedIndex());
		final String value = this.valueTextbox.getText();
		final Map<String, String> capabilities = this.mainWindow.getCompilingConfiguration().getProfile().getCapabilities();
		capabilities.put(item, value);
		
		addRow(capabilities, this.profileTable.getRowCount(), item);
		showAddButton();
		this.valueTextbox.setText("");
	}
	
	private void showAddButton(){
		final Map<String, String> capabilities = this.mainWindow.getCompilingConfiguration().getProfile().getCapabilities();
		
		if(UserCapabilities.values().size() == capabilities.size()){
			this.addCapabilityPanel.setVisible(false);
			return;
		}
		
		this.addCapabilityPanel.setVisible(true);
		
		this.capabilityNameListBox.clear();
		
		for(String capability : UserCapabilities.values())
			if(!capabilities.containsKey(capability))
				this.capabilityNameListBox.addItem(capability);
		
	}

	private void removeCapability(final String capability) {
		
		for(int rowNumber = 0; rowNumber < ManageUserProfilePanel.this.profileTable.getRowCount(); ++rowNumber){
			final String capabilityName = ((Label)ManageUserProfilePanel.this.profileTable.getWidget(rowNumber, this.CAPABILITY_COLUMN_NUMBER)).getText();
			if(capabilityName.equals(capability))
				ManageUserProfilePanel.this.profileTable.removeRow(rowNumber);
		}
		
	}
}
