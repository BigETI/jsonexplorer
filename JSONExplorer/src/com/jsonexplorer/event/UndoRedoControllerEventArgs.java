package com.jsonexplorer.event;

/**
 * Undo redo controller event arguments class
 * 
 * @author Ethem Kurt
 *
 * @param <T>
 *            Changes type
 */
public class UndoRedoControllerEventArgs<T> implements IEventArgs {

	/**
	 * State
	 */
	private T state;

	/**
	 * Constructor
	 * 
	 * @param state
	 *            State
	 */
	public UndoRedoControllerEventArgs(T state) {
		this.state = state;
	}

	/**
	 * Get State
	 * 
	 * @return State
	 */
	public T getState() {
		return state;
	}
}
