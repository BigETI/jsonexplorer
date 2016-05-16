import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.WindowConstants;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import java.awt.Insets;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

public class MainForm implements ActionListener {

	private JFrame frmJsonExplorer;
	private JTextField attribute_config_value_txt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		
		JTree json_tree = new JTree();
		main_splitPane.setLeftComponent(json_tree);
		
		JPanel attribute_panel = new JPanel();
		main_splitPane.setRightComponent(attribute_panel);
		attribute_panel.setLayout(new BorderLayout(0, 0));
		
		JSplitPane attribute_splitPane = new JSplitPane();
		attribute_splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		attribute_panel.add(attribute_splitPane, BorderLayout.CENTER);
		
		JPanel attribute_config_panel = new JPanel();
		attribute_config_panel.setPreferredSize(new Dimension(10, 210));
		attribute_splitPane.setLeftComponent(attribute_config_panel);
		GridBagLayout gbl_attribute_config_panel = new GridBagLayout();
		gbl_attribute_config_panel.columnWidths = new int[]{0, 0, 0};
		gbl_attribute_config_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_attribute_config_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_attribute_config_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		attribute_config_panel.setLayout(gbl_attribute_config_panel);
		
		JLabel attribute_config_name_lbl = new JLabel("JSON attribute name");
		GridBagConstraints gbc_attribute_config_name_lbl = new GridBagConstraints();
		gbc_attribute_config_name_lbl.anchor = GridBagConstraints.EAST;
		gbc_attribute_config_name_lbl.insets = new Insets(0, 0, 5, 5);
		gbc_attribute_config_name_lbl.gridx = 0;
		gbc_attribute_config_name_lbl.gridy = 0;
		attribute_config_panel.add(attribute_config_name_lbl, gbc_attribute_config_name_lbl);
		
		JSeparator attribute_config_separator_1 = new JSeparator();
		GridBagConstraints gbc_attribute_config_separator_1 = new GridBagConstraints();
		gbc_attribute_config_separator_1.insets = new Insets(0, 0, 5, 5);
		gbc_attribute_config_separator_1.gridx = 0;
		gbc_attribute_config_separator_1.gridy = 1;
		attribute_config_panel.add(attribute_config_separator_1, gbc_attribute_config_separator_1);
		
		JComboBox attribute_config_type_comboBox = new JComboBox();
		attribute_config_type_comboBox.setToolTipText("Attribute type");
		GridBagConstraints gbc_attribute_config_type_comboBox = new GridBagConstraints();
		gbc_attribute_config_type_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_attribute_config_type_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_attribute_config_type_comboBox.gridx = 0;
		gbc_attribute_config_type_comboBox.gridy = 2;
		attribute_config_panel.add(attribute_config_type_comboBox, gbc_attribute_config_type_comboBox);
		
		attribute_config_value_txt = new JTextField();
		attribute_config_value_txt.setEnabled(false);
		attribute_config_value_txt.setText("test");
		GridBagConstraints gbc_attribute_config_value_txt = new GridBagConstraints();
		gbc_attribute_config_value_txt.insets = new Insets(0, 0, 5, 0);
		gbc_attribute_config_value_txt.fill = GridBagConstraints.HORIZONTAL;
		gbc_attribute_config_value_txt.gridx = 1;
		gbc_attribute_config_value_txt.gridy = 2;
		attribute_config_panel.add(attribute_config_value_txt, gbc_attribute_config_value_txt);
		attribute_config_value_txt.setColumns(10);
		
		JSeparator attribute_config_separator_2 = new JSeparator();
		GridBagConstraints gbc_attribute_config_separator_2 = new GridBagConstraints();
		gbc_attribute_config_separator_2.insets = new Insets(0, 0, 5, 5);
		gbc_attribute_config_separator_2.gridx = 0;
		gbc_attribute_config_separator_2.gridy = 3;
		attribute_config_panel.add(attribute_config_separator_2, gbc_attribute_config_separator_2);
		
		JCheckBox attribute_config_boolean_value_chckbx = new JCheckBox("Active");
		attribute_config_boolean_value_chckbx.setEnabled(false);
		GridBagConstraints gbc_attribute_config_boolean_value_chckbx = new GridBagConstraints();
		gbc_attribute_config_boolean_value_chckbx.anchor = GridBagConstraints.WEST;
		gbc_attribute_config_boolean_value_chckbx.gridx = 1;
		gbc_attribute_config_boolean_value_chckbx.gridy = 4;
		attribute_config_panel.add(attribute_config_boolean_value_chckbx, gbc_attribute_config_boolean_value_chckbx);
		
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
		mntmExit.addActionListener(this);
		
		mnFile.add(mntmExit);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		System.exit(0);
	}

}
