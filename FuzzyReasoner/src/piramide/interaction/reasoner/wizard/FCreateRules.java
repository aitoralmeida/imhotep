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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import piramide.interaction.reasoner.RegionDistributionInfo;

public class FCreateRules extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private HashMap<String, Variable> inputVariables;  
	private HashMap<String, Variable> outputVariables;  
	private JPanel jContentPane = null;
	private JLabel lInput = null;
	private JComboBox cbInputVariables = null;
	private JLabel lInput1 = null;
	private JComboBox cbOutputVariables = null;
	private JLabel lInput2 = null;
	private JComboBox cbInputTerms = null;
	private JLabel lInput21 = null;
	private JComboBox cbOutputTerms = null;
	private JButton bAddInputVariable = null;
	private JButton bAddOutputVariable = null;
	private JTextArea taRules = null;
	private JButton bAddInputTerm = null;
	private JButton bAddOutputTerm = null;
	private JButton bThen = null;
	private JButton bAnd = null;
	private JButton bOr = null;
	private JButton bNewRule = null;
	private JButton bEndRule = null;
	private JButton bSafeFile = null;
	/**
	 * This is the default constructor
	 */
	public FCreateRules(HashMap<String, Variable> inputVariables, HashMap<String, Variable> outputVariables) {
		super();
		this.inputVariables = inputVariables;
		this.outputVariables = outputVariables;
		initialize();
		for(String varName : inputVariables.keySet()){
			this.cbInputVariables.addItem(varName);
		}
		
		for(String varName : outputVariables.keySet()){
			this.cbOutputVariables.addItem(varName);
		}
		
		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(804, 412);
		this.setContentPane(getJContentPane());
		this.setTitle("Create the rules");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (this.jContentPane == null) {
			this.lInput21 = new JLabel();
			this.lInput21.setText("Variable terms:");
			this.lInput21.setLocation(new Point(343, 45));
			this.lInput21.setSize(new Dimension(87, 16));
			this.lInput2 = new JLabel();
			this.lInput2.setText("Variable terms:");
			this.lInput2.setLocation(new Point(19, 49));
			this.lInput2.setSize(new Dimension(90, 16));
			this.lInput1 = new JLabel();
			this.lInput1.setBounds(new Rectangle(343, 16, 101, 16));
			this.lInput1.setText("Output Variables:");
			this.lInput = new JLabel();
			this.lInput.setBounds(new Rectangle(19, 16, 96, 16));
			this.lInput.setText("Input Variables:");
			this.jContentPane = new JPanel();
			this.jContentPane.setLayout(null);
			this.jContentPane.add(this.lInput, null);
			this.jContentPane.add(getCbInputVariables(), null);
			this.jContentPane.add(this.lInput1, null);
			this.jContentPane.add(getCbOutputVariables(), null);
			this.jContentPane.add(this.lInput2, null);
			this.jContentPane.add(getCbInputTerms(), null);
			this.jContentPane.add(this.lInput21, null);
			this.jContentPane.add(getCbOutputTerms(), null);
			this.jContentPane.add(getBAddInputVariable(), null);
			this.jContentPane.add(getBAddOutputVariable(), null);
			this.jContentPane.add(getTaRules(), null);
			this.jContentPane.add(getBAddInputTerm(), null);
			this.jContentPane.add(getBAddOutputTerm(), null);
			this.jContentPane.add(getBThen(), null);
			this.jContentPane.add(getBAnd(), null);
			this.jContentPane.add(getBOr(), null);
			this.jContentPane.add(getBNewRule(), null);
			this.jContentPane.add(getBEndRule(), null);
			this.jContentPane.add(getBSafeFile(), null);
		}
		return this.jContentPane;
	}

	/**
	 * This method initializes cbInputVariables	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbInputVariables() {
		if (this.cbInputVariables == null) {
			this.cbInputVariables = new JComboBox();
			this.cbInputVariables.setBounds(new Rectangle(118, 14, 150, 22));
			this.cbInputVariables.addActionListener(this);
		}
		return this.cbInputVariables;
	}

	/**
	 * This method initializes cbOutputVariables	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbOutputVariables() {
		if (this.cbOutputVariables == null) {
			this.cbOutputVariables = new JComboBox();
			this.cbOutputVariables.setLocation(new Point(463, 14));
			this.cbOutputVariables.setSize(new Dimension(150, 22));
			this.cbOutputVariables.addActionListener(this);
		}
		return this.cbOutputVariables;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.cbInputVariables)){
			this.cbInputTerms.removeAllItems();
			String selectedVarName = (String)this.cbInputVariables.getSelectedItem();
			List<RegionDistributionInfo> terms = this.inputVariables.get(selectedVarName).getTerms();
			for(RegionDistributionInfo term : terms){
				this.cbInputTerms.addItem(term.getName());
			}		
		} else if (e.getSource().equals(this.cbOutputVariables)){
			this.cbOutputTerms.removeAllItems();
			String selectedVarName = (String)this.cbOutputVariables.getSelectedItem();
			List<RegionDistributionInfo> terms = this.outputVariables.get(selectedVarName).getTerms();
			for(RegionDistributionInfo term : terms){
				this.cbOutputTerms.addItem(term.getName());
			}		
		} else if (e.getSource().equals(this.bAddInputVariable)){
			this.taRules.setText(this.taRules.getText() + " " + (String)this.cbInputVariables.getSelectedItem() + " IS");
		} else if (e.getSource().equals(this.bAddOutputVariable)){
			this.taRules.setText(this.taRules.getText() + " " + (String)this.cbOutputVariables.getSelectedItem() + " IS");
		} else if (e.getSource().equals(this.bAddInputTerm)){
			this.taRules.setText(this.taRules.getText() + " " + (String)this.cbInputTerms.getSelectedItem());
		} else if (e.getSource().equals(this.bAddOutputTerm)){
			this.taRules.setText(this.taRules.getText() + " " + (String)this.cbOutputTerms.getSelectedItem());
		} else if (e.getSource().equals(this.bThen)){
			this.taRules.setText(this.taRules.getText() + " THEN");
		} else if (e.getSource().equals(this.bAnd)){
			this.taRules.setText(this.taRules.getText() + " AND");
		} else if (e.getSource().equals(this.bOr)){
			this.taRules.setText(this.taRules.getText() + " OR");
		} else if (e.getSource().equals(this.bNewRule)){
			int ruleNum = getRuleNum (this.taRules.getText());
			this.taRules.setText(this.taRules.getText() + "RULE " + ruleNum + " : IF");
		} else if (e.getSource().equals(this.bEndRule)){
			this.taRules.setText(this.taRules.getText() + ";\n");
		} else if (e.getSource().equals(this.bSafeFile)){
				
		}
		
	}
	
	private int getRuleNum(String text){
		int num = 0;
		if (text.contains("RULE ")){
			String[] tokens = text.split("RULE ");
			String lastRule = tokens[tokens.length-1].substring(0, tokens[tokens.length-1].indexOf(":"));
			num = Integer.parseInt(lastRule.trim()) + 1;
		} 
		return num;
	}

	/**
	 * This method initializes cbInputTerms	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbInputTerms() {
		if (this.cbInputTerms == null) {
			this.cbInputTerms = new JComboBox();
			this.cbInputTerms.setLocation(new Point(118, 45));
			this.cbInputTerms.setSize(new Dimension(150, 22));
		}
		return this.cbInputTerms;
	}

	/**
	 * This method initializes cbOutputTerms	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbOutputTerms() {
		if (this.cbOutputTerms == null) {
			this.cbOutputTerms = new JComboBox();
			this.cbOutputTerms.setLocation(new Point(463, 45));
			this.cbOutputTerms.setSize(new Dimension(150, 22));
		}
		return this.cbOutputTerms;
	}

	/**
	 * This method initializes bAddInputVariable	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBAddInputVariable() {
		if (this.bAddInputVariable == null) {
			this.bAddInputVariable = new JButton();
			this.bAddInputVariable.setMnemonic(KeyEvent.VK_UNDEFINED);
			this.bAddInputVariable.setSize(new Dimension(41, 22));
			this.bAddInputVariable.setLocation(new Point(276, 15));
			this.bAddInputVariable.setText("+");
			this.bAddInputVariable.addActionListener(this);
		}
		return this.bAddInputVariable;
	}

	/**
	 * This method initializes bAddOutputVariable	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBAddOutputVariable() {
		if (this.bAddOutputVariable == null) {
			this.bAddOutputVariable = new JButton();
			this.bAddOutputVariable.setText("+");
			this.bAddOutputVariable.setLocation(new Point(621, 14));
			this.bAddOutputVariable.setSize(new Dimension(41, 22));
			this.bAddOutputVariable.setMnemonic(KeyEvent.VK_UNDEFINED);
			this.bAddOutputVariable.addActionListener(this);
		}
		return this.bAddOutputVariable;
	}

	/**
	 * This method initializes taRules	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTaRules() {
		if (this.taRules == null) {
			this.taRules = new JTextArea();
			this.taRules.setBounds(new Rectangle(8, 122, 772, 237));
		}
		return this.taRules;
	}

	/**
	 * This method initializes bAddInputTerm	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBAddInputTerm() {
		if (this.bAddInputTerm == null) {
			this.bAddInputTerm = new JButton();
			this.bAddInputTerm.setText("+");
			this.bAddInputTerm.setLocation(new Point(276, 45));
			this.bAddInputTerm.setSize(new Dimension(41, 22));
			this.bAddInputTerm.setMnemonic(KeyEvent.VK_UNDEFINED);
			this.bAddInputTerm.addActionListener(this);
		}
		return this.bAddInputTerm;
	}

	/**
	 * This method initializes bAddOutputTerm	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBAddOutputTerm() {
		if (this.bAddOutputTerm == null) {
			this.bAddOutputTerm = new JButton();
			this.bAddOutputTerm.setText("+");
			this.bAddOutputTerm.setLocation(new Point(621, 45));
			this.bAddOutputTerm.setSize(new Dimension(41, 22));
			this.bAddOutputTerm.setMnemonic(KeyEvent.VK_UNDEFINED);
			this.bAddOutputTerm.addActionListener(this);
		}
		return this.bAddOutputTerm;
	}

	/**
	 * This method initializes bThen	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBThen() {
		if (this.bThen == null) {
			this.bThen = new JButton();
			this.bThen.setText("THEN");
			this.bThen.setLocation(new Point(17, 83));
			this.bThen.setSize(new Dimension(65, 22));
			this.bThen.setMnemonic(KeyEvent.VK_UNDEFINED);
			this.bThen.addActionListener(this);
		}
		return this.bThen;
	}

	/**
	 * This method initializes bAnd	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBAnd() {
		if (this.bAnd == null) {
			this.bAnd = new JButton();
			this.bAnd.setText("AND");
			this.bAnd.setLocation(new Point(91, 83));
			this.bAnd.setSize(new Dimension(63, 22));
			this.bAnd.setMnemonic(KeyEvent.VK_UNDEFINED);
			this.bAnd.addActionListener(this);
		}
		return this.bAnd;
	}

	/**
	 * This method initializes bOr	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBOr() {
		if (this.bOr == null) {
			this.bOr = new JButton();
			this.bOr.setText("OR");
			this.bOr.setLocation(new Point(163, 83));
			this.bOr.setSize(new Dimension(54, 22));
			this.bOr.setMnemonic(KeyEvent.VK_UNDEFINED);
			this.bOr.addActionListener(this);
		}
		return this.bOr;
	}

	/**
	 * This method initializes bNewRule	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBNewRule() {
		if (this.bNewRule == null) {
			this.bNewRule = new JButton();
			this.bNewRule.setText("New rule");
			this.bNewRule.setPreferredSize(new Dimension(84, 22));
			this.bNewRule.setSize(new Dimension(84, 22));
			this.bNewRule.setLocation(new Point(224, 83));
			this.bNewRule.setMnemonic(KeyEvent.VK_UNDEFINED);
			this.bNewRule.addActionListener(this);
		}
		return this.bNewRule;
	}

	/**
	 * This method initializes bEndRule	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBEndRule() {
		if (this.bEndRule == null) {
			this.bEndRule = new JButton();
			this.bEndRule.setBounds(new Rectangle(313, 83, 84, 22));
			this.bEndRule.setMnemonic(KeyEvent.VK_UNDEFINED);
			this.bEndRule.setText("End rule");
			this.bEndRule.setPreferredSize(new Dimension(84, 22));
			this.bEndRule.addActionListener(this);
		}
		return this.bEndRule;
	}

	/**
	 * This method initializes bSafeFile	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBSafeFile() {
		if (this.bSafeFile == null) {
			this.bSafeFile = new JButton();
			this.bSafeFile.setText("Save File");
			this.bSafeFile.setLocation(new Point(403, 83));
			this.bSafeFile.setSize(new Dimension(84, 22));
			this.bSafeFile.setMnemonic(KeyEvent.VK_UNDEFINED);
			this.bSafeFile.addActionListener(this);
		}
		return this.bSafeFile;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
