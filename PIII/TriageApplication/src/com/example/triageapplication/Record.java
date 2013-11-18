package com.example.triageapplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;

/** A Record corresponding to a patient. */
public class Record implements Serializable {
	
	/**  A unique ID for serialization. */
	private static final long serialVersionUID = -2172838661384559738L;

	/** This Record's patient's name. */
	private String[] name;

	/** This Record's patient's health card number. */
	private String healthCardNum;

	/** This Record's patient's date of birth. */
	private String[] dob;
	
	/** This Record's patient's age. */
	private int age;

	/** This Record's patient's current temperature. */
	private double temperature;

	/** This Record's patient's current blood pressure. */
	private int bloodPressure;

	/** This Record's patient's current heart rate. */
	private int heartRate;
	
	/** This Record's patient's latest symptoms. */
	private String symptoms;

	/** This Record's patient's triage status. */
	private Boolean seenByDoctor;

	/** This Record's patient's arrival time. */
	private String arrivalTime;

	/** This Record's filename. */
	private String recordFile;
	
	
	/**
	 * Constructs a Record object for a patient who has a name, a health card
	 * number and a date of birth. The Record also contains the patient's age,
	 * arrival time, latest temperature measurement, latest blood pressure
	 * measurement, latest symptoms, and a marker of whether or not they have
	 * yet been seen by a doctor.
	 * @param name The name of the patient.
	 * @param healthCardNum The health card number of the patient.
	 * @param dob The date of birth of the patient.
	 */
	public Record(String[] name, String healthCardNum, String[] dob) {
		this.name = name;
		this.healthCardNum = healthCardNum;
		this.dob = dob;
		this.age = (Integer.parseInt(currentTime().substring(0, 4)) - 
				Integer.parseInt(dob[2]));
		this.arrivalTime = currentTime();
		this.temperature = 0.0;
		this.bloodPressure = 0;
		this.heartRate = 0;
		this.symptoms = "";
		this.seenByDoctor = false;
		this.recordFile = new String(healthCardNum);			
	}

	/**
	 * Creates a new file for this Record with the filename being the
	 * patient's health card number.
	 * @param context The context of this application
	 */
	public void setupFile(Context context){
		try {
			final String header = new String (
				  "											  " +
				  "		  PATIENT RECORD				 " +
				  "				   \n" +
				  "**********************************************" +
				  "***********************************************" +
				  "*********************\n" +
				  "*********************************************" +
				  "**********************************************" +
				  "***********************\n" +
				  "Name: " + this.name[0].toString() + " " + 
				  this.name[1].toString() + "\n" +
				  "Health Card: " + this.healthCardNum + "\n" +
				  "Birth Date: " + this.dob[0].toString() + "/" + 
				  this.dob[1].toString() + "/" +
				  this.dob[2].toString() + "\n" +
				  "Age: " + String.valueOf(this.age) + "\n" +
				  "*********************************************" +
				  "**********************************************" +
				  "***********************\n" +
				  "*********************************************" +
				  "**********************************************" +
				  "***********************\n" +
				  "\n");
		 
	   
			OutputStreamWriter outputStreamWriter = 
				new OutputStreamWriter(context.openFileOutput(recordFile, 
						Context.MODE_PRIVATE));
			outputStreamWriter.write(header);
			outputStreamWriter.close();
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates this Record's text file record to include the patient's
	 * arrival time at the hospital.
	 */
	protected void updateRecordAdmitted(Context context) {
		try {
			String admitted = new String (
			"***********************************************" +
			"*******************" +
			"************************************************\n" +
			"Admitted to Hospital: " + currentTime().toString()) + "\n\n";
		

			// setting true here should mean the file is NOT overwritten
		
			OutputStreamWriter outputStreamWriter = new 
					OutputStreamWriter(context.openFileOutput(recordFile, 
					Context.MODE_APPEND));
			outputStreamWriter.write(admitted);
			outputStreamWriter.close();
		}	
	
		catch (FileNotFoundException e) {
			e.printStackTrace(); 
		}
		catch(IOException e){
			e.printStackTrace();
		}		
	}

	/**
	 * Updates this Record's text file record to include the patient's:
	 * temperature, blood pressure, heart rate and symptoms; appends the
	 * current time.
	 */
	protected void updateRecordVitalsSymptoms(Context context) {
		try {
			String update = new String (
				"******************************************************" +
				"******************************************************" +
				"******\n" +
				"Date: " + currentTime() +
				"\n" +
				"Temperature: "+ String.valueOf(this.temperature) + 
				"\n" +
				"Blood Pressure: " + this.bloodPressure + "\n" +
				"Heart Rate: " + this.heartRate + "\n" +
				"Symptoms: " + this.symptoms + "\n\n");
	
				   
			OutputStreamWriter outputStreamWriter = 
					new OutputStreamWriter(context.openFileOutput(recordFile, 
					Context.MODE_APPEND));
			outputStreamWriter.write(update);
			outputStreamWriter.close();
		}	
	
		catch (FileNotFoundException e) {
			e.printStackTrace(); 
		}
		catch(IOException e){
			e.printStackTrace();
		}		
	}		
	
	/**
	 * Updates this Record's text file to include whether or not the
	 * Patient has been seen by a doctor; appends the current time.
	 */
	
	protected void updateRecordSeenByDoctor(Context context) {
		try {
			String doctor = new String (
					"************************************************" +
					"**************************************************" +
					"****************\n" +
					"Date: " + currentTime() +
					"\n" +
					"Seen by Doctor" + "\n\n");
		
			OutputStreamWriter outputStreamWriter =
					new OutputStreamWriter(context.openFileOutput(recordFile, 
					Context.MODE_APPEND));
			outputStreamWriter.write(doctor);
			outputStreamWriter.close();		}	
		
		catch (FileNotFoundException e) {
			e.printStackTrace(); 
		}
		catch(IOException e){
			e.printStackTrace();
		}			
	}
		
	/**
	 * Returns the current time and date as a String in the format 
	 * "yyyy/MM/dd HH:mm:ss".
	 * @return The current time and date.
	 */
	public static String currentTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date); 
	}
	
	/**
	 * Returns a String representation of this Record.
	 * @return A String representation of this Record.
	 */
	@Override
	public String toString(){
		return (this.getName()[0] + "," + this.getName()[1] + "," + 
	this.getHealthCardNum() + "," + this.getDob()[0] + "," + 
	this.getDob()[1] + "," + this.getDob()[2]	+ "," + 
	this.getTemperature() + "," + 	this.getBloodPressure() + 
	"," + this.getHeartRate() + "," + 	this.getSymptoms() + "," 
	+ this.getSeenByDoctor() + "," + this.getArrivalTime());
	}

	/**
	 * Returns this Person's name.
	 * @return This Person's name.
	 */
	public String[] getName() {
		return this.name;
	}
	
	/**
	 * Returns this Person's health card number.
	 * @return This Person's health card number.
	 */
	public String getHealthCardNum() {
		return this.healthCardNum;
	}
	
	/**
	 * Returns this Person's birthdate.
	 * @return This Person's birthdate.
	 */
	public String[] getDob() {
		return this.dob;
	} 
	
	/** 
	 * Returns the latest recorded symptoms of the patient 
	 * associated with this Record.
	 * @return The latest symptoms.
	 */
	private String getSymptoms() {
		return this.symptoms;
	}

	/** 
	 * Returns the latest temperature measurement of the patient 
	 * associated with this Record.
	 * @return The latest temperature.
	 */
	public Double getTemperature() {
		return this.temperature;
	}
	
	/** 
	 * Returns the latest blood pressure measurement of the patient 
	 * associated with this Record.
	 * @return The latest blood pressure.
	 */
	public int getBloodPressure() {
		return bloodPressure;
	}

	/** 
	 * Returns the latest heart rate measurement of the patient 
	 * associated with this Record.
	 * @return The latest heart rate.
	 */
	public int getHeartRate() {
		return heartRate;
	}

	/** 
	 * Sets the latest temperature measurement of the patient 
	 * associated with this record.
	 * @param temperature The new temperature.
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	/** 
	 * Sets the latest blood pressure measurement of the patient 
	 * associated with this Record.
	 * @param bloodPressure The new blood pressure.
	 */
	public void setBloodPressure(int bloodPressure) {
		this.bloodPressure = bloodPressure;
	}

	/** 
	 * Sets the latest heart rate measurement of the patient 
	 * associated with this Record.
	 * @param heartRate The new heart rate.
	 */
	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}

	/** 
	 * Returns when the patient associated with this Record 
	 * arrived at the hospital.
	 * @return The recorded arrival time.
	 */
	public String getArrivalTime() {
		return arrivalTime;
	}
	
	/** 
	 * Sets the arrival time of the patient associated with this Record.
	 * @param arrivalTime The new arrival time.
	 */
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;	
	}
	
	/** Returns whether the patient associated with this Record
	 * has been seen by a doctor. 
	 * @return True if the Patient has been seen by a doctor.
	 */
	public boolean getSeenByDoctor() {
		return this.seenByDoctor;
	}

	/** Sets whether the patient associated with this Record 
	 * has been seen by a doctor. 
	 * @param seenByDoctor True if the Patient has been seen by a doctor.
	 */
	public void setSeenByDoctor(boolean seenByDoctor) {
		this.seenByDoctor = seenByDoctor;
	}
		
	/** Sets the latest symptoms of the patient associated with this Record.
	 * @param symptoms The new symptoms.
	 */
	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}


}