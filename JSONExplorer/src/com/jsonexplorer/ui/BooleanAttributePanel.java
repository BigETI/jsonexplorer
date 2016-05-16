package com.jsonexplorer.ui;
import javax.swing.JToggleButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class BooleanAttributePanel extends AttributePanel<Boolean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 376014469331420923L;
	
	private JToggleButton value_toggleButton;
	
	private Boolean state = null;

	/**
	 * Create the panel.
	 */
	public BooleanAttributePanel() {
		super();
		
		value_toggleButton = new JToggleButton("Undefined");
		value_toggleButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				switch(arg0.getStateChange()) {
				case ItemEvent.SELECTED:
					setJSONValue(true);
					break;
				case ItemEvent.DESELECTED:
					setJSONValue(false);
					break;
				}
			}
		});
		GridBagConstraints gbc_value_toggleButton = new GridBagConstraints();
		gbc_value_toggleButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_value_toggleButton.insets = new Insets(0, 0, 5, 0);
		gbc_value_toggleButton.gridx = 0;
		gbc_value_toggleButton.gridy = 1;
		add(value_toggleButton, gbc_value_toggleButton);
	}
	
	private void setJSONValue(boolean b) {
		state = b;
		if (b)
			value_toggleButton.setText("True - click to set false");
		else
			value_toggleButton.setText("False - click to set true");
		value_toggleButton.setSelected(b);
	}
	
	public void setJSONAttribute(String name, Boolean o) {
		super.setJSONAttribute(name, o);
		if (o == null)
			setJSONValue(false);
		else
			setJSONValue(o);
	}
	
	public Boolean getJSONAttribute() {
		return state;
	}
	
	public void saveChanges() {
		// Failsafe
	}
}
