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
package piramide.interaction.reasoner.wizard;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import piramide.interaction.reasoner.creator.FclCreator;
import piramide.interaction.reasoner.creator.WarningStore;
import piramide.interaction.reasoner.db.DeviceCapability;
import piramide.interaction.reasoner.db.DatabaseManager;
import piramide.interaction.reasoner.db.IDatabaseManager;
import piramide.interaction.reasoner.db.MobileDevices;
import piramide.interaction.reasoner.db.UserCapabilities.UserCapability;

public class FCreateFile extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel lRuleBlock = null;
	private JTextField tBlockName = null;
	private JButton bSave = null;
	private final JFileChooser fc = new JFileChooser();
	private final String rules;
	private final FclCreator creator = new FclCreator();
	private final Map<DeviceCapability, Variable> inputVariables;
	private final Map<String, Variable> outputVariables;

	/**
	 * This is the default constructor
	 */
	public FCreateFile (Map<DeviceCapability, Variable> inputVariables, Map<String, Variable> outputVariables, String rules) {
		super();
		initialize();
		this.rules = rules;
		this.outputVariables = outputVariables;
		this.inputVariables = inputVariables;
		
		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(422, 113);
		this.setContentPane(getJContentPane());
		this.setTitle("Create rule file");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (this.jContentPane == null) {
			this.lRuleBlock = new JLabel();
			this.lRuleBlock.setBounds(new Rectangle(15, 16, 128, 16));
			this.lRuleBlock.setText("Function block name:");
			this.jContentPane = new JPanel();
			this.jContentPane.setLayout(null);
			this.jContentPane.add(this.lRuleBlock, null);
			this.jContentPane.add(getTBlockName(), null);
			this.jContentPane.add(getBSave(), null);
		}
		return this.jContentPane;
	}

	/**
	 * This method initializes tBlockName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTBlockName() {
		if (this.tBlockName == null) {
			this.tBlockName = new JTextField();
			this.tBlockName.setBounds(new Rectangle(147, 13, 244, 20));
		}
		return this.tBlockName;
	}

	/**
	 * This method initializes bSave	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBSave() {
		if (this.bSave == null) {
			this.bSave = new JButton();
			this.bSave.setBounds(new Rectangle(168, 44, 89, 21));
			this.bSave.setText("Save file");
			this.bSave.addActionListener(this);
		}
		return this.bSave;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.bSave)){
			this.fc.setApproveButtonText("Save");
			this.fc.setDialogTitle("Save as...");
			this.fc.setFileFilter(new RuleFileFilter());
			int returnVal = this.fc.showOpenDialog(this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	final File file;
	        	final File selectedFile = this.fc.getSelectedFile();
	        	String extension = RuleFileFilter.getExtension(selectedFile);
	        	
	            if (extension == null || extension.equals("")){
	            	file = new File(selectedFile.getAbsolutePath() + ".fcl");	            	
	            } else{
	            	file = selectedFile;
	            }  
	            
	            if (!this.tBlockName.getText().equals("")){
	            	try {
						String fileText = this.createRuleFile();
						final PrintStream ps = new PrintStream(file);
						ps.print(fileText);
						ps.flush();
						ps.close();
						
						JOptionPane.showMessageDialog(this,
		    				    "Rule file created: " + file.getAbsolutePath(),
		    				    "File Created",
		    				    JOptionPane.PLAIN_MESSAGE);
						this.dispose();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(this,
		    				    "Could not create rule: " + e1.getMessage(),
		    				    "Error creating rule file",
		    				    JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
	            	
	            } else {
	            	JOptionPane.showMessageDialog(this,
	    				    "Please state a function block name",
	    				    "Error creating rule file",
	    				    JOptionPane.ERROR_MESSAGE);
	            }
	            
	            
	        } 

			
		}
		
	}
	
	private String createRuleFile() throws Exception{
		final IDatabaseManager db = new DatabaseManager();
		final MobileDevices mobileDevices = db.getResults();
		final WarningStore warningStore = new WarningStore();
		final String res = this.creator.createRuleFile(this.tBlockName.getText(), this.inputVariables, new HashMap<UserCapability, Variable>(), this.outputVariables, mobileDevices, this.rules, warningStore);
		warningStore.print();
		return res;	
	}

	

}  //  @jve:decl-index=0:visual-constraint="10,10"
