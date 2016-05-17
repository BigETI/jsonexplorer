package com.jsonexplorer.ui;

import javax.swing.JToggleButton;

import com.jsonexplorer.core.EAttributeTypes;
import com.jsonexplorer.core.JSONInheritance;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**
 * Class to handle boolean attributes in an UI
 * 
 * @author Ethem Kurt
 *
 */
public class BooleanAttributePanel extends AttributePanel {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 376014469331420923L;

	/**
	 * Toggle button
	 */
	private JToggleButton value_toggleButton;

	/**
	 * Boolean state
	 */
	private Boolean state = null;

	/**
	 * Constructor
	 * 
	 * Create the panel.
	 * 
	 * @param json_inheritance
	 *            JSON inheritance
	 */
	public BooleanAttributePanel(JSONInheritance json_inheritance) {
		super(json_inheritance);

		value_toggleButton = new JToggleButton("Undefined");
		value_toggleButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				switch (arg0.getStateChange()) {
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

		if (json_inheritance.getValue() instanceof Boolean)
			setJSONValue(((Boolean) json_inheritance.getValue()).booleanValue());
	}

	/**
	 * Set JSON value to UI
	 * 
	 * @param b
	 *            boolean
	 */
	private void setJSONValue(boolean b) {
		state = b;
		if (b)
			value_toggleButton.setText("True - click to set false");
		else
			value_toggleButton.setText("False - click to set true");
		value_toggleButton.setSelected(b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jsonexplorer.ui.AttributePanel#getAttributeValue()
	 */
	@Override
	public Object getAttributeValue() {
		return state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jsonexplorer.ui.AttributePanel#getAttributeType()
	 */
	@Override
	public EAttributeTypes getAttributeType() {
		return EAttributeTypes.BOOLEAN;
	}
}
