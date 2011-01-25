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
import piramide.interaction.reasoner.web.client.ui.common.Resources;
import piramide.interaction.reasoner.web.client.ui.common.Styles;
import piramide.interaction.reasoner.web.client.ui.common.i18n.IPiramideMessages;
import piramide.interaction.reasoner.web.shared.model.ConfigurationException;
import piramide.interaction.reasoner.web.shared.model.ConfigurationLoader;
import piramide.interaction.reasoner.web.shared.model.FuzzyConfiguration;

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

public class FuzzyConfigurationTextEntry extends Composite {
	
	private static final IPiramideMessages messages = GWT.create(IPiramideMessages.class);
	private static final Resources resources = GWT.create(Resources.class);
	
	@UiField
	TextArea fuzzyConfigurationText;
	
	@UiField
	VerticalPanel mainPanel;
	
	private final IMainWindow mainWindow;

	private static FuzzyConfigurationTextEntryUiBinder uiBinder = GWT
			.create(FuzzyConfigurationTextEntryUiBinder.class);

	interface FuzzyConfigurationTextEntryUiBinder extends
			UiBinder<Widget, FuzzyConfigurationTextEntry> {
	}

	public FuzzyConfigurationTextEntry(IMainWindow mainWindow) {
		
		this.mainWindow = mainWindow;
		
		initWidget(uiBinder.createAndBindUi(this));
		
		final FuzzyConfiguration fuzzyConfiguration = this.mainWindow.getFuzzyConfiguration();
		
		if(fuzzyConfiguration.isDefault())
			this.fuzzyConfigurationText.setText(FuzzyConfiguration.SAMPLE);
		else
			this.fuzzyConfigurationText.setText(fuzzyConfiguration.toXml());
	}

	@UiHandler("loadButton")
	void onLoadButtonClicked(ClickEvent event){
		final FuzzyConfiguration fuzzyConfig;
		try {
			fuzzyConfig = ConfigurationLoader.loadFuzzyConfiguration(this.fuzzyConfigurationText.getText());
		} catch (ConfigurationException e) {
			this.mainWindow.showError(  messages.errorInConfigurationFile(e.getMessage()));
			return;
		}
		
		this.mainPanel.clear();
		
		this.mainWindow.setFuzzyConfiguration(fuzzyConfig);
		
		final Label correctLabel = new Label(messages.configurationSuccessfullyLoaded());
		correctLabel.setStyleName(Styles.important);
		
		final Image image = new Image(resources.tick());
		this.mainPanel.add(image);
		this.mainPanel.add(correctLabel);
	}
}
