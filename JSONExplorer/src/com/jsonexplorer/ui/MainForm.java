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
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.JFileChooser;

import javax.swing.JSeparator;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class MainForm implements ActionListener {

	private JFrame frmJsonExplorer;
	private JTree json_tree;
	private JSplitPane attribute_splitPane;
	private AttributePanel<?> attribute_config_panel = null;
	private Object root = new JSONObject();
	private static String load_file_name = null;

	/**
	 * Launch the application.
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

		json_tree = new JTree();
		json_tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				DefaultMutableTreeNode sn = (DefaultMutableTreeNode) json_tree.getLastSelectedPathComponent();
				if (sn != null) {
					loadAttributeConfig(sn.getUserObject());
				}
			}
		});
		main_splitPane.setLeftComponent(json_tree);

		JPanel attribute_panel = new JPanel();
		main_splitPane.setRightComponent(attribute_panel);
		attribute_panel.setLayout(new BorderLayout(0, 0));

		attribute_splitPane = new JSplitPane();
		attribute_splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		attribute_panel.add(attribute_splitPane, BorderLayout.CENTER);

		JScrollPane attribute_json_scrollPane = new JScrollPane();
		attribute_splitPane.setRightComponent(attribute_json_scrollPane);

		JLabel attribute_json_name_lbl = new JLabel("JSON attribute name - from origin");
		attribute_json_scrollPane.setColumnHeaderView(attribute_json_name_lbl);

		JTextArea attribute_json_textArea = new JTextArea();
		attribute_json_textArea.setFont(UIManager.getFont("CheckBoxMenuItem.font"));
		attribute_json_scrollPane.setViewportView(attribute_json_textArea);

		JTextArea attribute_json_lines_textArea = new JTextArea();
		attribute_json_lines_textArea.setFont(UIManager.getFont("CheckBoxMenuItem.font"));
		attribute_json_scrollPane.setRowHeaderView(attribute_json_lines_textArea);

		JMenuBar menuBar = new JMenuBar();
		frmJsonExplorer.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mntmExit.addActionListener(this);

		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showLoadFileDialog();
			}
		});
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(mntmOpen);

		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);

		JMenuItem mntmSaveTree = new JMenuItem("Save Tree");
		mntmSaveTree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showSaveFileDialog(root);
			}
		});
		mntmSaveTree.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSaveTree);

		JMenuItem mntmSaveCursor = new JMenuItem("Save Cursor");
		mntmSaveCursor
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnFile.add(mntmSaveCursor);

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		mnFile.add(mntmExit);

		if (load_file_name == null)
			updateView();
		else
			loadFile(load_file_name);
	}

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
		try {
			root = new JSONObject(sb.toString());
		} catch (JSONException e) {
			try {
				root = new JSONArray(sb.toString());
			} catch (JSONException _e) {
				root = new JSONObject();
				JOptionPane.showMessageDialog(frmJsonExplorer, "This is not a valid JSON file.", "Load file error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		updateView();
	}

	private void showLoadFileDialog() {
		JFileChooser jfc = new JFileChooser();
		File f;
		if (jfc.showOpenDialog(frmJsonExplorer) == JFileChooser.APPROVE_OPTION) {
			f = jfc.getSelectedFile();
			loadFile(f.getAbsolutePath());
		}
	}

	private void showSaveFileDialog(Object o) {
		JFileChooser jfc = new JFileChooser();
		File f;
		String l;
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

	private void appendJSONNodeView(DefaultMutableTreeNode dmtn, Object o) {
		JSONObject jo;
		JSONArray ja;
		DefaultMutableTreeNode t;
		if (o == JSONObject.NULL) {
			t = new DefaultMutableTreeNode("null");
			dmtn.add(t);
		} else if (o instanceof JSONObject) {
			jo = (JSONObject) o;
			for (String i : jo.keySet()) {
				t = new DefaultMutableTreeNode(i);
				dmtn.add(t);
				appendJSONNodeView(t, jo.get(i));
			}
		} else if (o instanceof JSONArray) {
			ja = (JSONArray) o;
			for (int i = 0, count = ja.length(); i < count; i++) {
				t = new DefaultMutableTreeNode("[" + i + "]");
				dmtn.add(t);
				appendJSONNodeView(t, ja.get(i));
			}
		} else if (o instanceof String) {
			t = new DefaultMutableTreeNode("\"" + ((String) o) + "\"");
			dmtn.add(t);
		} else if (o instanceof Integer) {
			t = new DefaultMutableTreeNode(((Integer) o).toString());
			dmtn.add(t);
		} else if (o instanceof Float) {
			t = new DefaultMutableTreeNode(((Float) o).toString());
			dmtn.add(t);
		} else if (o instanceof Double) {
			t = new DefaultMutableTreeNode(((Double) o).toString());
			dmtn.add(t);
		} else if (o instanceof BigInteger) {
			t = new DefaultMutableTreeNode(((BigInteger) o).toString());
			dmtn.add(t);
		} else if (o instanceof BigDecimal) {
			t = new DefaultMutableTreeNode(((BigDecimal) o).toString());
			dmtn.add(t);
		} else if (o instanceof Boolean) {
			t = new DefaultMutableTreeNode(((Boolean) o).toString());
			dmtn.add(t);
		}
	}

	private void updateView() {
		DefaultMutableTreeNode r = new DefaultMutableTreeNode("Root");
		appendJSONNodeView(r, root);
		((DefaultTreeModel) json_tree.getModel()).setRoot(r);
	}

	private void loadAttributeConfig(Object o) {
		// Test
		attribute_config_panel = new StringAttributePanel();
		attribute_splitPane.setLeftComponent(attribute_config_panel);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		System.exit(0);
	}

}
