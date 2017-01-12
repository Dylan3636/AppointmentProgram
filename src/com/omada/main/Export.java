package com.omada.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Export {
	public static void exportToExcel(String name,String[] columnNames ,Object[][] feat) throws IOException{
			XSSFWorkbook workbook = new XSSFWorkbook(); 
		    XSSFSheet sheet = workbook.createSheet(name);
		    int rownum = 1;
			int max =0;
			int ct = 0;
	    	Row fstRow = sheet.createRow(0);
	    	int cellN =0;
			for(Object col:columnNames){
		    		Cell cell = fstRow.createCell(cellN++); 
		    		if (col!=null){	
		    		cell.setCellValue(col.toString());	
		    		}else{
		        		cell.setCellValue("");	
		    		}
		    			
		    	
			}
		    for (Object[] objArr: feat){
		    	int cellnum=0;
		    	Row row = sheet.createRow(rownum++);
		    	for (Object obj: objArr){
		    		Cell cell = row.createCell(cellnum++); 
		    		if (obj!=null){	
		    		cell.setCellValue(obj.toString());	
		    		}else{
		        		cell.setCellValue("");	
		    		}
		    			
		    	}
		    	ct++;
		    	if(max < ((int)((((double) ct)/(feat.length)) *100))){
		    		System.out.println("Writting: " +(int)((((double) ct)/(feat.length)) *100) + "%");
		    		max = (int)((((double) ct)/(feat.length)) *100);
		    		}
		    	
		    }
				FileOutputStream out = new FileOutputStream(new File(name));
				workbook.write(out);
				out.close();
				workbook.close();
			
			}

	
}
