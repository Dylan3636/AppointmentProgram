package com.omada.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.omada.patient.Patient;

public class Log {
	private final Logger logger = Logger.getLogger(Patient.class.getName());
	private FileHandler fh = null;

    public Log() {
        //just to make our log file nicer :)
        SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
        try {
            fh = new FileHandler("LogFile"
                + format.format(Calendar.getInstance().getTime()) + ".log");
        } catch (Exception e) {
            e.printStackTrace();
        }

        fh.setFormatter(new SimpleFormatter());
        logger.addHandler(fh);
    }

    public void doLogging() {
        logger.info("info msg");
        logger.severe("error message");
        logger.fine("fine message"); //won't show because to high level of logging
    }
}
