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
import javax.swing.tree.MutableTreeNode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jsonexplorer.core.JSONInheritance;

import javafx.util.Pair;

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

public class MainForm implements ActionListener {

	private JFrame frmJsonExplorer;
	
	private JSplitPane attribute_splitPane;
	
	private JTree json_tree;
	
	private JLabel json_attribute_config_label;
	
	private CodeTextArea json_raw_panel;
	
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

		JPanel attribute_panel = new JPanel();
		main_splitPane.setRightComponent(attribute_panel);
		attribute_panel.setLayout(new BorderLayout(0, 0));

		attribute_splitPane = new JSplitPane();
		attribute_splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		attribute_panel.add(attribute_splitPane, BorderLayout.CENTER);
		
		json_raw_panel = new CodeTextArea();
		attribute_splitPane.setRightComponent(json_raw_panel);
		
		json_attribute_config_label = new JLabel("Select an attribute to edit");
		json_attribute_config_label.setFont(new Font("Tahoma", Font.BOLD, 20));
		json_attribute_config_label.setHorizontalAlignment(SwingConstants.CENTER);
		attribute_splitPane.setLeftComponent(json_attribute_config_label);
		
		JScrollPane json_scrollPane = new JScrollPane();
		json_scrollPane.setPreferredSize(new Dimension(200, 2));
		main_splitPane.setLeftComponent(json_scrollPane);
		
		json_tree = new JTree();
		json_tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				DefaultMutableTreeNode sn = (DefaultMutableTreeNode) json_tree.getLastSelectedPathComponent();
				if (sn != null)
					loadAttributeConfig(sn.getUserObject());
			}
		});
		json_scrollPane.setViewportView(json_tree);

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

	private void appendJSONNodeView(DefaultMutableTreeNode dmtn) {
		JSONObject jo;
		JSONArray ja;
		DefaultMutableTreeNode t;
		JSONInheritance ji = (JSONInheritance) dmtn.getUserObject();
		Object o = ji.getValue(), to;
		if (o == JSONObject.NULL) {
			t = new DefaultMutableTreeNode(new JSONInheritance(JSONObject.NULL, ji.getKey(), ji.getParent(), JSONObject.NULL.toString()));
			dmtn.add(t);
		} else if (o instanceof JSONObject) {
			jo = (JSONObject) o;
			for (String i : jo.keySet()) {
				to = jo.get(i);
				t = new DefaultMutableTreeNode(new JSONInheritance(to, i, o, "[" + i + "]"));
				dmtn.add(t);
				appendJSONNodeView(t);
			}
		} else if (o instanceof JSONArray) {
			ja = (JSONArray) o;
			for (int i = 0, count = ja.length(); i < count; i++) {
				to = ja.get(i);
				t = new DefaultMutableTreeNode(new JSONInheritance(to, i, o, "[" + i + "]"));
				dmtn.add(t);
				appendJSONNodeView(t);
			}
		} else if ((o instanceof String) || (o instanceof Integer) || (o instanceof Float) || (o instanceof Double) || (o instanceof BigInteger) || (o instanceof BigDecimal) || (o instanceof Boolean)) {
			t = new DefaultMutableTreeNode(new JSONInheritance(o, ji.getKey(), ji.getParent(), o.toString()));
			dmtn.add(t);
		} //else if (o instanceof Integer) {
//			t = new DefaultMutableTreeNode(new JSONInheritance(o, ji.getKey(), ji.getParent(), o.toString()));
//			dmtn.add(t);
//		} else if (o instanceof Float) {
//			t = new DefaultMutableTreeNode(new JSONInheritance(o, ji.getKey(), ji.getParent(), o.toString()));
//			dmtn.add(t);
//		} else if (o instanceof Double) {
//			t = new DefaultMutableTreeNode(new JSONInheritance(o, ji.getKey(), ji.getParent(), o.toString()));
//			dmtn.add(t);
//		} else if (o instanceof BigInteger) {
//			t = new DefaultMutableTreeNode(new JSONInheritance(o, ji.getKey(), ji.getParent(), o.toString()));
//			dmtn.add(t);
//		} else if (o instanceof BigDecimal) {
//			t = new DefaultMutableTreeNode(new JSONInheritance(o, ji.getKey(), ji.getParent(), o.toString()));
//			dmtn.add(t);
//		} else if (o instanceof Boolean) {
//			t = new DefaultMutableTreeNode(new JSONInheritance(o, ji.getKey(), ji.getParent(), o.toString()));
//			dmtn.add(t);
//		}
	}

	private void updateView() {
		DefaultMutableTreeNode r = new DefaultMutableTreeNode(new JSONInheritance(root, null, null, "[.]"));
		appendJSONNodeView(r);
		((DefaultTreeModel) json_tree.getModel()).setRoot(r);
		if (root instanceof JSONObject)
			json_raw_panel.getTextArea().setText(((JSONObject)root).toString(4));
		else if (root instanceof JSONArray)
			json_raw_panel.getTextArea().setText(((JSONArray)root).toString(4));
		else
			json_raw_panel.getTextArea().setText("");
	}

	private void loadAttributeConfig(Object o) {
		JSONInheritance ji;
		Object t;
		attribute_splitPane.setLeftComponent(json_attribute_config_label);
		if (attribute_config_panel != null) {
			attribute_config_panel.removeAll();
			attribute_config_panel = null;
		}
		if (o instanceof JSONInheritance) {
			ji = (JSONInheritance)o;
			t = ji.getValue();
			if (t == JSONObject.NULL) {
				attribute_config_panel = new AttributePanel<>();
				attribute_config_panel.setJSONAttribute(ji.getKey().toString(), null);
			}
			else if (t instanceof JSONObject) {
			} else if (t instanceof JSONArray) {
			} else if (t instanceof String) {
				attribute_config_panel = new StringAttributePanel();
				((StringAttributePanel)attribute_config_panel).setJSONAttribute(ji.getKey().toString(), (String) t);
			} else if (t instanceof Integer) {
			} else if (t instanceof Float) {
			} else if (t instanceof Double) {
			} else if (t instanceof BigInteger) {
			} else if (t instanceof BigDecimal) {
			} else if (t instanceof Boolean) {
				attribute_config_panel = new BooleanAttributePanel();
				((BooleanAttributePanel)attribute_config_panel).setJSONAttribute(ji.getKey().toString(), (Boolean) t);
			}
			attribute_splitPane.setLeftComponent(attribute_config_panel);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		System.exit(0);
	}

}
