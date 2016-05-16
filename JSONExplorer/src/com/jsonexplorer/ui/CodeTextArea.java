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

public class CodeTextArea extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3138299256714213785L;
	
	private JTextArea lines_textArea;
	
	private JTextArea text_textArea;
	
	private JScrollPane lines_scrollPane;
	
	private JScrollPane text_scrollPane;

	/**
	 * Create the panel.
	 */
	public CodeTextArea() {
		super();
		setLayout(new BorderLayout(0, 0));
		
		lines_scrollPane = new JScrollPane();
		lines_scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		lines_scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		lines_scrollPane.getViewport().addChangeListener(new ChangeListener() {
			
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
		text_scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		text_scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		text_scrollPane.getViewport().addChangeListener(new ChangeListener() {
			
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
		text_scrollPane.setViewportView(text_textArea);

	}
	
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

	public JTextArea getTextArea() {
		return text_textArea;
	}

}
