package com.omada.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.javatuples.Pair;

import com.omada.appointment.Appointments;
import com.omada.database.Format;
import com.omada.patient.Patient;

public class AppointmentTableListener implements TableModelListener {
	private HashMap<Integer,HashMap<String,HashMap<String,String>>> newApp;
	private HashMap<Integer,HashMap<String,HashMap<String,String>>> oldApp;
	private static String chair;
	public static Date date;
	@Override
	public void tableChanged(TableModelEvent e) {
		if(e.getType()==TableModelEvent.UPDATE){
			int row = e.getLastRow();
			int column = e.getColumn();
			boolean cnull=false;
			
			if(chair == null||chair.equals("Both")){
				if(column<AppointmentGUI.tableColNames.length){
					chair = "Chair 1";
				}else{
					chair ="Chair 2";
				}
				cnull=true;
				}
			DefaultTableModel table = ((DefaultTableModel) e.getSource());
			
			String columnName = table.getColumnName(column);
			if(columnName.equalsIgnoreCase("Time")){
				columnName = "Start Time";
			}
			String value = (String) table.getValueAt(row, column);
			if(value == null||value.equals("")){
				return;
			}
			String time;
			if(column ==0){
				Pair<Integer, Integer> tim = Format.timeParse(table.getValueAt(row-1, 0).toString());
				time = Format.timeFormat(tim.getValue0(),tim.getValue1()+15 );
			}else{
			time = (String)table.getValueAt(row, 0);
			}
			if(!AppointmentGUI.getSchedule().getAppointmentStartTimes(Format.chairParse(chair)).contains(time)){
				if(newApp.get(Integer.parseInt(Format.chairParse(chair))).containsKey(time)){
					newApp.get(Integer.parseInt(Format.chairParse(chair))).get(time).put("Chair", Format.chairParse(chair));
					newApp.get(Integer.parseInt(Format.chairParse(chair))).get(time).put(columnName, value);
				}else{
					HashMap<String,String> up = new HashMap<>();
					up.put("Date", Format.DATABASEDATEFORMAT.format(date));
					up.put(columnName, value);
					newApp.get(Integer.parseInt(Format.chairParse(chair))).put(time, up);
				}
			}else{
				if(columnName.equals("Name")){
					columnName = "Patient_ID";
					try {
						value = Patient.getPatientID(value).toString();
					} catch (Exception e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(new JFrame(), e1.getCause().toString());
					}
				}
				if(oldApp.get(Integer.parseInt(Format.chairParse(chair))).containsKey(time)){
					oldApp.get(Integer.parseInt(Format.chairParse(chair))).get(time).put(columnName, value);
				}else{
					HashMap<String,String> up = new HashMap<>();
					up.put(columnName, value);
					oldApp.get(Integer.parseInt(Format.chairParse(chair))).put(time, up);
				}
			}
			if(cnull){
				chair=null;
			}
			AppointmentGUI.btnUpdateAppointments.setVisible(true);
			AppointmentGUI.tglbtnMove.setSelected(false);
			AppointmentGUI.tglbtnMove.setEnabled(false);
			System.out.println(newApp);
			System.out.println(oldApp);
		}
	}
	public AppointmentTableListener(Date date,String chair){
		newApp = new HashMap<>();
		newApp.put(1, new HashMap<String,HashMap<String,String>>());
		newApp.put(2, new HashMap<String,HashMap<String,String>>());

		AppointmentTableListener.date = date;
		oldApp = new HashMap<>();
		oldApp.put(1, new HashMap<String,HashMap<String,String>>());
		oldApp.put(2, new HashMap<String,HashMap<String,String>>());

		AppointmentTableListener.chair=chair;
		AppointmentGUI.btnUpdateAppointments.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				for(String time:oldApp.get(1).keySet()){
					Appointments app = AppointmentGUI.getSchedule().getSchedule().get("1").get(time);
					System.out.println(AppointmentGUI.getAppointmentsByDate()+"he");
					try {
						app.updateAppointment(oldApp.get(1).get(time));
					} catch (Exception e) {
						JOptionPane.showMessageDialog(new JFrame(), "Appointment at time "+time+" could not be updated");
						e.printStackTrace();
					}
				}
				for(String time:oldApp.get(2).keySet()){
					Appointments app = AppointmentGUI.getSchedule().getSchedule().get("2").get(time);
					try {
						app.updateAppointment(oldApp.get(2).get(time));
					} catch (Exception e) {
						JOptionPane.showMessageDialog(new JFrame(), "Appointment at time "+time+" could not be updated");
						e.printStackTrace();
					}
				}

				for(String time:newApp.get(1).keySet()){
					try {
						newApp.get(1).get(time).put("Start Time", time);
						Appointments app = new Appointments(newApp.get(1).get(time));
						Appointments.makeAppointment(app);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(new JFrame(), "Appointment at time "+time+" could not be updated");
						e.printStackTrace();
					}
				}
				for(String time:newApp.get(2).keySet()){
					try {
						newApp.get(2).get(time).put("Start Time", time);
						Appointments app = new Appointments(newApp.get(2).get(time));
						Appointments.makeAppointment(app);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(new JFrame(), "Appointment at time "+time+" could not be updated");
						e.printStackTrace();
					}
				}
				
				
				
				newApp = new HashMap<>();
				newApp.put(1, new HashMap<String,HashMap<String,String>>());
				newApp.put(2, new HashMap<String,HashMap<String,String>>());

				oldApp = new HashMap<>();
				oldApp.put(1, new HashMap<String,HashMap<String,String>>());
				oldApp.put(2, new HashMap<String,HashMap<String,String>>());

				System.out.println(chair);
				if(AppointmentTableListener.chair!=null){
				AppointmentGUI.setTableGUI(chair);
				}else{
					AppointmentGUI.setTableGUI("Both");
				}
				
				AppointmentGUI.btnUpdateAppointments.setVisible(false);
				AppointmentGUI.tglbtnMove.setEnabled(true);

			}
			
		});
	}

}
