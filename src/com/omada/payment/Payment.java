package com.omada.payment;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.omada.database.Query;
import com.omada.database.Query.QueryException;
import com.omada.database.Table;
import com.omada.database.Update;
import com.omada.patient.Patient;
import com.omada.patient.Patient.PatientException;

public class Payment {
private Integer payment;
public void setPayment(Integer payment) {
	this.payment = payment;
}
private Integer patient_ID;
private boolean comitted;
private final Integer payment_id;
private Boolean update;
public Payment(String name, Integer payment) {
	this.payment = payment;
	comitted = false;
	try {
		this.patient_ID = Patient.findPatient(name).getID();
	} catch (PatientException e) {
		e.printStackTrace();
	} catch (QueryException e) {
		e.printStackTrace();
	}
	this.payment_id = (int)(100000*Math.random());
}
public Payment(String name, Integer payment, Integer payment_id) {
	this.payment = payment;
	update = true;
	try {
		this.patient_ID = Patient.findPatient(name).getID();
	} catch (PatientException e) {
		e.printStackTrace();
	} catch (QueryException e) {
		e.printStackTrace();
	}
	comitted = true;
	this.payment_id=payment_id;
}
public Payment(Integer patient_ID, Integer payment, Integer payment_ID) {
	this.patient_ID=patient_ID;
	this.payment = payment;
	this.payment_id = payment_ID;
	update = true;
	comitted = true;
}
public Integer getPayment() {
	return payment;
}
public Integer getNameID() {
	return patient_ID;
}
public Integer getPayment_id() {
	return payment_id;
}
public void commit(){
	if(comitted){
		return;
	}
	HashMap<String, String> key_values = new HashMap(){{put("Patient_ID",patient_ID.toString());
	put("Amount",payment.toString()); put("Transaction_ID",payment_id.toString());}};
	Update.insert(Table.PAYMENTS, key_values);
	comitted = true;
}
public static Payment findPayment(Integer appID){
	HashMap<String, String> eq = new HashMap<String,String>();
	eq.put("Patient_Appointments.Appointment_ID", appID.toString());
	eq.put("Payments.Transaction_ID", "Patient_Appointments.Transaction_ID");
	HashMap<String, ArrayList<String>> query = Query.advEq(new String[]{Table.PATIENT_APPOINTMENTS,Table.PAYMENTS}, new String[]{"Patient_Appointments.Patient_ID","Amount","Payments.Transaction_ID"}, eq);
	System.out.println(query);
	return new Payment(Integer.parseInt(query.get("Patient_Appointments.Patient_ID").get(0)), Integer.parseInt(query.get("Amount").get(0)), Integer.parseInt(query.get("Payments.Transaction_ID").get(0)));
}
public void commit(Integer appID){
	if(comitted){
		if(update != null){
			HashMap<String, String> key_values = new HashMap(){{put("Patient_ID",patient_ID.toString());
			put("Amount",payment.toString()); put("Transaction_ID",payment_id.toString());}};
			HashMap<String, String> eq=new HashMap<>();
			eq.put("Transaction_ID", this.getPayment_id().toString());
			try {
				Update.update(Table.PAYMENTS, key_values, eq);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return;
		}
		return;
	}
	HashMap<String, String> key_values = new HashMap(){{put("Patient_ID",patient_ID.toString());
	put("Amount",payment.toString()); put("Transaction_ID",payment_id.toString());}};
	Update.insert(Table.PAYMENTS, key_values);
	comitted = true;

	HashMap<String, String> up = new HashMap<>();
	up.put("Transaction_ID", getPayment_id().toString());
	HashMap<String, String> eq = new HashMap<>();
	eq.put("Appointment_ID", appID.toString());
	try {
		Update.update(Table.PATIENT_APPOINTMENTS, up , eq);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}
}
