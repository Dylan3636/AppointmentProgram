package com.omada.gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

import com.omada.appointment.Appointments;
import com.omada.database.Format;
import com.omada.database.Table;
import com.omada.main.Main;
import com.omada.patient.ContactNumberType;
import com.omada.patient.Contacts;
import com.omada.patient.Insurance_Company;
import com.omada.patient.Patient;
import com.omada.patient.Patient.PatientException;
import com.omada.patient.PatientType;
import com.omada.gui.AppointmentGUI.NameDocument;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.text.WordUtils;

import javax.swing.border.TitledBorder;
import java.awt.Cursor;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.DefaultCellEditor;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollBar;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.DefaultComboBoxModel;
import java.awt.Component;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.UIManager;

public class PatientGUI {
	public static JLayeredPane patientPane;
	public static JLayeredPane newPatientPane;
	public static JLayeredPane patientProfilePane;
	public static JLayeredPane previousPane;
	public static JFrame mainFrame;
	public static JButton btnApplyChanges; 
	static JTable table;
    public static HashMap<Integer,HashMap<String,String>> appointmentTableHash;
    public static HashMap<Integer,HashMap<String,String>> patientContactTableHash;
    public static HashMap<Integer,HashMap<String,String>> emergencyContactTableHash;
    
    
    public static HashMap<String,HashMap<Integer,HashMap<String,String>>> newPatientTableHash;
    public static ArrayList<Appointments> patientAppointments;
	public static Patient currentPatient;
	private static JTextField firstNameField;
	private static JTextField lastNameField;
	private static JTextField ageField;
	private static JComboBox patientTypeBox;
	private static JTextField allergiesField;
	private static JTextField emergencyField;
	private static JTextField emailField;
	private static JTable emergencyContactTable;
	private static JTable allergiesTable;
	private static JLabel lblAge;
	private static JTable contactNoTable;
	public static String title;
	protected static String sex;
	protected static String insurance_Company;
	protected static String stage;
	private static JTextField emailProfileField;
	private static JComponent lblNoApps;
	public static ArrayList<Contacts> patientContacts;
	public static Patient patient;
	public static JTextArea infoChangeArea;
	public static PatientTableChangeListener appointmentTableListener;
	private static JButton findPatient;
	static JComboBox<String> fsBox;
	
	public static void patientChoose(JFrame mainFrame,JLayeredPane layeredPane){
		patientPane= new JLayeredPane();
		mainFrame.getContentPane().add(patientPane, BorderLayout.CENTER);
		PatientGUI.mainFrame = mainFrame;		

		JButton newPatient = new JButton("New Patient");

		findPatient = new JButton("Find Patient");
		findPatient.addActionListener(new PatientButtonListener());
		JLabel lblPatients = new JLabel("Patients");
		lblPatients.setFont(new Font("SansSerif", Font.PLAIN, 36));
		lblPatients.setHorizontalAlignment(SwingConstants.CENTER);
		lblPatients.setLabelFor(patientPane);
		lblPatients.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		JButton back = backButton(layeredPane, patientPane);
		layeredPane.setVisible(false);
		newPatient.addActionListener(new PatientButtonListener(mainFrame,patientPane));
		GroupLayout gl_patientPane = new GroupLayout(patientPane);
		gl_patientPane.setHorizontalGroup(
			gl_patientPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_patientPane.createSequentialGroup()
					.addGroup(gl_patientPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_patientPane.createSequentialGroup()
							.addGap(48)
							.addComponent(newPatient, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE)
							.addGap(455)
							.addComponent(findPatient, GroupLayout.PREFERRED_SIZE, 293, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_patientPane.createSequentialGroup()
							.addGap(398)
							.addComponent(lblPatients, GroupLayout.PREFERRED_SIZE, 373, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(145, Short.MAX_VALUE))
		);
		gl_patientPane.setVerticalGroup(
			gl_patientPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_patientPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblPatients, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
					.addGap(208)
					.addGroup(gl_patientPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(newPatient, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE)
						.addComponent(findPatient, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)))
		);
		patientPane.setLayout(gl_patientPane);
				
	}
	/**
	 * @throws Exception 
	 * @wbp.parser.entryPoint
	 */

	public static void newPatient(JFrame mainFrame,JLayeredPane patientPane){
		newPatientTableHash=new HashMap<String,HashMap<Integer,HashMap<String,String>>>();

		newPatientPane = new JLayeredPane();
		mainFrame.getContentPane().add(newPatientPane, BorderLayout.CENTER);
		newPatientPane.setVisible(true);
		GridBagLayout gbl_newPatientPane = new GridBagLayout();
		gbl_newPatientPane.columnWidths = new int[]{138, 95, 201, 180, 202, 172, 120, 154, 0};
		gbl_newPatientPane.rowHeights = new int[]{64, 60, 70, 58, 56, 83, 177, 28, 33, 34, 0};
		gbl_newPatientPane.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_newPatientPane.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		newPatientPane.setLayout(gbl_newPatientPane);
		
		JLabel lblEnterPatientInfo = new JLabel("Enter Patient Info");
		lblEnterPatientInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterPatientInfo.setFont(new Font("SansSerif", Font.PLAIN, 36));
		lblEnterPatientInfo.setHorizontalTextPosition(SwingConstants.CENTER);
		GridBagConstraints gbc_lblEnterPatientInfo = new GridBagConstraints();
		gbc_lblEnterPatientInfo.fill = GridBagConstraints.BOTH;
		gbc_lblEnterPatientInfo.insets = new Insets(0, 0, 5, 5);
		gbc_lblEnterPatientInfo.gridwidth = 5;
		gbc_lblEnterPatientInfo.gridx = 2;
		gbc_lblEnterPatientInfo.gridy = 0;
		newPatientPane.add(lblEnterPatientInfo, gbc_lblEnterPatientInfo);
		
		JLabel lblTitle = new JLabel("Title");
		lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 20));
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.anchor = GridBagConstraints.EAST;
		gbc_lblTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitle.gridx = 0;
		gbc_lblTitle.gridy = 2;
		newPatientPane.add(lblTitle, gbc_lblTitle);
		
		JComboBox titleBox = new JComboBox();
		
		titleBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox bx = (JComboBox) e.getSource();
				 title = (String)bx.getSelectedItem();
			}
		});
		titleBox.setModel(new DefaultComboBoxModel(new String[] {"Mr.", "Ms.", "Mrs.","Dr."}));
		GridBagConstraints gbc_comboBox_3 = new GridBagConstraints();
		gbc_comboBox_3.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_3.gridx = 1;
		gbc_comboBox_3.gridy = 2;
		newPatientPane.add(titleBox, gbc_comboBox_3);
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setHorizontalAlignment(SwingConstants.CENTER);
		lblFirstName.setFont(new Font("SansSerif", Font.PLAIN, 20));
		GridBagConstraints gbc_lblFirstName = new GridBagConstraints();
		gbc_lblFirstName.anchor = GridBagConstraints.EAST;
		gbc_lblFirstName.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstName.gridx = 2;
		gbc_lblFirstName.gridy = 2;
		newPatientPane.add(lblFirstName, gbc_lblFirstName);
		lblFirstName.setLabelFor(firstNameField);
		
		firstNameField = new JTextField();
		firstNameField.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_firstNameField_1 = new GridBagConstraints();
		gbc_firstNameField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_firstNameField_1.insets = new Insets(0, 0, 5, 5);
		gbc_firstNameField_1.gridx = 3;
		gbc_firstNameField_1.gridy = 2;
		newPatientPane.add(firstNameField, gbc_firstNameField_1);
		
		
		JLabel lblMiddleName = new JLabel("Middle Name");
		lblMiddleName.setPreferredSize(new Dimension(100, 16));
		lblMiddleName.setHorizontalTextPosition(SwingConstants.LEFT);
		lblMiddleName.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		lblMiddleName.setBounds(new Rectangle(0, 0, 80, 30));
		lblMiddleName.setMinimumSize(new Dimension(65, 16));
		lblMiddleName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMiddleName.setFont(new Font("SansSerif", Font.PLAIN, 20));
		
		GridBagConstraints gbc_lblMiddleName = new GridBagConstraints();
		gbc_lblMiddleName.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblMiddleName.insets = new Insets(0, 0, 5, 5);
		gbc_lblMiddleName.gridx = 4;
		gbc_lblMiddleName.gridy = 2;
		newPatientPane.add(lblMiddleName, gbc_lblMiddleName);
		
		JTextField middleNameField = new JTextField();
		middleNameField.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_middleNameField = new GridBagConstraints();
		gbc_middleNameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_middleNameField.insets = new Insets(0, 0, 5, 5);
		gbc_middleNameField.gridx = 5;
		gbc_middleNameField.gridy = 2;
		lblMiddleName.setLabelFor(middleNameField);
		newPatientPane.add(middleNameField, gbc_middleNameField);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setHorizontalAlignment(SwingConstants.CENTER);
		lblLastName.setFont(new Font("SansSerif", Font.PLAIN, 20));
		GridBagConstraints gbc_lblLastName = new GridBagConstraints();
		gbc_lblLastName.anchor = GridBagConstraints.EAST;
		gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
		gbc_lblLastName.gridx = 6;
		gbc_lblLastName.gridy = 2;
		newPatientPane.add(lblLastName, gbc_lblLastName);
		lblLastName.setLabelFor(lastNameField);
		
		lastNameField = new JTextField();
		lastNameField.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_lastNameField_1 = new GridBagConstraints();
		gbc_lastNameField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_lastNameField_1.insets = new Insets(0, 0, 5, 0);
		gbc_lastNameField_1.gridx = 7;
		gbc_lastNameField_1.gridy = 2;
		newPatientPane.add(lastNameField, gbc_lastNameField_1);
		
		JLabel lblSex = new JLabel("Sex");
		lblSex.setFont(new Font("SansSerif", Font.PLAIN, 20));
		GridBagConstraints gbc_lblSex = new GridBagConstraints();
		gbc_lblSex.anchor = GridBagConstraints.EAST;
		gbc_lblSex.insets = new Insets(0, 0, 5, 5);
		gbc_lblSex.gridx = 0;
		gbc_lblSex.gridy = 3;
		newPatientPane.add(lblSex, gbc_lblSex);
		
		JComboBox sexBox = new JComboBox();
		sexBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox bx = (JComboBox) e.getSource();
				 sex = (String)bx.getSelectedItem();
			}
		});
		sexBox.setModel(new DefaultComboBoxModel(new String[] {"Male", "Female"}));
		GridBagConstraints gbc_comboBox_2 = new GridBagConstraints();
		gbc_comboBox_2.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_2.gridx = 1;
		gbc_comboBox_2.gridy = 3;
		newPatientPane.add(sexBox, gbc_comboBox_2);
		
		JLabel lblDob = new JLabel("D.O.B.");
		lblDob.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDob.setFont(new Font("SansSerif", Font.PLAIN, 20));
		GridBagConstraints gbc_lblDob = new GridBagConstraints();
		gbc_lblDob.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblDob.insets = new Insets(0, 0, 5, 5);
		gbc_lblDob.gridx = 2;
		gbc_lblDob.gridy = 3;
		newPatientPane.add(lblDob, gbc_lblDob);
		
		JFormattedTextField dobField = new JFormattedTextField(Format.DATEFORMAT);
		dobField.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		dobField.setToolTipText("dd/mm/yyyy\r\n");
		dobField.setColumns(10);
		GridBagConstraints gbc_dobField = new GridBagConstraints();
		gbc_dobField.fill = GridBagConstraints.HORIZONTAL;
		gbc_dobField.insets = new Insets(0, 0, 5, 5);
		gbc_dobField.gridx = 3;
		gbc_dobField.gridy = 3;
		newPatientPane.add(dobField, gbc_dobField);
		dobField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(Character.isAlphabetic((e.getKeyChar()))){
					e.consume();
				}
			}
		});		
		
		JLabel lblFirstVisitDate = new JLabel("First Visit Date");
		lblFirstVisitDate.setFont(new Font("SansSerif", Font.PLAIN, 20));
		GridBagConstraints gbc_lblFirstVisitDate = new GridBagConstraints();
		gbc_lblFirstVisitDate.anchor = GridBagConstraints.EAST;
		gbc_lblFirstVisitDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstVisitDate.gridx = 4;
		gbc_lblFirstVisitDate.gridy = 3;
		newPatientPane.add(lblFirstVisitDate, gbc_lblFirstVisitDate);
		
		JFormattedTextField fstVisitDateField = new JFormattedTextField(Format.DATEFORMAT);
		fstVisitDateField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(Character.isAlphabetic((e.getKeyChar()))){
					e.consume();
				}
			}
		});
		lblFirstVisitDate.setLabelFor(fstVisitDateField);
		fstVisitDateField.setToolTipText("dd/mm/yyyy\r\n");
		fstVisitDateField.setColumns(10);
		GridBagConstraints gbc_fstVisitDateField = new GridBagConstraints();
		gbc_fstVisitDateField.fill = GridBagConstraints.HORIZONTAL;
		gbc_fstVisitDateField.insets = new Insets(0, 0, 5, 5);
		gbc_fstVisitDateField.gridx = 5;
		gbc_fstVisitDateField.gridy = 3;
		newPatientPane.add(fstVisitDateField, gbc_fstVisitDateField);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("SansSerif", Font.PLAIN, 20));
		GridBagConstraints gbc_lblEmail = new GridBagConstraints();
		gbc_lblEmail.anchor = GridBagConstraints.EAST;
		gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmail.gridx = 6;
		gbc_lblEmail.gridy = 3;
		newPatientPane.add(lblEmail, gbc_lblEmail);
		lblEmail.setLabelFor(emailField);
		
		emailField = new JTextField();
		emailField.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_emailField = new GridBagConstraints();
		gbc_emailField.insets = new Insets(0, 0, 5, 0);
		gbc_emailField.fill = GridBagConstraints.HORIZONTAL;
		gbc_emailField.gridx = 7;
		gbc_emailField.gridy = 3;
		newPatientPane.add(emailField, gbc_emailField);
		emailField.setColumns(10);
		JComboBox tableBox = new JComboBox(ContactNumberType.values());
		
		JLabel lblContactNo = new JLabel("Contact No.");
		lblContactNo.setFont(new Font("SansSerif", Font.PLAIN, 20));
		GridBagConstraints gbc_lblContactNo = new GridBagConstraints();
		gbc_lblContactNo.anchor = GridBagConstraints.EAST;
		gbc_lblContactNo.insets = new Insets(0, 0, 5, 5);
		gbc_lblContactNo.gridx = 3;
		gbc_lblContactNo.gridy = 4;
		newPatientPane.add(lblContactNo, gbc_lblContactNo);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 4;
		gbc_scrollPane.gridy = 4;
		newPatientPane.add(scrollPane, gbc_scrollPane);
		
		contactNoTable = new JTable();
		contactNoTable.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null},
					{null, null, null},
				},
				new String[] {
					"Contact Name", "Contact No.", "Contact No. Type"
				}
			) {
				Class[] columnTypes = new Class[] {
					Object.class, Object.class, String.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
			contactNoTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(tableBox));
			contactNoTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			scrollPane.setViewportView(contactNoTable);
			contactNoTable.getModel().addTableModelListener(new newPatientTableListener("Contact_No"));

			JButton btnAddConRow = new JButton("Add Row");
			btnAddConRow.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					DefaultTableModel mod1 = (DefaultTableModel) contactNoTable.getModel();
					mod1.addRow(new Object[]{null});
				}
			});
			scrollPane.setRowHeaderView(btnAddConRow);

			
		JLabel lblAllergies = new JLabel("Allergies");
		lblAllergies.setFont(new Font("SansSerif", Font.PLAIN, 20));
		GridBagConstraints gbc_lblAllergies = new GridBagConstraints();
		gbc_lblAllergies.anchor = GridBagConstraints.EAST;
		gbc_lblAllergies.insets = new Insets(0, 0, 5, 5);
		gbc_lblAllergies.gridx = 6;
		gbc_lblAllergies.gridy = 4;
		newPatientPane.add(lblAllergies, gbc_lblAllergies);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_2.gridx = 7;
		gbc_scrollPane_2.gridy = 4;
		newPatientPane.add(scrollPane_2, gbc_scrollPane_2);
		
		allergiesTable = new JTable();
		allergiesTable.setModel(new DefaultTableModel(
			new Object[][] {
				{null},
				{null},
			},
			new String[] {
				"Allergy"
			}
		));
		allergiesTable.getModel().addTableModelListener(new newPatientTableListener("Allergies"));
		scrollPane_2.setViewportView(allergiesTable);
		
		JButton btnAddRo = new JButton("Add Row");
		btnAddRo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel mod = (DefaultTableModel) allergiesTable.getModel();
				mod.addRow(new Object[]{null});
			}
		});
		btnAddRo.setFont(new Font("SansSerif", Font.PLAIN, 10));
		btnAddRo.setActionCommand("Add Row");
		scrollPane_2.setRowHeaderView(btnAddRo);
		
		JLabel lblStage = new JLabel("Patient Type");
		lblStage.setFont(new Font("SansSerif", Font.PLAIN, 20));
		lblStage.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblStage = new GridBagConstraints();
		gbc_lblStage.anchor = GridBagConstraints.EAST;
		gbc_lblStage.insets = new Insets(0, 0, 5, 5);
		gbc_lblStage.gridx = 0;
		gbc_lblStage.gridy = 5;
		newPatientPane.add(lblStage, gbc_lblStage);
		
		JComboBox stageBox = new JComboBox(PatientType.values());
		stage = stageBox.getItemAt(0).toString();
		System.out.println(stage);
		stageBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox bx = (JComboBox) e.getSource();
				 stage = bx.getSelectedItem().toString();
			}
		});
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_1.gridx = 1;
		gbc_comboBox_1.gridy = 5;
		newPatientPane.add(stageBox, gbc_comboBox_1);
		
		JLabel lblEmergencyContact = new JLabel("Emergency Contact");
		lblEmergencyContact.setFont(new Font("SansSerif", Font.PLAIN, 20));
		GridBagConstraints gbc_lblEmergencyContact = new GridBagConstraints();
		gbc_lblEmergencyContact.anchor = GridBagConstraints.EAST;
		gbc_lblEmergencyContact.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmergencyContact.gridx = 3;
		gbc_lblEmergencyContact.gridy = 5;
		newPatientPane.add(lblEmergencyContact, gbc_lblEmergencyContact);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridwidth = 2;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 4;
		gbc_scrollPane_1.gridy = 5;
		newPatientPane.add(scrollPane_1, gbc_scrollPane_1);
		
		emergencyContactTable = new JTable();
		emergencyContactTable.setCellSelectionEnabled(true);
		emergencyContactTable.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null},
				{null, null, null},
			},
			new String[] {
					"Contact Name", "Contact No.", "Contact No. Type"			}
		) {
			Class[] columnTypes = new Class[] {
				Object.class, Object.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		emergencyContactTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(tableBox));
		emergencyContactTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		scrollPane_1.setViewportView(emergencyContactTable);
		
		emergencyContactTable.getModel().addTableModelListener(new newPatientTableListener("Emergency_Contact"));
		
		JButton btnAddRow = new JButton("Add Row");
		btnAddRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel mod1 = (DefaultTableModel) emergencyContactTable.getModel();
				mod1.addRow(new Object[]{null});
			}
		});
		scrollPane_1.setRowHeaderView(btnAddRow);
		
		JLabel lblInsuranceCompany_1 = new JLabel("Insurance Company");
		lblInsuranceCompany_1.setFont(new Font("SansSerif", Font.PLAIN, 20));
		GridBagConstraints gbc_lblInsuranceCompany_1 = new GridBagConstraints();
		gbc_lblInsuranceCompany_1.anchor = GridBagConstraints.EAST;
		gbc_lblInsuranceCompany_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblInsuranceCompany_1.gridx = 6;
		gbc_lblInsuranceCompany_1.gridy = 5;
		newPatientPane.add(lblInsuranceCompany_1, gbc_lblInsuranceCompany_1);
		
		JComboBox insuranceCompanyBox = new JComboBox(Insurance_Company.getInsurance_Companies().toArray(new String[0]));
		
		insuranceCompanyBox.setEditable(true);
		insuranceCompanyBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox bx = (JComboBox) e.getSource();
				 insurance_Company = (String)bx.getSelectedItem();
			}
		});

		GridBagConstraints gbc_insuranceCompanyBox = new GridBagConstraints();
		gbc_insuranceCompanyBox.insets = new Insets(0, 0, 5, 5);
		gbc_insuranceCompanyBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_insuranceCompanyBox.gridx = 7;
		gbc_insuranceCompanyBox.gridy = 5;
		newPatientPane.add(insuranceCompanyBox, gbc_insuranceCompanyBox);
		JButton addPatient = new JButton("Add Patient");
		addPatient.setHorizontalTextPosition(SwingConstants.CENTER);
		GridBagConstraints gbc_addPatient = new GridBagConstraints();
		gbc_addPatient.anchor = GridBagConstraints.NORTH;
		gbc_addPatient.fill = GridBagConstraints.HORIZONTAL;
		gbc_addPatient.insets = new Insets(0, 0, 5, 5);
		gbc_addPatient.gridwidth = 3;
		gbc_addPatient.gridx = 3;
		gbc_addPatient.gridy = 8;
		newPatientPane.add(addPatient, gbc_addPatient);
		
		
		addPatient.addActionListener(new ActionListener(){{
			
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			HashMap<String,String> patientAtt = new HashMap<String, String>(){{
				
				put(Table.NAME,Format.nameFormat(firstNameField.getText(), middleNameField.getText(), lastNameField.getText()));
				put(Format.columnNameFormat(lblFirstName.getText()),firstNameField.getText()); put(Format.columnNameFormat(lblMiddleName.getText()),middleNameField.getText()); put(Format.columnNameFormat(lblLastName.getText()),lastNameField.getText()); 
				put(Format.columnNameFormat(lblFirstVisitDate.getText()),fstVisitDateField.getText());
				put(Format.columnNameFormat(lblDob.getText()),dobField.getText());
				put(Format.columnNameFormat(Table.TITLE),titleBox.getSelectedItem().toString()); put(Format.columnNameFormat(lblEmail.getText()),emailField.getText());  
				put(Format.columnNameFormat(Table.SEX),sexBox.getSelectedItem().toString());
				System.out.println(stage);
				put(Format.columnNameFormat(Table.PATIENT_TYPE),stage);
				
			}};
			
			boolean good = true;
			try{
				Patient pat = new Patient(patientAtt);
				HashMap<Integer,HashMap<String,String>> contactHash = newPatientTableHash.get("Contact_No");
				try{
				for(int i =0; i<newPatientTableHash.get("Contact_No").size();i++){
					Contacts conP = new Contacts(contactHash.get(i),pat.getID(),"Patient");
					conP.commit();
				}}
				catch(NullPointerException e){
//					JOptionPane.showMessageDialog(new JFrame(), e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
				try{
				HashMap<Integer,HashMap<String,String>> emergencyHash = newPatientTableHash.get("Emergency_Contact");
				for(int i =0; i<newPatientTableHash.get("Emergency_Contact").size();i++){
					Contacts emCon = new Contacts(emergencyHash.get(i),pat.getID(),"Emergency");
					emCon.commit();
				}}
				catch(NullPointerException e){
//					JOptionPane.showMessageDialog(new JFrame(), e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
				try{
				if(!Insurance_Company.getInsurance_Companies().contains(insurance_Company)||insurance_Company!=null){
					Insurance_Company.addInsuranceCompany(insurance_Company);
				}}
				catch(NullPointerException e){
//					JOptionPane.showMessageDialog(new JFrame(), e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
				pat.commit();
				
				PatientGUI.newPatientPane.setVisible(false);
				PatientGUI.patientPane.setVisible(true);
				PatientGUI.previousPane=PatientGUI.newPatientPane;

			}catch(NullPointerException e){
//				JOptionPane.showMessageDialog(new JFrame(), e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				good = false;
			}catch(PatientException e){
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
				good = false;
			}
			if(good){
				JOptionPane.showMessageDialog(new JFrame(), "Successfully added Patient");
			}
		}});
		
		JLabel lblDateFornat = new JLabel("Date Fornat: dd/mm/yyyy");
		lblDateFornat.setFont(new Font("SansSerif", Font.PLAIN, 20));
		GridBagConstraints gbc_lblDateFornat = new GridBagConstraints();
		gbc_lblDateFornat.anchor = GridBagConstraints.EAST;
		gbc_lblDateFornat.fill = GridBagConstraints.VERTICAL;
		gbc_lblDateFornat.insets = new Insets(0, 0, 0, 5);
		gbc_lblDateFornat.gridwidth = 2;
		gbc_lblDateFornat.gridx = 0;
		gbc_lblDateFornat.gridy = 9;
		newPatientPane.add(lblDateFornat, gbc_lblDateFornat);
		patientPane.setVisible(false);
		JButton back = backButton(patientPane, newPatientPane);
		
	}

	protected static int fs;
	
	

	public static void patientProfile(Patient patient){
		PatientGUI.patient = patient;
		btnApplyChanges = new JButton("Apply Changes");
		appointmentTableHash=new HashMap<>();
		appointmentTableListener = new PatientTableChangeListener(patient,"Appointments",appointmentTableHash);
		
		PatientGUI.btnApplyChanges.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				HashMap<String,String> up = PatientProfileChangeListener.getUp();
				if(arg0.getActionCommand().equalsIgnoreCase("Apply Changes"))
				if(!up.isEmpty()){
					if(up.containsKey(Table.INSURANCE_COMPANY)){
						if(!Insurance_Company.getInsurance_Companies().contains(up.get(Table.INSURANCE_COMPANY))){
							Insurance_Company.addInsuranceCompany(up.get(Table.INSURANCE_COMPANY));
						}else{PatientGUI.patient.updatePatient(up);}
					}else{
					PatientGUI.patient.updatePatient(up);
					}
					PatientProfileChangeListener.refreshUp();
					}
			};});
		btnApplyChanges.setVisible(false);
		
		String name = patient.getAttributes().get(Table.NAME);
		patientProfilePane=new JLayeredPane();
		ArrayList<Object[]> data = new ArrayList<>();
		HashMap<String,String> appAtt = null;
		boolean noApps=false;
		try{
		patientAppointments = Patient.getPatientAppointments(name);
		
		for(Appointments app:patientAppointments){
			
			appAtt = (HashMap<String, String>) app.getAttributes().clone();
			
			Integer hour = Integer.parseInt(appAtt.get("Hour"));
			appAtt.remove("Hour");
			Integer minute= Integer.parseInt(appAtt.get("Minute"));
			appAtt.remove("Minute");
			appAtt.put("Start_Time", Format.timeFormat(hour, minute));
			appAtt.put("Free Slots", Format.timeSlotFormat(appAtt.get("Start_Time"), appAtt.get("Duration")));
			appAtt.remove("Start_Time");
			data.add(appAtt.values().toArray(new Object[0]));
			
//			appAtt.put(Table.PAYMENT_AMOUNT, app.getPayment().getPayment().toString());
		}

		
		
		} catch(Exception e){
			noApps=true;
			e.printStackTrace();
			
		}
		patientProfilePane.setVisible(true);
		GridBagLayout gbl_patientProfilePane = new GridBagLayout();
		gbl_patientProfilePane.columnWidths = new int[]{107, 199, 170, 307, 186, 316, 109};
		gbl_patientProfilePane.rowHeights = new int[]{118, 40, 35, 41, 53, 28, 169, 28, 0};
		gbl_patientProfilePane.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
		gbl_patientProfilePane.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		patientProfilePane.setLayout(gbl_patientProfilePane);
				//		JFrame test = new JFrame();
				//		test.getContentPane().setLayout(null);
						
		JLabel lblPatientProfile = new JLabel("Patient Profile");
		lblPatientProfile.setFont(new Font("SansSerif", Font.PLAIN, 25));
		lblPatientProfile.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblPatientProfile = new GridBagConstraints();
		gbc_lblPatientProfile.gridwidth = 5;
		gbc_lblPatientProfile.fill = GridBagConstraints.BOTH;
		gbc_lblPatientProfile.insets = new Insets(0, 0, 5, 5);
		gbc_lblPatientProfile.gridx = 1;
		gbc_lblPatientProfile.gridy = 0;
		patientProfilePane.add(lblPatientProfile, gbc_lblPatientProfile);
		
		JLabel lblName = new JLabel("First Name");
		lblName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblName.setFont(new Font("SansSerif", Font.PLAIN, 20));
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 1;
		patientProfilePane.add(lblName, gbc_lblName);
		lblName.setLabelFor(firstNameField);
		
		
		firstNameField = new JTextField(patient.getAttributes().get(Table.FIRST_NAME));
		firstNameField.setName(Table.FIRST_NAME);
		firstNameField.addActionListener(new PatientProfileChangeListener());
		GridBagConstraints gbc_firstNameField = new GridBagConstraints();
		gbc_firstNameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_firstNameField.insets = new Insets(0, 0, 5, 5);
		gbc_firstNameField.gridx = 1;
		gbc_firstNameField.gridy = 1;
		patientProfilePane.add(firstNameField, gbc_firstNameField);
		firstNameField.setColumns(10);
		firstNameField.setVisible(true);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblLastName.setFont(new Font("SansSerif", Font.PLAIN, 20));
		GridBagConstraints gbc_lblLastName = new GridBagConstraints();
		gbc_lblLastName.anchor = GridBagConstraints.EAST;
		gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
		gbc_lblLastName.gridx = 2;
		gbc_lblLastName.gridy = 1;
		patientProfilePane.add(lblLastName, gbc_lblLastName);
				lastNameField = new JTextField(patient.getAttributes().get(Table.LAST_NAME));
				lastNameField.setName(Table.LAST_NAME);
				lastNameField.addActionListener(new PatientProfileChangeListener());
				lastNameField.setColumns(10);
				GridBagConstraints gbc_lastNameField = new GridBagConstraints();
				gbc_lastNameField.fill = GridBagConstraints.HORIZONTAL;
				gbc_lastNameField.insets = new Insets(0, 0, 5, 5);
				gbc_lastNameField.gridx = 3;
				gbc_lastNameField.gridy = 1;
				patientProfilePane.add(lastNameField, gbc_lastNameField);
				
				lblAge = new JLabel("Age");
				lblAge.setFont(new Font("SansSerif", Font.PLAIN, 20));
				GridBagConstraints gbc_lblAge = new GridBagConstraints();
				gbc_lblAge.insets = new Insets(0, 0, 5, 5);
				gbc_lblAge.anchor = GridBagConstraints.EAST;
				gbc_lblAge.gridx = 4;
				gbc_lblAge.gridy = 1;
				patientProfilePane.add(lblAge, gbc_lblAge);
				
				ageField = new JTextField();
				ageField.setColumns(10);
				try{
				ageField.setText(patient.getAge().toString());
				}catch(NullPointerException n){
				}
				ageField.setEditable(false);
				GridBagConstraints gbc_ageField = new GridBagConstraints();
				gbc_ageField.fill = GridBagConstraints.HORIZONTAL;
				gbc_ageField.insets = new Insets(0, 0, 5, 5);
				gbc_ageField.gridx = 5;
				gbc_ageField.gridy = 1;
				patientProfilePane.add(ageField, gbc_ageField);
				
						
						JLabel lblEmail = new JLabel("Email");
						lblEmail.setHorizontalAlignment(SwingConstants.TRAILING);
						lblEmail.setFont(new Font("SansSerif", Font.PLAIN, 20));
						GridBagConstraints gbc_lblEmail = new GridBagConstraints();
						gbc_lblEmail.anchor = GridBagConstraints.EAST;
						gbc_lblEmail.fill = GridBagConstraints.VERTICAL;
						gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
						gbc_lblEmail.gridx = 0;
						gbc_lblEmail.gridy = 2;
						patientProfilePane.add(lblEmail, gbc_lblEmail);
						
						emailProfileField = new JTextField(patient.getAttributes().get(Table.EMAIL));
						emailProfileField.setName(Table.EMAIL);
						emailProfileField.addActionListener(new PatientProfileChangeListener());
						GridBagConstraints gbc_emailProfileField = new GridBagConstraints();
						gbc_emailProfileField.insets = new Insets(0, 0, 5, 5);
						gbc_emailProfileField.fill = GridBagConstraints.HORIZONTAL;
						gbc_emailProfileField.gridx = 1;
						gbc_emailProfileField.gridy = 2;
						patientProfilePane.add(emailProfileField, gbc_emailProfileField);
						emailProfileField.setColumns(10);
				
				
						
						JLabel contactNumberLabel = new JLabel("Contact No.");
						contactNumberLabel.setHorizontalAlignment(SwingConstants.TRAILING);
						contactNumberLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
						GridBagConstraints gbc_contactNumberLabel = new GridBagConstraints();
						gbc_contactNumberLabel.fill = GridBagConstraints.BOTH;
						gbc_contactNumberLabel.insets = new Insets(0, 0, 5, 5);
						gbc_contactNumberLabel.gridx = 2;
						gbc_contactNumberLabel.gridy = 2;
						patientProfilePane.add(contactNumberLabel, gbc_contactNumberLabel);
		
						
						ArrayList<Object[]> pData = new ArrayList<>();
						patientContacts = new ArrayList<>();
						try{
							patientContactTableHash=new HashMap<>();
							HashMap<String,ArrayList<String>> query = patient.getContactInfo("Patient");
							if(query.get(Table.ContactName)==null){
								throw(new Exception ("No Contact info"));
							}
							for (int i =0;i<query.get(Table.ContactName).size();i++){
								ArrayList<String> row = new ArrayList<>();
								String[] keys = query.keySet().toArray(new String[0]);
								HashMap<String,String> contactAtts = new HashMap<>();
								Arrays.sort(keys);
								Integer id = null;
								for(String s:keys){	
									if(s.equalsIgnoreCase(Table.PATIENT_ID)){
										id = Integer.parseInt(query.get(s).get(i));
									}else{
									row.add(query.get(s).get(i));
									contactAtts.put(s, query.get(s).get(i));
									}
									if(id!=null){
									patientContacts.add(new Contacts(contactAtts,id,"Patient"));
									}
									}
								pData.add(row.toArray(new Object[0]));
							}
						}catch(Exception e){
							if(!e.getMessage().equals("No Contact info")){
							Main.log.doLogging(e.getMessage(), "severe");
							e.printStackTrace();
							}
						}
							JComboBox tableBox = new JComboBox<Object>(ContactNumberType.values());
				
				JTable contactPatNoTable = new JTable();
				contactPatNoTable.setModel(new DefaultTableModel(
						pData.toArray(new Object[0][0]) ,
						new String[] {
							"Contact Name", "Contact No.", "Contact No. Type"
						}
					) {
						Class[] columnTypes = new Class[] {
							Object.class, Object.class, String.class
						};
						public Class getColumnClass(int columnIndex) {
							return columnTypes[columnIndex];
						}
					});
				JButton btnAddRow = new JButton("Add Row");
				btnAddRow.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						DefaultTableModel mod1 = (DefaultTableModel) contactPatNoTable.getModel();
						mod1.addRow(new Object[]{null});
					}
				});

				contactPatNoTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(tableBox));
				contactPatNoTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);							
				contactPatNoTable.getModel().addTableModelListener(new PatientTableChangeListener(patient,"Patient",patientContactTableHash));
				
				JScrollPane contactScrollPane = new JScrollPane(contactPatNoTable);
				contactScrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
				GridBagConstraints gbc_contactScrollPane = new GridBagConstraints();
				gbc_contactScrollPane.insets = new Insets(0, 0, 5, 5);
				gbc_contactScrollPane.fill = GridBagConstraints.BOTH;
				gbc_contactScrollPane.gridx = 3;
				gbc_contactScrollPane.gridy = 2;
				//							contactScrollPane.setViewportView();
				
				patientProfilePane.add(contactScrollPane, gbc_contactScrollPane);
				contactScrollPane.setRowHeaderView(btnAddRow);

				
				JLabel lblEmergencyName = new JLabel("Emergency Name/No.");
				lblEmergencyName.setHorizontalAlignment(SwingConstants.TRAILING);
				lblEmergencyName.setFont(new Font("SansSerif", Font.PLAIN, 20));
				GridBagConstraints gbc_lblEmergencyName = new GridBagConstraints();
				gbc_lblEmergencyName.anchor = GridBagConstraints.EAST;
				gbc_lblEmergencyName.fill = GridBagConstraints.VERTICAL;
				gbc_lblEmergencyName.insets = new Insets(0, 0, 5, 5);
				gbc_lblEmergencyName.gridx = 4;
				gbc_lblEmergencyName.gridy = 2;
				patientProfilePane.add(lblEmergencyName, gbc_lblEmergencyName);
		
				
				ArrayList<Object[]> eData = new ArrayList<>();
				try{
					HashMap<String,ArrayList<String>> query = patient.getContactInfo("Emergency");
					if(query.get(Table.ContactName)==null){
						throw(new Exception ("No Contact info"));
					}
					for (int i =0;i<query.get(Table.ContactName).size();i++){
						ArrayList<String> row = new ArrayList<>();
						String[] keys = query.keySet().toArray(new String[0]);
						Arrays.sort(keys);
						for(String s:keys){	
							row.add(query.get(s).get(i));
						}
						eData.add(row.toArray(new Object[0]));
					}
				}catch(Exception e){
					if(!e.getMessage().equals("No Contact info")){
						Main.log.doLogging(e.getMessage(), "severe");
						e.printStackTrace();
					}
				}
				JTable contactEmNoTable = new JTable();
		contactEmNoTable.setModel(new DefaultTableModel(
				eData.toArray(new Object[0][0]) ,
				new String[] {
					"Contact Name", "Contact No.", "Contact No. Type"
				}
			) {
				Class[] columnTypes = new Class[] {
					Object.class, Object.class, String.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
				JButton btnAddEmRow = new JButton("Add Row");
				btnAddEmRow.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						DefaultTableModel mod1 = (DefaultTableModel) contactEmNoTable.getModel();
						mod1.addRow(new Object[]{null});
					}
				});
						contactEmNoTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(tableBox));
				contactEmNoTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);							
				
				JScrollPane contactEmScrollPane = new JScrollPane(contactEmNoTable);
				emergencyContactTableHash=new HashMap<>();
				contactEmNoTable.getModel().addTableModelListener(new PatientTableChangeListener(patient,"Emergency",emergencyContactTableHash));
				contactEmScrollPane.setRowHeaderView(btnAddEmRow);

				contactEmScrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
				GridBagConstraints gbc_contactEmScrollPane = new GridBagConstraints();
				gbc_contactEmScrollPane.insets = new Insets(0, 0, 5, 5);
				gbc_contactEmScrollPane.fill = GridBagConstraints.BOTH;
				gbc_contactEmScrollPane.gridx = 5;
				gbc_contactEmScrollPane.gridy = 2;
				//							contactScrollPane.setViewportView();
				patientProfilePane.add(contactEmScrollPane, gbc_contactEmScrollPane);

		
				
				JLabel lblPatientType = new JLabel("Patient Type");
				lblPatientType.setHorizontalAlignment(SwingConstants.TRAILING);
				lblPatientType.setFont(new Font("SansSerif", Font.PLAIN, 20));
				GridBagConstraints gbc_lblPatientType = new GridBagConstraints();
				gbc_lblPatientType.fill = GridBagConstraints.BOTH;
				gbc_lblPatientType.insets = new Insets(0, 0, 5, 5);
				gbc_lblPatientType.gridx = 0;
				gbc_lblPatientType.gridy = 3;
				patientProfilePane.add(lblPatientType, gbc_lblPatientType);
		
		patientTypeBox = new JComboBox(PatientType.values());
		patientTypeBox.setSelectedItem(patient.getPatientType());
		System.out.println(patient.getPatientType());
		patientTypeBox.setName("Patient_Type");
		GridBagConstraints gbc_patientTypeField = new GridBagConstraints();
		gbc_patientTypeField.fill = GridBagConstraints.HORIZONTAL;
		gbc_patientTypeField.insets = new Insets(0, 0, 5, 5);
		gbc_patientTypeField.gridx = 1;
		gbc_patientTypeField.gridy = 3;
		patientTypeBox.addActionListener(new PatientProfileChangeListener(){
			
		});
		patientProfilePane.add(patientTypeBox, gbc_patientTypeField);
				
				
				JLabel lblAllergies = new JLabel("Allergies");
				lblAllergies.setHorizontalAlignment(SwingConstants.TRAILING);
				lblAllergies.setFont(new Font("SansSerif", Font.PLAIN, 20));
				GridBagConstraints gbc_lblAllergies = new GridBagConstraints();
				gbc_lblAllergies.fill = GridBagConstraints.BOTH;
				gbc_lblAllergies.insets = new Insets(0, 0, 5, 5);
				gbc_lblAllergies.gridx = 2;
				gbc_lblAllergies.gridy = 3;
				patientProfilePane.add(lblAllergies, gbc_lblAllergies);
				
				allergiesField = new JTextField(patient.getAttributes().get(Table.ALLERGIES));
				allergiesField.setColumns(10);
				GridBagConstraints gbc_allergiesField = new GridBagConstraints();
				gbc_allergiesField.fill = GridBagConstraints.HORIZONTAL;
				gbc_allergiesField.insets = new Insets(0, 0, 5, 5);
				gbc_allergiesField.gridx = 3;
				gbc_allergiesField.gridy = 3;
				patientProfilePane.add(allergiesField, gbc_allergiesField);
		
				
				JLabel lblInsuranceCompany = new JLabel("Insurance Company");
				lblInsuranceCompany.setHorizontalAlignment(SwingConstants.TRAILING);
				lblInsuranceCompany.setFont(new Font("SansSerif", Font.PLAIN, 20));
				GridBagConstraints gbc_lblInsuranceCompany = new GridBagConstraints();
				gbc_lblInsuranceCompany.anchor = GridBagConstraints.EAST;
				gbc_lblInsuranceCompany.fill = GridBagConstraints.VERTICAL;
				gbc_lblInsuranceCompany.insets = new Insets(0, 0, 5, 5);
				gbc_lblInsuranceCompany.gridx = 4;
				gbc_lblInsuranceCompany.gridy = 3;
				patientProfilePane.add(lblInsuranceCompany, gbc_lblInsuranceCompany);
		

		patientProfilePane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		mainFrame.getContentPane().add(patientProfilePane, null);
		
		System.out.println(patient.getAttributes().get(Table.LAST_NAME+1));
		backButton(previousPane,patientProfilePane);
		
		JComboBox<Object> insuranceCompanyBox = new JComboBox<Object>(Insurance_Company.getInsurance_Companies().toArray(new String[0]));
		insuranceCompanyBox.setSelectedItem(patient.getAttributes().get(Table.INSURANCE_COMPANY));
		insuranceCompanyBox.setName(Table.INSURANCE_COMPANY);
		insuranceCompanyBox.setEditable(true);
		insuranceCompanyBox.addActionListener(new PatientProfileChangeListener());
		insuranceCompanyBox.setVisible(true);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 5;
		gbc_comboBox.gridy = 3;
		patientProfilePane.add(insuranceCompanyBox, gbc_comboBox);
		
				lblNoApps = new JLabel("No Recent Appointments");
				lblNoApps.setVisible(false);
				
				JScrollPane scrollPane_1 = new JScrollPane();
				GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
				gbc_scrollPane_1.gridwidth = 5;
				gbc_scrollPane_1.gridheight = 2;
				gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
				gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
				gbc_scrollPane_1.gridx = 1;
				gbc_scrollPane_1.gridy = 4;
				patientProfilePane.add(scrollPane_1, gbc_scrollPane_1);
				
				infoChangeArea = new JTextArea();
				scrollPane_1.setViewportView(infoChangeArea);
				
				JLabel lblInformationToBe = new JLabel("Information to be changed");
				lblInformationToBe.setHorizontalAlignment(SwingConstants.CENTER);
				scrollPane_1.setColumnHeaderView(lblInformationToBe);
				lblNoApps.setFont(new Font("SansSerif", Font.ITALIC, 30));
				GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
				gbc_lblNewLabel.gridwidth = 5;
				gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel.gridx = 1;
				gbc_lblNewLabel.gridy = 6;
				patientProfilePane.add(lblNoApps, gbc_lblNewLabel);
				if(noApps){
					lblNoApps.setVisible(true);
				}
		
		
		
//		if(appAtt!= null){
		table = new JTable();
		table.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		
		table.setSize(new Dimension(1200, 60));
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
		table.setName("Recent Appoinments");
		


		
		table.setModel(new DefaultTableModel(
			data.toArray(new Object[0][0]),
			new String[]{"Type","Chair","Free Slots","Duration","Date"}
				){
			
			public boolean isCellEditable(int row, int column){
				fs = table.getColumnModel().getColumnIndex("Free Slots");

				return !(row != table.getSelectedRow() && column ==fs);
			}
		});
		int dur = table.getColumnModel().getColumnIndex("Duration");
		int cha = table.getColumnModel().getColumnIndex("Chair");
		int dat = table.getColumnModel().getColumnIndex("Date");
		fsBox = new JComboBox<String>();
		
		table.getColumnModel().getColumn(fs).setCellEditor(new DefaultCellEditor(fsBox));
		
		table.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				ArrayList<String> list = Appointments.getFreeSpotsList(table.getValueAt(table.getSelectedRow(), dat).toString(),  table.getValueAt(table.getSelectedRow(), dur).toString(), table.getValueAt(table.getSelectedRow(), cha).toString());
				if(table.getValueAt(table.getSelectedRow(),fs)!=null){
				list.add(table.getValueAt(table.getSelectedRow(), fs).toString());	
				}
				list.sort(new Comparator(){

					@Override
					public int compare(Object a, Object b) {
						try {
						return	Format.TIMEFORMAT2.parse(a.toString().split("-")[0]).compareTo(Format.TIMEFORMAT2.parse(b.toString().split("-")[0]));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return 0;
					}
					
				});
				fsBox = new JComboBox(list.toArray(new String[0]));
				
				table.getColumnModel().getColumn(fs).setCellEditor(new DefaultCellEditor(fsBox));
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		table.setCellSelectionEnabled(false);
		table.setRowSelectionAllowed(true);
		table.setBounds(921, 458, -883, -106);
		table.getModel().addTableModelListener(appointmentTableListener);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridwidth = 7;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 6;
		patientProfilePane.add(scrollPane, gbc_scrollPane);
//		}
		
		JButton btnNewAppointment = new JButton("New Appointment");
		btnNewAppointment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(table!=null){
				DefaultTableModel mod1 = (DefaultTableModel) table.getModel();
				mod1.addRow(new Object[]{null});
				}else{
					table = new JTable();
					table.setModel(new DefaultTableModel(
						new Object[][]{null},new String[]{"Type","Chair","Free Slots","Duration","Date"}	
							));
				}
				lblNoApps.setVisible(false);
				
			}
		});
		GridBagConstraints gbc_btnNewAppointment = new GridBagConstraints();
		gbc_btnNewAppointment.anchor = GridBagConstraints.NORTH;
		gbc_btnNewAppointment.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewAppointment.gridx = 1;
		gbc_btnNewAppointment.gridy = 7;
		patientProfilePane.add(btnNewAppointment, gbc_btnNewAppointment);
		GridBagConstraints gbc_btnApplyChanges = new GridBagConstraints();
		gbc_btnApplyChanges.gridwidth = 3;
		gbc_btnApplyChanges.anchor = GridBagConstraints.NORTH;
		gbc_btnApplyChanges.insets = new Insets(0, 0, 0, 5);
		gbc_btnApplyChanges.gridx = 2;
		gbc_btnApplyChanges.gridy = 7;
		patientProfilePane.add(btnApplyChanges, gbc_btnApplyChanges);

	}
	
	public static void findPatientBox(JFrame frmFindPatient){
		frmFindPatient.setTitle("Find Patient");
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{60, 195, 88, 60, 56, 0};
		gridBagLayout.rowHeights = new int[]{41, 40, 33, 27, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frmFindPatient.getContentPane().setLayout(gridBagLayout);
		
		HashMap<String, ArrayList<String>> currentPatients = Patient.getCurrentNames();
		String[] currentNames = currentPatients.get(Table.NAME).toArray(new String[0]);
		Arrays.sort(currentNames);
		
		JComboBox nameBox = new JComboBox(currentNames);

		JButton btnOk = new JButton("Ok");
		btnOk.setBackground(UIManager.getColor("Tree.dropLineColor"));
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.insets = new Insets(0, 0, 0, 5);
		gbc_btnOk.gridx = 3;
		gbc_btnOk.gridy = 3;
		frmFindPatient.getContentPane().add(btnOk, gbc_btnOk);
		btnOk.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				PatientButtonListener.setName(nameBox.getSelectedItem().toString());
				frmFindPatient.dispose();
				PatientButtonListener.found = true;
				findPatient.getActionListeners()[0].actionPerformed(new ActionEvent(findPatient, 0,"Find Patient" ));
			}
			
		});

		nameBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
		nameBox.setFocusCycleRoot(true);
		nameBox.setInheritsPopupMenu(true);
		nameBox.setEditable(true);
		JTextComponent j = (JTextComponent) nameBox.getEditor().getEditorComponent();
		j.addKeyListener(new KeyListener(){



			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER&& !nameBox.isPopupVisible()){
					
					btnOk.getActionListeners()[0].actionPerformed(new ActionEvent(nameBox,0,"Ok"));
				}
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
		});


		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(PatientGUI.class.getResource("/javax/swing/plaf/metal/icons/ocean/info.png")));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 1;
		frmFindPatient.getContentPane().add(label, gbc_label);
		j.setDocument(new NameDocument(nameBox,false));
		nameBox.setSelectedIndex(0);
		GridBagConstraints gbc_nameBox = new GridBagConstraints();
		gbc_nameBox.gridwidth = 4;
		gbc_nameBox.insets = new Insets(0, 0, 5, 0);
		gbc_nameBox.fill = GridBagConstraints.BOTH;
		gbc_nameBox.gridx = 1;
		gbc_nameBox.gridy = 1;
		frmFindPatient.getContentPane().add(nameBox, gbc_nameBox);
		
		
		
		JButton btnCancel = new JButton("Cancel");
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.gridx = 4;
		gbc_btnCancel.gridy = 3;
		frmFindPatient.getContentPane().add(btnCancel, gbc_btnCancel);
		btnCancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frmFindPatient.setVisible(false);
			}
			
		});

		
		frmFindPatient.setSize(500, 250);
	}

	public static JButton backButton(JLayeredPane previousPane,JLayeredPane currentPane){
				GridBagLayout gridBagLayout = new GridBagLayout();
				gridBagLayout.columnWidths = new int[]{172, 1189, 0};
				gridBagLayout.rowHeights = new int[]{146, 628, 0};
				gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
				gridBagLayout.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
				GridBagConstraints gbc_back_1 = new GridBagConstraints();
				gbc_back_1.insets = new Insets(0, 0, 5, 5);
				gbc_back_1.gridheight = 2;
				gbc_back_1.gridx = 0;
				gbc_back_1.gridy = 0;
				JButton back = new JButton("Back");
				back.setBounds(new Rectangle(0, 0, 60, 60));
				GridBagConstraints gbc_back_11 = new GridBagConstraints();
				gbc_back_11.insets = new Insets(0, 0, 5, 5);
				gbc_back_11.fill = GridBagConstraints.HORIZONTAL;
				gbc_back_11.gridx = 0;
				gbc_back_11.gridy = 0;
//				test.getContentPane().add(back, gbc_back_11);
				currentPane.add(back, gbc_back_11);
				back.setInheritsPopupMenu(true);
				back.setIcon(new ImageIcon("C:\\Users\\R7-371T\\Desktop\\LabWork\\OrthoDatabase\\src\\resources\\Go back.png"));
				back.setVisible(true);
				back.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						if(e.getActionCommand().equals("Back")){
							currentPane.setVisible(false);
							previousPane.setVisible(true);
						}
				
					}});
				
				return back;

	}
}
