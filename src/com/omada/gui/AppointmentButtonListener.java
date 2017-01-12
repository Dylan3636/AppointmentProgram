package com.omada.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppointmentButtonListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()){
		case "Appointments":
			AppointmentGUI.appointmentHome();
		}
	}

}
