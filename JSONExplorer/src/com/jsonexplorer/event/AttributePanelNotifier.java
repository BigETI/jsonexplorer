package com.jsonexplorer.event;

/**
 * Attribute panel notifier class
 * 
 * @author Ethem Kurt
 *
 */
public class AttributePanelNotifier extends Notifier<IAttributePanelListener, AttributePanelEventArgs> {

	/**
	 * Sets "onSaveChanged" event off
	 * 
	 * @param args
	 *            Event arguments
	 */
	public void setOnSaveChanges(AttributePanelEventArgs args) {
		for (IAttributePanelListener i : getListeners()) {
			i.onSaveChanges(args);
		}
	}

	/**
	 * Sets "onChangeType" event off
	 * 
	 * @param args
	 *            Event arguments
	 */
	public void setOnChangeType(AttributePanelEventArgs args) {
		for (IAttributePanelListener i : getListeners()) {
			i.onChangeType(args);
		}
	}
}
