package com.jsonexplorer.event;

/**
 * Undo redo controller listener interface
 * 
 * @author Ethem Kurt
 *
 * @param <T>
 *            Changes type
 */
public interface IUndoRedoControllerListener<T> {

	/**
	 * Commit event
	 * 
	 * @param args
	 *            Arguments
	 */
	public void onCommit(UndoRedoControllerEventArgs<T> args);

	/**
	 * Undo event
	 * 
	 * @param args
	 *            Arguments
	 */
	public void onUndo(UndoRedoControllerEventArgs<T> args);

	/**
	 * Redo event
	 * 
	 * @param args
	 *            Arguments
	 */
	public void onRedo(UndoRedoControllerEventArgs<T> args);

	/**
	 * Clear event
	 * 
	 * @param args
	 *            Arguments
	 */
	public void onClear(UndoRedoControllerEventArgs<T> args);
}
