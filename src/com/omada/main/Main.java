package com.omada.main;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Component;
import java.awt.Insets;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.CardLayout;
import javax.swing.SpringLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Font;
import javax.swing.table.DefaultTableModel;

import com.omada.appointment.Appointments;
import com.omada.database.Database;
import com.omada.database.Database.DatabaseException;
import com.omada.gui.AppointmentButtonListener;
import com.omada.gui.PatientButtonListener;
import com.omada.orthoCalendar.DailySchedule;
import com.omada.database.Format;
import com.omada.patient.Patient;

import java.awt.Window.Type;
import java.awt.Frame;
public class Main {
	 static File ipFile = new File("ip");
	public static String URL;
//	public static final String URL ="jdbc:mysql://localhost:3306/orthoDatabase?&useSSL=false";

	public static final String DRIVER = "com.mysql.jdbc.Driver";
//	public static String user = "Admin";
//	public static String password = "$password";

	public static String user = "root";
	public static String password = "password";

	private static JTable table;
	private static JFrame mainFrame;
	private static JLayeredPane mainLayeredPane;
	public static Log log;
	static PrintWriter WRITER;
	private static String ip;
	public static Integer count =1;
	public static JLayeredPane getMainLayeredPane() {
		return mainLayeredPane;
	}

	public static JFrame getMainFrame() {
		return mainFrame;
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, FileNotFoundException, UnsupportedEncodingException, DatabaseException{
		
		In in = new In(new File("ip.txt"));
		ip = in.readAll();
		in.close();
		URL ="jdbc:mysql://"+ip+":3306/orthoDatabase?&useSSL=false"; 
		Database.setconnection();
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		}
		log = new Log();
		log.doLogging("Start up","info");
		
		
		mainFrame = new JFrame();
		mainFrame.setTitle("Omada Centre");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(1345, 786);
		mainFrame.setLocationRelativeTo(null);
		
		mainLayeredPane = new JLayeredPane();
		mainLayeredPane.setBounds(250, 47, 1, 1);
		mainFrame.getContentPane().add(mainLayeredPane, BorderLayout.CENTER);
		GridBagLayout gbl_layeredPane = new GridBagLayout();
		gbl_layeredPane.columnWidths = new int[]{200, 887, 200, 0};
		gbl_layeredPane.rowHeights = new int[]{56, 39, 264, 48, 45, 48, 0};
		gbl_layeredPane.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_layeredPane.rowWeights = new double[]{0.0, 1.0, 0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		mainLayeredPane.setLayout(gbl_layeredPane);
				JButton Administration = new JButton("Administration");
				Administration.setActionCommand("Administration");
				Administration.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
					}
				});
				
				JLabel lblNoAppointments = new JLabel ("No Appointments Today");
				lblNoAppointments.setFont(new Font("SansSerif", Font.ITALIC, 30));
				lblNoAppointments.setVisible(false);
				GridBagConstraints gbc_lblNoAppointments = new GridBagConstraints();
				gbc_lblNoAppointments.insets = new Insets(0, 0, 5, 0);
				gbc_lblNoAppointments.gridwidth = 3;
				gbc_lblNoAppointments.gridx = 0;
				gbc_lblNoAppointments.gridy = 1;
				mainLayeredPane.add(lblNoAppointments, gbc_lblNoAppointments);
				
				JLabel lblTodaysAppointments = new JLabel("Today's Appointments");
				lblTodaysAppointments.setName("Today's Appointments");
				lblTodaysAppointments.setFont(new Font("SansSerif", Font.ITALIC, 25));
				GridBagConstraints gbc_lblTodaysAppointments = new GridBagConstraints();
				gbc_lblTodaysAppointments.insets = new Insets(0, 0, 5, 5);
				gbc_lblTodaysAppointments.gridx = 1;
				gbc_lblTodaysAppointments.gridy = 1;
				mainLayeredPane.add(lblTodaysAppointments, gbc_lblTodaysAppointments);
				JButton btnPatients = new JButton("Patients");
				mainLayeredPane.setLayer(btnPatients, 0);
				GridBagConstraints gbc_btnPatients = new GridBagConstraints();
				gbc_btnPatients.fill = GridBagConstraints.HORIZONTAL;
				gbc_btnPatients.insets = new Insets(0, 0, 5, 5);
				gbc_btnPatients.gridx = 0;
				gbc_btnPatients.gridy = 3;
				mainLayeredPane.add(btnPatients, gbc_btnPatients);
				
				btnPatients.addActionListener(new PatientButtonListener(mainFrame, mainLayeredPane));
				GridBagConstraints gbc_Administration = new GridBagConstraints();
				gbc_Administration.insets = new Insets(0, 0, 5, 0);
				gbc_Administration.fill = GridBagConstraints.BOTH;
				gbc_Administration.gridx = 2;
				gbc_Administration.gridy = 4;
				mainLayeredPane.add(Administration, gbc_Administration);
		
				JButton btnAppointments = new JButton("Appointments");
				GridBagConstraints gbc_btnAppointments = new GridBagConstraints();
				gbc_btnAppointments.fill = GridBagConstraints.HORIZONTAL;
				gbc_btnAppointments.insets = new Insets(0, 0, 0, 5);
				gbc_btnAppointments.gridx = 0;
				gbc_btnAppointments.gridy = 5;
				mainLayeredPane.add(btnAppointments, gbc_btnAppointments);
				btnAppointments.addActionListener(new AppointmentButtonListener());
				
		ArrayList<Object> data = new ArrayList<>();
		HashMap<String, String> appAtt = null;
		String[] keys = null;
		try {
			ArrayList<Appointments> todayApps = Appointments.getAppointmentsByDate(Format.DATABASEDATEFORMAT.format(Calendar.getInstance().getTime()));
			for(Appointments app:todayApps){	
				appAtt = (HashMap<String, String>) app.getAttributes().clone();
				Integer hour = Integer.parseInt(appAtt.get("Hour"));
				appAtt.remove("Hour");
				Integer minute= Integer.parseInt(appAtt.get("Minute"));
				appAtt.remove("Minute");
				appAtt.put("Start Time", Format.timeFormat(hour, minute));
				appAtt.put("Name", app.getPatientName());
				appAtt.put("Payment", app.getPayment().getPayment().toString());
				try{
				appAtt.put("Contact", Patient.findPatient(app.getPatientName()).getContacts("Patient").toString());
				}catch(Exception e){
					e.printStackTrace();
				}
				keys = appAtt.keySet().toArray(new String[0]);
				Arrays.sort(keys);
				String [] rowDat = new String[keys.length];
				int i =0;
				for(String key:keys){
					rowDat[i] = appAtt.get(key);
					i++;
				}
				data.add(rowDat);

				}
		}catch(Appointments.AppointmentException e2){
			lblTodaysAppointments.setVisible(false);
			lblNoAppointments.setVisible(true);
		}
		catch (Exception e1) {
			log.doLogging(e1.getMessage(), "severe");
			e1.printStackTrace();
		}
//		if(appAtt!=null){
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBorder(null);
			scrollPane.setName("Today's Appointments\r\n");
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
			gbc_scrollPane.gridx = 1;
			gbc_scrollPane.gridy = 2;
			mainLayeredPane.add(scrollPane, gbc_scrollPane);
			
		table = new JTable();
		table.setModel(new DefaultTableModel(
				data.toArray(new Object[0][0]),keys)
		);
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
		scrollPane.setViewportView(table);
//		}
		mainFrame.setVisible(true);
		mainLayeredPane.setVisible(true);

		
		
//		JButton btnAdministration = new JButton("Administration");
//		btnAdministration.setBounds(200, 300, 100, 90);
//		
//		mainFrame.getContentPane().add(btnAdministration);
	}
	public static String getIp() {
		return ip;
	}
	public static class Log {
		private final Logger logger = Logger.getLogger(Main.class.getName());
		private FileHandler fh = null;

	    public FileHandler getFh() {
			return fh;
		}

		public Log() {
	        //just to make our log file nicer :)
	        SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
	        try {
	            fh = new FileHandler("LogFile"
	                + format.format(Calendar.getInstance().getTime()) + ".log"){
	            	public File getFile(){
						return new File("LogFile"
	                + format.format(Calendar.getInstance().getTime()) + ".log");
	            		
	            	}
	            };
//	    		System.setErr(new PrintStream(new File("LogFile" + format.format(Calendar.getInstance().getTime()) + ".log")));

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        fh.setFormatter(new Formatter() {
	            @Override
	            public String format(LogRecord record) {
	                SimpleDateFormat logTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	                Calendar cal = new GregorianCalendar();
	                cal.setTimeInMillis(record.getMillis());
	                return record.getLevel()
	                        + logTime.format(cal.getTime())
	                        + " || "
	                        + record.getSourceClassName().substring(
	                                record.getSourceClassName().lastIndexOf(".")+1,
	                                record.getSourceClassName().length())
	                        + "."
	                        + record.getSourceMethodName()
	                        + "() : "
	                        + record.getMessage() + "\n\n";
	            }

				
	        });

	        logger.addHandler(fh);
	        
	    }

	    public void doLogging(String message, String logType) {
	        switch (logType){
	        case "info":
	        	logger.info(message);
	        	break;
	        case "severe":
	        	logger.severe(message);
	        	break;
	        default:
	        	break;
	        }
	        }
	        }


}
