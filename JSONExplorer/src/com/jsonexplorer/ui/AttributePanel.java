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
	
	private JLabel attribute_name_label;

	/**
	 * Create the panel.
	 */
	public AttributePanel() {
		super();
		setBorder(new EmptyBorder(10, 10, 10, 10));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		attribute_name_label = new JLabel("JSON attribute name");
		GridBagConstraints gbc_attribute_name_label = new GridBagConstraints();
		gbc_attribute_name_label.insets = new Insets(0, 0, 5, 0);
		gbc_attribute_name_label.gridx = 0;
		gbc_attribute_name_label.gridy = 0;
		add(attribute_name_label, gbc_attribute_name_label);
		
		JButton btnSaveChanges = new JButton("Save changes");
		GridBagConstraints gbc_btnSaveChanges = new GridBagConstraints();
		gbc_btnSaveChanges.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSaveChanges.insets = new Insets(0, 0, 5, 0);
		gbc_btnSaveChanges.gridx = 0;
		gbc_btnSaveChanges.gridy = 2;
		add(btnSaveChanges, gbc_btnSaveChanges);
		
		JButton btnChangeType = new JButton("Change type");
		GridBagConstraints gbc_btnChangeType = new GridBagConstraints();
		gbc_btnChangeType.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnChangeType.gridx = 0;
		gbc_btnChangeType.gridy = 3;
		add(btnChangeType, gbc_btnChangeType);

	}
	
	public void setJSONAttribute(String name, T o) {
		attribute_name_label.setText("Attribute: \"" + name + "\"");
	}
	
	public T getJSONAttribute() {
		return null;
	}
	
	public void saveChanges() {
		// Failsafe
	}

}
