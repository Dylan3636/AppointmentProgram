package com.omada.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.omada.database.Database.DatabaseException;
import com.omada.main.Main;

public class Update {
	private static Connection myConn;
	public static String insert(String table,HashMap<String,String> key_values){
		
		myConn = Database.getconnection();
		Statement myStat = null;
		try {
			myStat = myConn.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		Set<String> keySet = key_values.keySet();
		Collection<String> valueSet = key_values.values();
		
		String keys ="(";
		for(String key:keySet){
			keys += key +", ";
		}
		keys = keys.substring(0, keys.length()-2);
		keys +=")";
		
		String values ="(";
		for(String value:valueSet){
			try {
				values += Integer.parseInt(value) +", ";
			} catch (NumberFormatException e) {
				values += "'" + value+"'" +", ";
			}
		}
		values = values.substring(0, values.length()-2);
		values +=")";
		
		String sql = String.format("Insert into %s %s values %s",table,keys,values);
		System.out.println(sql);
		Main.log.doLogging(sql, "info");
		try {
			myStat.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			Main.log.doLogging(e.getCause().toString(), "severe");

			return(e.toString());
		}
		return ("Succsesfully Updated " + table);		
	}
	public static void update(String table_name,HashMap<String,String> up,HashMap<String,String> eq ) throws SQLException{
		Connection myConn = Database.getconnection();
		try {
			Statement myStat = myConn.createStatement();
			
			Set<String> keys = up.keySet();
			String set = "";
			for(String key:keys){
				set += String.format("%s=%s, ",key,Format.syntaxParse(up.get(key)));
			}
			set= set.substring(0, set.length()-2);
			
			keys = eq.keySet();
			String where = "";
			for(String key:keys){
				where += String.format("%s=%s and ",key,Format.syntaxParse(eq.get(key)));
			}
			where = where.substring(0, where.length()-4);
			String sql = String.format("UPDATE %s Set %s Where %s;", table_name,set,where);
			System.out.println(sql);
			Main.log.doLogging(sql, "info");
			myStat.executeUpdate(sql);
			JOptionPane.showMessageDialog(new JFrame(),"Update Successfull!");
		} catch (SQLException e) {
			
			JOptionPane.showMessageDialog(new JFrame(),"Could not update "+table_name,"Update Unsuccessfull!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			Main.log.doLogging(e.getCause().toString(), "severe");
			throw e;
		}
	}
	public static void delete(String table_name,HashMap<String,String> eq ){
		Connection myConn = Database.getconnection();
		try {
			Statement myStat = myConn.createStatement();
			Set<String> keys = eq.keySet();
			String where = "";
			for(String key:keys){
				where += String.format("%s=%s and ",key,Format.syntaxParse(eq.get(key)));
			}
			where = where.substring(0, where.length()-4);
			String sql = String.format("delete from %s where %s",table_name, where);
			Main.log.doLogging(sql, "info");
			
			myStat.executeUpdate(sql);
			JOptionPane.showMessageDialog(new JFrame(),"Deletion Successfull!");

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		HashMap<String, String> key_values = new HashMap<>();
		
		
		key_values.put("ID", "1234567");
		key_values.put("Name", "John Doe");
		key_values.put("First_Visit_Date", "12/06/2016");
		String table = "Patients";
		
//		System.out.println(Insert.insert(table, key_values));
		Set<String> keySet = key_values.keySet();
		Collection<String> valueSet = key_values.values();
		
		String keys ="(";
		for(String key:keySet){
			keys += key +",";
		}
		keys = keys.substring(0, keys.length()-1);
		keys +=")";
		
		String values ="(";
		for(String value:valueSet){
			try {
				values += Integer.parseInt(value) +", ";
			} catch (NumberFormatException e) {
				values += "'" + value+"'" +", ";
			}		}
		values = values.substring(0, values.length()-1);
		values +=")";
		
		@SuppressWarnings("unused")
		String sql = String.format("Insert into %s %s values %s",table,keys,values);

		System.out.println((int)(1000000*Math.random()));
	}
	
}
