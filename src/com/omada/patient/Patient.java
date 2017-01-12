package com.omada.patient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.tuple.Triple;

import com.omada.appointment.Appointment;
import com.omada.appointment.Appointments;
import com.omada.database.Format;
import com.omada.database.Query;
import com.omada.database.Query.QueryException;
import com.omada.patient.Patient.PatientException;
import com.omada.database.Table;
import com.omada.database.Update;

public class Patient {
	private static final String PATIENTS = "PATIENTS"; 
	private static final String NAME = "Name";
	private static final String ID = "ID";

	private static final Set<String> keySet = Table.PATIENT_COLUMNS.keySet();
	private static HashMap< String,ArrayList<String>> currentNames=Query.basicQuery("Patients",new String[]{NAME,ID});
	
	public static HashMap<String, ArrayList<String>> getCurrentNames() {
		return currentNames;
	}
	private String name;
	private Integer id; 
	private HashMap<String, String> attributes;
	private Integer age;
	private PatientType patientType;
	public PatientType getPatientType() {
		return patientType;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge() {
		try {
			Calendar dob =Calendar.getInstance();
			dob.setTime(Format.DATABASEDATEFORMAT.parse(attributes.get(Table.DOB)));
			Calendar today = Calendar.getInstance();
			if(dob.get(Calendar.MONTH)<today.get(Calendar.MONTH)){
				age =(today.get(Calendar.YEAR)-dob.get(Calendar.YEAR));
			}else if(dob.get(Calendar.DAY_OF_MONTH)>=today.get(Calendar.DAY_OF_MONTH)){
				age =(today.get(Calendar.YEAR)-dob.get(Calendar.YEAR));
			}else{
				age = (today.get(Calendar.YEAR)-dob.get(Calendar.YEAR)-1);
			}			
		} catch (ParseException e) {
			
}
	}

	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public Patient (HashMap<String,String> attributes) throws PatientException{
		patientType = PatientType.getType( attributes.get("Patient_Type"));	
		try{
				attributes = setName(attributes);
			}catch(PatientException e){
					int ans = JOptionPane.showConfirmDialog(new JFrame(), e.getMessage(),"Are you sure?",JOptionPane.INFORMATION_MESSAGE);
					if(ans==0){
						currentNames.get(Table.NAME).remove(attributes.get(Table.NAME));
						try {
							setName(attributes);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}else{
						return;
					}
			}
			try {
				attributes.put(Table.DOB, Format.DATABASEDATEFORMAT.format(Format.DATEFORMAT.parse(attributes.get(Table.DOB))));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.attributes=attributes;
			setAge();
	}

	private Patient(HashMap<String,String> attributes,Integer id){
		patientType = PatientType.getType( attributes.get("Patient_Type"));	
		attributes.remove("Patient_Type");
		this.attributes=attributes;
		this.id = id;
		this.name=attributes.get(Table.NAME);
		setAge();
	}

	
	public HashMap<String,String> setName(HashMap<String,String> att) throws PatientException {
		String name = att.get(Table.NAME);
		if(!currentNames.get(NAME).contains(name)){
			String str = Query.basicQuery(PATIENTS, "MAX("+ID+")").get(0);
			currentNames.get(Table.NAME).add(name);
			this.id = Integer.parseInt(str)+1;
			this.name = name;
		}else{
			throw PatientException.DUPLICATENAME;
		}
		att.put(Table.PATIENT_ID, id.toString());
		return att
				;
	}

	public String getName() {
		return name;
	}
	public Integer getID() {
		return id;
	}
	public void setAttribute(String key,  String value){
		this.attributes.put(key, value);
	}
	public String commit() throws PatientException{
		attributes.put(Table.PATIENT_ID, getID().toString());
		attributes.put(Table.PATIENT_TYPE, getPatientType().toString());
		for(String key:keySet){
			if(Table.PATIENT_COLUMNS.get(key)){
				try{
				if(attributes.get(key).equals("")||attributes.get(key)==null){
						throw (new PatientException(String.format("%s field cannot be empty!", key)));
				}
				}catch(NullPointerException e){
					System.out.println(attributes.get(key));
					throw (new PatientException(String.format("%s field cannot be empty!", key)));
				}
			}}

		return Update.insert("PATIENTS", attributes);
	}

	public static String punctName(String name){
		if(name.charAt(name.length()-1)=='s'){
			return name+"'";
		}else return name +"'s";
	}
	public HashMap<String, ArrayList<String>> getContactInfo(String type){
		String[] table_names=new String[]{Table.PATIENTS,Table.CONTACTS};
		String[] column_names= new String[]{Table.PATIENT_ID,Table.ContactName,Table.ContactNo,Table.ContactNoType};
		HashMap<String, String> eq= new HashMap<String,String>(){{
			put(Table.PATIENT_ID,getID().toString()); put(Table.ContactType,Format.syntaxParse(type));
			put("Patients."+Table.PATIENT_ID,Table.CONTACTS+"."+"Patient_ID");
		}};
		return Query.advEq(table_names, column_names, eq);
	}
	public ArrayList<Contacts> getContacts(String type){

		String[] table_names=new String[]{Table.PATIENTS,Table.CONTACTS};
		String[] column_names= new String[]{Table.PATIENT_ID,Table.ContactName,Table.ContactNo,Table.ContactNoType};
		HashMap<String, String> eq= new HashMap<String,String>(){{
			put(Table.PATIENT_ID,getID().toString()); put(Table.ContactType,Format.syntaxParse(type));
			put("Patients."+Table.PATIENT_ID,Table.CONTACTS+"."+"Patient_ID");
		}};
		HashMap<String, ArrayList<String>> query = Query.advEq(table_names, column_names, eq);
		ArrayList<Contacts> contacts=new ArrayList<>();
		for(int i =0; i<query.get(Table.PATIENT_ID).size();i++){
			HashMap<String, String> map = new HashMap<String,String>();
			for(String key :query.keySet()){
			if(key.equals(Table.PATIENT_ID)){
				Integer id = Integer.parseInt(query.get(key).get(i));
			}else{
				map.put(key, query.get(key).get(i));
			}
			
		}
		Contacts c = new Contacts(map,id,type);
		contacts.add(c);
		}
		return contacts;
	}
	public static boolean validPatientName(String patient) throws Exception {
		if(currentNames.get(NAME).contains(patient)){
			return true;
		}else{
			throw PatientException.NAMENOTFOUND;
		}

	}
	public void updatePatient(HashMap<String,String> up){
		HashMap<String,String> eq =new HashMap<>();

		String table_name=PATIENTS;
		eq.put(Table.PATIENT_ID,getID().toString());
		try {
			Update.update(table_name, up, eq);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static Patient findPatient(String name) throws PatientException, QueryException {
		HashMap <String,String> hash = new HashMap<String,String>();
		hash.put(NAME,Format.syntaxParse(name));
		String[] cols = keySet.toArray(new String[]{});
		HashMap<String,ArrayList<String>> query = Query.basicEq(PATIENTS, cols, hash);
		HashMap<String,String> attr = new HashMap<>();
		if(query.get(Table.PATIENT_ID)==null){
			throw PatientException.NAMENOTFOUND;
		}
		for(String key:keySet){
			attr.put(key, query.get(key).get(0));
		}
		Integer id = Integer.parseInt(attr.get(Table.PATIENT_ID));
		attr.remove(Table.PATIENT_ID);
		return new Patient(attr,id);
	}
	public static Integer getPatientID(String name) throws QueryException, PatientException  {

		HashMap <String,String> hash = new HashMap<String,String>();
		hash.put(NAME,Format.syntaxParse(name));
		HashMap<String,ArrayList<String>> query = Query.basicEq(PATIENTS, new String[]{ID}, hash);
		System.out.println(query.keySet());
		if(query.get(ID).isEmpty()){
			throw PatientException.NAMENOTFOUND;
		}
		return Integer.parseInt(query.get(ID).get(0));
	}
	public static ArrayList<Appointments> getPatientAppointments(String name) throws Exception{
		Integer pat_id = getPatientID(name);
		HashMap<String,String> eq = new HashMap<>();
		eq.put("Patients." + ID, pat_id.toString());
		eq.put("Patient_Appointments.Patient_ID", pat_id.toString());
		eq.put("Patient_Appointments.Appointment_ID", "Appointments.Appointment_ID");
		eq.put("Patient_Appointments.Transaction_ID", "Payments.Transaction_ID");
		HashMap<String,ArrayList<String>> query = Query.advEq(new String[] {"PATIENTS","PAYMENTS","PATIENT_APPOINTMENTS","APPOINTMENTS"}, Table.APPOINTMENTS_COLUMN_NAMES , eq);
		ArrayList<Appointments> table = new ArrayList<>();
		if(query.get(Table.NAME)==null){
			throw new Exception ("Patient has no recent Appointments");
		}
		for(Integer i =0; i<query.get(Table.NAME).size();i++){
			HashMap<String,String> row = new HashMap<>();
			Integer app_id = null;
			Integer hour = null;
			Integer minute = null;
			for (String key: query.keySet()){
			if(key.equalsIgnoreCase("Appointments.Appointment_ID")){
				app_id = Integer.parseInt(query.get(key).get(i));
			}else if(key.equalsIgnoreCase("Hour")){hour = Integer.parseInt(query.get(key).get(i));}else if(key.equalsIgnoreCase("Minute")){minute =Integer.parseInt(query.get(key).get(i));}else {
				row.put(key,query.get(key).get(i));
			}
			}
			String strt_time = Format.timeFormat(hour, minute);
			row.remove("Hour");
			row.remove("Minute");
			row.put("Start_Time", strt_time);
			table.add(new Appointments(app_id,row));
		}
		return table;
	}
	public static void main(String[] args) throws Exception{
		String name = "John Doe";
		String name2 = "Jane Doe";
		String first = "Jane";
		String last = "Doe";
		String mid = " ";
		HashMap<String, String> att = new HashMap<String,String>(){{put(Table.FIRST_NAME,first);put(Table.LAST_NAME,last);put(Table.MIDDLE_NAME,mid);put(Table.NAME,first+mid+last);}};
//		
		Calendar dob =Calendar.getInstance();
		dob.setTime(Format.DATEFORMAT.parse("22/06/1985"));
		Calendar today = Calendar.getInstance();
		if(dob.get(Calendar.MONTH)>today.get(Calendar.MONTH)){
			System.out.print(dob.get(Calendar.YEAR)-today.get(Calendar.YEAR));
		}else if(dob.get(Calendar.DAY_OF_MONTH)>=today.get(Calendar.DAY_OF_MONTH)){
			System.out.print(-dob.get(Calendar.YEAR)+today.get(Calendar.YEAR));
		}else{
			System.out.print(-dob.get(Calendar.YEAR)+today.get(Calendar.YEAR)+-1);
		}
	}
	public static class PatientException extends Exception{
		
		public static final Patient.PatientException NAMENOTFOUND = new Patient.PatientException("Name not found in Database!");
		public static final Patient.PatientException DUPLICATENAME = new Patient.PatientException("Potential Duplicate Name!");

		public PatientException(String m){
			super(m);
		}
	}
	
}
