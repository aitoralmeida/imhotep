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
package piramide.interaction.reasoner.web.client.ui.common.i18n;

import com.google.gwt.i18n.client.Messages;

public interface IPiramideMessages extends Messages{
	public String fuzzyConfiguration();
	public String profileConfiguration();
	public String findDeviceID();
	public String updateDatabase();
	public String uploadFile();
	public String enterLinguisticTerms();
	public String enterRules();
	public String manageUserProfile();
	public String displayName();
	public String totalTrends();
	public String decayedTrends();
	public String typeMobileDevice();
	public String pasteConfigurationFile();
	public String loadConfiguration();
	public String load();
	public String configurationSuccessfullyLoaded();
	public String capability();
	public String value();
	public String errorInConfigurationFile(String message);
	public String name();
	public String type();
	public String edit();
	public String delete();
	public String validated();
	public String notValidated();
	public String input();
	public String output();
	public String variables();
	public String save();
	public String cancel();
	public String device();
	public String user();
	public String view();
	public String linguisticTerms();
	public String add();
	public String remove();
	public String addLinguisticTerm();
	public String userOrDevice();
	public String inputVariables();
	public String outputVariables();
	public String variableTerms();
	public String noInputVariableDefined();
	public String noOutputVariableDefined();
	public String newRule();
	public String endRule();
	public String loading();
	public String testProfile();
	public String selectLocation();
	public String loadingResults();
	public String warnings();
	public String pleaseWaitWhileApplicationIsLoading();
	public String welcomeToImhotep();
	public String help();
	public String step1();
	public String step2();
	public String step3();
}
