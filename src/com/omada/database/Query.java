package com.omada.database;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.omada.main.Main;

public class Query {
static Connection myConn = Database.getconnection();
	public static HashMap<String,ArrayList<String>> basicQuery(String table_name, String[] column_names, String where){

		try{
		Statement myStat = myConn.createStatement();

		String col="";
		for(String column_name:column_names){
			col+=", " +column_name;
		}
		col=col.substring(2);

		String sql = "SELECT " + col +" "
				+ "From "+table_name
				+ "Where " + where+";";
		Main.log.doLogging(sql + "1", "info");
		ResultSet resultSet = myStat.executeQuery(sql);
		
		return extract(resultSet, column_names);}
		catch(SQLException e){
			Main.log.doLogging(e.getMessage(), "severe");
			myConn = Database.getconnection();
			return null;
		}
	}
	public static ArrayList<String> basicQuery(String table_name, String column_name){


		Statement myStat;
		try {
			myStat = myConn.createStatement();
			
			String sql = "SELECT " + column_name+" "
					+ "From "+table_name+";";
			Main.log.doLogging(sql + "2", "info");

			ResultSet resultSet = myStat.executeQuery(sql);

			ArrayList<String> list = new ArrayList<>();

			while(resultSet.next()){
				list.add(resultSet.getString(column_name));
			}
//			myConn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			Main.log.doLogging(e.getMessage(), "severe");
			myConn = Database.getconnection();
			return null;
		}


	}
	public static HashMap<String,ArrayList<String>> basicQuery(String table_name, String[] column_names){

		ResultSet resultSet;
		Statement myStat = null;
		try {
			String col="";
			for(String column_name:column_names){
				col+=", " +column_name;
			}
			col=col.substring(2);

			String sql = "SELECT " + col +" "
					+ "From "+table_name +";";

			Main.log.doLogging(sql + "3", "info");

			myStat = myConn.createStatement();
			resultSet = myStat.executeQuery(sql);
			return extract(resultSet, column_names);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Main.log.doLogging(e.getMessage() , "severe");
			myConn = Database.getconnection();
			return null;
		}}

	public static HashMap<String,ArrayList<String>> basicEq(String table_name, String[] column_names, HashMap<String,String> eq) throws QueryException{


		try {
			Statement myStat = myConn.createStatement();

			String col="";
			for(String column_name:column_names){
				col+=", " +column_name;
			}
			col=col.substring(2) + " ";
			
			Set<String> keys = eq.keySet();
			String where = "";
			for(String key: keys){
				where += String.format("%s.%s = %s and ", table_name,key,eq.get(key));
			}
			where = where.substring(0, where.length()-4);
			String sql = "SELECT " + col +" "
					+ "From "+table_name +" "
					+ "Where " + where+";";
			Main.log.doLogging(sql + "5", "info");
			ResultSet resultSet = myStat.executeQuery(sql);

			return extract(resultSet,column_names);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Main.log.doLogging(e.getMessage(), "severe");

			e.printStackTrace();
			myConn = Database.getconnection();
		}catch (NullPointerException n){
			myConn = Database.getconnection();
			if(myConn!=null){
			throw QueryException.RETRY;
			}
			}
		return null;
	}
	public static HashMap<String,ArrayList<String>> advEq(String[] table_names, String[] column_names, HashMap<String,String> eq)  {


		try {
			Statement myStat = myConn.createStatement();

			String col="";
			for(String column_name:column_names){
				col+=", " +column_name;
			}
			col=col.substring(2)+" ";
			
			String sel="";
			for(String table_name:table_names){
				sel+=", " +table_name;
			}
			sel=sel.substring(2);
			
			Set<String> keys = eq.keySet();
			String where = "";
			for(String key: keys){
				where += String.format("%s = %s and ",key, eq.get(key));
			}
			where = where.substring(0, where.length()-4);
			String sql = "SELECT " + col +" "
					+ "From "+sel+" "
					+ "Where " + where+";";
			Main.log.doLogging(sql + "6", "info");

			ResultSet resultSet = myStat.executeQuery(sql);
			return extract(resultSet,column_names);
		}catch(Exception e){
			e.printStackTrace();
			Main.log.doLogging(e.getMessage() , "severe");
			myConn = Database.getconnection();
		}
		return null;
	}
	private static HashMap<String,ArrayList<String>> extract(ResultSet resultSet, String[] column_names) throws SQLException{
		HashMap<String,ArrayList<String>> map = new HashMap<>();
		ArrayList<String> list;
		while(resultSet.next()){
		for(String column_name:column_names){
			if(!map.containsKey(column_name)){
			list = new ArrayList<>();
			}else{
				list = map.get(column_name);
			}
				String result = resultSet.getString(column_name);
				list.add(result);
			
			map.put(column_name, list);
		}
		}
//		myConn.close();
		return map;
	}
	public static void main(String[] args) throws IOException, URISyntaxException{
		System.out.println(String.format("%s.%s = %s and ", "Patients","name","james"));
		Desktop act = Desktop.getDesktop();
	
	}
	public static class QueryException extends Exception{
		public static final QueryException RETRY = new QueryException("Try Again!");
		public QueryException(String message){
			super(message);
		}
	}
}
