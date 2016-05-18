package com.jsonexplorer.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import com.jsonexplorer.core.EAttributeTypes;
import com.jsonexplorer.core.JSONInheritance;

import java.awt.Dimension;

/**
 * Class to handle string attributes in an UI
 * 
 * @author Ethem Kurt
 *
 */
public class StringAttributePanel extends AttributePanel {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -8010322718028948928L;

	/**
	 * Code editor
	 */
	private CodeTextArea value_code_editor;

	/**
	 * Constructor
	 * 
	 * Create the panel.
	 * 
	 * @param json_inheritance
	 *            JSON inheritance
	 */
	public StringAttributePanel(JSONInheritance json_inheritance) {
		super(json_inheritance);
		GridBagLayout gridBagLayout = (GridBagLayout) getLayout();
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0 };

		value_code_editor = new CodeTextArea();
		value_code_editor.setSize(new Dimension(0, 200));
		value_code_editor.setPreferredSize(new Dimension(109, 200));
		GridBagConstraints gbc_value_code_editor = new GridBagConstraints();
		gbc_value_code_editor.insets = new Insets(0, 0, 5, 5);
		gbc_value_code_editor.fill = GridBagConstraints.BOTH;
		gbc_value_code_editor.gridx = 0;
		gbc_value_code_editor.gridy = 1;
		add(value_code_editor, gbc_value_code_editor);

		value_code_editor.getTextArea().setText(json_inheritance.getValue().toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jsonexplorer.ui.AttributePanel#getJSONAttribute()
	 */
	@Override
	public Object getAttributeValue() {
		return value_code_editor.getTextArea().getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jsonexplorer.ui.AttributePanel#getAttributeType()
	 */
	@Override
	public EAttributeTypes getAttributeType() {
		return EAttributeTypes.STRING;
	}
}
