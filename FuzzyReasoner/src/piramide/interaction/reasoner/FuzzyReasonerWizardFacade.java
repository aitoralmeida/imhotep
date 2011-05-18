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
package piramide.interaction.reasoner;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.jFuzzyLogic.FIS;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.encoders.ImageEncoder;
import org.jfree.chart.encoders.ImageEncoderFactory;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;

import piramide.interaction.reasoner.creator.FclCreator;
import piramide.interaction.reasoner.creator.InvalidSyntaxException;
import piramide.interaction.reasoner.creator.WarningStore;
import piramide.interaction.reasoner.db.DatabaseException;
import piramide.interaction.reasoner.db.DeviceCapability;
import piramide.interaction.reasoner.db.IDatabaseManager;
import piramide.interaction.reasoner.db.MobileDevice;
import piramide.interaction.reasoner.db.MobileDevices;
import piramide.interaction.reasoner.db.UserCapabilities.UserCapability;
import piramide.interaction.reasoner.db.decay.DecayFunctionFactory.DecayFunctions;
import piramide.interaction.reasoner.wizard.Variable;

public class FuzzyReasonerWizardFacade implements IFuzzyReasonerWizardFacade {

	private final IDatabaseManager dbManager;
	private final FuzzyReasoner fuzzyReasoner;
	
	public FuzzyReasonerWizardFacade() throws FuzzyReasonerException {
		try{
			this.fuzzyReasoner = new FuzzyReasoner();
		}catch(DatabaseException e) {
			throw new FuzzyReasonerException("Error creating database: " + e.getMessage(), e);
		}
		
		this.dbManager = this.fuzzyReasoner.getDatabaseManager();
	}
	
	public void initializeCacheData(){
		try {
			this.dbManager.getResults();
		} catch (DatabaseException e) {}
	}

	@Override
	public MobileDevice retrieveDeviceID(String request)
			throws FuzzyReasonerException {
		return this.dbManager.retrieveDeviceNames(request);
	}	
	
	@Override
	public List<MobileDevice> searchDeviceNames(String query, int max) throws FuzzyReasonerException {
		return this.dbManager.searchDeviceNames(query, max);
	}

	@Override
	public void generateMembershipFunctionGraph(boolean isInput, boolean isDevices, String variableName, RegionDistributionInfo[] linguisticTerms, OutputStream destination, int width, int height, Geolocation geo, DecayFunctions decayFunction, Calendar when) {
		BufferedImage img;
		if(variableName == null){
			img = createErrorMessagesImage("Error generating graph: variableName not provided");
		}else if(linguisticTerms == null){
			img = createErrorMessagesImage("Error generating graph: linguisticTerms not provided");
		}else if(isInput && isDevices && !isValidDeviceVariableName(variableName)){
			img = createErrorMessagesImage("Error generating graph: invalid device variable name: " + variableName);
		}else if(isInput && !isDevices && !isValidUserVariableName(variableName)){
			img = createErrorMessagesImage("Error generating graph: invalid user variable name: " + variableName);
		}else{
			try {
				final WarningStore warningStore = new WarningStore();
				final net.sourceforge.jFuzzyLogic.rule.Variable variable = processVariable(
						isInput, isDevices, variableName, linguisticTerms, geo,
						decayFunction, when, warningStore);
				
				final JFreeChart theChart = variable.chart(false);
				
				final String [] messages = warningStore.getMessages();
				if(messages.length > 0){
					final Font font = TextTitle.DEFAULT_FONT;
					final Font bigBold = new Font(font.getName(), Font.BOLD, font.getSize() + 2);
					final Font bold = new Font(font.getName(), Font.BOLD, font.getSize());
					
				    theChart.addSubtitle(new TextTitle("WARNINGS:", bigBold, Color.RED, Title.DEFAULT_POSITION, Title.DEFAULT_HORIZONTAL_ALIGNMENT, Title.DEFAULT_VERTICAL_ALIGNMENT, Title.DEFAULT_PADDING));
				    for(String message : messages)
				    	theChart.addSubtitle(new TextTitle(message, bold, Color.RED, Title.DEFAULT_POSITION, Title.DEFAULT_HORIZONTAL_ALIGNMENT, Title.DEFAULT_VERTICAL_ALIGNMENT, Title.DEFAULT_PADDING));
				}
				img = theChart.createBufferedImage(width, height);
				
			} catch (FuzzyReasonerException e) {
				e.printStackTrace();
				img = createErrorMessagesImage("Error generating graph: " + e.getMessage()); 
			} 
		}
		
		try{
			final ImageEncoder myEncoder = ImageEncoderFactory.newInstance("png");
			myEncoder.encode(img, destination);
			destination.flush();
			destination.close();
		}catch(IOException e){
			// Cry
			e.printStackTrace();
			return;
		}
	}

	public net.sourceforge.jFuzzyLogic.rule.Variable processVariable(
			boolean isInput, boolean isDevices, String variableName,
			RegionDistributionInfo[] linguisticTerms, Geolocation geo,
			DecayFunctions decayFunction, Calendar when,
			final WarningStore warningStore) throws DatabaseException,
			InvalidSyntaxException {
		final Map<DeviceCapability, Variable> deviceInputVariables = new HashMap<DeviceCapability, Variable>();
		final Map<UserCapability, Variable> userInputVariables = new HashMap<UserCapability, Variable>();
		final Map<String, Variable> outputVariables = new HashMap<String, Variable>();
		final MobileDevices mobileDevices;
		if(isInput){
			if(isDevices){
				mobileDevices = this.dbManager.getResults(geo, decayFunction, when);
				
				final Variable var = new Variable(variableName, Arrays.asList(linguisticTerms));
				deviceInputVariables.put(DeviceCapability.valueOf(variableName), var);
				outputVariables.put("this", new Variable("is", Arrays.asList(new RegionDistributionInfo("nt",0.5),new RegionDistributionInfo("required",0.5))));
			}else{
				mobileDevices = new MobileDevices(new ArrayList<MobileDevice>());
				
				final Variable var = new Variable(variableName, Arrays.asList(linguisticTerms));
				userInputVariables.put(UserCapability.valueOf(variableName), var);
				
				outputVariables.put("this", new Variable("is", Arrays.asList(new RegionDistributionInfo("nt",0.5),new RegionDistributionInfo("required",0.5))));
			}
		}else{
			mobileDevices = new MobileDevices(new ArrayList<MobileDevice>());
			outputVariables.put(variableName, new Variable(variableName, Arrays.asList(linguisticTerms)));
		}
		
		
		final String rules = "// to generate the graph, no rule is required \n";
		
		final FclCreator creator = new FclCreator();
		
		final Set<RegionDistributionInfo> set = new HashSet<RegionDistributionInfo>(Arrays.asList(linguisticTerms));
		if(set.size() != linguisticTerms.length)
			warningStore.add("Repeated values provided!");
		
		final String fileContent = creator.createRuleFile("temporal", deviceInputVariables, userInputVariables, outputVariables, mobileDevices, rules, warningStore);
		
		final ByteArrayInputStream bais = new ByteArrayInputStream(fileContent.getBytes());
		
		final FIS fis = FIS.load(bais,true);
		
		final net.sourceforge.jFuzzyLogic.rule.Variable variable = fis.getVariable(variableName);
		return variable;
	}
	
    private boolean isValidDeviceVariableName(String variableName) {
    	try {
			DeviceCapability.valueOf(variableName);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

    private boolean isValidUserVariableName(String variableName) {
    	try {
			UserCapability.valueOf(variableName);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private BufferedImage createErrorMessagesImage(String text) {
		final Font font = TextTitle.DEFAULT_FONT;
		final Font bigBold = new Font(font.getName(), Font.BOLD, 24);
        final FontRenderContext frc = new FontRenderContext(null, true, false);
        final TextLayout layout = new TextLayout(text, bigBold, frc);
        final Rectangle2D bounds = layout.getBounds();
        final int w = (int) Math.ceil(bounds.getWidth());
        final int h = (int) Math.ceil(bounds.getHeight());
        final BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,w,h);
        g.setColor(Color.RED);
        g.setFont(bigBold);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.drawString(text, (float) - bounds.getX(), (float) - bounds.getY());
        g.dispose();
        return image;
    }

	@Override
	public String[] getGeolocationRegions() throws FuzzyReasonerException {
		return this.dbManager.getGeolocation();
	}

	@Override
	public FuzzyInferredResult getInferredValues(String deviceName, WarningStore warningStore, Map<String, Object> initialCapabilities, Map<String, RegionDistributionInfo[]> inputVariables, Geolocation geo, DecayFunctions decayFunction, Calendar when, Map<String, RegionDistributionInfo[]> outputVariables, String rules)
			throws FuzzyReasonerException {
		
		final FIS fis = this.fuzzyReasoner.generateFISobject(deviceName, warningStore, initialCapabilities, inputVariables, geo, decayFunction, when, outputVariables, rules);
		
		final HashMap<String, LinkedHashMap<String, Double>> results = new HashMap<String, LinkedHashMap<String,Double>>();
		
		for(String variableName : inputVariables.keySet()){
			results.put(variableName, new LinkedHashMap<String, Double>());
			
			for(RegionDistributionInfo linguisticTerm : inputVariables.get(variableName)){
				final double currentValue = fis.getVariable(variableName).getMembership(linguisticTerm.getName());
				results.get(variableName).put(linguisticTerm.getName(), Double.valueOf(currentValue));
			}
		}
		
		for(String variableName : outputVariables.keySet()){
			results.put(variableName, new LinkedHashMap<String, Double>());
			
			for(RegionDistributionInfo linguisticTerm : outputVariables.get(variableName)){
				final double currentValue = fis.getVariable(variableName).getMembership(linguisticTerm.getName());
				results.get(variableName).put(linguisticTerm.getName(), Double.valueOf(currentValue));
			}
		}
		
		final HashMap<String, Double> defuzzifiedValues = new HashMap<String, Double>();
		for(String outputVariable : outputVariables.keySet()){
			final double value = fis.getVariable(outputVariable).defuzzify();
			defuzzifiedValues.put(outputVariable, Double.valueOf(value));
		}
		
		final FuzzyInferredResult result = new FuzzyInferredResult(results, defuzzifiedValues);
		return result;
	}
}
