package com.jsonexplorer.event;

/**
 * Attribute panel listener interface
 * 
 * @author Ethem Kurt
 *
 */
public interface IAttributePanelListener {

	/**
	 * Save changes event
	 * 
	 * @param args
	 *            Event arguments
	 */
	public void onSaveChanges(AttributePanelEventArgs args);

	/**
	 * Change type event
	 * 
	 * @param args
	 *            Event arguments
	 */
	public void onChangeType(AttributePanelEventArgs args);
}
