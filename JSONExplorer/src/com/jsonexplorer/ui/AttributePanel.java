package com.jsonexplorer.ui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jsonexplorer.core.EAttributeTypes;
import com.jsonexplorer.core.JSONInheritance;
import com.jsonexplorer.event.AttributePanelEventArgs;
import com.jsonexplorer.event.AttributePanelNotifier;

import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Class to handle JSON attributes in an UI
 * 
 * @author Ethem Kurt
 *
 */
public class AttributePanel extends JPanel {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 5387610110943932297L;

	/**
	 * Notifier
	 */
	private AttributePanelNotifier notifier = new AttributePanelNotifier();

	/**
	 * JSON inheritance
	 */
	private JSONInheritance json_inheritance = null;

	/**
	 * Attribute name label
	 */
	private JLabel attribute_name_label;

	/**
	 * Constructor
	 * 
	 * Creates the panel
	 * 
	 * @param json_inheritance
	 *            JSON inheritance
	 */
	public AttributePanel(JSONInheritance json_inheritance) {
		super();
		setBorder(new EmptyBorder(10, 10, 10, 10));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		attribute_name_label = new JLabel("JSON attribute name");
		GridBagConstraints gbc_attribute_name_label = new GridBagConstraints();
		gbc_attribute_name_label.insets = new Insets(0, 0, 5, 0);
		gbc_attribute_name_label.gridx = 0;
		gbc_attribute_name_label.gridy = 0;
		add(attribute_name_label, gbc_attribute_name_label);

		JButton btnSaveChanges = new JButton("Save changes");
		btnSaveChanges.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				saveChanges();
			}
		});
		GridBagConstraints gbc_btnSaveChanges = new GridBagConstraints();
		gbc_btnSaveChanges.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSaveChanges.insets = new Insets(0, 0, 5, 0);
		gbc_btnSaveChanges.gridx = 0;
		gbc_btnSaveChanges.gridy = 2;
		add(btnSaveChanges, gbc_btnSaveChanges);

		JButton btnChangeType = new JButton("Change type");
		btnChangeType.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				changeAttributeType((EAttributeTypes) JOptionPane.showInputDialog(null,
						"Select the type you want to set:", "Change attribute type", JOptionPane.PLAIN_MESSAGE, null,
						EAttributeTypes.values(), getAttributeType()));
			}
		});
		GridBagConstraints gbc_btnChangeType = new GridBagConstraints();
		gbc_btnChangeType.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnChangeType.gridx = 0;
		gbc_btnChangeType.gridy = 3;
		add(btnChangeType, gbc_btnChangeType);

		this.json_inheritance = json_inheritance;
		if (json_inheritance != null)
			attribute_name_label.setText("Attribute: " + json_inheritance.getPath());
	}

	/**
	 * Get notifier
	 * 
	 * @return Notifier
	 */
	public AttributePanelNotifier getNotifier() {
		return notifier;
	}

	/**
	 * Get JSON inheritance
	 * 
	 * @return JSON inheritance
	 */
	public JSONInheritance getJSONInheritance() {
		return json_inheritance;
	}

	/**
	 * Get JSON attribute value from UI
	 * 
	 * @return Attribute value
	 */
	public Object getAttributeValue() {
		return JSONObject.NULL;
	}

	/**
	 * Save changes
	 */
	public void saveChanges() {
		JSONObject jo;
		JSONArray ja;
		String jok;
		int jak;
		Object value = getAttributeValue();
		if (json_inheritance.getParent().getValue() instanceof JSONObject) {
			jo = (JSONObject) json_inheritance.getParent().getValue();
			if (json_inheritance.getKey() instanceof String) {
				jok = (String) json_inheritance.getKey();
				if (jo.opt(jok) != null) {
					jo.put(jok, value);
					json_inheritance = new JSONInheritance(value, jok, json_inheritance.getParent(),
							json_inheritance.getName());
					notifier.setOnSaveChanges(new AttributePanelEventArgs(this, json_inheritance));
				}
			}
		} else if (json_inheritance.getParent().getValue() instanceof JSONArray) {
			ja = (JSONArray) json_inheritance.getParent().getValue();
			if (json_inheritance.getKey() instanceof Integer) {
				jak = (Integer) json_inheritance.getKey();
				if ((ja.length() < jak) && (jak >= 0)) {
					ja.put(jak, value);
					json_inheritance = new JSONInheritance(value, jak, json_inheritance.getParent(),
							json_inheritance.getName());
					notifier.setOnSaveChanges(new AttributePanelEventArgs(this, json_inheritance));
				}
			}
		}
	}

	/**
	 * Change attribute type
	 * 
	 * @param at
	 *            Attribute type
	 */
	private void changeAttributeType(EAttributeTypes at) {
		JSONObject jo;
		JSONArray ja;
		String jok;
		int jak;
		if (at != null) {
			if (json_inheritance.getParent().getValue() instanceof JSONObject) {
				jo = (JSONObject) json_inheritance.getParent().getValue();
				if (json_inheritance.getKey() instanceof String) {
					jok = (String) json_inheritance.getKey();
					if (jo.opt(jok) != null) {
						jo.put(jok, at.getDefaultValue());
						json_inheritance = new JSONInheritance(at.getDefaultValue(), jok, json_inheritance.getParent(),
								json_inheritance.getName());
						notifier.setOnChangeType(new AttributePanelEventArgs(this, json_inheritance));
					}
				}
			} else if (json_inheritance.getParent().getValue() instanceof JSONArray) {
				ja = (JSONArray) json_inheritance.getParent().getValue();
				if (json_inheritance.getKey() instanceof Integer) {
					jak = (Integer) json_inheritance.getKey();
					if ((ja.length() < jak) && (jak >= 0)) {
						ja.put(jak, at.getDefaultValue());
						json_inheritance = new JSONInheritance(at.getDefaultValue(), jak, json_inheritance.getParent(),
								json_inheritance.getName());
						notifier.setOnChangeType(new AttributePanelEventArgs(this, json_inheritance));
					}
				}
			}
		}
	}

	/**
	 * Get attribute type
	 * 
	 * @return Attribute type
	 */
	public EAttributeTypes getAttributeType() {
		return EAttributeTypes.NULL;
	}
}
