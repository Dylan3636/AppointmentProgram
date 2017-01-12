package com.omada.orthoCalendar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import com.omada.appointment.Appointments;
import com.omada.appointment.Appointments.AppointmentException;
import com.omada.database.Format;
import com.omada.database.Table;
import com.tools.fsm.DFA;
import com.tools.fsm.NFA;
import com.tools.fsm.State.StateType;
import com.tools.fsm.States;

public class DailySchedule {
	private Calendar date;
	private HashMap<String,HashMap<String,Appointments>> schedule;
	public DailySchedule(Date date){
		this.date =Calendar.getInstance();
		this.date.setTime(date);
		setSchedule();
	}
	public void setSchedule(){
		try {
			schedule = Appointments.getOccupiedTimes(Appointments.getAppointmentsByDate(Format.DATABASEDATEFORMAT.format(date.getTime())));
		} catch (AppointmentException e) {
			schedule = Appointments.getOccupiedTimes(new ArrayList<Appointments>());
		}
	}
	public Object[][] getTableFormat(String chair, String[] tableColNames){
		HashMap<String, Appointments> schedData = schedule.get(chair);
		ArrayList<Object[]> data = new ArrayList<>();
		for(String time:schedData.keySet()){
			Object[] dat = extractAppInfo(schedData.get(null), tableColNames);

			if(schedData.get(time)!=null){
				if(schedData.get(time).getStartTime().equals(time)){
					dat = extractAppInfo(schedData.get(time),tableColNames);
				}
			}
			dat[0] = time;
			data.add(dat);
		}
		data.sort(new Comparator(){

			@Override
			public int compare(Object arg0, Object arg1) {
				Object[] ar0 = (Object[])arg0;
				Object[] ar1 = (Object[])arg1;
				try {
					Date d0 = Format.TIMEFORMAT2.parse(ar0[0].toString());
					Date d1 = Format.TIMEFORMAT2.parse(ar1[0].toString());
					return d0.compareTo(d1);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 0;
			}
			
		});
		return data.toArray(new Object[0][]);
	}
	public static Object[] extractAppInfo(Appointments app,String[] tableColNames){

		Object[] rowData = new Object[tableColNames.length];
		HashMap<String, String> appAtt = new HashMap<String, String>();
		if(app == null){
			return rowData;
		}
		appAtt.putAll(app.getAttributes());
		Integer hour = Integer.parseInt(appAtt.get("Hour"));
		appAtt.remove("Hour");
		Integer minute= Integer.parseInt(appAtt.get("Minute"));
		appAtt.remove("Minute");
		appAtt.put("Payment", app.getPayment().getPayment().toString());
		appAtt.put("Time", Format.timeFormat(hour, minute));
		appAtt.put(Table.NAME, app.getPatientName());
//		appointmentsByDate.get(Integer.parseInt(chair)).put(appAtt.get("Time"), app);
		int i =0;
		for(String colName:tableColNames){
			rowData[i] = appAtt.get(colName);
			i++;
		}
		return rowData;
	
	}

	public ArrayList<String> getAppointmentStartTimes(String chair){
		ArrayList<String> list = new ArrayList<String>();
		for(String key : schedule.get(chair).keySet() ){
			if(schedule.get(chair).get(key)!=null){
				if(key.equals(schedule.get(chair).get(key).getStartTime())){
					list.add(key);
				}
			}
		}
		return list;
	}
	public void changeDate(DateChange change){
		switch (change){
		default:
			break;
		case DAYBACK:
			date.set(Calendar.DAY_OF_MONTH,date.get(Calendar.DAY_OF_MONTH)-1 );
			break;
		case DAYFORWARD:
			date.set(Calendar.DAY_OF_MONTH,date.get(Calendar.DAY_OF_MONTH)+1 );
			break;
		case WEEKBACK:
			date.set(Calendar.WEEK_OF_MONTH,date.get(Calendar.WEEK_OF_MONTH)-1 );
			break;
		case WEEKFORWARD:
			date.set(Calendar.WEEK_OF_MONTH,date.get(Calendar.WEEK_OF_MONTH)+1 );
			break;
		case MONTHBACK:
			date.set(Calendar.MONTH,date.get(Calendar.MONTH)-1 );
			break;		

		case MONTHFORWARD:
			date.set(Calendar.MONTH,date.get(Calendar.MONTH)+1 );
			break;		
		}
		setSchedule();
	}
	public void changeAppointmentDate(String chair, String time, Date date){
		HashMap<String,String> update = new HashMap<>();
		update.put("Date", Format.DATABASEDATEFORMAT.format(date));
		schedule.get(Format.chairParse(chair)).get(time).updateAppointment(update);
		schedule.get(Format.chairParse(chair)).remove(time);
	}
	public ArrayList<String> getFreeSpots(String duration,String chair){

		ArrayList<Appointments> appList=new ArrayList<>();
		
		HashMap<String, HashMap<String, Appointments>> appTimes = getSchedule();
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
		DFA dfa = freeSpotDFA(Format.durationParse(duration));
		dfa.setVisible(true);
		dfa.startSearch();
		Integer dur = Format.durationParse(duration);
		for(int j=0;j<set.length-Format.durationParse(duration);j++){
//			boolean clear=true;	
			String s = new Boolean(appTimes.get(chair).get(set[j])==null).toString().substring(0,1);
			States current = dfa.search(s);
			if(current.isFinal()){
				freeSpots.add(String.format("%s-%s", set[Math.max((j-dur),0)],set[Math.min(Math.max(j-dur,0)+Format.durationParse(duration),39)]));
			}
			}
		return freeSpots;

	}
	public static DFA freeSpotDFA(Integer duration){
		
		Integer i = new Integer(0);
		HashSet<States> states = new HashSet<>();
		HashMap<States,HashMap<String,HashSet<States>>> transition = new HashMap<>();
		HashSet<String> alphabet = new HashSet<String>();
		alphabet.add(Boolean.TRUE.toString().substring(0,1));
		alphabet.add(Boolean.FALSE.toString().substring(0,1));

		while(i<duration){
			i++;
			States state = new States(new Integer(i-1).toString());
			if((i-1)==0){
				state.setType(StateType.START);
			}
			states.add(state);
			
			
			HashMap<String,HashSet<States>> map = new HashMap<>();
			
			HashSet<States> set = new HashSet<>();
			States next = new States(i.toString());
			
			if(i.intValue()==duration.intValue()){
				next.setType(StateType.FINAL);
			}
			set.add(next);
			map.put(Boolean.TRUE.toString().substring(0,1), set);
			transition.put(state, map);			
		}
		States finalState = new States(duration.toString());
		finalState.setType(StateType.FINAL);
		
		HashSet<States> set = new HashSet<>();
		set.add(finalState);
		set.add(new States(new Integer(duration-1).toString()));
		transition.put(finalState,new HashMap<String,HashSet<States>>(){{put(Boolean.TRUE.toString().substring(0,1), set);}});
		HashSet<States> copy = new HashSet<>();
		copy.add(new States("1"));
		transition.put(new States("[]"), new HashMap<String,HashSet<States>>(){{put(Boolean.TRUE.toString().substring(0,1), copy);}});
		states.add(finalState);
		HashSet<States> start = new HashSet<>();
		HashSet<States> finish = new HashSet<>();
		System.out.println(transition);
		start.add(new States("0"));
		finish.add(finalState);
		
		NFA nfa = new NFA(states,alphabet,transition,start,finish);
		DFA dfa = nfa.toDFA();
		dfa.setVisible(true);
		return dfa;
	}
	public HashMap<String,HashMap<String,Appointments>> getSchedule() {
		return schedule;
	}
	public void setSchedule(HashMap<String,HashMap<String,Appointments>> schedule) {
		this.schedule = schedule;
	}
	public enum DateChange{
		DAYFORWARD, DAYBACK,MONTHFORWARD, MONTHBACK, WEEKFORWARD, WEEKBACK;
	}
	public static void main(String[] args){
		DailySchedule sched = new DailySchedule(Calendar.getInstance().getTime());
		System.out.println(sched.getFreeSpots("2X", "1"));
	}
}
