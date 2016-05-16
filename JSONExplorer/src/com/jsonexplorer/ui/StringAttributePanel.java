package com.jsonexplorer.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;

public class StringAttributePanel extends AttributePanel<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8010322718028948928L;
	private CodeTextArea value_codeTextArea;

	/**
	 * Create the panel.
	 */
	public StringAttributePanel() {
		super();
		GridBagLayout gridBagLayout = (GridBagLayout) getLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0};

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

	@Override
	public void setJSONAttribute(String name, String o) {
		super.setJSONAttribute(name, o);
		value_codeTextArea.getTextArea().setText(o);
	}

	@Override
	public String getJSONAttribute() {
		return value_codeTextArea.getTextArea().getText();
	}

	@Override
	public void saveChanges() {
		super.saveChanges();
		// ...
	}
}
