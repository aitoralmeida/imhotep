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
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;

import piramide.interaction.reasoner.wizard.FCreateOutputVariables;
import piramide.interaction.reasoner.wizard.IFCreateVariable;

public class FCreateInputVariables extends JFrame implements ActionListener, IFCreateVariable {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private final HashMap<String, Variable> inputVariables = new HashMap<String, Variable>();  
	private JList lVariables = null;
	private final DefaultListModel variablesModel = new DefaultListModel();
	private JLabel jLabel = null;
	private JButton bAddVariable = null;
	private JButton bDeleteVariable = null;
	private JButton bNext = null;
	
	/**
	 * This is the default constructor
	 */
	public FCreateInputVariables() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("Rule Creation Wizard");
	}


	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (this.jContentPane == null) {
			this.jLabel = new JLabel();
			this.jLabel.setBounds(new Rectangle(16, 10, 91, 16));
			this.jLabel.setText("Rule Variables");
			this.jContentPane = new JPanel();
			this.jContentPane.setLayout(null);
			this.jContentPane.add(getLVariables(), null);
			this.jContentPane.add(this.jLabel, null);
			this.jContentPane.add(getBAddVariable(), null);
			this.jContentPane.add(getBDeleteVariable(), null);
			this.jContentPane.add(getBNext(), null);
		}
		return this.jContentPane;
	}

	/**
	 * This method initializes lVariables	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getLVariables() {
		if (this.lVariables == null) {
			this.lVariables = new JList(this.variablesModel);
			this.lVariables.setBounds(new Rectangle(15, 29, 377, 175));
		}
		return this.lVariables;
	}
	
	public void addVariable(Variable variable){
		if(!this.inputVariables.containsKey(variable.getName())){
			this.inputVariables.put(variable.getName(), variable);
			this.variablesModel.addElement(variable.getName());
		} else{
			JOptionPane.showMessageDialog(this,
				    "A variable with the same name already exists",
				    "Error adding variable",
				    JOptionPane.ERROR_MESSAGE);

		}
		
		
	}

	/**
	 * This method initializes bAddVariable	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBAddVariable() {
		if (this.bAddVariable == null) {
			this.bAddVariable = new JButton();
			this.bAddVariable.setText("Add Variable");
			this.bAddVariable.setSize(new Dimension(122, 25));
			this.bAddVariable.setLocation(new Point(19, 213));
			this.bAddVariable.addActionListener(this);
		}
		return this.bAddVariable;
	}

	/**
	 * This method initializes bDeleteVariable	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBDeleteVariable() {
		if (this.bDeleteVariable == null) {
			this.bDeleteVariable = new JButton();
			this.bDeleteVariable.setBounds(new Rectangle(147, 213, 122, 25));
			this.bDeleteVariable.setText("Delete Variable");
			this.bDeleteVariable.addActionListener(this);
		}
		return this.bDeleteVariable;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.bAddVariable)){
			FCreateNewVariable fNewVariable =  new FCreateNewVariable(this);
			fNewVariable.setVisible(true);
		} else if (e.getSource().equals(this.bDeleteVariable)){
			int selectedIndex = this.lVariables.getSelectedIndex();
			String selectedItem = (String)this.lVariables.getSelectedValue();
			this.variablesModel.remove(selectedIndex);
			this.inputVariables.remove(selectedItem);
		} else if (e.getSource().equals(this.bNext)){
			FCreateOutputVariables fCreateOutputVar = new FCreateOutputVariables(this.inputVariables);
			fCreateOutputVar.setVisible(true);
			this.dispose();
		}
		
	}

	/**
	 * This method initializes bNext	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBNext() {
		if (this.bNext == null) {
			this.bNext = new JButton();
			this.bNext.setBounds(new Rectangle(275, 213, 106, 26));
			this.bNext.setText("Next");
			this.bNext.addActionListener(this);
		}
		return this.bNext;
	}
	
	public HashMap<String, Variable> getVariables() {
		return this.inputVariables;
	}

}
