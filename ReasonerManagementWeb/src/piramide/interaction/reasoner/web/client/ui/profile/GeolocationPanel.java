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
import piramide.interaction.reasoner.web.shared.model.CompilingConfiguration;
import piramide.interaction.reasoner.web.shared.model.Geolocation;
import piramide.interaction.reasoner.web.shared.model.GeolocationRegions;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class GeolocationPanel extends Composite {

	private static GeolocationPanelUiBinder uiBinder = GWT.create(GeolocationPanelUiBinder.class);

	interface GeolocationPanelUiBinder extends
			UiBinder<Widget, GeolocationPanel> {
	}
	
	private final IMainWindow mainWindow;
	
	@UiField
	ListBox geoListBox;

	public GeolocationPanel(IMainWindow mainWindow) {
		this.mainWindow = mainWindow;
		initWidget(uiBinder.createAndBindUi(this));
		
		for(String item : GeolocationRegions.values())
			this.geoListBox.addItem(item);
		
		final CompilingConfiguration config = this.mainWindow.getCompilingConfiguration();
		final String geoText = config.getGeolocation().getGeo();
		for(int i = 0; i < this.geoListBox.getItemCount(); ++i){
			final String text = this.geoListBox.getItemText(i);
			if(text.equals(geoText)){
				this.geoListBox.setSelectedIndex(i);
				break;
			}
		}
	}

	@UiHandler("saveButton")
	void onSaveButtonClicked(ClickEvent event){
		final int geoIndex = this.geoListBox.getSelectedIndex();
		final String text = this.geoListBox.getItemText(geoIndex);
		final CompilingConfiguration config = this.mainWindow.getCompilingConfiguration();
		config.setGeolocation(new Geolocation(text));
	}
	
}
