package com.omada.patient;

import java.util.ArrayList;
import java.util.HashMap;

import com.omada.database.Query;
import com.omada.database.Update;

public class Allergy implements PatientRelation {
	private String allergy;
	private Integer patient_ID;
	private static ArrayList<String> allergies = Query.basicQuery("Allergies", "Allergy");

	public String getAllergy() {
		return allergy;
	}
	/* (non-Javadoc)
	 * @see com.omada.patient.PatientRelation#getPatient_ID()
	 */
	@Override
	public Integer getPatient_ID() {
		return patient_ID;
	}
	public static ArrayList<String> getAllergies() {
		return allergies;
	}
	public Allergy(String allergy, Integer patient_ID){
		this.allergy=allergy;
		this.patient_ID= patient_ID;
		allergies.add(allergy);
	}
	/* (non-Javadoc)
	 * @see com.omada.patient.PatientRelation#commit()
	 */
	@Override
	public void commit(){
		HashMap<String,String> hash = new HashMap(){{put("Allergy",allergy);put("Patient_ID",patient_ID.toString());}};
		Update.insert("Allergies",hash );
	}
}
