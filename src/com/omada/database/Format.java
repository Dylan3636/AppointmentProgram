package com.omada.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.javatuples.Pair;

public class Format {
	public static final SimpleDateFormat READFORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mma");
	public static final SimpleDateFormat TIMEFORMAT = new SimpleDateFormat("HH:mm");
	public static final SimpleDateFormat TIMEFORMAT2= new SimpleDateFormat("hh:mma");
	public static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("dd/MM/yyyy");
	public static final SimpleDateFormat DATABASEDATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat DAYOFWEEK = new SimpleDateFormat("EEEEEE");
	public static String timeFormat(Integer hour, Integer minute){
		try {
			return Format.TIMEFORMAT2.format(Format.TIMEFORMAT.parse(hour+":"+minute));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hour+":"+minute;
	}
	public static Pair<Integer,Integer> timeParse(String time){
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(TIMEFORMAT2.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Pair<Integer, Integer>(cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE));
	}
	public static String toDatabaseFormat(String date) throws ParseException{
		return DATABASEDATEFORMAT.format(DATEFORMAT.parse(date));
	}

	public static String syntaxParse(String value){
		try {
			value = Integer.parseInt(value)+"";
		} catch (NumberFormatException e) {
			value = "'" + value+"'";
		}		
		return value;
	}
	public static String timeSlotFormat(String strt_time, String duration){
		Integer dur = Integer.parseInt(durationParse(duration).toString());
		Pair<Integer, Integer> pair = timeParse(strt_time);
		Integer hour = pair.getValue0();
		Integer min = pair.getValue1();
		hour += (dur*15)/60;
		min += (dur*15)%60;
		return String.format("%s-%s", strt_time,Format.timeFormat(hour, min));
	}
	public static String dateFormat(Date date){
		return DATABASEDATEFORMAT.format(date);
	}
	public static String chairParse (String chair){
		return chair.substring(chair.length()-1);
	}
	public static String columnNameFormat(String value){
		if(value.contains(".")){
			String val ="";
			for(String c :value.split("\\.")){
				val += c;
			}
			return val.replace(' ', '_');
		}
		return value.replace(' ', '_');
	}
	public static Integer durationParse(String duration){
		return Integer.parseInt(duration.substring(0,duration.length()-1));
	}
	public static String nameFormat(String first,String middle,String last){
		if(middle!=null){
		return String.format("%s %s %s", first,middle,last);
		}else{
			return first + " " + last;
		}
	}

	
}
