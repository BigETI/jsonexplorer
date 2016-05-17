package com.jsonexplorer.event;

/**
 * Undo redo controller notifier
 * 
 * @author Ethem Kurt
 *
 * @param <T>
 *            Changes type
 */
public class UndoRedoControllerNotifier<T>
		extends Notifier<IUndoRedoControllerListener<T>, UndoRedoControllerEventArgs<T>> {

	/**
	 * Trigger "onCommit" event
	 * 
	 * @param args
	 *            Arguments
	 */
	protected void setOnCommit(UndoRedoControllerEventArgs<T> args) {
		for (IUndoRedoControllerListener<T> i : getListeners()) {
			i.onCommit(args);
		}
	}

	/**
	 * Trigger "onUndo" event
	 * 
	 * @param args
	 *            Arguments
	 */
	protected void setOnUndo(UndoRedoControllerEventArgs<T> args) {
		for (IUndoRedoControllerListener<T> i : getListeners()) {
			i.onUndo(args);
		}
	}

	/**
	 * Trigger "onRedo" event
	 * 
	 * @param args
	 *            Arguments
	 */
	protected void setOnRedo(UndoRedoControllerEventArgs<T> args) {
		for (IUndoRedoControllerListener<T> i : getListeners()) {
			i.onRedo(args);
		}
	}

	/**
	 * Trigger "onClear" event
	 * 
	 * @param args
	 *            Arguments
	 */
	protected void setOnClear(UndoRedoControllerEventArgs<T> args) {
		for (IUndoRedoControllerListener<T> i : getListeners()) {
			i.onClear(args);
		}
	}
}
