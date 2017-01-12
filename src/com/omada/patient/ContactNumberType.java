package com.omada.patient;

public enum ContactNumberType {
	Mobile("Mobile"), Work("Work"),Home("Home");
	private String type;
	private ContactNumberType(String type){
		this.type=type;
	}
	public String toString(){
		return type;
	}
	public static void main(String[] args){
		System.out.println(ContactNumberType.Home);
	}
}
