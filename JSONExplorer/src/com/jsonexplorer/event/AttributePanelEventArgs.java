package com.jsonexplorer.event;

import com.jsonexplorer.core.JSONInheritance;
import com.jsonexplorer.ui.AttributePanel;

/**
 * Class to handle attribute panel event arguments
 * 
 * @author Ethem Kurt
 *
 */
public class AttributePanelEventArgs implements IEventArgs {

	/**
	 * Attribute panel
	 */
	private AttributePanel attribute_panel;

	/**
	 * JSON inheritance
	 */
	private JSONInheritance json_inheritance;

	/**
	 * Constructor
	 * 
	 * @param attribute_panel
	 *            Attribute panel
	 * @param json_ineritance
	 *            JSON inheritance
	 */
	public AttributePanelEventArgs(AttributePanel attribute_panel, JSONInheritance json_inheritance) {
		this.attribute_panel = attribute_panel;
		this.json_inheritance = json_inheritance;
	}

	/**
	 * Get attribute panel
	 * 
	 * @return Attribute panel
	 */
	public AttributePanel getAttributePanel() {
		return attribute_panel;
	}

	/**
	 * Get JSON inheritance
	 * 
	 * @return JSON inheritance
	 */
	public JSONInheritance getJSONInheritance() {
		return json_inheritance;
	}
}
