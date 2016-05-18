package com.jsonexplorer.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTree;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JFileChooser;

import javax.swing.JSeparator;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jsonexplorer.core.JSONInheritance;
import com.jsonexplorer.core.UndoRedoController;
import com.jsonexplorer.event.AttributePanelEventArgs;
import com.jsonexplorer.event.IAttributePanelListener;
import com.jsonexplorer.event.IUndoRedoControllerListener;
import com.jsonexplorer.event.UndoRedoControllerEventArgs;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.awt.event.InputEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class for main entry amd application window
 * 
 * @author Ethem Kurt
 *
 */
public class MainForm implements ActionListener, IUndoRedoControllerListener<Object> {

	/**
	 * Application frame
	 */
	private JFrame json_explorer_frame;

	/**
	 * Attribute split pane
	 */
	private JSplitPane attribute_split_pane;

	/**
	 * JSON tree view
	 */
	private JTree json_tree;

	/**
	 * JSON attribute configuration label (placeholder)
	 */
	private JLabel json_attribute_config_label;

	/**
	 * Raw JSON editor
	 */
	private CodeTextArea json_raw_code_text_editor;

	/**
	 * Attribute configuration panel
	 */
	private AttributePanel attribute_config_panel = null;

	/**
	 * Save selected node menu item
	 */
	private JMenuItem file_save_selected_node_menu_item;

	/**
	 * Root JSON object/array
	 */
	private Object root = new JSONObject();

	private UndoRedoController<Object> undo_redo_controller = null;

	/**
	 * Command line load file name
	 */
	private static String load_file_name = null;

	/**
	 * Main entry
	 * 
	 * Launch the application.
	 * 
	 * @param args
	 *            Command line arguments
	 */
	public static void main(String[] args) {
		if (args.length > 0)
			load_file_name = args[0];
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm window = new MainForm();
					window.json_explorer_frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor
	 * 
	 * Create the application.
	 */
	public MainForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		json_explorer_frame = new JFrame();
		json_explorer_frame.setTitle("JSON Explorer");
		json_explorer_frame.setBounds(100, 100, 724, 508);
		json_explorer_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JSplitPane main_split_pane = new JSplitPane();
		json_explorer_frame.getContentPane().add(main_split_pane, BorderLayout.CENTER);

		JPanel attribute_panel = new JPanel();
		main_split_pane.setRightComponent(attribute_panel);
		attribute_panel.setLayout(new BorderLayout(0, 0));

		attribute_split_pane = new JSplitPane();
		attribute_split_pane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		attribute_panel.add(attribute_split_pane, BorderLayout.CENTER);

		json_attribute_config_label = new JLabel("Select an attribute to edit");
		json_attribute_config_label.setFont(new Font("Tahoma", Font.BOLD, 20));
		json_attribute_config_label.setHorizontalAlignment(SwingConstants.CENTER);
		attribute_split_pane.setLeftComponent(json_attribute_config_label);

		JPanel json_raw_panel = new JPanel();
		attribute_split_pane.setRightComponent(json_raw_panel);
		json_raw_panel.setLayout(new BorderLayout(0, 0));

		JPanel json_raw_buttons_panel = new JPanel();
		json_raw_panel.add(json_raw_buttons_panel, BorderLayout.SOUTH);
		GridBagLayout gbl_json_raw_buttons_panel = new GridBagLayout();
		gbl_json_raw_buttons_panel.columnWidths = new int[] { 0, 0 };
		gbl_json_raw_buttons_panel.rowHeights = new int[] { 0, 0, 0 };
		gbl_json_raw_buttons_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_json_raw_buttons_panel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		json_raw_buttons_panel.setLayout(gbl_json_raw_buttons_panel);

		JButton parse_json_button = new JButton("Parse JSON");
		parse_json_button.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				hideAttributeConfig();
				if (parseJSON(json_raw_code_text_editor.getTextArea().getText()))
					undo_redo_controller.commit(root, "Parse JSON");
				else
					JOptionPane.showMessageDialog(json_explorer_frame, "This is not valid JSON.", "Parse JSON error",
							JOptionPane.ERROR_MESSAGE);
				updateView();
			}
		});
		GridBagConstraints gbc_parse_json_button = new GridBagConstraints();
		gbc_parse_json_button.fill = GridBagConstraints.HORIZONTAL;
		gbc_parse_json_button.insets = new Insets(0, 0, 5, 0);
		gbc_parse_json_button.gridx = 0;
		gbc_parse_json_button.gridy = 0;
		json_raw_buttons_panel.add(parse_json_button, gbc_parse_json_button);

		JButton revert_json_button = new JButton("Revert JSON");
		revert_json_button.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				hideAttributeConfig();
				updateView();
			}
		});
		GridBagConstraints gbc_revert_json_button = new GridBagConstraints();
		gbc_revert_json_button.fill = GridBagConstraints.HORIZONTAL;
		gbc_revert_json_button.gridx = 0;
		gbc_revert_json_button.gridy = 1;
		json_raw_buttons_panel.add(revert_json_button, gbc_revert_json_button);

		json_raw_code_text_editor = new CodeTextArea();
		json_raw_panel.add(json_raw_code_text_editor, BorderLayout.CENTER);

		JScrollPane json_scroll_pane = new JScrollPane();
		json_scroll_pane.setPreferredSize(new Dimension(200, 2));
		main_split_pane.setLeftComponent(json_scroll_pane);

		json_tree = new JTree();
		json_tree.addTreeSelectionListener(new TreeSelectionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.
			 * event.TreeSelectionEvent)
			 */
			public void valueChanged(TreeSelectionEvent arg0) {
				DefaultMutableTreeNode sn = (DefaultMutableTreeNode) json_tree.getLastSelectedPathComponent();
				JSONInheritance ji;
				file_save_selected_node_menu_item.setEnabled(false);
				if (sn != null) {
					if (sn.getUserObject() instanceof JSONInheritance) {
						ji = (JSONInheritance) sn.getUserObject();
						showAttributeConfig(ji);
						if ((ji.getValue() instanceof JSONObject) || (ji.getValue() instanceof JSONArray))
							file_save_selected_node_menu_item.setEnabled(true);
					}
				}
			}
		});
		json_scroll_pane.setViewportView(json_tree);

		JPopupMenu json_tree_popup_menu = new JPopupMenu();
		addPopup(json_tree, json_tree_popup_menu);

		JMenuItem popup_remove_node_menu_item = new JMenuItem("Remove node");
		popup_remove_node_menu_item.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				removeSelectedNode();
			}
		});

		JMenuItem popup_copy_menu_item = new JMenuItem("Copy");
		popup_copy_menu_item.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				copySelectedNode();
			}
		});
		popup_copy_menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		json_tree_popup_menu.add(popup_copy_menu_item);

		JMenuItem popup_paste_menu_item = new JMenuItem("Paste");
		popup_paste_menu_item.setEnabled(false);
		popup_paste_menu_item.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				pasteSelectedNode();
			}
		});
		popup_paste_menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		json_tree_popup_menu.add(popup_paste_menu_item);

		JSeparator popup_separator_1 = new JSeparator();
		json_tree_popup_menu.add(popup_separator_1);
		popup_remove_node_menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		json_tree_popup_menu.add(popup_remove_node_menu_item);

		JMenu popup_add_node_menu = new JMenu("Add node...");
		popup_add_node_menu.setEnabled(false);
		json_tree_popup_menu.add(popup_add_node_menu);

		JMenuItem popup_add_json_object_menu_item = new JMenuItem("JSON object");
		popup_add_json_object_menu_item.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				addJSONObjectToSelection();
			}
		});
		popup_add_node_menu.add(popup_add_json_object_menu_item);

		JMenuItem popup_add_json_array_menu_item = new JMenuItem("JSON array");
		popup_add_json_array_menu_item.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				addJSONArrayToSelection();
			}
		});
		popup_add_node_menu.add(popup_add_json_array_menu_item);

		JMenuItem popup_add_json_attribute_menu_item = new JMenuItem("JSON attribute");
		popup_add_json_attribute_menu_item.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				addJSONAttributeToSelection();
			}
		});
		popup_add_node_menu.add(popup_add_json_attribute_menu_item);

		JMenuBar menu_bar = new JMenuBar();
		json_explorer_frame.setJMenuBar(menu_bar);

		JMenu file_menu = new JMenu("File");
		menu_bar.add(file_menu);

		JMenuItem file_exit_menu_item = new JMenuItem("Exit");
		file_exit_menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		file_exit_menu_item.addActionListener(this);

		JMenuItem file_open_menu_item = new JMenuItem("Open");
		file_open_menu_item.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				showLoadFileDialog();
			}
		});
		file_open_menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		file_menu.add(file_open_menu_item);

		JSeparator file_separator_1 = new JSeparator();
		file_menu.add(file_separator_1);

		JMenuItem file_save_tree_menu_item = new JMenuItem("Save Tree");
		file_save_tree_menu_item.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				showSaveFileDialog(root);
			}
		});
		file_save_tree_menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		file_menu.add(file_save_tree_menu_item);

		file_save_selected_node_menu_item = new JMenuItem("Save selected node");
		file_save_selected_node_menu_item.setEnabled(false);
		file_save_selected_node_menu_item.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode sn = (DefaultMutableTreeNode) json_tree.getLastSelectedPathComponent();
				JSONInheritance ji;
				if (sn != null) {
					if (sn.getUserObject() instanceof JSONInheritance) {
						ji = (JSONInheritance) sn.getUserObject();
						if ((ji.getValue() instanceof JSONObject) || (ji.getValue() instanceof JSONArray))
							showSaveFileDialog(ji.getValue());
					}
				}

			}
		});
		file_save_selected_node_menu_item
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		file_menu.add(file_save_selected_node_menu_item);

		JSeparator file_separator_2 = new JSeparator();
		file_menu.add(file_separator_2);

		file_menu.add(file_exit_menu_item);

		JMenu edit_menu = new JMenu("Edit");
		menu_bar.add(edit_menu);

		JMenuItem edit_undo_menu_item = new JMenuItem("Undo");
		edit_undo_menu_item.setEnabled(false);
		edit_undo_menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		edit_menu.add(edit_undo_menu_item);

		JMenuItem edit_redo_menu_item = new JMenuItem("Redo");
		edit_redo_menu_item.setEnabled(false);
		edit_redo_menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		edit_menu.add(edit_redo_menu_item);

		undo_redo_controller = new UndoRedoController<>(edit_undo_menu_item, edit_redo_menu_item, copyJSONObject(root), "Init");

		JSeparator edit_separator_1 = new JSeparator();
		edit_menu.add(edit_separator_1);

		JMenuItem edit_remove_node_menu_item = new JMenuItem("Remove node");
		edit_remove_node_menu_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeSelectedNode();
			}
		});

		JMenuItem edit_copy_menu_item = new JMenuItem("Copy");
		edit_copy_menu_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copySelectedNode();
			}
		});
		edit_copy_menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		edit_menu.add(edit_copy_menu_item);

		JMenuItem edit_paste_menu_item = new JMenuItem("Paste");
		edit_paste_menu_item.setEnabled(false);
		edit_paste_menu_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pasteSelectedNode();
			}
		});
		edit_paste_menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		edit_menu.add(edit_paste_menu_item);

		JSeparator edit_separator_2 = new JSeparator();
		edit_menu.add(edit_separator_2);
		edit_remove_node_menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		edit_menu.add(edit_remove_node_menu_item);

		JMenu edit_add_node_menu = new JMenu("Add node...");
		edit_add_node_menu.setEnabled(false);
		edit_menu.add(edit_add_node_menu);

		JMenuItem edit_add_json_object_menu_item = new JMenuItem("JSON object");
		edit_add_json_object_menu_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addJSONObjectToSelection();
			}
		});
		edit_add_node_menu.add(edit_add_json_object_menu_item);

		JMenuItem edit_add_json_array_menu_item = new JMenuItem("JSON array");
		edit_add_json_array_menu_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addJSONArrayToSelection();
			}
		});
		edit_add_node_menu.add(edit_add_json_array_menu_item);

		JMenuItem edit_add_json_attribute_menu_item = new JMenuItem("JSON attribute");
		edit_add_json_attribute_menu_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addJSONAttributeToSelection();
			}
		});
		edit_add_node_menu.add(edit_add_json_attribute_menu_item);
		undo_redo_controller.addListener(this);

		if (load_file_name == null)
			updateView();
		else
			loadFile(load_file_name);
	}

	/**
	 * Parse JSON to vizualization
	 * 
	 * @param json
	 *            JSON
	 * @return Success
	 */
	private boolean parseJSON(String json) {
		boolean ret = false;
		try {
			root = new JSONObject(json);
			ret = true;
		} catch (JSONException e) {
			try {
				root = new JSONArray(json);
				ret = true;
			} catch (JSONException _e) {
				// root = new JSONObject();
			}
		}
		return ret;
	}

	/**
	 * Load JSON file
	 * 
	 * @param file_name
	 *            File name
	 */
	private void loadFile(String file_name) {
		String l;
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
			while ((l = br.readLine()) != null) {
				sb.append(l);
				sb.append("\n");
			}
		} catch (IOException e) {
			sb = new StringBuilder("{}");
			JOptionPane.showMessageDialog(json_explorer_frame, e.getMessage(), "Load file error",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			//
		}
		if (parseJSON(sb.toString()))
			undo_redo_controller.commit(copyJSONObject(root), "Load file");
		else
			JOptionPane.showMessageDialog(json_explorer_frame, "This is not valid JSON.", "Parse JSON error",
					JOptionPane.ERROR_MESSAGE);
		updateView();
	}

	/**
	 * Show load file dialog
	 */
	private void showLoadFileDialog() {
		JFileChooser jfc = new JFileChooser();
		File f;
		if (jfc.showOpenDialog(json_explorer_frame) == JFileChooser.APPROVE_OPTION) {
			f = jfc.getSelectedFile();
			loadFile(f.getAbsolutePath());
		}
	}

	/**
	 * Show save file dialog
	 * 
	 * @param o
	 *            JSON object/array portion
	 */
	private void showSaveFileDialog(Object o) {
		JFileChooser jfc = new JFileChooser();
		File f;
		if (jfc.showSaveDialog(json_explorer_frame) == JFileChooser.APPROVE_OPTION) {
			f = jfc.getSelectedFile();
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
				bw.write(jsonToString(o));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(json_explorer_frame, e.getMessage(), "save file error",
						JOptionPane.ERROR_MESSAGE);
			} finally {
				//
			}
		}
	}

	/**
	 * JSON object/array to string
	 * 
	 * @param o
	 *            JSON object/array
	 * @return JSON
	 */
	private String jsonToString(Object o) {
		String ret = null;
		JSONObject jo;
		JSONArray ja;
		if (o instanceof JSONObject) {
			jo = (JSONObject) o;
			ret = jo.toString(4);
		} else if (o instanceof JSONArray) {
			ja = (JSONArray) o;
			ret = ja.toString(4);
		}
		return ret;
	}

	/**
	 * Append JSON node to ttree view
	 * 
	 * @param dmtn
	 */
	private void appendJSONNodeView(DefaultMutableTreeNode dmtn) {
		JSONObject jo;
		JSONArray ja;
		DefaultMutableTreeNode t;
		JSONInheritance ji = (JSONInheritance) dmtn.getUserObject();
		Object o = ji.getValue(), to;
		if (o == JSONObject.NULL) {
			t = new DefaultMutableTreeNode(
					new JSONInheritance(JSONObject.NULL, ji.getKey(), ji.getParent(), JSONObject.NULL.toString()));
			dmtn.add(t);
		} else if (o instanceof JSONObject) {
			jo = (JSONObject) o;
			for (String i : jo.keySet()) {
				to = jo.get(i);
				t = new DefaultMutableTreeNode(new JSONInheritance(to, i, ji, "[" + i + "]"));
				dmtn.add(t);
				appendJSONNodeView(t);
			}
		} else if (o instanceof JSONArray) {
			ja = (JSONArray) o;
			for (int i = 0, count = ja.length(); i < count; i++) {
				to = ja.get(i);
				t = new DefaultMutableTreeNode(new JSONInheritance(to, i, ji, "[" + i + "]"));
				dmtn.add(t);
				appendJSONNodeView(t);
			}
		} else if ((o instanceof String) || (o instanceof Integer) || (o instanceof Float) || (o instanceof Double)
				|| (o instanceof BigInteger) || (o instanceof BigDecimal) || (o instanceof Boolean)) {
			t = new DefaultMutableTreeNode(new JSONInheritance(o, ji.getKey(), ji.getParent(), o.toString()));
			dmtn.add(t);
		}
	}

	/**
	 * Update view from root
	 */
	private void updateView() {
		DefaultMutableTreeNode r = new DefaultMutableTreeNode(new JSONInheritance(root, null, null, "[.]"));
		appendJSONNodeView(r);
		((DefaultTreeModel) json_tree.getModel()).setRoot(r);
		if (root instanceof JSONObject)
			json_raw_code_text_editor.getTextArea().setText(((JSONObject) root).toString(4));
		else if (root instanceof JSONArray)
			json_raw_code_text_editor.getTextArea().setText(((JSONArray) root).toString(4));
		else
			json_raw_code_text_editor.getTextArea().setText("");
	}

	/**
	 * Hide attribute configuration
	 */
	private void hideAttributeConfig() {
		attribute_split_pane.setLeftComponent(json_attribute_config_label);
		if (attribute_config_panel != null) {
			attribute_config_panel.getNotifier().removeListeners();
			attribute_config_panel.removeAll();
			attribute_config_panel = null;
		}
	}

	/**
	 * Copy JSON object
	 * 
	 * @return New root reference
	 */
	private Object copyJSONObject(Object o) {
		Object ret = null;
		if (o instanceof JSONObject) {
			ret = new JSONObject(((JSONObject) o).toString(0));
		} else if (o instanceof JSONArray) {
			ret = new JSONArray(((JSONArray) o).toString(0));
		} else
			ret = new JSONObject();
		return ret;
	}

	/**
	 * Show attribute configuration by JSON inheritance
	 * 
	 * @param o
	 *            JSONInheritance
	 */
	private void showAttributeConfig(JSONInheritance ji) {
		Object t;
		hideAttributeConfig();
		t = ji.getValue();
		if (t == JSONObject.NULL)
			attribute_config_panel = new AttributePanel(ji);
		else if (t instanceof String)
			attribute_config_panel = new StringAttributePanel(ji);
		else if ((t instanceof Integer) || (t instanceof BigInteger))
			attribute_config_panel = new IntegerAttributePanel(ji);
		else if ((t instanceof Float) || (t instanceof Double) || (t instanceof BigDecimal))
			attribute_config_panel = new DecimalAttributePanel(ji);
		else if (t instanceof Boolean)
			attribute_config_panel = new BooleanAttributePanel(ji);
		if (attribute_config_panel != null) {
			attribute_config_panel.getNotifier().addListener(new IAttributePanelListener() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see com.jsonexplorer.event.IAttributePanelListener#
				 * onBeforeSaveChanges(com.jsonexplorer.event.
				 * AttributePanelEventArgs)
				 */
				@Override
				public void onBeforeSaveChanges(AttributePanelEventArgs args) {
					// root = copyJSONObject(root);
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * com.jsonexplorer.event.IAttributePanelListener#onSaveChanges(
				 * com.jsonexplorer.event.AttributePanelEventArgs)
				 */
				@Override
				public void onSaveChanges(AttributePanelEventArgs args) {
					undo_redo_controller.commit(copyJSONObject(root), "Change value");
					updateView();
					showAttributeConfig(attribute_config_panel.getJSONInheritance());
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see com.jsonexplorer.event.IAttributePanelListener#
				 * onBeforeChangeType(com.jsonexplorer.event.
				 * AttributePanelEventArgs)
				 */
				@Override
				public void onBeforeChangeType(AttributePanelEventArgs args) {
					// root = copyJSONObject(root);
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * com.jsonexplorer.event.IAttributePanelListener#onChangeType(
				 * com.jsonexplorer.event.AttributePanelEventArgs)
				 */
				@Override
				public void onChangeType(AttributePanelEventArgs args) {
					undo_redo_controller.commit(copyJSONObject(root), "Change type");
					updateView();
					showAttributeConfig(attribute_config_panel.getJSONInheritance());
				}
			});
			attribute_split_pane.setLeftComponent(attribute_config_panel);
		}
	}

	/**
	 * Remove selected node
	 */
	private void removeSelectedNode() {
		DefaultMutableTreeNode sn = (DefaultMutableTreeNode) json_tree.getLastSelectedPathComponent();
		JSONInheritance ji;
		JSONObject jo;
		JSONArray ja;
		String jok;
		int jak;
		if (sn != null) {
			if (sn.getUserObject() instanceof JSONInheritance) {
				ji = (JSONInheritance) sn.getUserObject();
				if (ji.getParent() == null) {
					root = new JSONObject();
					undo_redo_controller.commit(copyJSONObject(root), "Remove node");
					updateView();
				} else {
					if (ji.getParent().getValue() instanceof JSONObject) {
						jo = (JSONObject) ji.getParent().getValue();
						if (ji.getKey() instanceof String) {
							jok = (String) ji.getKey();
							if (jo.has(jok)) {
								jo.remove(jok);
								undo_redo_controller.commit(copyJSONObject(root), "Remove node");
								updateView();
							}
						}
					} else if (ji.getParent().getValue() instanceof JSONArray) {
						ja = (JSONArray) ji.getParent().getValue();
						if (ji.getKey() instanceof Integer) {
							jak = ((Integer) ji.getKey()).intValue();
							if ((ja.length() > jak) && (jak >= 0)) {
								ja.remove(jak);
								undo_redo_controller.commit(copyJSONObject(root), "Remove node");
								updateView();
							}
						}
					}
				}
			}
		}
	}

	private void setStringClipboard(String content) {
		StringSelection stringSelection = new StringSelection(content);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, new ClipboardOwner() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.datatransfer.ClipboardOwner#lostOwnership(java.awt.
			 * datatransfer.Clipboard, java.awt.datatransfer.Transferable)
			 */
			@Override
			public void lostOwnership(Clipboard clipboard, Transferable contents) {
				//
			}
		});
	}

	/**
	 * Copy selected node
	 */
	private void copySelectedNode() {
		DefaultMutableTreeNode sn = (DefaultMutableTreeNode) json_tree.getLastSelectedPathComponent();
		JSONInheritance ji;
		if (sn != null) {
			if (sn.getUserObject() instanceof JSONInheritance) {
				ji = (JSONInheritance) sn.getUserObject();
				if (ji.getValue() instanceof JSONObject)
					setStringClipboard(((JSONObject) ji.getValue()).toString(4));
				else if (ji.getValue() instanceof JSONArray)
					setStringClipboard(((JSONArray) ji.getValue()).toString(4));
				else
					setStringClipboard(ji.getValue().toString());
			}
		}
	}

	/**
	 * Paste selected node
	 */
	private void pasteSelectedNode() {
		//
	}

	/**
	 * Add new JSON object to selection
	 */
	private void addJSONObjectToSelection() {
		//
	}

	/**
	 * Add new JSON array to selection
	 */
	private void addJSONArrayToSelection() {
		//
	}

	/**
	 * Add new JSON attribute to selection
	 */
	private void addJSONAttributeToSelection() {
		//
	}

	/**
	 * Add popup
	 * 
	 * @param component
	 *            Component
	 * @param popup
	 *            Popup
	 */
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		System.exit(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jsonexplorer.event.IUndoRedoControllerListener#onCommit(com.
	 * jsonexplorer.event.UndoRedoControllerEventArgs)
	 */
	@Override
	public void onCommit(UndoRedoControllerEventArgs<Object> args) {
		//
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jsonexplorer.event.IUndoRedoControllerListener#onUndo(com.
	 * jsonexplorer.event.UndoRedoControllerEventArgs)
	 */
	@Override
	public void onUndo(UndoRedoControllerEventArgs<Object> args) {
		root = copyJSONObject(args.getState());
		updateView();
		hideAttributeConfig();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jsonexplorer.event.IUndoRedoControllerListener#onRedo(com.
	 * jsonexplorer.event.UndoRedoControllerEventArgs)
	 */
	@Override
	public void onRedo(UndoRedoControllerEventArgs<Object> args) {
		root = copyJSONObject(args.getState());
		updateView();
		hideAttributeConfig();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jsonexplorer.event.IUndoRedoControllerListener#onClear(com.
	 * jsonexplorer.event.UndoRedoControllerEventArgs)
	 */
	@Override
	public void onClear(UndoRedoControllerEventArgs<Object> args) {
		root = copyJSONObject(args.getState());
		updateView();
		hideAttributeConfig();
	}
}
