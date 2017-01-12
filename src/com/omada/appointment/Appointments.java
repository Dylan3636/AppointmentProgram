package com.omada.appointment;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.javatuples.Pair;

import com.omada.database.Format;
import com.omada.database.Query;
import com.omada.database.Table;
import com.omada.database.Update;
import com.omada.patient.Patient;
import com.omada.payment.Payment;

public class Appointments extends Appointment {
	public Appointments(HashMap<String,String> attributes) throws Exception{
		super(attributes);		
	}
	public Appointments(Integer appointment_id, HashMap<String,String> attributes){
		super(appointment_id,attributes);		
	}
	public static void makeAppointment(Appointments app){
		Integer strt_hour = Integer.parseInt(app.getAttributes().get("Hour"));
		Integer strt_minute = Integer.parseInt(app.getAttributes().get("Minute"));
		String duration = app.getAttributes().get("Minute");


		ArrayList<Appointments> dayApps;
		boolean good=false;
		try {
			dayApps = getAppointmentsByDate(app.getAttributes().get("Date"));
			good = app.isValidAppointment(getOccupiedTimes(dayApps), app.getAttributes().get("Chair"), true);
		} catch (AppointmentException e1) {
			if(e1.equals(AppointmentException.NOAPPSONDATE)){
			good = true;
			}
			e1.printStackTrace();
		}
		
		if(good){
			try {
				app.commit();
				JOptionPane.showMessageDialog(new JFrame(), "Appointment added successfully!"); 
			} catch (Exception e) {
				JOptionPane.showMessageDialog(new JFrame(), "Error! Appointment not added!"); 
				e.printStackTrace();
			}

		}
	}
	public void updateAppointment(HashMap<String,String> update) {
		HashMap<String,String> eq =new HashMap<>();

		String table_name="APPOINTMENTS";
		Appointments app = this;
		if(update.containsKey("Patient_ID")){
			try {
				Update.update(Table.PATIENT_APPOINTMENTS, new HashMap<String,String>(){{put("Patient_ID",update.get("Patient_ID"));}}, new HashMap<String,String>(){{put("Appointment_ID",getAppointment_id().toString());}});
			} catch (SQLException e) {
				e.printStackTrace();
			}
			update.remove("Patient_ID");
		}
		if(update.containsKey("Payment")){
			Payment p = Payment.findPayment(this.getAppointment_id());
			p.setPayment((int)Integer.parseInt(update.get("Payment")));
			super.addPayment(p);
			update.remove("Payment");
		}
		boolean good=false;
		if(update.isEmpty()){
			return;
		}
		if(update.containsKey("Start Time")){
			Pair<Integer,Integer> time = Format.timeParse(update.get("Start Time"));
			Integer hr = time.getValue0();
			Integer min = time.getValue1();
			update.remove("Start Time");
			update.put("Hour", hr.toString());
			update.put("Minute", min.toString());
		}else if(update.containsKey("Start_Time")){
			Pair<Integer,Integer> time = Format.timeParse(update.get("Start_Time"));
			Integer hr = time.getValue0();
			Integer min = time.getValue1();
			update.remove("Start_Time");
			update.put("Hour", hr.toString());
			update.put("Minute", min.toString());
		}
		HashMap<String,String> up = new HashMap<>();
		up.putAll(this.getAttributes());
		up.putAll(update);
		this.setAttributes(up);
		try {
			ArrayList<Appointments> dayApps = getAppointmentsByDate(app.getAttributes().get("Date"));
			good = app.isValidAppointment(getOccupiedTimes(dayApps), app.getAttributes().get("Chair"), true);
		} catch (AppointmentException e1) {
			good = true;
			e1.printStackTrace();
		}
		if(good){
				update.put("Chair", up.get("Chair"));
		eq.put("Appointment_ID",app.getAppointment_id().toString());
		try {
			Update.update(table_name, update, eq);
			JOptionPane.showMessageDialog(new JFrame(), "Appointment updated successfully!"); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		}

}
	public static HashMap<String, HashMap<String, Appointments>> getOccupiedTimes(ArrayList<Appointments> apps){

		HashMap<String,HashMap<String,Appointments>> appTimes = new HashMap<>();
		appTimes.put("1", new HashMap<>());
		appTimes.put("2", new HashMap<>());

		Calendar cal = Calendar.getInstance();
for(Integer i = 0 ;i<40;i++){

	cal.set(Calendar.HOUR_OF_DAY, (i/4)+8);
	cal.set(Calendar.MINUTE, (i%4)*15);
	appTimes.get("1").put(Format.TIMEFORMAT2.format(cal.getTime()),null);
	appTimes.get("2").put(Format.TIMEFORMAT2.format(cal.getTime()),null);
}

			for(Appointments app:apps){				
					Pair<Integer, Integer> pair = Format.timeParse(app.getStartTime());
					cal.set(Calendar.HOUR_OF_DAY, pair.getValue0());
					cal.set(Calendar.MINUTE, pair.getValue1());
						Integer dur = Format.durationParse(app.getDuration());
						int i=0;
						String chair = app.getAttributes().get("Chair");
						while(i<dur){
							cal.set(Calendar.HOUR_OF_DAY, (pair.getValue0()+((pair.getValue1()+i*15)/60)));
							cal.set(Calendar.MINUTE, (pair.getValue1()+(i*15))%60);
							appTimes.get(chair).put(Format.TIMEFORMAT2.format(cal.getTime()), app);
							i++;
						}
				
					

		}	
		return appTimes;
	}
	public Boolean isValidAppointment(HashMap<String,HashMap<String,Appointments>>appTimes,String chair, boolean check){
		Integer hour = Integer.parseInt(getAttributes().get("Hour"));
		Integer minute = Integer.parseInt(getAttributes().get("Minute"));
		Calendar cal = Calendar.getInstance();
		ArrayList<String> occTime = new ArrayList<>();
		
		for(int i = 0 ;i<Format.durationParse(getDuration());i++){
			cal.set(Calendar.HOUR_OF_DAY, hour + (minute +(i*15))/60);
			cal.set(Calendar.MINUTE, (minute +(i*15))%60);
			occTime.add(Format.TIMEFORMAT2.format(cal.getTime()));
		}

		boolean valid= true;
		Appointments app2 = null;
		for(String time :occTime){
			if(appTimes.get(chair).get(time)!=null){
				if(!appTimes.get(chair).get(time).getAppointment_id().equals( this.appointment_id)){
				app2 = appTimes.get(chair).get(time);
				valid =false;
				break;
				}
			};
		}

		if(!valid &&check){
		int opt = JOptionPane.showOptionDialog(new JFrame(), String.format("Conflict with %s %s appointment at %s. Would you like to swap chairs and try again or find empty appointment slots?",app2.getPatientName(),app2.getAttributes().get("Duration"),Format.timeFormat(Integer.parseInt(app2.getAttributes().get("Hour")),Integer.parseInt(app2.getAttributes().get("Minute")))),"Appointment Conflict",  JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.DEFAULT_OPTION,  null, new String[]{"Change Chair","Find Empty Slots","Cancel"},"Change Chair" );
		if(opt==0){
			if (this.getAttributes().get("Chair").equals("1")){
				chair = "2";
				this.getAttributes().put("Chair","2" );
			}else{
				chair= "1";
				this.getAttributes().put("Chair", "1");
			}
			return this.isValidAppointment(appTimes, chair,false);
		}else if(opt==1){
		String[] set = appTimes.get(chair).keySet().toArray(new String[0]);
		Arrays.sort(set,new Comparator<String>(){

			@Override
			public int compare(String o1, String o2) {
				try {
					return Format.TIMEFORMAT2.parse(o1).compareTo(Format.TIMEFORMAT2.parse(o2));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return 0;
			}
			
		});
		ArrayList<String> freeSpots = new ArrayList<>();
		for(int j=0;j<set.length-Format.durationParse(this.getDuration());j++){
			boolean clear=true;	
			if(appTimes.get(chair).get(set[j])==null){
					
					for(int i = 0;i<Format.durationParse(this.getDuration());i++){
					clear =appTimes.get(chair).get(set[j +i])==null;
					if(!clear)
						break;
					}
					if(clear){
						freeSpots.add(String.format("%s-%s", set[j],set[Math.min(j+Format.durationParse(getDuration()),39)]));
					}
				}
			}
		freeSlots(freeSpots.toArray(new String[0]), this);
//		JOptionPane.showOptionDialog(new JFrame(), "Free spots", "Free Time", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, freeSpots.toArray(), freeSpots.get(0));
		}
		}

		return valid;
	}
public static ArrayList<String> getFreeSpotsList(String date, String duration, String chair){
	ArrayList<Appointments> appList=new ArrayList<>();
	try {
		appList = getAppointmentsByDate(date);
	} catch (AppointmentException e1) {
		e1.printStackTrace();
	}
	System.out.println(appList);
	
	HashMap<String, HashMap<String, Appointments>> appTimes = getOccupiedTimes(appList);
	ArrayList<String> freeSpots = new ArrayList<>();
	String[] set = appTimes.get(chair).keySet().toArray(new String[0]);
	Arrays.sort(set,new Comparator<String>(){

		@Override
		public int compare(String o1, String o2) {
			try {
				return Format.TIMEFORMAT2.parse(o1).compareTo(Format.TIMEFORMAT2.parse(o2));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return 0;
		}
		
	});

	for(int j=0;j<set.length-Format.durationParse(duration);j++){
		boolean clear=true;	
		if(appTimes.get(chair).get(set[j])==null){
				
				for(int i = 0;i<Format.durationParse(duration);i++){
				clear =appTimes.get(chair).get(set[j +i])==null;
				if(!clear)
					break;
				}
				if(clear){
					freeSpots.add(String.format("%s-%s", set[j],set[Math.min(j+Format.durationParse(duration),39)]));
				}
			}
		}
	return freeSpots;

}
public static ArrayList<Appointments> getAppointmentsByDate(String date) throws AppointmentException{
	String[] table_name = Table.TABLENAMESARRAY;		
	String[] column_names = new String[]{"Appointments.Appointment_ID","Payments.Transaction_ID","Name","Date","Hour","Minute","Duration","Amount","Chair"};

	HashMap<String, String> eq = new HashMap<>();		
	try {
		eq.put("APPOINTMENTS.date", Format.syntaxParse(Format.DATABASEDATEFORMAT.format(Format.DATEFORMAT.parse(date))));
	} catch (ParseException e1) {
		eq.put("APPOINTMENTS.date", Format.syntaxParse(date));
	}
	eq.put("APPOINTMENTS.APPOINTMENT_ID","PATIENT_APPOINTMENTS.APPOINTMENT_ID" );
	eq.put("PATIENTS.ID", "PATIENT_APPOINTMENTS.PATIENT_ID");
	eq.put("PAYMENTS.Transaction_ID", "ifnull(PATIENT_APPOINTMENTS.Transaction_ID,0)");
	HashMap<String, ArrayList<String>> query = Query.advEq(table_name, column_names, eq);

	ArrayList<Appointments> appointments = new ArrayList<>();
	try{
		for(int i =0;i<query.get("Name").size();i++){
			Integer app_id = null;
			Integer hour=null;
			Integer minute=null;
			HashMap<String,String> attributes=new HashMap<>();
			for(String key:query.keySet()){
				if(key.equalsIgnoreCase("Appointments.Appointment_ID")){
					app_id = Integer.parseInt(query.get(key).get(i));
				}else if(key.equalsIgnoreCase("Hour")){hour = Integer.parseInt(query.get(key).get(i));}else if(key.equalsIgnoreCase("Minute")){minute =Integer.parseInt(query.get(key).get(i));}
				else{
					attributes.put(key, query.get(key).get(i));
				}
			}

			String time = Format.timeFormat(hour, minute);
			attributes.put(Table.start_time, time);

			Appointments app;
			app= new Appointments(app_id,attributes);
			appointments.add(app);


		}
	}catch (NullPointerException e){
		throw AppointmentException.NOAPPSONDATE;
	}
	return appointments;
}

public static Appointment findAppointment(String name,String date,String time) throws Exception{
	String[] table_name = Table.TABLENAMESARRAY;		
	String[] column_names =Table.APPOINTMENTS_COLUMN_NAMES;
	Date date2 = Format.READFORMAT.parse(date + " " + time); 
	String[] tim = Format.TIMEFORMAT.format(date2).split("(:)");

	Integer hour = Integer.parseInt(tim[0]);
	Integer minute = Integer.parseInt(tim[1]);

	HashMap<String, String> eq = new HashMap<>();		
	eq.put("APPOINTMENTS.date", Format.syntaxParse(date));
	eq.put("PATIENTS.Name", Format.syntaxParse(name));
	eq.put("APPOINTMENTS.Hour", hour.toString());
	eq.put("APPOINTMENTS.Minute", minute.toString());

	eq.put("APPOINTMENTS.APPOINTMENT_ID","PATIENT_APPOINTMENTS.APPOINTMENT_ID" );
	eq.put("PATIENTS.ID", "PATIENT_APPOINTMENTS.PATIENT_ID");
	eq.put("PAYMENTS.Transaction_ID", "ifnull(PATIENT_APPOINTMENTS.Transaction_ID,0)");
	HashMap<String, ArrayList<String>> query = Query.advEq(table_name, column_names, eq);

	ArrayList<Appointments> appointments = new ArrayList<>();

	try{
		for(int i =0;i<query.get("Name").size();i++){
			Integer app_id = null;
			HashMap<String,String> attributes=new HashMap<>();
			for(String key:query.keySet()){
				if(key.equalsIgnoreCase("Appointments.Appointment_ID")){
					app_id = Integer.parseInt(query.get(key).get(i));
				}else if(key.equalsIgnoreCase("Hour")){hour = Integer.parseInt(query.get(key).get(i));}else if(key.equalsIgnoreCase("Minute")){minute = Integer.parseInt(query.get(key).get(i));}else{
					attributes.put(key, query.get(key).get(i));
				}
			}
			String strt_time = Format.timeFormat(hour, minute);
			attributes.put(Table.start_time, strt_time);
			Appointments app;
			app= new Appointments(app_id,attributes);
			appointments.add(app);
		}}catch (NullPointerException e){
			throw AppointmentException.NOAPPSONDATE;
		}
	return appointments.get(0);

}

protected void commit() throws SQLException,Exception{
	Integer appointment_id = super.getAppointment_id();
	Integer patient_id = Patient.getPatientID(super.getPatientName());

	HashMap<String,String> key_values = new HashMap<>();

	key_values.put("appointment_id", appointment_id.toString());
	key_values.put("patient_id", patient_id.toString());

	super.commit();		
	if(super.getPayment()!=null){
		key_values.put("Transaction_ID", super.getPayment().getPayment_id().toString());
	}
	Update.insert("Patient_appointments", key_values);
}
public static class AppointmentException extends Exception{
	public static final AppointmentException NOAPPSONDATE= new AppointmentException("No Appointments on that date!");
	public AppointmentException(String m){
		super(m);
	}
}

public static void main(String[] args){
	String pair = Format.timeFormat(1, 60);
	System.out.println(pair);
}
}
