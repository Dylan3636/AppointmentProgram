package com.omada.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.naming.NameNotFoundException;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.text.WordUtils;

import com.omada.patient.Patient;
import com.omada.patient.Patient.PatientException;
import com.omada.database.Format;
import com.omada.database.Query;
import com.omada.database.Table;
import com.omada.patient.*;
public class PatientButtonListener extends AbstractAction {
	JFrame mainFrame;
	JLayeredPane layeredPane;
	HashMap<String,String> attributes;
	private JFrame frame;
	public static Boolean found;
	public static String name;
	public String getName() {
		return name;
	}

	public static void  setName(String name) {
		PatientButtonListener.name = name;
	}
	public static final Exception namenotfound = PatientException.NAMENOTFOUND;
	@Override
	public void actionPerformed (ActionEvent e) {
		switch(e.getActionCommand()){
		case("Patients"): PatientGUI.patientChoose(mainFrame, layeredPane);
		break;
		case("New Patient"): PatientGUI.newPatient(mainFrame, layeredPane);
		break;
		case("Add Patient"):
				addPatient();
			break;
		case("Find Patient"):

		if(!found){
			if(!frame.isVisible()){
			frame.setVisible(true);
			}
			}
		
		if(name==null){break;}
		if(name.equals("")){break;}
			try {
				PatientGUI.previousPane=PatientGUI.patientPane;
				PatientGUI.patientProfile(Patient.findPatient(name));
				PatientGUI.patientPane.setVisible(false);
			} catch (Query.QueryException | PatientException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(new JFrame(), e1.getMessage());
			}
			found = false;
			name = null;
			break;
		default:
			break;
		}
		
	}
	
	public PatientButtonListener(JFrame mainFrame,JLayeredPane layeredPane){
		this.mainFrame=mainFrame;
		this.layeredPane = layeredPane;
	}
	public PatientButtonListener(){
		found = false;
		name = null;
		if(frame==null){
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setVisible(false);
		PatientGUI.findPatientBox(frame);
		}

	}
	public PatientButtonListener(HashMap<String,String> attributes){
		this.attributes = attributes;
	}
	public void addPatient() {
		Patient newPatient;
		try {
			newPatient = new Patient(attributes);
			newPatient.commit();
		} catch (PatientException e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getCause(),"Error!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
}
