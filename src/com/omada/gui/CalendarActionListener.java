package com.omada.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.omada.appointment.Appointments;
import com.omada.database.Format;
import com.omada.orthoCalendar.DailySchedule.DateChange;

public class CalendarActionListener implements ActionListener {
	private Boolean move;
	CalendarActionListener(Boolean popup){
		this.move=popup;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Date date;
		Calendar cal=null;;
		DateChange change;
		switch(e.getActionCommand()){
		case "month-":
			date = AppointmentGUI.getDate();
			cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-1);	
			change=DateChange.MONTHBACK;
			break;
		case "week-":
			date = AppointmentGUI.getDate();
			cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR)-1);
			change=DateChange.WEEKBACK;

			break;
		case "day-":
			date = AppointmentGUI.getDate();
			cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)-1);
			change=DateChange.DAYBACK;

			break;
		case "day+":
			date = AppointmentGUI.getDate();
			cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+1);
			change=DateChange.DAYFORWARD;

			break;
		case "week+":
			date = AppointmentGUI.getDate();
			cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR)+1);
			change=DateChange.WEEKFORWARD;

			break;
		case "month+":
			date = AppointmentGUI.getDate();
			cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);		
			change=DateChange.MONTHFORWARD;
			break;		
		default:
				return;
		}
			AppointmentGUI.btnUpdateAppointments.setVisible(false);
			AppointmentGUI.setDate(cal.getTime());
			
			if(AppointmentGUI.tglbtnMove.isSelected()&& !AppointmentGUI.getChair().equals("Both")){
				Integer selectedRow = AppointmentGUI.getTable().getSelectedRow();
				if(selectedRow==-1){
					return;
				}
				String time = AppointmentGUI.getTable().getValueAt(selectedRow, 0).toString();
				try {
					AppointmentGUI.getSchedule().changeAppointmentDate(AppointmentGUI.getChair(), time, cal.getTime());
				}catch(NullPointerException n){
					JOptionPane.showMessageDialog(new JFrame(), "Ensure row with writting is selected!");
					return;
				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(new JFrame(), e1.getMessage());
					e1.printStackTrace();
				}
				AppointmentGUI.tglbtnMove.setEnabled(true);
				AppointmentGUI.tglbtnMove.setSelected(false);

			}
			AppointmentGUI.getSchedule().changeDate(change);			
			if(!AppointmentGUI.getChair().equals("Both")){
				AppointmentGUI.tglbtnMove.setEnabled(true);
				AppointmentGUI.tglbtnMove.setSelected(false);
				AppointmentGUI.setTableGUI(AppointmentGUI.getChair());
			}else{
				AppointmentGUI.setTableGUI(AppointmentGUI.getChair());
			}
			if(!Format.DATEFORMAT.format(AppointmentGUI.getToday()).equals(AppointmentGUI.dateField.getText())){
			try {
				AppointmentGUI.lblAppointments.setText(Format.DAYOFWEEK.format(Format.DATEFORMAT.parse(AppointmentGUI.dateField.getText()))+"'s Appointments");
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			}else{
				AppointmentGUI.lblAppointments.setText("Today's Appointments");
			}
		}
		

}
