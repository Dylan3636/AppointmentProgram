package com.omada.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.javatuples.Pair;

import com.omada.appointment.Appointments;
import com.omada.database.Format;
import com.omada.database.Table;
import com.omada.patient.Contacts;
import com.omada.patient.Patient;

public class PatientTableChangeListener implements TableModelListener{
	private Patient patient;
	private ArrayList<Integer> newRows;
	private HashMap<Integer, HashMap<String, String>> hash;
	public HashMap<Integer, HashMap<String, String>> getHash() {
		return hash;
	}
	private String type;
	private String columnName;
	private Object data;
	public void tableChanged(TableModelEvent e) {

		if(e.getType()==TableModelEvent.INSERT){
			Integer row = e.getLastRow();
			newRows.add(row);
		}
		if(e.getType()==TableModelEvent.UPDATE){
		 Integer row = e.getFirstRow();
	     int column = e.getColumn();
	     TableModel model = (TableModel)e.getSource();
	     columnName = model.getColumnName(column);
	     data = model.getValueAt(row, column);
	     if(data!=null){
	    	 
	    	 if(columnName.equals("Free Slots")){
	    		 data=data.toString().split("-")[0];
	    		 columnName = "Start Time";
	    	 }
	    	 if(columnName.equals("Date")){
	    		 try {
					data = Format.toDatabaseFormat(data.toString());
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
	    	 }
	     if(!hash.containsKey(row)){
	    		  hash.put(row,new HashMap<String,String>(){{put(Format.columnNameFormat(columnName),data.toString());}});
	    	 }else
	    	 { hash.get(row).put(Format.columnNameFormat(columnName),data.toString());
	     }
	     System.out.println(newRows+"\n"+hash);
	     PatientGUI.btnApplyChanges.setVisible(true);}
		}
		}
	PatientTableChangeListener(Patient patient,String type, HashMap<Integer,HashMap<String,String>> hash){
		this.patient=patient;
		System.out.println(patient.getName());
		this.hash = hash;
		this.type = type;
		newRows = new ArrayList<>();
	     PatientGUI.btnApplyChanges.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					switch(type){
					case("Appointments"):
					for(Integer appPos:hash.keySet())
						if(newRows.contains(appPos)){
							try {
								hash.get(appPos).put(Table.NAME, patient.getName());
								Appointments.makeAppointment(new Appointments(hash.get(appPos)));
							} catch (Exception e) {
								JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
							}
						}else{
							try {
								PatientGUI.patientAppointments.get(appPos).updateAppointment(hash.get(appPos));
							}
						catch (Exception e) {
								JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
							};
						}
					     PatientGUI.btnApplyChanges.setVisible(false);
					PatientTableChangeListener.this.hash=new HashMap<>();
					break;
					case("Patient"):
						for(Integer appPos:hash.keySet())
							if(newRows.contains(appPos)){
								String contactType = "Patient";
								new Contacts(hash.get(appPos),patient.getID(),contactType).commit();;
							}else{
							try {
								PatientGUI.patientContacts.get(appPos).update(hash.get(appPos));
							} catch (Exception e) {
								JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
							};}
						     PatientGUI.btnApplyChanges.setVisible(false);
						break;
					case("Emergency"):
						for(Integer appPos:hash.keySet())
							if(newRows.contains(appPos)){
								String contactType = "Emergency";
								new Contacts(hash.get(appPos),patient.getID(),contactType).commit();;
							}else{
							try {
								PatientGUI.patientContacts.get(appPos).update(hash.get(appPos));
							} catch (Exception e) {
								JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
							};
							}
						     PatientGUI.btnApplyChanges.setVisible(false);
						break;
				}
					}
	     });

	}
	
}
