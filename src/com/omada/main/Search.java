package com.omada.main;

import java.util.ArrayList;

public class Search {
	public static ArrayList<String> nameSearch(ArrayList<String> names, String sub){
		ArrayList<String> found = new ArrayList<>();
		for(String name:names){
			if(name.contains(sub)){
				found.add(name);
			}
		}
		return found;
	}
}
