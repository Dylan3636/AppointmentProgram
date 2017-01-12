package com.omada.database;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

import com.omada.main.Main;
import javax.swing.JProgressBar;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import javax.swing.JTextPane;
import javax.swing.JLabel;
public class Database {
	private static Connection myConn;
	private static JFrame frame;
	private static JProgressBar progressBar;
	private static JLabel label;
	/**
	 * @wbp.parser.entryPoint
	 */

	static {
		frame = new JFrame("Attempting to connect to Database.");
		frame.setBounds(new Rectangle(125, 50, 750, 250));
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(29, 39, 424, 14);
		progressBar.setIndeterminate(true);
		frame.getContentPane().add(progressBar);
		frame.setVisible(false);

		label = new JLabel("");
		label.setBounds(21, 99, 424, 26);
		frame.getContentPane().add(label);

	}

	static{
		try {myConn = setconnection();
		}catch(DatabaseException e){}
	}
	public Database() {
		// TODO Auto-generated constructor stub
	}
	public static Connection getconnection(){
		try{
			try {
				if(myConn.isClosed()){
					return setconnection();
				}else if(!myConn.isValid(10)){
					return setconnection();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return setconnection();
			}
		}	catch (DatabaseException e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		return myConn;
	}
		public static Connection setconnection() throws DatabaseException{
		final String URL = Main.URL; 
		final String USER = Main.user;
		final String PASSWORD = Main.password;
		final String DRIVER = Main.DRIVER;
		

        try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			myConn = DriverManager.getConnection(URL, USER,PASSWORD);
			return myConn;
		} catch (Exception e) {
			e.printStackTrace();
			
//			String ip = JOptionPane.showInputDialog(new JFrame(),"Error Cannot Start! Try changing ip address",DatabaseException.NOCONNECTION.getMessage(),JOptionPane.ERROR_MESSAGE);
			String ip = Main.getIp();

			while(Main.count<6){
				frame.setVisible(true);

			ip = ip.substring(0,ip.length()-1)+Main.count.toString();
			label.setText("Trying ip address "+ip);
//			progressBar.setValue((int)(((double)Main.count/6.0) *100));
			try {
				PrintWriter writer = new PrintWriter("ip.txt", "UTF-8");
				writer.print(ip);
				writer.close();
				Main.count++;
				Main.main(null);
			} catch (FileNotFoundException | UnsupportedEncodingException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
			}
			if(ip==null){
			System.exit(1);
			}else{
				frame.setVisible(false);
				ip = JOptionPane.showInputDialog(new JFrame(),"Error Cannot Start! Try changing ip address",DatabaseException.NOCONNECTION.getMessage(),JOptionPane.ERROR_MESSAGE);
				if(ip==null){
				System.exit(1);
				}

				try {
					PrintWriter writer = new PrintWriter("ip.txt", "UTF-8");
					writer.print(ip);
					writer.close();
					Main.main(null);
				} catch (FileNotFoundException | UnsupportedEncodingException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				}
			}
		
			
		return null;

	}
	public static class DatabaseException extends Exception{

		public static final DatabaseException NOCONNECTION = new DatabaseException("Unable to Connect to Database! Please Check Connection.");
		DatabaseException(String m){
			super(m);
		}
		
		
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) throws ClassNotFoundException{
		final String DRIVER = "com.mysql.jdbc.Driver";
		String url= "jdbc:mysql://192.168.1.2:3306/?";
		String user = "Admin";
		String password = "$password";
		
        Class.forName("com.mysql.jdbc.Driver");

//	    try {
////			Class.forName(DRIVER).newInstance();
//		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		try {
			Connection myConn = DriverManager.getConnection(url+"&useSSL=false",user,password);
			Statement myStat = myConn.createStatement();
			
			String sql = "CREATE DATABASE orthoDatabase";
			
			myStat.executeUpdate(sql);
			System.out.println("insert complete");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
