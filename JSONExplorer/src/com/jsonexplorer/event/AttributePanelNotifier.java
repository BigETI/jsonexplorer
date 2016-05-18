package com.jsonexplorer.event;

/**
 * Attribute panel notifier class
 * 
 * @author Ethem Kurt
 *
 */
public class AttributePanelNotifier extends Notifier<IAttributePanelListener, AttributePanelEventArgs> {

	/**
	 * Trigger "onBeforeSaveChanges" event
	 * 
	 * @param args
	 *            Event arguments
	 */
	public void setOnBeforeSaveChanges(AttributePanelEventArgs args) {
		for (IAttributePanelListener i : getListeners()) {
			i.onBeforeSaveChanges(args);
		}
	}

	/**
	 * Trigger "onSaveChanges" event
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
	 * Trigger "onBeforeChangeType" event
	 * 
	 * @param args
	 *            Event arguments
	 */
	public void setOnBeforeChangeType(AttributePanelEventArgs args) {
		for (IAttributePanelListener i : getListeners()) {
			i.onBeforeChangeType(args);
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
