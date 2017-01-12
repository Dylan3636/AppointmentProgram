package com.omada.patient;

import java.util.ArrayList;
import java.util.HashMap;

import com.omada.database.Query;
import com.omada.database.Update;

public abstract class Insurance_Company {
	
	private String insurance_Company;
	private static ArrayList<String> insurance_Companies = Query.basicQuery("Insurance_Companies", "Insurance_Company");
	public static ArrayList<String> getInsurance_Companies() {
		return insurance_Companies;
	}
	public static void addInsuranceCompany(String company) {
		HashMap<String,String> hash = new HashMap(){{put("Insurance_Company",company);}};
		Update.insert("Insurance_Companies",hash );
	}

}
