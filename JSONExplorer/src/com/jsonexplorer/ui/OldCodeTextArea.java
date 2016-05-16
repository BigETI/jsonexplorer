package com.jsonexplorer.ui;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.ScrollPaneConstants;

public class OldCodeTextArea extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1141451967149945198L;

	private JTextArea lines_textArea;

	private JTextArea text_textArea;

	/**
	 * Create the panel.
	 */
	public OldCodeTextArea() {
		super();
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		getViewport().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				
			}
		});

		lines_textArea = new JTextArea();
		lines_textArea.setText("    1.    ");
		lines_textArea.setEnabled(false);
		lines_textArea.setEditable(false);
		setRowHeaderView(lines_textArea);

		text_textArea = new JTextArea();
		text_textArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateLines();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateLines();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateLines();
			}

		});
		setViewportView(text_textArea);
	}

	private void updateLines() {
		int i, lines = text_textArea.getLineCount() + 1;
		StringBuilder sb = new StringBuilder();
		for (i = 1; i <= lines; i++) {
			sb.append("    ");
			sb.append(i);
			sb.append(".    \n");
		}
		lines_textArea.setText(sb.toString());
	}

	public JTextArea getTextArea() {
		return text_textArea;
	}

}
