package com.omada.appointment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import com.omada.database.Format;
import com.omada.database.Table;
import com.omada.database.Update;
import com.omada.gui.AppointmentGUI;
import com.omada.gui.PatientButtonListener;
import com.omada.gui.PatientGUI;
import com.omada.gui.AppointmentGUI.NameDocument;
import com.omada.orthoCalendar.DailySchedule;
import com.omada.patient.Patient;
import com.omada.payment.Payment;

public class Appointment {
private Date date;
private HashMap<String,String> attributes;
private String patientName;
public String getPatientName() {
	return patientName;
}
private Payment payment;
public void setPayment(Payment payment) {
	this.payment = payment;
}
public String getStartTime(){
	return Format.timeFormat(Integer.parseInt(getAttributes().get("Hour")), Integer.parseInt(getAttributes().get("Minute")));
}
public Payment getPayment() {
	return payment;
}
public String getDuration(){
	return getAttributes().get("Duration");
}
public HashMap<String, String> getAttributes() {
	return attributes;
}
public void setAttributes(HashMap<String, String> attributes) {
	this.attributes = attributes;
}
public Date getDate() {
	return date;
}
public String toString(){
	return String.format("%s %s appointment at %s on Chair %s",  Format.syntaxParse(getPatientName()),getAttributes().get("Duration"),Format.timeFormat(Integer.parseInt(getAttributes().get("Hour")),Integer.parseInt(getAttributes().get("Minute"))),getAttributes().get("Chair"));
}
private Boolean alreadyCommitted;
protected Integer appointment_id;
	public Appointment(HashMap<String,String> attributes) throws Exception {
		this.appointment_id= (int) (10000000*Math.random());
		this.alreadyCommitted=false;
		if(attributes.containsKey("Payment")){
			addPayment(new Payment(attributes.get(Table.NAME),Integer.parseInt(attributes.get("Payment"))));
			attributes.remove("Payment");
		}else{
			addPayment(new Payment(attributes.get(Table.NAME),0));
		}

			Patient.validPatientName(attributes.get(Table.NAME));
			this.patientName=attributes.get(Table.NAME);
			attributes.remove(Table.NAME);
		String time = attributes.get("Start Time");
		if(time ==null){
		time = attributes.get("Start_Time");
		}
		if(time ==null){
			time = Format.timeFormat(Integer.parseInt(attributes.get("Hour")), Integer.parseInt(attributes.get("Minute")));
		}
		String date = attributes.get("Date");
		try {
			this.date = Format.READFORMAT.parse(date+" "+time);
			attributes.put("Date", Format.dateFormat(this.date));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String tim[] = Format.TIMEFORMAT.format(this.date).split("(:)");
		
		String hour = tim[0];
		String minute = tim[1];
		attributes.remove("Start Time");
		attributes.remove("Start_Time");
		attributes.put("Hour", hour);
		attributes.put("Minute", minute);
		attributes.put("Appointment_ID", appointment_id.toString());
		this.attributes = attributes;
	}
	protected Appointment(Integer appointment_id,HashMap<String,String> attributes){
		this.alreadyCommitted=true;
		if(attributes.containsKey(Table.PAYMENT_AMOUNT)){
			if(attributes.containsKey("Payments.Transaction_ID")){
				setPayment(new Payment(attributes.get(Table.NAME),Integer.parseInt(attributes.get(Table.PAYMENT_AMOUNT)),Integer.parseInt(attributes.get("Payments.Transaction_ID"))));
				attributes.remove(Table.PAYMENT_AMOUNT);
				attributes.remove("Payments.Transaction_ID");

			}else{
			setPayment(new Payment(attributes.get(Table.NAME),Integer.parseInt(attributes.get(Table.PAYMENT_AMOUNT))));
			attributes.remove(Table.PAYMENT_AMOUNT);
			}
		}else{
			addPayment(new Payment(attributes.get(Table.NAME),0));
		}
		patientName = attributes.get(Table.NAME);
		attributes.remove(Table.NAME);
		String time = attributes.get("Start_Time");
		if(time ==null){
			time = Format.timeFormat(Integer.parseInt(attributes.get("Hour")), Integer.parseInt(attributes.get("Minute")));
		}
		String date = attributes.get("Date");
		try {
			this.date = Format.READFORMAT.parse(date+" "+time);
			attributes.put("Date", Format.DATEFORMAT.format(this.date));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String tim[] = Format.TIMEFORMAT.format(this.date).split("(:)");

		String hour = tim[0];
		String minute = tim[1];
		attributes.remove("Start_Time");
		attributes.put("Hour", hour);
		attributes.put("Minute", minute);
		this.appointment_id=appointment_id;
		this.attributes = attributes;
	}
		
	public Integer getAppointment_id() {
		return appointment_id;
	}
	protected void commit() throws SQLException, Exception{
		String table = "APPOINTMENTS";
		if(payment!=null){
		payment.commit(this.appointment_id);
		}
		Update.insert(table, attributes);
		this.alreadyCommitted=true;
	}
	public void delete(){
		if(alreadyCommitted){
		HashMap<String, String> eq = new HashMap<String,String>();
		eq.put("Appointment_ID", this.getAppointment_id().toString());
		Update.delete(Table.APPOINTMENTS, eq);
		}
		}
	public void addPayment(Payment payment){
		payment.commit(this.appointment_id);
		setPayment(payment);	
	}
	public void freeSlots(String[] freeSlots,Appointments app){
		System.out.println(app.getAttributes());
		JFrame frame =new JFrame();
		frame.setTitle("Free Time Slots");
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{60, 195, 88, 60, 56, 0};
		gridBagLayout.rowHeights = new int[]{41, 40, 33, 27, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		
		JComboBox<String> timeBox = new JComboBox<String>(freeSlots);

		JButton btnOk = new JButton("Ok");
		btnOk.setBackground(UIManager.getColor("Tree.dropLineColor"));
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.insets = new Insets(0, 0, 0, 5);
		gbc_btnOk.gridx = 3;
		gbc_btnOk.gridy = 3;
		frame.getContentPane().add(btnOk, gbc_btnOk);
		btnOk.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String sel =(timeBox.getSelectedItem().toString());
				String start = sel.split("-")[0];
				Pair<Integer,Integer> pair = Format.timeParse(start);
				app.getAttributes().put("Hour", pair.getValue0().toString());
				app.getAttributes().put("Minute", pair.getValue1().toString());
				if(getAlreadyCommitted()){
				app.updateAppointment(app.getAttributes());
				}else{
					Appointments.makeAppointment(app);
				}
				if(AppointmentGUI.getChair().equalsIgnoreCase("Both")){
					AppointmentGUI.setDualTableData(app.getDate());
				}else{
					AppointmentGUI.setTableData(app.getDate());
				}
				frame.dispose();
			}
			
		});

		timeBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
		JTextComponent j = (JTextComponent) timeBox.getEditor().getEditorComponent();
		j.addKeyListener(new KeyListener(){



			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER && !timeBox.isPopupVisible()){
					btnOk.getActionListeners()[0].actionPerformed(new ActionEvent(timeBox,0,"Ok"));
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
		frame.getContentPane().add(label, gbc_label);
		GridBagConstraints gbc_nameBox = new GridBagConstraints();
		gbc_nameBox.gridwidth = 4;
		gbc_nameBox.insets = new Insets(0, 0, 5, 0);
		gbc_nameBox.fill = GridBagConstraints.BOTH;
		gbc_nameBox.gridx = 1;
		gbc_nameBox.gridy = 1;
		frame.getContentPane().add(timeBox, gbc_nameBox);
		
		
		
		JButton btnCancel = new JButton("Cancel");
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.gridx = 4;
		gbc_btnCancel.gridy = 3;
		frame.getContentPane().add(btnCancel, gbc_btnCancel);
		btnCancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
			
		});

		timeBox.setEditable(false);
		frame.setSize(500, 250);
		frame.setVisible(true);
	}

	public static void main(String[] args){
		DateFormat.getDateInstance(0);
		try {
			System.out.println(new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("dd/MM/yyyy hh:mma").parse("22/07/2016 5:30PM")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public Boolean getAlreadyCommitted() {
		return alreadyCommitted;
	}
	public void setAlreadyCommitted(Boolean alreadyCommitted) {
		this.alreadyCommitted = alreadyCommitted;
	}
	}