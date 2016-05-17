package com.jsonexplorer.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
	private CodeTextArea value_codeTextArea;

	/**
	 * Constructor
	 * 
	 * Create the panel.
	 */
	public StringAttributePanel(JSONInheritance json_Inheritance) {
		super(json_Inheritance);
		GridBagLayout gridBagLayout = (GridBagLayout) getLayout();
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0 };

		value_codeTextArea = new CodeTextArea();
		value_codeTextArea.setSize(new Dimension(0, 200));
		value_codeTextArea.setPreferredSize(new Dimension(109, 200));
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(value_codeTextArea, gbc_scrollPane);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jsonexplorer.ui.AttributePanel#getJSONAttribute()
	 */
	@Override
	public Object getAttributeValue() {
		return value_codeTextArea.getTextArea().getText();
	}
}
