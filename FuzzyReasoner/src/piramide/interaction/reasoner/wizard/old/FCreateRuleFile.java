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
import javax.swing.JList;
import java.awt.Rectangle;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JButton;

import piramide.interaction.reasoner.wizard.Variable;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("unqualified-field-access")
public class FCreateRuleFile extends JFrame implements ActionListener, IFCreateVariable{

	private final HashMap<String, Variable> inputVariables = new HashMap<String, Variable>();  //  @jve:decl-index=0:
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JList lVariables = null;
	private final DefaultListModel variablesModel = new DefaultListModel();
	private JLabel jLabel = null;
	private JButton bAddVariable = null;
	private JButton bDeleteVariable = null;
	private JButton bNext = null;

	/**
	 * This is the default constructor
	 */
	public FCreateRuleFile() {
		super();
		initialize();
	}
	
	public HashMap<String, Variable> getVariables() {
		return this.inputVariables;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(472, 293);
		this.setContentPane(getJContentPane());
		this.setTitle("Rule Creation Wizard");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(16, 10, 91, 16));
			jLabel.setText("Rule Variables");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getLVariables(), null);
			jContentPane.add(jLabel, null);
			jContentPane.add(getBAddVariable(), null);
			jContentPane.add(getBDeleteVariable(), null);
			jContentPane.add(getBNext(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes lVariables	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getLVariables() {
		if (lVariables == null) {
			lVariables = new JList(variablesModel);
			lVariables.setBounds(new Rectangle(15, 29, 377, 175));
		}
		return lVariables;
	}
	
	public void addVariable(Variable variable){
		if(!inputVariables.containsKey(variable.getName())){
			inputVariables.put(variable.getName(), variable);
			variablesModel.addElement(variable.getName());
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
		if (bAddVariable == null) {
			bAddVariable = new JButton();
			bAddVariable.setText("Add Variable");
			bAddVariable.setSize(new Dimension(122, 25));
			bAddVariable.setLocation(new Point(19, 213));
			bAddVariable.addActionListener(this);
		}
		return bAddVariable;
	}

	/**
	 * This method initializes bDeleteVariable	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBDeleteVariable() {
		if (bDeleteVariable == null) {
			bDeleteVariable = new JButton();
			bDeleteVariable.setBounds(new Rectangle(147, 213, 122, 25));
			bDeleteVariable.setText("Delete Variable");
			bDeleteVariable.addActionListener(this);
		}
		return bDeleteVariable;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(bAddVariable)){
			FCreateNewVariable fNewVariable =  new FCreateNewVariable(this);
			fNewVariable.setVisible(true);
		} else if (e.getSource().equals(bDeleteVariable)){
			int selectedIndex = lVariables.getSelectedIndex();
			String selectedItem = (String)lVariables.getSelectedValue();
			variablesModel.remove(selectedIndex);
			inputVariables.remove(selectedItem);
		} else if (e.getSource().equals(bNext)){
			FCreateOutputVar fCreateOutputVar = new FCreateOutputVar(inputVariables);
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
		if (bNext == null) {
			bNext = new JButton();
			bNext.setBounds(new Rectangle(275, 213, 106, 26));
			bNext.setText("Next");
			bNext.addActionListener(this);
		}
		return bNext;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
