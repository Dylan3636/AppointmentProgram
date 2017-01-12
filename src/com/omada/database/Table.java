package com.omada.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.omada.main.Main;

public class Table {
	public static final String APPOINTMENTS = "APPOINTMENTS";
	
	public static final String INSURANCE_COMPANIES = "Insurance_Companies";
	
	public static final String CONTACTS = "Contacts";
	public static final String ContactType = "Contact_Type";
	public static final String ContactName = "Contact_Name";
	public static final String ContactNo = "Contact_No";
	public static final String ContactNoType = "Contact_No_Type";
	
	public static final String PATIENTS = "PATIENTS";
	public static final String NAME = "Name";
	public static final String PATIENT_ID = "ID";
	public static final String FIRST_NAME = "First_Name";
	public static final String LAST_NAME = "Last_Name";
	public static final String MIDDLE_NAME = "Middle_Name";
	public static final String FST_VISIT = "First_Visit_Date";
	public static final String CONTACT_NO = "Contact_No";
	public static final String AGE = "Age";
	public static final String PATIENT_TYPE = "Patient_Type";
	public static final String ALLERGIES = "Allergies";
	public static final String INSURANCE_COMPANY = "Insurance_Company";
	public static final String EMAIL = "Email";
	public static final String TITLE = "Title";
	public static final String SEX = "Sex";
	public static final String DOB = "DOB";
	
	public static final String PATIENT_APPOINTMENTS = "PATIENT_APPOINTMENTS";
	public static final String start_time="Start_Time";
	public static final String[] APPOINTMENTS_COLUMN_NAMES = new String[]{"Appointments.Appointment_ID","Name","Date","Hour","Minute","Duration","Amount","Type","Chair"};

	public static final String PAYMENTS = "PAYMENTS";
	public static final String PAYMENT_AMOUNT = "Amount";
	
	
	public static final String[] TABLENAMESARRAY ={PATIENTS,APPOINTMENTS,PATIENT_APPOINTMENTS,PAYMENTS};
	
	public static final HashMap<String,Boolean> PATIENT_COLUMNS = new HashMap<String, Boolean>(){/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{put(NAME,true);put(PATIENT_ID,true);put(FIRST_NAME,true);put(LAST_NAME,true);put(MIDDLE_NAME,false);put(FST_VISIT,false);put(INSURANCE_COMPANY,false);put(EMAIL,false);
	put(TITLE,true); put(SEX,true);put(DOB,true);put(PATIENT_TYPE,true);
	}};
	
	public static final String[] APPOINTMENTS_COL_NAMES = new String[] {
			"Date", "Start Time", "Duration", "Type", "Payment"
		};
	
	public static void constructOrho() throws ClassNotFoundException, SQLException {
		
		Connection myConn = Database.getconnection();
		Statement myStat = myConn.createStatement();
		String sql = "CREATE TABLE PATIENTS("
				+ "Name Varchar(20) not null,"
				+ "ID Integer,"
				+ "First_Visit_Date Varchar(10),"
				+ "Primary Key (ID));";
		String sql2 = "CREATE TABLE PAYMENTS("
				+ "Name_ID Integer not null,"
				+ "Transaction_ID Integer,"
				+ "Amount Integer not null,"
				+ "Primary Key (Transaction_ID),"
				+ "Foreign Key (Name_ID) References PATIENTS(ID));";
		String sql3 = "CREATE TABLE APPOINTMENTS("
				+ "Month_Year Varchar(7),"
				+ "Day Integer,"
				+ "Hour Integer,"
				+ "Minute Integer,"
				+ "Duration Varchar(2),"
				+ "Type Varchar(20),"
				+ "Appointment_ID Integer,"
				+ "Primary Key (Appointment_ID));";
		String sql4 = "CREATE TABLE PATIENT_APPOINTMENTS("
				+ "Name_ID Integer,"
				+ "Transaction_ID Integer,"
				+ "Appointment_ID Integer,"
				+ "Primary Key (Name_ID,Appointment_ID),"
				+ "Foreign Key (Name_ID) References PATIENTS (ID),"
				+ "Foreign Key (Appointment_ID) References APPOINTMENTS(Appointment_ID),"
				+ "Foreign Key (Transaction_ID) References PAYMENTS(Transaction_ID));";
		System.out.println(sql4);
		myStat.executeUpdate(sql4);
	}

}
