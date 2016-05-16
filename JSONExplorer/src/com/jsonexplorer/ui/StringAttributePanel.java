package com.jsonexplorer.ui;

import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;

public class StringAttributePanel extends AttributePanel<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8010322718028948928L;

	private JTextArea value_textArea;
	private JTextArea lines_textArea;

	/**
	 * Create the panel.
	 */
	public StringAttributePanel() {
		super();
		GridBagLayout gridBagLayout = (GridBagLayout) getLayout();
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0 };
		gridBagLayout.columnWeights = new double[] { 1.0 };

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(scrollPane, gbc_scrollPane);

		lines_textArea = new JTextArea();
		lines_textArea.setEnabled(false);
		lines_textArea.setEditable(false);
		lines_textArea.setText("    1.    ");
		scrollPane.setRowHeaderView(lines_textArea);

		value_textArea = new JTextArea();
		value_textArea.getDocument().addDocumentListener(new DocumentListener() {

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
		scrollPane.setViewportView(value_textArea);
	}
	
	private void updateLines() {
		int i, lines = value_textArea.getLineCount();
		StringBuilder sb = new StringBuilder();
		for (i = 1; i <= lines; i++) {
			sb.append("    ");
			sb.append(i);
			sb.append(".    \n");
		}
		lines_textArea.setText(sb.toString());
	}

	@Override
	public void setJSONAttribute(String name, String o) {
		super.setJSONAttribute(name, o);
		value_textArea.setText(o);
	}

	@Override
	public String getJSONAttribute() {
		return value_textArea.getText();
	}
}
