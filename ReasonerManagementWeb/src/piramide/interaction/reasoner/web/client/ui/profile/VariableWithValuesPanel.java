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

import piramide.interaction.reasoner.web.client.ui.common.Styles;
import piramide.interaction.reasoner.web.shared.ImageUrlBuilder;
import piramide.interaction.reasoner.web.shared.model.Geolocation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class VariableWithValuesPanel extends Composite {

	private static VariableWithValuesPanelUiBinder uiBinder = GWT
			.create(VariableWithValuesPanelUiBinder.class);
	
	interface VariableWithValuesPanelUiBinder extends
			UiBinder<Widget, VariableWithValuesPanel> {
	}
	
	@UiField
	Label variableNameLabel;
	
	@UiField
	VerticalPanel termsPanel;
	
	@UiField
	Label currentValueLabel;
	
	@UiField
	Image variableImage;

	public VariableWithValuesPanel(String variableName, Geolocation geo, boolean devices, boolean input, Map<String, Double> term2value, double variableValue) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.currentValueLabel.setText(Double.valueOf(variableValue).toString());
		this.variableNameLabel.setText(variableName);
		
		double maxValue = 0.0;
		for(String trend : term2value.keySet())
			if(maxValue < term2value.get(trend).doubleValue())
				maxValue = term2value.get(trend).doubleValue();
		
		final ImageUrlBuilder builder = new ImageUrlBuilder(variableName, geo.getGeo(), devices, input, term2value.keySet().toArray(new String[]{}));
		this.variableImage.setUrl(builder.toString());
		
		for(String trend : term2value.keySet()){
			
			final HorizontalPanel row = new HorizontalPanel();
			row.setSpacing(2);
			final Label linguisticTermName = new Label(trend + ": ");
			final Double value = term2value.get(trend);
			final Label linguisticTermValue = new Label(value.toString());
			
			if(value.doubleValue() == maxValue){
				linguisticTermName.setStyleName(Styles.important);
				linguisticTermValue.setStyleName(Styles.important);
			}
			
			row.add(linguisticTermName);
			row.add(linguisticTermValue);
			
			this.termsPanel.add(row);
		}
	}
}
