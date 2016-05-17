package com.jsonexplorer.event;

/**
 * Attribute panel notifier class
 * 
 * @author Ethem Kurt
 *
 */
public class AttributePanelNotifier extends Notifier<IAttributePanelListener, AttributePanelEventArgs> {

	/**
	 * Trigger "onSaveChanged" event
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
	 * Trigger "onChangeType" event
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
