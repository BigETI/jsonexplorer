package com.jsonexplorer.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JMenuItem;
import com.jsonexplorer.event.UndoRedoControllerNotifier;

import javafx.util.Pair;

/**
 * Class to handle undo and redo functions
 * 
 * @author Ethem Kurt
 *
 * @param <T>
 *            Changes type
 */
public class UndoRedoController<T> extends UndoRedoControllerNotifier<T> {

	/**
	 * Undo menu item
	 */
	private JMenuItem undo_menu_item;

	/**
	 * Redo menu item
	 */
	private JMenuItem redo_menu_item;

	/**
	 * Undo cache
	 */
	private LinkedList<Pair<T, String>> undo_cache = new LinkedList<>();

	/**
	 * Redo cache
	 */
	private LinkedList<Pair<T, String>> redo_cache = new LinkedList<>();

	/**
	 * Current state
	 */
	private Pair<T, String> current_state = null;

	/**
	 * Constructor
	 * 
	 * @param undo_menu_item
	 *            Undo menu item
	 * @param redo_menu_item
	 *            Redo menu item
	 */
	public UndoRedoController(JMenuItem undo_menu_item, JMenuItem redo_menu_item) {
		this.undo_menu_item = undo_menu_item;
		this.redo_menu_item = redo_menu_item;
		if (undo_menu_item != null)
			undo_menu_item.addActionListener(new ActionListener() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
				 * ActionEvent)
				 */
				@Override
				public void actionPerformed(ActionEvent e) {
					undo();
				}
			});
		if (redo_menu_item != null)
			redo_menu_item.addActionListener(new ActionListener() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
				 * ActionEvent)
				 */
				@Override
				public void actionPerformed(ActionEvent e) {
					redo();
				}
			});
		updateMenuItems();
	}

	/**
	 * Update menu items
	 */
	private void updateMenuItems() {
		boolean enabled;
		if (current_state != null) {
			if (undo_menu_item != null) {
				enabled = (undo_cache.size() > 0);
				if (enabled)
					undo_menu_item.setText("Undo (" + undo_cache.getLast().getValue() + ")");
				else
					undo_menu_item.setText("Undo");
				undo_menu_item.setEnabled(enabled);
			}

			if (redo_menu_item != null) {
				enabled = (redo_cache.size() > 0);
				if (enabled)
					redo_menu_item.setText("Undo (" + redo_cache.getLast().getValue() + ")");
				else
					redo_menu_item.setText("Undo");
				redo_menu_item.setEnabled(enabled);
			}
		}
	}

	/**
	 * Commit state and action
	 * 
	 * @param state
	 *            State
	 * @param action
	 *            Action
	 */
	public void commit(T state, String action) {
		if (current_state != null)
			undo_cache.add(current_state);
		current_state = new Pair<>(state, action);
		redo_cache.clear();
		updateMenuItems();
	}

	/**
	 * Undo action
	 * 
	 * @return Undo sstate
	 */
	public T undo() {
		if (undo_cache.size() > 0) {
			if (current_state != null)
				redo_cache.add(current_state);
			current_state = undo_cache.getLast();
			undo_cache.removeLast();
			updateMenuItems();
		}
		return current_state.getKey();
	}

	/**
	 * Redo action
	 * 
	 * @return Redo state
	 */
	public T redo() {
		if (redo_cache.size() > 0) {
			if (current_state != null)
				undo_cache.add(current_state);
			current_state = redo_cache.getLast();
			redo_cache.removeLast();
			updateMenuItems();
		}
		return current_state.getKey();
	}

	/**
	 * Get current state
	 * 
	 * @return Current state
	 */
	public T getCurrentState() {
		return current_state.getKey();
	}

	/**
	 * Clear undo and redo cache
	 * 
	 * @param init_state
	 *            Initial state
	 * @param action
	 *            Action
	 */
	public void clear(T init_state, String action) {
		current_state = new Pair<>(init_state, action);
		undo_cache.clear();
		redo_cache.clear();
		updateMenuItems();
	}
}
