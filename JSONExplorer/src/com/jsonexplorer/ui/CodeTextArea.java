package com.jsonexplorer.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.ScrollPaneConstants;
import java.awt.Dimension;

/**
 * Class to create a code editor
 * 
 * @author Ethem Kurt
 *
 */
public class CodeTextArea extends JPanel {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -3138299256714213785L;

	/**
	 * Lines text area
	 */
	private JTextArea lines_textArea;

	/**
	 * Text area
	 */
	private JTextArea text_textArea;

	/**
	 * Lines scroll pane
	 */
	private JScrollPane lines_scrollPane;

	/**
	 * Text scroll pane
	 */
	private JScrollPane text_scrollPane;

	/**
	 * Constructor
	 * 
	 * Create the panel.
	 */
	public CodeTextArea() {
		super();
		setLayout(new BorderLayout(0, 0));

		lines_scrollPane = new JScrollPane();
		lines_scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		lines_scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		lines_scrollPane.getViewport().addChangeListener(new ChangeListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * javax.swing.event.ChangeListener#stateChanged(javax.swing.event.
			 * ChangeEvent)
			 */
			@Override
			public void stateChanged(ChangeEvent e) {
				try {
					lines_scrollPane.getViewport().setViewPosition(text_scrollPane.getViewport().getViewPosition());
				} finally {
					//
				}
			}
		});
		add(lines_scrollPane, BorderLayout.WEST);

		lines_textArea = new JTextArea();
		lines_textArea.setEnabled(false);
		lines_textArea.setEditable(false);
		lines_textArea.setText("    1.    ");
		lines_scrollPane.setViewportView(lines_textArea);

		text_scrollPane = new JScrollPane();
		text_scrollPane.setPreferredSize(new Dimension(2, 200));
		text_scrollPane.getViewport().addChangeListener(new ChangeListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * javax.swing.event.ChangeListener#stateChanged(javax.swing.event.
			 * ChangeEvent)
			 */
			@Override
			public void stateChanged(ChangeEvent e) {
				try {
					lines_scrollPane.getViewport().setViewPosition(text_scrollPane.getViewport().getViewPosition());
				} finally {
					//
				}
			}
		});
		add(text_scrollPane, BorderLayout.CENTER);

		text_textArea = new JTextArea();
		text_textArea.getDocument().addDocumentListener(new DocumentListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * javax.swing.event.DocumentListener#changedUpdate(javax.swing.
			 * event.DocumentEvent)
			 */
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateLines();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * javax.swing.event.DocumentListener#insertUpdate(javax.swing.event
			 * .DocumentEvent)
			 */
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateLines();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * javax.swing.event.DocumentListener#removeUpdate(javax.swing.event
			 * .DocumentEvent)
			 */
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateLines();
			}

		});
		text_scrollPane.setViewportView(text_textArea);

	}

	/**
	 * Update lines
	 */
	private void updateLines() {
		int i, lines = text_textArea.getLineCount();
		StringBuilder sb = new StringBuilder();
		for (i = 1; i <= lines; i++) {
			sb.append("    ");
			sb.append(i);
			sb.append(".    \n");
		}
		lines_textArea.setText(sb.toString());
	}

	/**
	 * Get text area
	 * 
	 * @return Text area
	 */
	public JTextArea getTextArea() {
		return text_textArea;
	}

}
