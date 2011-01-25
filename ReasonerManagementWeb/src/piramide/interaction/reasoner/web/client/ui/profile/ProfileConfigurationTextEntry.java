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

import piramide.interaction.reasoner.web.client.ui.IMainWindow;
import piramide.interaction.reasoner.web.client.ui.common.Resources;
import piramide.interaction.reasoner.web.client.ui.common.Styles;
import piramide.interaction.reasoner.web.client.ui.common.i18n.IPiramideMessages;
import piramide.interaction.reasoner.web.shared.model.CompilingConfiguration;
import piramide.interaction.reasoner.web.shared.model.ConfigurationException;
import piramide.interaction.reasoner.web.shared.model.ConfigurationLoader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ProfileConfigurationTextEntry extends Composite {
	
	private static final IPiramideMessages messages = GWT.create(IPiramideMessages.class);
	private static final Resources resources = GWT.create(Resources.class);
	
	@UiField
	TextArea profileConfigurationText;
	
	@UiField
	VerticalPanel mainPanel;
	
	private final IMainWindow mainWindow;

	private static ProfileConfigurationTextEntryUiBinder uiBinder = GWT
			.create(ProfileConfigurationTextEntryUiBinder.class);

	interface ProfileConfigurationTextEntryUiBinder extends
			UiBinder<Widget, ProfileConfigurationTextEntry> {
	}

	public ProfileConfigurationTextEntry(IMainWindow mainWindow) {
		
		this.mainWindow = mainWindow;
		
		initWidget(uiBinder.createAndBindUi(this));
		
		final CompilingConfiguration compilingConfiguration = this.mainWindow.getCompilingConfiguration();
		
		if(compilingConfiguration.isDefault())
			this.profileConfigurationText.setText(CompilingConfiguration.SAMPLE);
		else
			this.profileConfigurationText.setText(compilingConfiguration.toXml());
	}

	@UiHandler("loadButton")
	void onLoadButtonClicked(ClickEvent event){
		final CompilingConfiguration compilingConfig;
		try {
			compilingConfig = ConfigurationLoader.loadProfileConfiguration(this.profileConfigurationText.getText());
		} catch (ConfigurationException e) {
			this.mainWindow.showError(  messages.errorInConfigurationFile(e.getMessage()));
			return;
		}
		
		this.mainPanel.clear();
		
		this.mainWindow.setProfileConfiguration(compilingConfig);
		
		final Label correctLabel = new Label(messages.configurationSuccessfullyLoaded());
		correctLabel.setStyleName(Styles.important);
		
		final Image image = new Image(resources.tick());
		this.mainPanel.add(image);
		this.mainPanel.add(correctLabel);
	}
}
