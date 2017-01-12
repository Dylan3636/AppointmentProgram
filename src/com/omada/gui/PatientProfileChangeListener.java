package com.omada.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.omada.database.Table;
import com.omada.patient.Insurance_Company;

public class PatientProfileChangeListener implements ActionListener {
	private String key;
	private static HashMap<String,String> up;
	public PatientProfileChangeListener() {
		up=new HashMap<>();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource().getClass().equals(new JTextField().getClass())){
			JTextField field = (JTextField) e.getSource();
			String value = field.getText();
			String key = field.getName();
			up.put(key, value);
			try{
				System.out.println(up);
				System.out.println(PatientGUI.appointmentTableListener);
			PatientGUI.infoChangeArea.setText(up.toString()+"\n"+PatientGUI.appointmentTableListener.getHash().toString());
			}catch(Exception e1){
				e1.printStackTrace();
			}
			System.out.println(up);
		}else if (e.getSource().getClass().equals(new JComboBox().getClass())){
			JComboBox box = (JComboBox) e.getSource();
			String key = box.getName(); 
			String value = box.getSelectedItem().toString();
			up.put(key, value);
		}

		PatientGUI.btnApplyChanges.setVisible(true);
	}
	public static void refreshUp(){
		up=new HashMap<>();
	}
	public static HashMap<String,String> getUp(){
		return up;
	}
}
