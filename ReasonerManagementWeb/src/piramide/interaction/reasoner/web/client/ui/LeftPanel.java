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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class LeftPanel extends Composite {

	private static LeftPanelUiBinder uiBinder = GWT
			.create(LeftPanelUiBinder.class);

	interface LeftPanelUiBinder extends UiBinder<Widget, LeftPanel> {}
	
	private RightPanel rightPanel;
	
	private final Anchor [] anchors;
	
	@UiField
	Anchor manageUserProfileAnchor;
	
	@UiField
	Anchor enterLinguisticTermsAnchor;
	
	@UiField
	Anchor enterRulesAnchor;
	
	@UiField
	Anchor findDeviceAnchor;
	
	@UiField
	Anchor selectLocationAnchor;
	
	@UiField
	Anchor testProfileAnchor;
	
	@UiField
	Anchor loadFuzzyConfigurationAnchor;
	
	@UiField
	Anchor loadProfileConfigurationAnchor;
	
	@UiField
	Anchor showHelpAnchor;
	
	public LeftPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.anchors = new Anchor[]{
				this.loadFuzzyConfigurationAnchor,
				this.enterLinguisticTermsAnchor,
				this.enterRulesAnchor,
				
				this.loadProfileConfigurationAnchor,
				this.manageUserProfileAnchor,
				this.findDeviceAnchor,
				this.selectLocationAnchor,
				this.testProfileAnchor,
				
				this.showHelpAnchor
		};
	}
	
	public void enable(){
		for(Anchor anchor : this.anchors){
			anchor.setVisible(true);
			anchor.setEnabled(true);
		}
	}
	
	public void disable(){
		for(Anchor anchor : this.anchors){
			anchor.setVisible(false);
			anchor.setEnabled(false);
		}
	}
	
	public void setRightPanel(RightPanel rightPanel){
		this.rightPanel = rightPanel;
	}
	
	@UiHandler("manageUserProfileAnchor")
	void onManageUserProfileAnchor(ClickEvent event){
		this.rightPanel.loadManageUserProfilePanel();
	}
	
	@UiHandler("enterLinguisticTermsAnchor")
	void onEnterLinguisticTermsClicked(ClickEvent event){
		this.rightPanel.loadLinguisticTermsPanel();
	}

	@UiHandler("enterRulesAnchor")
	void onEnterRulesClicked(ClickEvent event){
		this.rightPanel.loadEnterRulesPanel();
	}
	
	@UiHandler("findDeviceAnchor")
	void onFindDeviceAnchorClicked(ClickEvent event){
		this.rightPanel.loadFindDevicePanel();
	}

	@UiHandler("selectLocationAnchor")
	void onSelectLocationAnchorClicked(ClickEvent event){
		this.rightPanel.loadSelectLocationPanel();
	}

	@UiHandler("testProfileAnchor")
	void onTestProfileAnchorClicked(ClickEvent event){
		this.rightPanel.loadTestProfilePanel();
	}

	@UiHandler("loadFuzzyConfigurationAnchor")
	void onLoadFuzzyConfigurationAnchorClicked(ClickEvent event){
		this.rightPanel.loadFuzzyConfigurationPanel();
	}
	
	@UiHandler("loadProfileConfigurationAnchor")
	void onLoadProfileConfigurationAnchorClicked(ClickEvent event){
		this.rightPanel.loadProfileConfigurationPanel();
	}
	
	@UiHandler("showHelpAnchor")
	void onShowHelpAnchorClicked(ClickEvent event){
		this.rightPanel.loadHelpPanel();
	}
}