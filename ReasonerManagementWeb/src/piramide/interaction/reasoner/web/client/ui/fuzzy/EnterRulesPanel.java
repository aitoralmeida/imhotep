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

import java.util.HashMap;
import java.util.Map;

import piramide.interaction.reasoner.web.client.ui.IMainWindow;
import piramide.interaction.reasoner.web.shared.model.FuzzyConfiguration;
import piramide.interaction.reasoner.web.shared.model.Variable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class EnterRulesPanel extends Composite {

	private static EnterRulesPanelUiBinder uiBinder = GWT.create(EnterRulesPanelUiBinder.class);

	interface EnterRulesPanelUiBinder extends
			UiBinder<Widget, EnterRulesPanel> {}

	@UiField
	Label noOutputVariableDefinedLabel;
	
	@UiField
	Label noInputVariableDefinedLabel;
	
	@UiField
	VerticalPanel outputVariablePanel;
	
	@UiField
	VerticalPanel inputVariablePanel;
	
	@UiField
	ListBox outputVariablesListbox;
	
	@UiField
	ListBox inputVariablesListbox;
	
	@UiField
	ListBox outputVariableTermsListbox;
	
	@UiField
	ListBox inputVariableTermsListbox;

	@UiField
	TextArea rulesTextArea;
	
	@UiField
	Button newRuleButton;
	
	@UiField
	Button andButton;
	
	@UiField
	Button orButton;
	
	@UiField
	Button thenButton;
	
	@UiField
	Button endRuleButton;
	
	@UiField
	Button outputVariableAdd;
	
	@UiField
	Button inputVariableAdd;
	
	@UiField
	Button saveButton;
	
	private final IMainWindow mainWindow;
	
	private final Map<String, String []> inputVariables = new HashMap<String, String []>();
	private final Map<String, String []> outputVariables = new HashMap<String, String []>();
	
	private final boolean enabled;
	
	public EnterRulesPanel(IMainWindow mainWindow) {
		
		this.mainWindow = mainWindow;
		
		initWidget(uiBinder.createAndBindUi(this));
		
		final FuzzyConfiguration config = this.mainWindow.getFuzzyConfiguration();
		
		for(Variable var : config.getInput().getDeviceVariables())
			this.inputVariables.put(var.getName(), var.getTerms());
		
		for(Variable var : config.getInput().getUserVariables())
			this.inputVariables.put(var.getName(), var.getTerms());
		
		for(Variable var : config.getOutput().getVariables())
			this.outputVariables.put(var.getName(), var.getTerms());

		this.rulesTextArea.setText(config.getRules());

		this.enabled = this.inputVariables.size() != 0 && this.outputVariables.size() != 0;
		
		if(this.inputVariables.size() == 0){
			this.noInputVariableDefinedLabel.setVisible(true);
			this.inputVariablePanel.setVisible(false);
		}else{
			this.noInputVariableDefinedLabel.setVisible(false);
			this.inputVariablePanel.setVisible(true);
			
			for(String input : this.inputVariables.keySet())
				this.inputVariablesListbox.addItem(input);
			
			final String firstVariable = this.inputVariables.keySet().iterator().next();
			for(String term : this.inputVariables.get(firstVariable))
				this.inputVariableTermsListbox.addItem(term);
		}
		
		if(this.outputVariables.size() == 0){
			this.noOutputVariableDefinedLabel.setVisible(true);
			this.outputVariablePanel.setVisible(false);
		}else{
			this.noOutputVariableDefinedLabel.setVisible(false);
			this.outputVariablePanel.setVisible(true);
			
			for(String output : this.outputVariables.keySet())
				this.outputVariablesListbox.addItem(output);
			
			final String firstVariable = this.outputVariables.keySet().iterator().next();
			for(String term : this.outputVariables.get(firstVariable))
				this.outputVariableTermsListbox.addItem(term);
		}
		
		this.inputVariableAdd.setEnabled(this.enabled);
		this.outputVariableAdd.setEnabled(this.enabled);
		this.newRuleButton.setEnabled(this.enabled);
		this.andButton.setEnabled(this.enabled);
		this.orButton.setEnabled(this.enabled);
		this.thenButton.setEnabled(this.enabled);
		this.endRuleButton.setEnabled(this.enabled);
		this.rulesTextArea.setEnabled(this.enabled);
		this.saveButton.setEnabled(this.enabled);
		
		this.rulesTextArea.setCursorPos(this.rulesTextArea.getText().length());
	}
	
	@UiHandler("inputVariableAdd")
	void onInputVariableAddClicked(ClickEvent event){
		final String variable = getSelectedInputVariable();
		final String term     = getSelectedInputTerm();
		insertText(" " + variable + " IS " + term);
	}
	
	@UiHandler("outputVariableAdd")
	void onOutputVariableAddClicked(ClickEvent event){
		final String variable = getSelectedOutputVariable();
		final String term     = getSelectedOutputTerm();
		insertText(" " + variable + " IS " + term);
	}
	
	@UiHandler("andButton")
	void onAndButtonClicked(ClickEvent event){
		insertText(" AND");
	}
	
	@UiHandler("orButton")
	void onOrButtonClicked(ClickEvent event){
		insertText(" OR");
	}
	
	@UiHandler("thenButton")
	void onThenButtonClicked(ClickEvent event){
		insertText(" THEN");
	}
	
	@UiHandler("endRuleButton")
	void onEndRuleButtonClicked(ClickEvent event){
		insertText(";\n");
	}
	
	@UiHandler("newRuleButton")
	void onNewRuleButtonClicked(ClickEvent event){
		final String [] tokens = this.rulesTextArea.getText().split(" ");
		int maxValue = 0;
		for(String token : tokens){
			final String trimmedToken = token.trim();
			if(!trimmedToken.startsWith("RULE"))
				continue;
			
			final String ruleName;
			if(trimmedToken.indexOf(":") >= 0){
				ruleName = trimmedToken.substring(0, trimmedToken.indexOf(":"));
			}else
				ruleName = trimmedToken;
			
			final String restOfToken = ruleName.substring("RULE".length());
			int value;
			try {
				value = Integer.parseInt(restOfToken);
			} catch (NumberFormatException e) {
				continue;
			}
			if(value > maxValue)
				maxValue = value;
		}
		insertText("\nRULE RULE" + (maxValue + 1) + ": IF");
	}
	
	@UiHandler("saveButton")
	void onSaveButtonClicked(ClickEvent event){
		final String rules = this.rulesTextArea.getText();
		final FuzzyConfiguration config = this.mainWindow.getFuzzyConfiguration();
		config.setRules(rules);
	}
	
	private void insertText(String text){
		final String currentText = this.rulesTextArea.getText();
		final int pos = this.rulesTextArea.getCursorPos();
		
		final StringBuffer buffer = new StringBuffer(currentText);
		buffer.insert(pos, text);
	
		this.rulesTextArea.setText(buffer.toString());
		this.rulesTextArea.setCursorPos(pos + text.length());
	}
	
	@UiHandler("inputVariablesListbox")
	void onInputVariablesChanged(ChangeEvent event){
		final String selectedVariable = getSelectedInputVariable();
		final String [] terms = this.inputVariables.get(selectedVariable);
		this.inputVariableTermsListbox.clear();
		for(String term : terms)
			this.inputVariableTermsListbox.addItem(term);
	}

	@UiHandler("outputVariablesListbox")
	void onOutputVariablesChanged(ChangeEvent event){
		final String selectedVariable = getSelectedOutputVariable();
		final String [] terms = this.outputVariables.get(selectedVariable);
		this.outputVariableTermsListbox.clear();
		for(String term : terms)
			this.outputVariableTermsListbox.addItem(term);		
	}

	private String getSelectedInputVariable() {
		return this.inputVariablesListbox.getItemText(this.inputVariablesListbox.getSelectedIndex());
	}
	
	private String getSelectedInputTerm() {
		return this.inputVariableTermsListbox.getItemText(this.inputVariableTermsListbox.getSelectedIndex());
	}
	
	private String getSelectedOutputVariable() {
		return this.outputVariablesListbox.getItemText(this.outputVariablesListbox.getSelectedIndex());
	}
	
	private String getSelectedOutputTerm() {
		return this.outputVariableTermsListbox.getItemText(this.outputVariableTermsListbox.getSelectedIndex());
	}
	
}
