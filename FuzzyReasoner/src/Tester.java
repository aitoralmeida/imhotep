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
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.jFuzzyLogic.FIS;

import org.apache.commons.io.FileUtils;
import org.jfree.chart.JFreeChart;

import piramide.interaction.reasoner.creator.FclCreator;
import piramide.interaction.reasoner.creator.WarningStore;
import piramide.interaction.reasoner.db.DatabaseManager;
import piramide.interaction.reasoner.db.DeviceCapability;
import piramide.interaction.reasoner.db.IDatabaseManager;
import piramide.interaction.reasoner.db.MobileDevice;
import piramide.interaction.reasoner.db.MobileDevices;
import piramide.interaction.reasoner.db.QueryInformation;
import piramide.interaction.reasoner.db.Trend;
import piramide.interaction.reasoner.db.UserCapabilities.UserCapability;
import piramide.interaction.reasoner.wizard.Variable;


public class Tester {
	
	public static void main(String[] args) throws Exception {
		
		final String filename = "fcl/generated.fcl";
		final String [] linguisticTerms = {"muuuuuuypeque", "muypeque", "peque", "normal", "grande", "muygrande", "muuuuygrande"};

		final boolean database = true;
		
		System.out.println("Creating dataset " + System.currentTimeMillis());
		
		final MobileDevices mobileDevices = createDataset(database);
		System.out.println(mobileDevices.getMobileDevices().size());
		
		final Map<DeviceCapability, Variable> inputVariables = new HashMap<DeviceCapability, Variable>();
		
		final Variable realSizeVar = new Variable("real_size", Arrays.asList(linguisticTerms));
		final Variable resoSizeVar = new Variable("reso_size", Arrays.asList(linguisticTerms));
		
		inputVariables.put(DeviceCapability.real_size, realSizeVar);
		inputVariables.put(DeviceCapability.reso_size, resoSizeVar);
		
		final Map<String, Variable> outputVariables = new HashMap<String, Variable>();
		outputVariables.put("hey", new Variable("hey", Arrays.asList("ho","lets","go")));
		final String rules = "// the rules \n";
		
		final FclCreator creator = new FclCreator();
		System.out.println("Creating rule file " + System.currentTimeMillis());
		
		final WarningStore warningStore = new WarningStore();
		final String fileContent = creator.createRuleFile("prueba", inputVariables, new HashMap<UserCapability, Variable>(), outputVariables, mobileDevices, rules, warningStore);
		warningStore.print();
		final File file = new File(filename);
		file.createNewFile();
		System.out.println("Dumping the rule file " + System.currentTimeMillis());
		FileUtils.writeStringToFile(file, fileContent);
		
		System.out.println("Processing the file " + System.currentTimeMillis());
        final FIS fis = FIS.load(filename,true);
        net.sourceforge.jFuzzyLogic.rule.Variable realSize = fis.getVariable("real_size");
        
        
        JFreeChart theChart = realSize.chart(false);
        BufferedImage img = theChart.createBufferedImage(1000, 1000);
        
        /*
        FileOutputStream fos = new FileOutputStream("imagen.png");
        ImageEncoder myEncoder = ImageEncoderFactory.newInstance("png");
        myEncoder.encode(img, fos);
        fos.flush();
        fos.close();
        */
        
        fis.chart();
		
	}

	private static MobileDevices createDataset(boolean database) throws Exception {
		if(database){
			final IDatabaseManager db = new DatabaseManager();
			return db.getResults();
		}
		final QueryInformation queryInfo = new QueryInformation();
		final Calendar now = Calendar.getInstance();
		queryInfo.setMaxYear(now.get(Calendar.YEAR));
		queryInfo.setMaxMonth(now.get(Calendar.MONTH));

		final MobileDevice device1 = createMobileDevice(queryInfo, 2.0, "device1", 0.0);
		final MobileDevice device2 = createMobileDevice(queryInfo, 1.0, "device2", 1.0);
		final MobileDevice device3 = createMobileDevice(queryInfo, 3.0, "device3", 2.0);
		final MobileDevice device4 = createMobileDevice(queryInfo, 2.0, "device4", 3.0);
		
		return new MobileDevices(Arrays.asList(device1, device2, device3, device4));	
	}

	private static MobileDevice createMobileDevice(QueryInformation queryInfo, double trend, String name, double realSize) {
		final List<Trend> trends = Arrays.asList(new Trend(2009, 12, trend));
		final Map<DeviceCapability, Number> capabilities = new HashMap<DeviceCapability, Number>();
		capabilities.put(DeviceCapability.real_size, new Double(realSize));
		MobileDevice device1 = new MobileDevice(name, capabilities, trends, queryInfo);
		return device1;
	}
}
