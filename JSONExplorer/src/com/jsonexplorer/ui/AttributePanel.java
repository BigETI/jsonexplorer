package com.jsonexplorer.ui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Insets;

public class AttributePanel<T> extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5387610110943932297L;
	
	JLabel lblJsonAttributeName;

	/**
	 * Create the panel.
	 */
	public AttributePanel() {
		setBorder(new EmptyBorder(10, 10, 10, 10));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		lblJsonAttributeName = new JLabel("JSON attribute name");
		GridBagConstraints gbc_lblJsonAttributeName = new GridBagConstraints();
		gbc_lblJsonAttributeName.insets = new Insets(0, 0, 5, 0);
		gbc_lblJsonAttributeName.gridx = 0;
		gbc_lblJsonAttributeName.gridy = 0;
		add(lblJsonAttributeName, gbc_lblJsonAttributeName);
		
		JButton btnChangeType = new JButton("Change type");
		GridBagConstraints gbc_btnChangeType = new GridBagConstraints();
		gbc_btnChangeType.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnChangeType.gridx = 0;
		gbc_btnChangeType.gridy = 2;
		add(btnChangeType, gbc_btnChangeType);

	}
	
	public void setJSONAttribute(String name, T o) {
		lblJsonAttributeName.setText("Attribute: \"" + name + "\"");
	}
	
	public T getJSONAttribute() {
		return null;
	}

}
