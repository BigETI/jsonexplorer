package com.jsonexplorer.event;

/**
 * Attribute panel listener interface
 * 
 * @author Ethem Kurt
 *
 */
public interface IAttributePanelListener {

	/**
	 * Before save changes event
	 * 
	 * @param args
	 *            Event arguments
	 */
	public void onBeforeSaveChanges(AttributePanelEventArgs args);

	/**
	 * Save changes event
	 * 
	 * @param args
	 *            Event arguments
	 */
	public void onSaveChanges(AttributePanelEventArgs args);

	/**
	 * Before change type event
	 * 
	 * @param args
	 *            Event arguments
	 */
	public void onBeforeChangeType(AttributePanelEventArgs args);

	/**
	 * Change type event
	 * 
	 * @param args
	 *            Event arguments
	 */
	public void onChangeType(AttributePanelEventArgs args);
}
