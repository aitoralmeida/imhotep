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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class LowerPanel extends Composite {

	private static LowerPanelUiBinder uiBinder = GWT
			.create(LowerPanelUiBinder.class);

	interface LowerPanelUiBinder extends UiBinder<Widget, LowerPanel> {
	}

	@UiField
	LeftPanel leftPanel;
	
	@UiField
	RightPanel rightPanel;
	
	public LowerPanel(IMainWindow mainWindow) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.rightPanel.setMainWindow(mainWindow);
		this.leftPanel.setRightPanel(this.rightPanel);
	}
	
	public void enable(){
		this.rightPanel.loadWelcomePanel();
		this.leftPanel.enable();
	}
	
	public void disable(){
		this.rightPanel.loadWaitingPanel();
		this.leftPanel.disable();
	}
}
