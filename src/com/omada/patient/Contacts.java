package com.omada.patient;

import java.sql.SQLException;
import java.util.HashMap;

import com.omada.database.Table;
import com.omada.database.Update;

public class Contacts implements PatientRelation{
	private Integer patient_ID;
	private HashMap<String,String> info;
	private String contactType;
	public String getContactType() {
		return contactType;
	}
	public HashMap<String, String> getInfo() {
		return info;
	}
	@Override
	public Integer getPatient_ID() {
		return patient_ID;
	}
	public Contacts(HashMap<String,String> info,Integer patient_ID,String type){
		this.info = info;
		this.contactType=type;
		this.patient_ID=patient_ID;
	}
	@Override
	public void commit() {
		info.put("Patient_ID",patient_ID.toString());
		info.put("Contact_Type",contactType );
		Update.insert("Contacts",info );
	}
	public void update(HashMap<String,String> up){
		String table_name = Table.CONTACTS;
		HashMap<String, String> eq = new HashMap<String,String>(){{
			put(Table.CONTACT_NO,info.get(Table.CONTACT_NO));put("Patient_ID",patient_ID.toString());
		}};
		try {
			Update.update(table_name, up, eq);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String toString(){
		return String.format("(%s) %s",info.get(Table.ContactNoType),info.get(Table.ContactNo) );
	}
	
}

