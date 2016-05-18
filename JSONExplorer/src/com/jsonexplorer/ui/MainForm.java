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
	private JFrame frmJsonExplorer;

	/**
	 * Attribute split pane
	 */
	private JSplitPane attribute_splitPane;

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
	private CodeTextArea json_raw_panel_editor;

	/**
	 * Attribute configuration panel
	 */
	private AttributePanel attribute_config_panel = null;

	/**
	 * Save selected node menu item
	 */
	private JMenuItem mntmSaveSelectedNode;

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
					window.frmJsonExplorer.setVisible(true);
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
		frmJsonExplorer = new JFrame();
		frmJsonExplorer.setTitle("JSON Explorer");
		frmJsonExplorer.setBounds(100, 100, 724, 508);
		frmJsonExplorer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JSplitPane main_splitPane = new JSplitPane();
		frmJsonExplorer.getContentPane().add(main_splitPane, BorderLayout.CENTER);

		JPanel attribute_panel = new JPanel();
		main_splitPane.setRightComponent(attribute_panel);
		attribute_panel.setLayout(new BorderLayout(0, 0));

		attribute_splitPane = new JSplitPane();
		attribute_splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		attribute_panel.add(attribute_splitPane, BorderLayout.CENTER);

		json_attribute_config_label = new JLabel("Select an attribute to edit");
		json_attribute_config_label.setFont(new Font("Tahoma", Font.BOLD, 20));
		json_attribute_config_label.setHorizontalAlignment(SwingConstants.CENTER);
		attribute_splitPane.setLeftComponent(json_attribute_config_label);

		JPanel json_raw_panel = new JPanel();
		attribute_splitPane.setRightComponent(json_raw_panel);
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
				if (parseJSON(json_raw_panel_editor.getTextArea().getText()))
					undo_redo_controller.commit(root, "Parse JSON");
				else
					JOptionPane.showMessageDialog(frmJsonExplorer, "This is not valid JSON.", "Parse JSON error",
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

		json_raw_panel_editor = new CodeTextArea();
		json_raw_panel.add(json_raw_panel_editor, BorderLayout.CENTER);

		JScrollPane json_scrollPane = new JScrollPane();
		json_scrollPane.setPreferredSize(new Dimension(200, 2));
		main_splitPane.setLeftComponent(json_scrollPane);

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
				mntmSaveSelectedNode.setEnabled(false);
				if (sn != null) {
					if (sn.getUserObject() instanceof JSONInheritance) {
						ji = (JSONInheritance) sn.getUserObject();
						showAttributeConfig(ji);
						if ((ji.getValue() instanceof JSONObject) || (ji.getValue() instanceof JSONArray))
							mntmSaveSelectedNode.setEnabled(true);
					}
				}
			}
		});
		json_scrollPane.setViewportView(json_tree);

		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(json_tree, popupMenu);

		JMenuItem mntmRemoveNode = new JMenuItem("Remove node");
		mntmRemoveNode.addActionListener(new ActionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				//
			}
		});
		mntmRemoveNode.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		popupMenu.add(mntmRemoveNode);

		JMenuBar menuBar = new JMenuBar();
		frmJsonExplorer.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mntmExit.addActionListener(this);

		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {

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
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(mntmOpen);

		JSeparator mntmFileSeparator_1 = new JSeparator();
		mnFile.add(mntmFileSeparator_1);

		JMenuItem mntmSaveTree = new JMenuItem("Save Tree");
		mntmSaveTree.addActionListener(new ActionListener() {

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
		mntmSaveTree.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSaveTree);

		mntmSaveSelectedNode = new JMenuItem("Save selected node");
		mntmSaveSelectedNode.setEnabled(false);
		mntmSaveSelectedNode.addActionListener(new ActionListener() {

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
		mntmSaveSelectedNode
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnFile.add(mntmSaveSelectedNode);

		JSeparator mntmFileSeparator_2 = new JSeparator();
		mnFile.add(mntmFileSeparator_2);

		mnFile.add(mntmExit);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenuItem mntmUndo = new JMenuItem("Undo");
		mntmUndo.setEnabled(false);
		mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		mnEdit.add(mntmUndo);

		JMenuItem mntmRedo = new JMenuItem("Redo");
		mntmRedo.setEnabled(false);
		mntmRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnEdit.add(mntmRedo);

		undo_redo_controller = new UndoRedoController<>(mntmUndo, mntmRedo, copyJSONObject(root), "Init");
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
			JOptionPane.showMessageDialog(frmJsonExplorer, e.getMessage(), "Load file error",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			//
		}
		if (parseJSON(sb.toString()))
			undo_redo_controller.commit(copyJSONObject(root), "Load file");
		else
			JOptionPane.showMessageDialog(frmJsonExplorer, "This is not valid JSON.", "Parse JSON error",
					JOptionPane.ERROR_MESSAGE);
		updateView();
	}

	/**
	 * Show load file dialog
	 */
	private void showLoadFileDialog() {
		JFileChooser jfc = new JFileChooser();
		File f;
		if (jfc.showOpenDialog(frmJsonExplorer) == JFileChooser.APPROVE_OPTION) {
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
		if (jfc.showSaveDialog(frmJsonExplorer) == JFileChooser.APPROVE_OPTION) {
			f = jfc.getSelectedFile();
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
				bw.write(jsonToString(o));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frmJsonExplorer, e.getMessage(), "save file error",
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
			json_raw_panel_editor.getTextArea().setText(((JSONObject) root).toString(4));
		else if (root instanceof JSONArray)
			json_raw_panel_editor.getTextArea().setText(((JSONArray) root).toString(4));
		else
			json_raw_panel_editor.getTextArea().setText("");
	}

	/**
	 * Hide attribute configuration
	 */
	private void hideAttributeConfig() {
		attribute_splitPane.setLeftComponent(json_attribute_config_label);
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
					updateView();
					showAttributeConfig(attribute_config_panel.getJSONInheritance());
				}
			});
			attribute_splitPane.setLeftComponent(attribute_config_panel);
		}
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
		root = args.getState();
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
