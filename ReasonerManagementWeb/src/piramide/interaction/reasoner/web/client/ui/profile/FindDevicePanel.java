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
import java.util.Map;

import piramide.interaction.reasoner.web.client.ReasonerManager;
import piramide.interaction.reasoner.web.client.ReasonerManagerAsync;
import piramide.interaction.reasoner.web.client.ui.IMainWindow;
import piramide.interaction.reasoner.web.client.ui.common.Styles;
import piramide.interaction.reasoner.web.client.ui.common.i18n.IPiramideMessages;
import piramide.interaction.reasoner.web.shared.DeviceNameSuggestion;
import piramide.interaction.reasoner.web.shared.model.CompilingConfiguration;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class FindDevicePanel extends Composite {

	private static FindDevicePanelUiBinder uiBinder = GWT.create(FindDevicePanelUiBinder.class);
	private static IPiramideMessages messages = GWT.create(IPiramideMessages.class);

	interface FindDevicePanelUiBinder extends UiBinder<Widget, FindDevicePanel> {}
	
	private final ReasonerManagerAsync reasonerManager = GWT.create(ReasonerManager.class);
	@UiField
	VerticalPanel suggestBoxPanel;
	
	@UiField
	Label displayNameLabel;

	@UiField
	Label totalTrendsLabel;
	
	@UiField
	Label decayedTrendsLabel;
	
	@UiField
	FlexTable table;
	
	@UiField
	VerticalPanel mobileDisplayPanel;

	private IMainWindow mainWindow;
	private static Map<String, DeviceNameSuggestion> pastSuggestions = new HashMap<String, DeviceNameSuggestion>();
	
	public FindDevicePanel(IMainWindow mainWindow) {
		
		this.mainWindow = mainWindow;
		
		initWidget(uiBinder.createAndBindUi(this));

		this.suggestBoxPanel.add(this.box);
		
		this.box.addSelectionHandler(this.suggestionHandler);
		final String deviceName = this.mainWindow.getCompilingConfiguration().getDeviceName();
		this.box.setText(deviceName);
		
		if(!deviceName.isEmpty()){
			if(pastSuggestions.containsKey(deviceName)){
				fillWithSuggestion(pastSuggestions.get(deviceName));
			}else{
				this.reasonerManager.retrieveDeviceID(deviceName, new AsyncCallback<DeviceNameSuggestion>() {
					
					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}
	
					@Override
					public void onSuccess(DeviceNameSuggestion result) {
						if(result != null){
							FindDevicePanel.pastSuggestions.put(deviceName, result);
							fillWithSuggestion(result);
						}
					}
				});
			}
		}
	}
	
	private final SuggestOracle oracle = new SuggestOracle() {
		
		@Override
		public boolean isDisplayStringHTML(){
			return true;
		}
		
		@Override
		public void requestSuggestions(final Request request, final Callback callback) {
			FindDevicePanel.this.reasonerManager.searchDeviceID(request, new AsyncCallback<Response>() {
				@Override
				public void onSuccess(Response result) {
					callback.onSuggestionsReady(request, result);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					System.err.println("Ha habido un error");
					caught.printStackTrace();
				}
			});
		}
	};

	private final SuggestBox box = new SuggestBox(this.oracle);
	
	private final SelectionHandler<Suggestion> suggestionHandler = new SelectionHandler<Suggestion>() {
		@Override
		public void onSelection(SelectionEvent<Suggestion> event) {
			final DeviceNameSuggestion suggestion = (DeviceNameSuggestion)event.getSelectedItem();
			fillWithSuggestion(suggestion);
		}
	};
	
	private void fillWithSuggestion(final DeviceNameSuggestion suggestion) {
		FindDevicePanel.this.mobileDisplayPanel.setVisible(true);
		
		FindDevicePanel.this.displayNameLabel.setText(suggestion.getReplacementString());
		FindDevicePanel.this.decayedTrendsLabel.setText(Double.toString(suggestion.getDecayedTrend()));
		FindDevicePanel.this.totalTrendsLabel.setText(Double.toString(suggestion.getTotalTrend()));
		
		FindDevicePanel.this.table.clear();
		final Label capabilityNameLabel = new Label(messages.capability());
		final Label capabilityValueLabel = new Label(messages.value());
		
		capabilityNameLabel.addStyleName(Styles.important);
		capabilityValueLabel.addStyleName(Styles.important);
		
		FindDevicePanel.this.table.setWidget(0, 0, capabilityNameLabel);
		FindDevicePanel.this.table.setWidget(0, 1, capabilityValueLabel);
		
		final Map<String, Number> capabilities = suggestion.getCapabilities();
		for(String capabilityName : capabilities.keySet()){
			final int newPosition = FindDevicePanel.this.table.getRowCount() + 1;
			FindDevicePanel.this.table.setWidget(newPosition, 0, new Label(capabilityName));
			FindDevicePanel.this.table.setWidget(newPosition, 1, new Label(capabilities.get(capabilityName).toString()));
		}
	}	

	@UiHandler("saveButton")
	void onSaveButtonClicked(ClickEvent event){
		final String deviceName = this.displayNameLabel.getText();
		final CompilingConfiguration config = this.mainWindow.getCompilingConfiguration();
		config.setDeviceName(deviceName);
	}
}
