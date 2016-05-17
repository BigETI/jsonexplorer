package com.jsonexplorer.ui;

import com.jsonexplorer.core.JSONInheritance;
import javax.swing.JFormattedTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

/**
 * Class to handle integer attributes in an UI
 * 
 * @author Ethem Kurt
 *
 * @param <T>
 *            Number type
 */
public abstract class NumberAttributePanel<T extends Number> extends AttributePanel {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -8746072468394025804L;

	/**
	 * Formatted text field
	 */
	private JFormattedTextField value_formattedTextField;

	/**
	 * Constructor
	 * 
	 * Create attribute panel
	 * 
	 * @param json_inheritance
	 *            JSON inheritance
	 */
	public NumberAttributePanel(JSONInheritance json_inheritance, NumberFormat number_format) {
		super(json_inheritance);
		GridBagLayout gridBagLayout = (GridBagLayout) getLayout();
		gridBagLayout.columnWeights = new double[] { 1.0 };

		value_formattedTextField = new JFormattedTextField(number_format);
		value_formattedTextField.setText(json_inheritance.getValue().toString());
		GridBagConstraints gbc_value_formattedTextField = new GridBagConstraints();
		gbc_value_formattedTextField.insets = new Insets(0, 0, 5, 0);
		gbc_value_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_value_formattedTextField.gridx = 0;
		gbc_value_formattedTextField.gridy = 1;
		add(value_formattedTextField, gbc_value_formattedTextField);
	}

	/**
	 * Get formatted text field
	 * 
	 * @return Formatted text field
	 */
	protected JFormattedTextField getFormattedTextField() {
		return value_formattedTextField;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jsonexplorer.ui.AttributePanel#getAttributeValue()
	 */
	@Override
	public abstract Object getAttributeValue();
}
