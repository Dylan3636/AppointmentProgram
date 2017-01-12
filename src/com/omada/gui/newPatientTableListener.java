package com.omada.gui;

import java.util.HashMap;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.omada.database.Format;

public class newPatientTableListener implements TableModelListener{

	String tableName;
	public void tableChanged(TableModelEvent e) {
		if(e.getType()==TableModelEvent.UPDATE){
		Integer row = e.getFirstRow();
	     Integer column = e.getColumn();
	     TableModel model = (TableModel)e.getSource();
	     String columnName = model.getColumnName(column);
	     Object data = model.getValueAt(row, column);
	     HashMap<Integer,HashMap<String,String>> rowHash;
	     if(!PatientGUI.newPatientTableHash.containsKey(tableName)){
	    	 rowHash = new HashMap<>();
	    	 PatientGUI.newPatientTableHash.put(tableName, rowHash);
	     }else{
	    	 rowHash = PatientGUI.newPatientTableHash.get(tableName);
	     }
	     HashMap<String,String> colHash;
	     if(!rowHash.containsKey(row)){
	    	 colHash = new HashMap<>();
	    	 rowHash.put(row, colHash);
	     }else{
	    	 colHash=rowHash.get(row);
	     }
	     if(!columnName.equalsIgnoreCase("Contact No. Type")){
	     colHash.put(Format.columnNameFormat(columnName), (String) data);
	     }else{
	    	 colHash.put(Format.columnNameFormat(columnName), data.toString());
	     }
	}}
	public newPatientTableListener(String tableName){
		this.tableName=tableName;
	}
		}
	