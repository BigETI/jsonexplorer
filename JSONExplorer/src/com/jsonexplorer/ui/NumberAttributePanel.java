package com.jsonexplorer.ui;

import com.jsonexplorer.core.EAttributeTypes;
import com.jsonexplorer.core.JSONInheritance;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JTextField;

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
	 * Text field
	 */
	private JTextField value_text_field;

	/**
	 * Constructor
	 * 
	 * Create attribute panel
	 * 
	 * @param json_inheritance
	 *            JSON inheritance
	 */
	public NumberAttributePanel(JSONInheritance json_inheritance) {
		super(json_inheritance);
		GridBagLayout gridBagLayout = (GridBagLayout) getLayout();
		gridBagLayout.columnWeights = new double[] { 1.0 };

		value_text_field = new JTextField();
		GridBagConstraints gbc_value_text_field = new GridBagConstraints();
		gbc_value_text_field.insets = new Insets(0, 0, 5, 5);
		gbc_value_text_field.fill = GridBagConstraints.HORIZONTAL;
		gbc_value_text_field.gridx = 0;
		gbc_value_text_field.gridy = 1;
		add(value_text_field, gbc_value_text_field);
		value_text_field.setColumns(10);

		value_text_field.setText(json_inheritance.getValue().toString());
	}

	/**
	 * Get text field
	 * 
	 * @return Text field
	 */
	protected JTextField getTextField() {
		return value_text_field;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jsonexplorer.ui.AttributePanel#getAttributeValue()
	 */
	@Override
	public abstract Object getAttributeValue();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jsonexplorer.ui.AttributePanel#getAttributeType()
	 */
	@Override
	public abstract EAttributeTypes getAttributeType();
}
