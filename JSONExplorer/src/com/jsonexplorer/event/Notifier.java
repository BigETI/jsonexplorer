package com.jsonexplorer.event;

import java.util.LinkedHashSet;

/**
 * A class to handle listeners
 * 
 * @author Ethem Kurt
 *
 * @param <LT>
 *            Listener type
 * @param <T>
 *            Event arguments
 */
public abstract class Notifier<LT, T extends IEventArgs> {

	/**
	 * Listeners
	 */
	private LinkedHashSet<LT> listeners = new LinkedHashSet<LT>();

	/**
	 * Add new listener
	 * 
	 * @param listener
	 */
	public void addListener(LT listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	/**
	 * Remove listener
	 * 
	 * @param listener
	 *            Listener
	 */
	public void removeListener(LT listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	/**
	 * Remove all listeners
	 */
	public void clear() {
		listeners.clear();
	}

	/**
	 * Get all listeners
	 * 
	 * @return All listeners
	 */
	protected LinkedHashSet<LT> getListeners() {
		return listeners;
	}
}
