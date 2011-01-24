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
package piramide.interaction.reasoner.wizard.old;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JTextField;

import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.Rectangle;
import javax.swing.JList;
import javax.swing.JButton;

import piramide.interaction.reasoner.wizard.Variable;

import java.awt.Point;
import java.util.Vector;

public class FCreateNewVariable extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L; //  @jve:decl-index=0:
	private JPanel jContentPane = null;
	private JLabel jLabel = null;
	private JTextField tfVarName = null;
	private JList lLinguisticTerms = null;
	private JButton bAddTerm = null;
	private JButton bDeleteTerm = null;
	private JButton bAddVariable = null;
	private final DefaultListModel terms = new DefaultListModel();
	private final IFCreateVariable parent;
	/**
	 * This is the default constructor
	 */
	public FCreateNewVariable(IFCreateVariable parent) {
		super();
		initialize();
		this.parent = parent;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(397, 312);
		this.setContentPane(getJContentPane());
		this.setTitle("Create new variable");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (this.jContentPane == null) {
			this.jLabel = new JLabel();
			this.jLabel.setBounds(new Rectangle(14, 16, 87, 16));
			this.jLabel.setName("lVarNAme");
			this.jLabel.setText("Variable Name");
			this.jContentPane = new JPanel();
			this.jContentPane.setLayout(null);
			this.jContentPane.add(this.jLabel, null);
			this.jContentPane.add(getTfVarName(), null);
			this.jContentPane.add(getLLinguisticTerms(), null);
			this.jContentPane.add(getBAddTerm(), null);
			this.jContentPane.add(getBDeleteTerm(), null);
			this.jContentPane.add(getBAddVariable(), null);
		}
		return this.jContentPane;
	}

	/**
	 * This method initializes tfVarName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTfVarName() {
		if (this.tfVarName == null) {
			this.tfVarName = new JTextField();
			this.tfVarName.setBounds(new Rectangle(105, 14, 272, 20));
		}
		return this.tfVarName;
	}

	/**
	 * This method initializes lLinguisticTerms	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getLLinguisticTerms() {
		if (this.lLinguisticTerms == null) {
			this.lLinguisticTerms = new JList(this.terms);
			this.lLinguisticTerms.setBounds(new Rectangle(17, 38, 359, 167));
			this.lLinguisticTerms.setName("lLinguisticTerms");
		}
		return this.lLinguisticTerms;
	}

	/**
	 * This method initializes bAddTerm	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBAddTerm() {
		if (this.bAddTerm == null) {
			this.bAddTerm = new JButton();
			this.bAddTerm.setBounds(new Rectangle(15, 211, 90, 27));
			this.bAddTerm.setText("Add Term");
			this.bAddTerm.addActionListener(this);
				
		}
		return this.bAddTerm;
	}

	/**
	 * This method initializes bDeleteTerm	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBDeleteTerm() {
		if (this.bDeleteTerm == null) {
			this.bDeleteTerm = new JButton();
			this.bDeleteTerm.setLocation(new Point(110, 211));
			this.bDeleteTerm.setText("Delete Term");
			this.bDeleteTerm.setSize(new Dimension(116, 27));
			this.bDeleteTerm.addActionListener(this);
		}
		return this.bDeleteTerm;
	}

	/**
	 * This method initializes bAddVariable	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBAddVariable() {
		if (this.bAddVariable == null) {
			this.bAddVariable = new JButton();
			this.bAddVariable.setLocation(new Point(232, 211));
			this.bAddVariable.setText("Add Variable");
			this.bAddVariable.setSize(new Dimension(109, 27));
			this.bAddVariable.addActionListener(this);
		}
		return this.bAddVariable;
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
		if (e.getSource().equals(this.bAddTerm)){
			String s = (String)JOptionPane.showInputDialog
			(       this,
                    "Add the new term",
                    "Add term",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "");
			this.terms.add(this.terms.getSize(), s);			
		}			
		else if (e.getSource().equals(this.bDeleteTerm)){
			int selectedIndex = this.lLinguisticTerms.getSelectedIndex();
			this.terms.remove(selectedIndex);
			System.out.println(""+ selectedIndex);
		} else if(e.getSource().equals(this.bAddVariable)){
			final String variableName = this.tfVarName.getText();
			final Vector<String> variableTerms = new Vector<String>();
			for (int i = 0; i < this.terms.getSize(); i++) {
				variableTerms.add((String)this.terms.get(i));				
			}
			final Variable variable = new Variable(variableName, variableTerms);
			this.parent.addVariable(variable);
			this.dispose();
		}
		
		

		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10" 