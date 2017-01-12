package com.omada.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.TextField;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JTextPane;

public class AppointmentPopUpMenu extends JPopupMenu {
	/**
	 * @throws Exception 
	 * @wbp.parser.entryPoint
	 */
	
	public void move(Boolean show){
		JFrame frame = new JFrame();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{98, 51, 77, 145, 0, 122, 118, 0};
		gridBagLayout.rowHeights = new int[]{33, 70, 102, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		Button Month1 = new Button("Month-");
		Month1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_Month1 = new GridBagConstraints();
		gbc_Month1.fill = GridBagConstraints.VERTICAL;
		gbc_Month1.insets = new Insets(0, 0, 5, 5);
		gbc_Month1.gridx = 0;
		gbc_Month1.gridy = 0;
		frame.getContentPane().add(Month1, gbc_Month1);
		
		JButton btnNewButton = new JButton("Week-");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.VERTICAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 0;
		frame.getContentPane().add(btnNewButton, gbc_btnNewButton);
		
		JButton Day1 = new JButton("Day-");
		GridBagConstraints gbc_Day1 = new GridBagConstraints();
		gbc_Day1.fill = GridBagConstraints.VERTICAL;
		gbc_Day1.insets = new Insets(0, 0, 5, 5);
		gbc_Day1.gridx = 2;
		gbc_Day1.gridy = 0;
		frame.getContentPane().add(Day1, gbc_Day1);
		
		TextField textField = new TextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.anchor = GridBagConstraints.NORTH;
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 3;
		gbc_textField.gridy = 0;
		frame.getContentPane().add(textField, gbc_textField);
		
		JButton Day = new JButton("Day+");
		Day.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		GridBagConstraints gbc_Day = new GridBagConstraints();
		gbc_Day.fill = GridBagConstraints.VERTICAL;
		gbc_Day.insets = new Insets(0, 0, 5, 5);
		gbc_Day.gridx = 4;
		gbc_Day.gridy = 0;
		frame.getContentPane().add(Day, gbc_Day);
		
		JButton Week = new JButton("Week+");
		GridBagConstraints gbc_Week = new GridBagConstraints();
		gbc_Week.fill = GridBagConstraints.VERTICAL;
		gbc_Week.insets = new Insets(0, 0, 5, 5);
		gbc_Week.gridx = 5;
		gbc_Week.gridy = 0;
		frame.getContentPane().add(Week, gbc_Week);
		
		Button Month = new Button("Month+");
		GridBagConstraints gbc_Month = new GridBagConstraints();
		gbc_Month.insets = new Insets(0, 0, 5, 0);
		gbc_Month.fill = GridBagConstraints.VERTICAL;
		gbc_Month.gridx = 6;
		gbc_Month.gridy = 0;
		frame.getContentPane().add(Month, gbc_Month);
		
		JTextPane textPane = new JTextPane();
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.gridwidth = 5;
		gbc_textPane.insets = new Insets(0, 0, 5, 5);
		gbc_textPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane.gridx = 1;
		gbc_textPane.gridy = 1;
		frame.getContentPane().add(textPane, gbc_textPane);
	}

	public AppointmentPopUpMenu() {
		JMenuItem move = new JMenuItem("Move");
		move.addActionListener(new ActionListener(){
		
			
		
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
			
		});
		this.add(move);
		this.add(new JMenuItem("Copy"));
	}
	public AppointmentPopUpMenu(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	public static class PopUpClickListener extends MouseAdapter{
		  public void mousePressed(MouseEvent e){
		        if (e.isPopupTrigger())
		            doPop(e);
		    }


		    private void doPop(MouseEvent e){
		        AppointmentPopUpMenu menu = new AppointmentPopUpMenu();
		        menu.show(e.getComponent(), e.getX(), e.getY());
		    }
	}
	public static void main(String[] args){
		JFrame j = new JFrame();
		JLayeredPane lp = new JLayeredPane();
		lp.setComponentPopupMenu((JPopupMenu)new AppointmentPopUpMenu());
		j.setContentPane(lp);
		j.setBounds(100, 100, 1000, 1000);
		j.setVisible(true);
	}
}
