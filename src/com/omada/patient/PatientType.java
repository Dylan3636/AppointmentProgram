package com.omada.patient;

import org.apache.commons.lang3.text.WordUtils;

public enum PatientType {
CONSULTATION("Consultation"),OBSERVATION("Observation"),PHASE1("Phase1"),PHASE2("Phase2"),FULL("Full Active Treatment"),PARTIAL("Partial Treatment"),RETENTION ("Retention"),RETREATMENT("Retreatment");
private final String name;
	public String toString(){
		if(this.equals(FULL)){
			return "Full Active Treatment";
		}else if(this.equals(PARTIAL)){
			return "Partial Treatment";
		}else{
			return WordUtils.capitalizeFully(this.name());
		}
	}
	public Boolean equals(PatientType type){
		return type.name().equals(this.name());
	}
	private PatientType(String name){
		this.name = name;
	}
	
	public static PatientType getType(String type){
		for(PatientType pat:PatientType.values()){
			if (pat.name.equalsIgnoreCase(type)){
				return pat;
			}
		}
		return null;
	}
	public static void main(String[] args){
		System.out.println(PatientType.getType("Consultation").toString()+1);
	}
}
