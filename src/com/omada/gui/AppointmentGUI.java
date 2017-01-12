package com.omada.gui;

import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.plaf.TableUI;

import org.apache.commons.lang3.text.WordUtils;
import org.javatuples.Pair;

import com.omada.appointment.Appointments;
import com.omada.appointment.Appointments.AppointmentException;
import com.omada.database.Format;
import com.omada.database.Table;
import com.omada.main.Export;
import com.omada.main.Main;
import com.omada.main.Search;
import com.omada.orthoCalendar.DailySchedule;
import com.omada.patient.Patient;

import javax.swing.JLabel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.awt.event.ActionEvent;
import java.awt.Rectangle;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JToggleButton;

public class AppointmentGUI {
	private static final Date today = Calendar.getInstance().getTime();
	public static Date getToday() {
		return today;
	}
	private static Date date;
	private static JTable table;
	public static JFormattedTextField dateField;
	private static HashMap<Integer,ArrayList<Integer>> occupiedRows;
	private static HashMap<Integer, ArrayList<Integer>> appointmentStartRows;
	public static JComboBox<String> nameBox;
	public static JButton btnUpdateAppointments;
	private static HashMap<Integer, HashMap<String, Appointments>> appointmentsByDate;
	private static String chair;
	private static JScrollPane leftScrollPane;
	private static JScrollPane scrollPane;
	private static JButton btnExportToExcel;
	private static Pair<String[],Object[][]> exportData;
	public static JToggleButton tglbtnMove;
	private static JButton btnDeleteAppointment;
	public static JLabel lblAppointments;
	public static String getChair() {
		return chair;
	}

	public static void setChair(String chair) {
		AppointmentGUI.chair = chair;
	}

	public static HashMap<Integer,HashMap<String, Appointments>> getAppointmentsByDate() {
		return appointmentsByDate;
	}

	public static HashMap<Integer, ArrayList<Integer>> getAppointmentStartRows() {		
		return appointmentStartRows;
	}
	public static DailySchedule getSchedule(){
		return schedule;
	}


	public static HashMap<Integer, ArrayList<Integer>> getOccupiedRows() {
		return occupiedRows;
	}
	public static final String[] tableColNames ={"Time","Name","Duration","Payment","Chair"};
	public static Date getDate() {
		return date;
	}
	private static DailySchedule schedule;


	/**
	 * @wbp.parser.entryPoint
	 */
	public static void appointmentHome(){
		date = today;
		schedule = new DailySchedule(date);
		exportData = new Pair<String[],Object[][]>(null,null);
		JLayeredPane home = new JLayeredPane();
		home.setVisible(true);
		GridBagLayout gbl_home = new GridBagLayout();
		gbl_home.columnWidths = new int[]{0, 54, 47, 105, 103, 120, 249, 120, 120, 110, 54, 54, 30, 0};
		gbl_home.rowHeights = new int[]{0, 54, 0, 0, 212, 113, 0, 0, 0};
		gbl_home.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_home.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		home.setLayout(gbl_home);
		Main.getMainFrame().getContentPane().add(home);
		backButton(Main.getMainLayeredPane(), home);
		Main.getMainLayeredPane().setVisible(false);


		JButton btnMonth = new JButton("month-");
		btnMonth.addActionListener(new CalendarActionListener(false));
		JButton back_1 = new JButton("Back");
		back_1.setHorizontalTextPosition(SwingConstants.RIGHT);
		back_1.setHorizontalAlignment(SwingConstants.RIGHT);
		back_1.setBounds(new Rectangle(600, 50, 500, 500));
		GridBagConstraints gbc_back_1 = new GridBagConstraints();
		gbc_back_1.insets = new Insets(0, 0, 5, 5);
		gbc_back_1.fill = GridBagConstraints.BOTH;
		gbc_back_1.gridx = 1;
		gbc_back_1.gridy = 1;


		lblAppointments = new JLabel("Today's Appointments");
		lblAppointments.setFont(new Font("Tahoma", Font.ITALIC, 30));
		GridBagConstraints gbc_lblAppointments = new GridBagConstraints();
		gbc_lblAppointments.gridwidth = 3;
		gbc_lblAppointments.insets = new Insets(0, 0, 5, 5);
		gbc_lblAppointments.gridx = 5;
		gbc_lblAppointments.gridy = 1;
		home.add(lblAppointments, gbc_lblAppointments);
		GridBagConstraints gbc_btnMonth = new GridBagConstraints();
		gbc_btnMonth.insets = new Insets(0, 0, 5, 5);
		gbc_btnMonth.gridx = 3;
		gbc_btnMonth.gridy = 2;
		home.add(btnMonth, gbc_btnMonth);

		JButton btnWeek = new JButton("week-");
		btnWeek.addActionListener(new CalendarActionListener(false));
		GridBagConstraints gbc_btnWeek = new GridBagConstraints();
		gbc_btnWeek.insets = new Insets(0, 0, 5, 5);
		gbc_btnWeek.gridx = 4;
		gbc_btnWeek.gridy = 2;
		home.add(btnWeek, gbc_btnWeek);

		JButton btnDay = new JButton("day-");
		btnDay.addActionListener(new CalendarActionListener(false));
		GridBagConstraints gbc_btnDay = new GridBagConstraints();
		gbc_btnDay.insets = new Insets(0, 0, 5, 5);
		gbc_btnDay.gridx = 5;
		gbc_btnDay.gridy = 2;
		home.add(btnDay, gbc_btnDay);

		dateField = new JFormattedTextField(Format.DATEFORMAT);
		dateField.setHorizontalAlignment(SwingConstants.CENTER);
		dateField.setText(Format.DATEFORMAT.format(today));
		dateField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					date=Format.DATEFORMAT.parse(dateField.getText());
					if(!chair.equals("Both")){
						if(tglbtnMove.isSelected()){
							Integer selectedRow = AppointmentGUI.getTable().getSelectedRow();
							if(selectedRow==-1){
								return;
							}
							String time = AppointmentGUI.getTable().getValueAt(selectedRow, 0).toString();

							try {
								schedule.changeAppointmentDate(chair, time,Format.DATEFORMAT.parse(dateField.getText()) );
							}catch(NullPointerException n){
								JOptionPane.showMessageDialog(new JFrame(), "Ensure row with writting is selected!");
								return;
							}
							catch (Exception e1) {
								JOptionPane.showMessageDialog(new JFrame(), e1.getMessage());
								e1.printStackTrace();
							}

						}
//					setTableData(Format.DATEFORMAT.parse(dateField.getText()));	
					setTableGUI(chair);
					tglbtnMove.setSelected(false);
					}else{		
						setTableGUI(chair);
					}
					if(!Format.DATEFORMAT.format(AppointmentGUI.getToday()).equals(AppointmentGUI.dateField.getText())){
					lblAppointments.setText(Format.DAYOFWEEK.format(Format.DATEFORMAT.parse(dateField.getText()))+"'s Appointments");
					}else{
						AppointmentGUI.lblAppointments.setText("Today's Appointments");
					}
					} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		});

		GridBagConstraints gbc_lblDate = new GridBagConstraints();
		gbc_lblDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblDate.gridx = 6;
		gbc_lblDate.gridy = 2;
		home.add(dateField, gbc_lblDate);

		JButton btnDay_1 = new JButton("day+");
		btnDay_1.addActionListener(new CalendarActionListener(false));
		GridBagConstraints gbc_btnDay_1 = new GridBagConstraints();
		gbc_btnDay_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnDay_1.gridx = 7;
		gbc_btnDay_1.gridy = 2;
		home.add(btnDay_1, gbc_btnDay_1);

		JButton btnWeek_1 = new JButton("week+");
		btnWeek_1.addActionListener(new CalendarActionListener(false));
		GridBagConstraints gbc_btnWeek_1 = new GridBagConstraints();
		gbc_btnWeek_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnWeek_1.gridx = 8;
		gbc_btnWeek_1.gridy = 2;
		home.add(btnWeek_1, gbc_btnWeek_1);

		JButton btnMonth_1 = new JButton("month+");
		btnMonth_1.addActionListener(new CalendarActionListener(false));
		GridBagConstraints gbc_btnMonth_1 = new GridBagConstraints();
		gbc_btnMonth_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnMonth_1.gridx = 9;
		gbc_btnMonth_1.gridy = 2;
		home.add(btnMonth_1, gbc_btnMonth_1);

		JComboBox chairBox = new JComboBox(new String[]{"Chair 1","Chair 2","Both"});
		chair = chairBox.getItemAt(0).toString().substring(chairBox.getItemAt(0).toString().length()-1, chairBox.getItemAt(0).toString().length());
		chairBox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!chairBox.getSelectedItem().equals("Both")){
					chair = chairBox.getSelectedItem().toString().substring(chairBox.getSelectedItem().toString().length()-1, chairBox.getSelectedItem().toString().length());
					tglbtnMove.setEnabled(true);
					btnDeleteAppointment.setVisible(true);
					setTableData(date);
				}else{
					System.out.println(chairBox.getSelectedItem());
					chair = chairBox.getSelectedItem().toString();
					tglbtnMove.setSelected(false);
					tglbtnMove.setEnabled(false);
					btnDeleteAppointment.setVisible(false);
					setDualTableData(date);
				}
			}

		});

		GridBagConstraints gbc_chairBox = new GridBagConstraints();
		gbc_chairBox.insets = new Insets(0, 0, 5, 5);
		gbc_chairBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_chairBox.gridx = 6;
		gbc_chairBox.gridy = 3;
		home.add(chairBox, gbc_chairBox);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 7;
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 3;
		gbc_scrollPane.gridy = 4;
		home.add(scrollPane, gbc_scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		btnUpdateAppointments = new JButton("Update Appointments");
		btnUpdateAppointments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnUpdateAppointments.setVisible(false);

		leftScrollPane = new JScrollPane();
		GridBagConstraints gbc_leftScrollPane = new GridBagConstraints();
		gbc_leftScrollPane.gridwidth = 7;
		gbc_leftScrollPane.gridheight = 2;
		gbc_leftScrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_leftScrollPane.fill = GridBagConstraints.BOTH;
		gbc_leftScrollPane.gridx = 3;
		gbc_leftScrollPane.gridy = 4;
		home.add(leftScrollPane, gbc_leftScrollPane);
		
		btnExportToExcel = new JButton("Export to Excel");
		GridBagConstraints gbc_btnExportToExcel = new GridBagConstraints();
		gbc_btnExportToExcel.insets = new Insets(0, 0, 5, 5);
		gbc_btnExportToExcel.gridx = 3;
		gbc_btnExportToExcel.gridy = 6;
		home.add(btnExportToExcel, gbc_btnExportToExcel);
		btnExportToExcel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
				Export.exportToExcel(Format.DATABASEDATEFORMAT.format(date)+" Appointments.xlsx", exportData.getValue0(), exportData.getValue1());
				JOptionPane.showMessageDialog(new JFrame(), "Appointments exported successfully!");
				}catch(Exception e){
					Main.log.doLogging(e.getMessage(), "severe");
				JOptionPane.showMessageDialog(new JFrame(), "Ensure the "+Format.DATABASEDATEFORMAT.format(date)+" Appointments.xlsx " + "is closed!");
				}
			}
			
		});
		


		GridBagConstraints gbc_btnUpdateAppointments = new GridBagConstraints();
		gbc_btnUpdateAppointments.insets = new Insets(0, 0, 5, 5);
		gbc_btnUpdateAppointments.gridx = 6;
		gbc_btnUpdateAppointments.gridy = 6;
		home.add(btnUpdateAppointments, gbc_btnUpdateAppointments);
		
		tglbtnMove = new JToggleButton("Move");
		GridBagConstraints gbc_tglbtnMove = new GridBagConstraints();
		gbc_tglbtnMove.insets = new Insets(0, 0, 5, 5);
		gbc_tglbtnMove.gridx = 8;
		gbc_tglbtnMove.gridy = 6;
		home.add(tglbtnMove, gbc_tglbtnMove);
		
		btnDeleteAppointment = new JButton("Delete Appointment");
		btnDeleteAppointment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Integer row = table.getSelectedRow();
				Appointments app = AppointmentGUI.appointmentsByDate.get(Integer.parseInt(Format.chairParse(chair))).get(table.getValueAt(row, 0).toString());
				app.delete();
				if(!chair.equalsIgnoreCase("Both")){
					setTableData(date);
				}
			}
		});
		GridBagConstraints gbc_btnDeleteAppointment = new GridBagConstraints();
		gbc_btnDeleteAppointment.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteAppointment.gridx = 9;
		gbc_btnDeleteAppointment.gridy = 6;
		home.add(btnDeleteAppointment, gbc_btnDeleteAppointment);
//		setTableData(today);
		setTableGUI(chair);
	}
	public static JTable getTable() {
		return table;
	}

	@SuppressWarnings("unchecked")
	private static Pair<String[], Object[][]> tableData(Date date, String chair){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		boolean contin = true;

		ArrayList<Appointments> appointments = null;
		try {
			appointments = Appointments.getAppointmentsByDate(Format.DATABASEDATEFORMAT.format(date));
		} catch (AppointmentException e) {
			e.printStackTrace();	
			contin=false;
		}
		if(contin){
			ArrayList<Object> data = new ArrayList<Object>();
			for(Appointments app:appointments){
				Object[] rowData = new Object[tableColNames.length];
				HashMap<String, String> appAtt = new HashMap<String, String>();
				if(!app.getAttributes().get("Chair").equals(chair.toString()))
					continue;
				appAtt.putAll(app.getAttributes());
				Integer hour = Integer.parseInt(appAtt.get("Hour"));
				appAtt.remove("Hour");
				Integer minute= Integer.parseInt(appAtt.get("Minute"));
				appAtt.remove("Minute");
				appAtt.put("Payment", app.getPayment().getPayment().toString());
				appAtt.put("Time", Format.timeFormat(hour, minute));
				appAtt.put(Table.NAME, app.getPatientName());
				appointmentsByDate.get(Integer.parseInt(chair)).put(appAtt.get("Time"), app);
				int i =0;
				for(String colName:tableColNames){
					rowData[i] = appAtt.get(colName);
					i++;
				}
				data.add(rowData);
			}
			System.out.println(Arrays.deepToString(data.toArray()));
			return new Pair<String[],Object[][]> (tableColNames,data.toArray(new Object[0][0]));
		}
		return new Pair<String[],Object[][]>(tableColNames,null);
	}
	public static void setTableGUI(String type){
		schedule = new DailySchedule(date);
		if("Both".equals(type)){
			
		}else{
			Object[][] data = schedule.getTableFormat(chair, tableColNames);			
			
			HashMap<String, ArrayList<String>> currentPatients = Patient.getCurrentNames();
			String[] currentNames = currentPatients.get(Table.NAME).toArray(new String[0]);
			Arrays.sort(currentNames);
			nameBox = new JComboBox(currentNames);
			JTextComponent j = (JTextComponent) nameBox.getEditor().getEditorComponent();
			j.setDocument(new NameDocument(nameBox,true));


			table.setModel(new DefaultTableModel(
					data,tableColNames){
				@Override
				public boolean isCellEditable(int row, int column){
						String time = table.getValueAt(row, 0).toString();
						boolean b = schedule.getSchedule().get(chair).get(time)==null;
						b=b&& column!=0;
						b=b||schedule.getAppointmentStartTimes(Format.chairParse(chair)).contains(time);
					return ( b );
				}

			});		

			dateField.setText(Format.DATEFORMAT.format(date));
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			table.setVisible(true);
		
			exportData=new Pair<String[],Object[][]>(tableColNames,data);

			nameBox.setEditable(true);
			table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(nameBox));
			int dur = table.getColumnModel().getColumnIndex("Duration");
			table.getColumnModel().getColumn(dur).setCellEditor(new DefaultCellEditor(new JComboBox(new String[]{"1X","2X","3X","4X"})));

			table.setDefaultRenderer(Object.class, new ShowOccCells(chair) );
			table.getModel().addTableModelListener(new AppointmentTableListener(date,chair));
			table.setVisible(true);		
		}
	}
	public static Object[] extractAppInfo(Appointments app){

		Object[] rowData = new Object[tableColNames.length];
		HashMap<String, String> appAtt = new HashMap<String, String>();
		if(app == null){
			return rowData;
		}
		appAtt.putAll(app.getAttributes());
		Integer hour = Integer.parseInt(appAtt.get("Hour"));
		appAtt.remove("Hour");
		Integer minute= Integer.parseInt(appAtt.get("Minute"));
		appAtt.remove("Minute");
		appAtt.put("Payment", app.getPayment().getPayment().toString());
		appAtt.put("Time", Format.timeFormat(hour, minute));
		appAtt.put(Table.NAME, app.getPatientName());
//		appointmentsByDate.get(Integer.parseInt(chair)).put(appAtt.get("Time"), app);
		int i =0;
		for(String colName:tableColNames){
			rowData[i] = appAtt.get(colName);
			i++;
		}
		return rowData;
	
	}
	public static void setDualTableData(Date date){
		scrollPane.setVisible(false);
		AppointmentGUI.date =date;

		appointmentsByDate = new HashMap<Integer,HashMap<String,Appointments>>();
		appointmentsByDate.put(1, new HashMap<String,Appointments>());
		appointmentsByDate.put(2, new HashMap<String,Appointments>());


		JTable leftTable = new JTable();
		leftScrollPane.setViewportView(leftTable);
		//		leftScrollPane.setVisible(true);

		Pair<String[], Object[][]> tableDataLeft = tableData(date,"1");
		
		Pair<String[], Object[][]> tableDataRight = tableData(date,"2");
		

		HashMap<String, ArrayList<String>> currentPatients = Patient.getCurrentNames();
		String[] currentNames = currentPatients.get(Table.NAME).toArray(new String[0]);
		Arrays.sort(currentNames);

		nameBox = new JComboBox(currentNames);
		JTextComponent j = (JTextComponent) nameBox.getEditor().getEditorComponent();
		j.setDocument(new NameDocument(nameBox,true));
		
		String[] columnNames = new String[2*tableDataLeft.getValue0().length -1];
		columnNames[0] = "Time";
		for(int i =1;i<tableColNames.length;i++){
			columnNames[i] = tableDataLeft.getValue0()[i];
			columnNames[i+tableColNames.length-1]= tableDataLeft.getValue0()[i];
		}
		Object[][] calData = calendarTableFormat(tableDataLeft.getValue1(),tableDataRight.getValue1());
		
		leftTable.setModel(new DefaultTableModel(
				calData,columnNames){
			@Override
			public boolean isCellEditable(int row, int column){
				boolean b;
				Integer chairNum;
				if(column<tableColNames.length){
					chairNum = 1;
				}else{
					chairNum =2;
				}
				try{
					b =appointmentStartRows.get(chairNum).contains(row);
				}catch(Exception e){
					b=true;
					e.printStackTrace();
				}
				return ((!AppointmentGUI.getOccupiedRows().get(chairNum).contains(row)&&column!=0)|| b );
			}

		});	
		dateField.setText(Format.DATEFORMAT.format(date));

		nameBox.setEditable(true);
		leftTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(nameBox));
		int dur = leftTable.getColumnModel().getColumnIndex("Duration");
		leftTable.getColumnModel().getColumn(dur).setCellEditor(new DefaultCellEditor(new JComboBox(new String[]{"1X","2X","3X","4X"})));

		leftTable.getColumnModel().getColumn(tableColNames.length).setCellEditor(new DefaultCellEditor(nameBox));
		leftTable.getColumnModel().getColumn(tableColNames.length+dur-1).setCellEditor(new DefaultCellEditor(new JComboBox(new String[]{"1X","2X","3X","4X"})));

		leftTable.setDefaultRenderer(Object.class, new ShowOccCells(null) );
		leftTable.getModel().addTableModelListener(new AppointmentTableListener(date,null));
		leftTable.setVisible(true);

		exportData=new Pair<String[],Object[][]>(columnNames,calData);


	}
	public static void setTableData(Date date){
		
		appointmentsByDate = new HashMap<Integer,HashMap<String,Appointments>>();
		appointmentsByDate.put(1, new HashMap<String,Appointments>());
		appointmentsByDate.put(2, new HashMap<String,Appointments>());

		scrollPane.setVisible(true);
		AppointmentGUI.date =date;

		table.setVisible(false);

		Pair<String[], Object[][]> tableData = tableData(date,chair);

		HashMap<String, ArrayList<String>> currentPatients = Patient.getCurrentNames();
		String[] currentNames = currentPatients.get(Table.NAME).toArray(new String[0]);
		Arrays.sort(currentNames);
		nameBox = new JComboBox(currentNames);
		JTextComponent j = (JTextComponent) nameBox.getEditor().getEditorComponent();
		j.setDocument(new NameDocument(nameBox,true));
		Object[][] calData = calendarTableFormat(tableData.getValue1(),chair);
		if(tableData.getValue1() != null){
			table.setModel(new DefaultTableModel(
					calData,tableData.getValue0()){
				@Override
				public boolean isCellEditable(int row, int column){
					boolean b;
					try{
						b =appointmentStartRows.get(Integer.parseInt(Format.chairParse(chair))).contains(row);
					}catch(Exception e){
						b=true;
						e.printStackTrace();
					}
					return ((!AppointmentGUI.getOccupiedRows().get(Integer.parseInt(Format.chairParse(chair))).contains(row)&&column!=0)|| b );
				}

			});		

			dateField.setText(Format.DATEFORMAT.format(date));
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Object[] fill = (new Object[tableColNames.length]);
			Arrays.fill(fill," ");
			model.addRow(fill);
			table.setVisible(true);
		}else{
			Object[] fill = (new Object[tableColNames.length]);
			calData = new Object[][]{fill};
			table.setModel(new DefaultTableModel(
					calendarTableFormat(calData,chair),tableColNames){
				@Override
				public boolean isCellEditable(int row, int column){
					boolean b;
					try{
						b =appointmentStartRows.get(Integer.parseInt(Format.chairParse(chair))).contains(row);
					}catch(Exception e){
						b=true;
						e.printStackTrace();
					}
					return ((!AppointmentGUI.getOccupiedRows().get(Integer.parseInt(Format.chairParse(chair))).contains(row)&&column!=0)|| b );
				}

			}
					);
			dateField.setText(Format.DATEFORMAT.format(date));
		}
		exportData=new Pair<String[],Object[][]>(tableData.getValue0(),calData);

		nameBox.setEditable(true);
		table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(nameBox));
		int dur = table.getColumnModel().getColumnIndex("Duration");
		table.getColumnModel().getColumn(dur).setCellEditor(new DefaultCellEditor(new JComboBox(new String[]{"1X","2X","3X","4X"})));

		table.setDefaultRenderer(Object.class, new ShowOccCells(chair) );
		table.getModel().addTableModelListener(new AppointmentTableListener(date,chair));
		table.setVisible(true);
	}
	public static Object[][] calendarTableFormat(Object[][] data,String chair){
		Calendar cal = Calendar.getInstance();
		appointmentStartRows = new HashMap<Integer,ArrayList<Integer>>();
		appointmentStartRows.put(1, new ArrayList<Integer>());
		appointmentStartRows.put(2, new ArrayList<Integer>());
		occupiedRows = new HashMap<Integer,ArrayList<Integer>>();
		occupiedRows.put(1, new ArrayList<Integer>());
		occupiedRows.put(2, new ArrayList<Integer>());

		try {
			cal.setTime(Format.TIMEFORMAT2.parse("8:00AM"));
		} catch (ParseException e) {
			e.printStackTrace();
		};
		ArrayList<Object[]> array = new ArrayList<>();
		for(int i = 0 ;i<40;i++){
			Object[] row = new Object[tableColNames.length];
			Object[] row2 = new Object[tableColNames.length-1];
			cal.set(Calendar.HOUR_OF_DAY, (i/4)+8);
			cal.set(Calendar.MINUTE, (i%4)*15);
			row[0] = Format.TIMEFORMAT2.format(cal.getTime());
			if(data!=null){
			for(Object[] rowData:data){
				if(rowData[0]!=null){
					if(((String)rowData[0]).equalsIgnoreCase((String) row[0])){

						row = rowData;
						Integer dur = Format.durationParse((String)rowData[2]);
						appointmentStartRows.get(Integer.parseInt(Format.chairParse(chair))).add(i);
						for(int j=i;j<i+dur;j++){
							occupiedRows.get(Integer.parseInt(Format.chairParse(chair))).add(j);
						}
					}
				}}}
			array.add(row);	
		}
		return array.toArray(new Object[0][0]);
	}
	public static Object[][] calendarTableFormat(Object[][] left,Object[][] right){
		Calendar cal = Calendar.getInstance();
		appointmentStartRows = new HashMap<Integer,ArrayList<Integer>>();
		appointmentStartRows.put(1, new ArrayList<Integer>());
		appointmentStartRows.put(2, new ArrayList<Integer>());
		occupiedRows = new HashMap<Integer,ArrayList<Integer>>();
		occupiedRows.put(1, new ArrayList<Integer>());
		occupiedRows.put(2, new ArrayList<Integer>());
		try {
			cal.setTime(Format.TIMEFORMAT2.parse("8:00AM"));
		} catch (ParseException e) {
			e.printStackTrace();
		};
		ArrayList<Object[]> array = new ArrayList<>();
		for(int i = 0 ;i<40;i++){
			Object[] row = new Object[2*tableColNames.length-1];
			Arrays.fill(row, "");
			cal.set(Calendar.HOUR_OF_DAY, (i/4)+8);
			cal.set(Calendar.MINUTE, (i%4)*15);
			row[0] = Format.TIMEFORMAT2.format(cal.getTime());
			if(left!=null){
				for(int h = 0;h<left.length;h++){
					Object[] rowData = left[h];
					if(rowData[0]!=null){
						if(((String)rowData[0]).equalsIgnoreCase((String) row[0])){
							for(int count=0;count<rowData.length;count++){
							row[count] = rowData[count];
							}
							Integer dur = Format.durationParse((String)rowData[2]);
							appointmentStartRows.get(1).add(i);	
							for(int j=i;j<i+dur;j++){
								occupiedRows.get(1).add(j);
							}
						}
					}
				}
			}
			if(right!=null){
				try{
				for(int h = 0;h<right.length;h++){
					Object[] rowData = right[h];
					if(rowData[0]!=null){
						if(((String)rowData[0]).equalsIgnoreCase((String) row[0])){
							for(int count=1;count<rowData.length;count++){
							row[tableColNames.length+count-1] = rowData[count];
							}
							Integer dur = Format.durationParse((String)rowData[2]);
							appointmentStartRows.get(2).add(i);
							for(int j=i;j<i+dur;j++){
								occupiedRows.get(2).add(j);
							}
						}
					}
				}
				}catch(ArrayIndexOutOfBoundsException e){
					e.printStackTrace();
				}
				
			}
			array.add(row);	
		}
		return array.toArray(new Object[0][0]);
	}

	public static void main(String[] args){
		appointmentHome();
	}
	public static class ShowOccCells extends DefaultTableCellRenderer{
		private String chairStr;
		public ShowOccCells(String chair) {
			super();
			this.chairStr=chair;
		}

		public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected ,boolean hasFocus,int row,int column) {
			Component c = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
			Color col;
			Integer chair;
			if(chairStr==null||chairStr.equals("Both")){
				if(column<tableColNames.length){
					chair = 1;
				}else{
					chair =2;
				}
			}else{
				chair = Integer.parseInt(Format.chairParse(chairStr));
			}
//			if(occupiedRows.get(chair).contains(row)){
//				col=Color.LIGHT_GRAY;
//
//			}else{
//				col = UIManager.getColor("Table.background");		
//			}

			
			if(schedule.getSchedule().get(chair.toString()).get(table.getValueAt(row, 0).toString())!=null){
				col=Color.LIGHT_GRAY;

			}else{
				col = UIManager.getColor("Table.background");		
			}
			if(!(isSelected||hasFocus)){
				setBackground(col);
			}
			return c;
		}

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
		JButton back = new JButton();
		back.setActionCommand("Back");
		currentPane.add(back, gbc_back_1);
		back.setInheritsPopupMenu(true);
		back.setIcon(new ImageIcon("C:\\Users\\R7-371T\\Desktop\\LabWork\\OrthoDatabase\\src\\resources\\Go back.png"));
		back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(e.getActionCommand().equals("Back")){
					currentPane.setVisible(false);
					previousPane.setVisible(true);
				}

			}});

		return back;
	}
	public static class NameDocument extends PlainDocument{
		String sub ="";
		ArrayList<String> list = Patient.getCurrentNames().get(Table.NAME);
		ArrayList<String> exit =list;
		Boolean exit1=false;
		Boolean selecting=false;
		JComboBox nameBox;

		public NameDocument (JComboBox nameBox,Boolean start){
			this.nameBox = nameBox;
//			this.exit1=start;
		}
		@Override
		public void insertString(int offs, String str, AttributeSet a){
			if(exit1){
				exit1=false;
				return;
			}
			if(!list.contains(str)){

			String ex = sub.substring(offs);
			sub=sub.substring(0, offs);
			sub+= str+ex;
			sub = WordUtils.capitalize(sub);
			ArrayList<String> list = Search.nameSearch(Patient.getCurrentNames().get(Table.NAME), sub);
			exit1 = true;
			nameBox.setModel(new DefaultComboBoxModel(list.toArray(new String[0])));
			
			exit1 = false;

			//			nameBox.setSelectedItem(list.get(0));
			JTextComponent j = (JTextComponent)nameBox.getEditor().getEditorComponent();
			nameBox.setVisible(true);
			}
			try{
				nameBox.setPopupVisible(true);
			}
			catch(Exception e){

			}
			try {
				super.insertString(offs,str,a);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}

		}
		@Override
		public void remove(int offs, int len){
			try {
				if(exit1){
					return;
				}else{

					super.remove(offs, len);
					sub = super.getText(0, getLength());
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			ArrayList<String> list = Search.nameSearch(Patient.getCurrentNames().get(Table.NAME), sub);
			exit1 = true;
			nameBox.setModel(new DefaultComboBoxModel(list.toArray(new String[0])));
			exit1 = false;
			try{
				nameBox.setPopupVisible(true);
			} catch(Exception e){

			}

		}

	}

	public static void setDate(Date time) {
		date = time;
	}

}
