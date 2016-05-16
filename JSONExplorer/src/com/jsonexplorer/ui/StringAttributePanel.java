package com.jsonexplorer.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class StringAttributePanel extends AttributePanel<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8010322718028948928L;
	private CodeTextArea value_scrollPane;

	/**
	 * Create the panel.
	 */
	public StringAttributePanel() {
		super();
		GridBagLayout gridBagLayout = (GridBagLayout) getLayout();
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0 };
		gridBagLayout.columnWeights = new double[] { 1.0 };

		value_scrollPane = new CodeTextArea();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(value_scrollPane, gbc_scrollPane);
	}

	@Override
	public void setJSONAttribute(String name, String o) {
		super.setJSONAttribute(name, o);
		value_scrollPane.getTextArea().setText(o);
	}

	@Override
	public String getJSONAttribute() {
		return value_scrollPane.getTextArea().getText();
	}

	@Override
	public void saveChanges() {
		super.saveChanges();
		// ...
	}
}
