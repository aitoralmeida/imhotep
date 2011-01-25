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
package piramide.interaction.reasoner.web.client.ui;

import piramide.interaction.reasoner.web.client.ui.common.Styles;
import piramide.interaction.reasoner.web.client.ui.common.i18n.IPiramideMessages;
import piramide.interaction.reasoner.web.client.ui.fuzzy.EnterRulesPanel;
import piramide.interaction.reasoner.web.client.ui.fuzzy.FuzzyConfigurationTextEntry;
import piramide.interaction.reasoner.web.client.ui.fuzzy.LinguisticTermsManagerPanel;
import piramide.interaction.reasoner.web.client.ui.profile.FindDevicePanel;
import piramide.interaction.reasoner.web.client.ui.profile.GeolocationPanel;
import piramide.interaction.reasoner.web.client.ui.profile.ManageUserProfilePanel;
import piramide.interaction.reasoner.web.client.ui.profile.ProfileConfigurationTextEntry;
import piramide.interaction.reasoner.web.client.ui.profile.TestProfilePanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RightPanel extends Composite {

	private static IPiramideMessages messages = GWT.create(IPiramideMessages.class);
	private static RightPanelUiBinder uiBinder = GWT
			.create(RightPanelUiBinder.class);

	interface RightPanelUiBinder extends UiBinder<Widget, RightPanel> {
	}

	@UiField
	VerticalPanel panel;

	private IMainWindow mainWindow;
	
	public RightPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
	}
	
	public void setMainWindow(IMainWindow mainWindow){
		this.mainWindow = mainWindow;
	}

	void loadWaitingPanel(){
		this.panel.clear();
		final Label label = new Label(messages.pleaseWaitWhileApplicationIsLoading());
		label.setStyleName(Styles.important);
		this.panel.add(label);
	}
	
	void loadWelcomePanel(){
		this.panel.clear();
		final Label label = new Label(messages.welcomeToImhotep());
		label.setStyleName(Styles.important);
		this.panel.add(label);
	}
	
	void loadLinguisticTermsPanel(){
		this.panel.clear();
		this.panel.add(new LinguisticTermsManagerPanel(this.mainWindow));
	}
	
	void loadFindDevicePanel() {
		this.panel.clear();
		this.panel.add(new FindDevicePanel(this.mainWindow));
	}

	void loadFuzzyConfigurationPanel() {
		this.panel.clear();
		this.panel.add(new FuzzyConfigurationTextEntry(this.mainWindow));
	}

	void loadEnterRulesPanel() {
		this.panel.clear();
		this.panel.add(new EnterRulesPanel(this.mainWindow));
	}

	void loadProfileConfigurationPanel() {
		this.panel.clear();
		this.panel.add(new ProfileConfigurationTextEntry(this.mainWindow));
	}
	
	void loadHelpPanel(){
		this.panel.clear();
		this.panel.add(new HelpPanel());
	}

	void loadManageUserProfilePanel() {
		this.panel.clear();
		this.panel.add(new ManageUserProfilePanel(this.mainWindow));
	}

	void loadTestProfilePanel() {
		this.panel.clear();
		this.panel.add(new TestProfilePanel(this.mainWindow));
	}

	void loadSelectLocationPanel() {
		this.panel.clear();
		this.panel.add(new GeolocationPanel(this.mainWindow));
	}
}
