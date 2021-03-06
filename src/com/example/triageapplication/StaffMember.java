package com.example.triageapplication;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.example.exceptions.InvalidDayOfBirthException;
import com.example.exceptions.InvalidHealthCardNumberException;
import com.example.exceptions.InvalidMonthOfBirthException;
import com.example.exceptions.InvalidYearOfBirthException;
import com.example.exceptions.NoRecordSpecifiedException;
import com.example.exceptions.NotCheckedInException;

import android.content.Context;

/** A staff member. */
public class StaffMember implements Serializable {
	
	/** A unique ID used for serialization. */
	private static final long serialVersionUID = 4154792375501851873L;
	
	/** A manager of Records for this hospital. */
	private static RecordManager records;
	
	/** This StaffMember's username. */
	private String username;

	/** Constructs a StaffMember with a username.
	 * @param username This StaffMember's username.
	 */
	public StaffMember(String username){
		this.username = username;
	}
	
	/**
	 * Creates a new RecordManager that all StaffMembers have access to that
	 * manages a collection of Records stored in directory dir in a file named
	 * fileName.
	 * @param dir The directory in which the data file is stored.
	 * @param fileName The data file containing Record information
	 * @param context The context of this Android application.
	 * @throws IOException 
	 */
	public void createRecordManager(File dir, String fileName1, 
			String fileName2, Context context)
	throws IOException {
		records = new RecordManager(dir, fileName1, fileName2, context);
	}

	/** Creates a Record for a patient with a name, a date of birth and a
	 * health card number.
	 * @param name The Patient's name.
	 * @param dob The Patient's date of birth.
	 * @param healthCardNumber The Patient's health card number.
	 * @param context The context of the Android application.				
	 * @throws InvalidDayOfBirthException If the day of birth is invalid.
	 * @throws InvalidMonthOfBirthException If the  month of birth is invalid. 
	 * @throws InvalidYearOfBirthException If the year of birth is invalid.
	 * @throws InvalidHealthCardNumberException If the health card number is
	 * invalid.
	 */	
	public void addPatient(String[] name, String[] dob, String 
			healthCardNumber, Context context) throws 
			InvalidDayOfBirthException, InvalidMonthOfBirthException, 
			InvalidYearOfBirthException, InvalidHealthCardNumberException {
		if (!Pattern.matches("0[1-9]|[12][0-9]|3[01]", dob[0])) {
            throw new InvalidDayOfBirthException();
            }
		if (!Pattern.matches("0[1-9]|[1][012]", dob[1])) {
			throw new InvalidMonthOfBirthException();
			}
		if (!Pattern.matches("(19|20)[0-9][0-9]", dob[2])) {
			throw new InvalidYearOfBirthException();
			}
		if (!Pattern.matches("[0-9]{11}", healthCardNumber)) {
			throw new InvalidHealthCardNumberException();
			}

		else {
			Record r = new Record(name, healthCardNumber, dob);
			
			// check if file exists, only if it doesn't does it create a new
			// file for it
			File file = new File(context.getFilesDir(), healthCardNumber);
			if(!file.exists()) {
			    r.setupFile(context);
			}
			
			r.updateRecordAdmitted(context);
			r.setCheckedOut(false);
			records.add(r);
			try {
				records.saveRecordsToFile("PatientsAndRecords",
						context.openFileOutput(
						"PatientsAndRecords", Context.MODE_PRIVATE));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	} 

	/**
	 * Gets the username of this StaffMember.
	 * @return Username of this StaffMember.
	 */
	public String getUsername() {
		return username;
	}

	/** Updates a Record's latest temperature measurement. 
	 * @param The Record in question.
	 * @param temperature The new temperature.
	 * @throws NoRecordSpecifiedException If this record does not exist.
	 */
	public void setTemperature(Record record, double temperature) throws
	NoRecordSpecifiedException {
		if (record == null) {
			throw new NoRecordSpecifiedException("No record has been " +
					"specified.");
		}
		record.setTemperature(temperature);
	}
	
	/** Updates a Record's latest blood pressure measurement. 
	 * @param The Record in question.
	 * @param bloodPressure The new blood pressure.
	 * @throws NoRecordSpecifiedException If this record does not exist.
	 */
	public void setBloodPressure(Record record, int bloodPressure) throws
	NoRecordSpecifiedException {
		if (record == null) {
			throw new NoRecordSpecifiedException(
					"No record has been specified.");
		}
		record.setBloodPressure(bloodPressure);
	}
	
	/** Updates a Record's latest heart rate measurement. 
	 * @param The Record in question.
	 * @param heartRate The new heart rate.
	 * @throws NoRecordSpecifiedException If this record does not exist.
	 */
	public void setHeartRate(Record record, int heartRate)throws
	NoRecordSpecifiedException{
		if (record == null) {
			throw new NoRecordSpecifiedException(
					"No record has been specified.");
		}
		record.setHeartRate(heartRate);
	}
	
	/** Updates a Record to mark whether a patient has been seen by a doctor.
	 * @param The Record in question.
	 * @param seenByDoctor True if the patient has been seen by a doctor.
	 * @throws NoRecordSpecifiedException If this record does not exist.
	 */
	public void setSeenByDoctor(Record record, boolean seenByDoctor, 
			Context context) throws NoRecordSpecifiedException {			
		if (record == null) {
			throw new NoRecordSpecifiedException("No record has been " +
					"specified.");
		}
		
		if (seenByDoctor == true){
		    record.setSeenByDoctor(seenByDoctor);		
		    record.updateRecordSeenByDoctor(context);
            records.removePatientFromUrgency(record);
		}
	}
	
	/** Discharges a patient by removing their Record from the urgency list,
	 * but retaining them in the master patient list.
	 * @param The health card number of the patient in question.
	 * @param context The context of the Android application.
	 * @throws NotCheckedInException If this patient is not checked in.
	 */
	public void dischargePatient(String healthCardNum, Context context) throws
	NotCheckedInException {
		Record record = records.getRecord(healthCardNum);
		
		if (record.isCheckedOut()) {
		    throw new NotCheckedInException();
		}
		
		record.discharge(context);
		record.setCheckedOut(true);
		
		//Remove old record and add new record
		records.removePatient(record.getHealthCardNum());
		
		// This is in case a patient is not seen by a Doctor before they leave
		if (records.getUrgencyRecords().contains(record.getHealthCardNum())) {
		    records.removePatientFromUrgency(record);
		}
		
		records.add(record);
		
		try {
		    records.saveRecordsToFile("PatientsAndRecords",
		    		context.openFileOutput("PatientsAndRecords",
		    				Context.MODE_PRIVATE));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/** Updates a Record's latest vitals recording. 
	 * @param The Record in question.
	 * @param symptoms The new symptoms.
	 * @throws NoRecordSpecifiedException If this record does not exist.
	 */
	public void setSymptoms(Record record, String symptoms) throws
	NoRecordSpecifiedException {
		
		if (record == null) {
			throw new NoRecordSpecifiedException("No record has been " +
					"specified.");
		}
		
		record.setSymptoms(symptoms);
	}
		
	/** Saves the information about vitals in a Record to disk.
	 * @param The Record in question.
	 * @throws NoRecordSpecifiedException If this record does not exist.
	 */
	public void updateVitals(Record record, Context context) throws
	NoRecordSpecifiedException{
		
		if (record == null) {
			throw new NoRecordSpecifiedException(
					"No record has been specified.");
		}
		
		record.updateRecordVitalsSymptoms(context);
	}
	
	/** Saves the information about vitals in a Record to disk.
	 * @param The Record in question.
	 * @throws NoRecordSpecifiedException If this record does not exist.
	 */
	public void updatePrescription(Record record, Context context) throws
	NoRecordSpecifiedException {
		
		if (record == null) {
			throw new NoRecordSpecifiedException(
					"No record has been specified.");
		}
		
		record.updateRecordPrescription(context);
	}
	
	/**
	 * Returns a Record whose patient has a given health card number.
	 * @param healthcardNum A patient's health card number.
	 * @return The Record of a patient who has the health card number.
	 * @throws NoRecordSpecifiedException If this record does not exist.
	 */
	public Record getRecord(String healthCardNum) throws
	NoRecordSpecifiedException {
		Record record = records.getRecord(healthCardNum);
		
		if (record == null) {
			throw new NoRecordSpecifiedException("No record " +
					"has been specified.");
		}
		
		return records.getRecord(healthCardNum);
	}
	
	/**
	 * Gets record manager of this hospital.
	 * @return A Record Manager for this hospital.
	 */
	public static RecordManager getRecords() {
		return records;
	}
	
	/**
	 * Returns a StringBuilder containing the Record information from a record
	 * filed with the name from parameter "healthNum".
	 * @param context Context from an Activity for Reading.
	 * @param healthNum Name of Record File (a Patient's health card number).
	 * @return A StringBuilder of all the information from the Record File.
	 */
	public StringBuilder getInfo(Context context, Record record) {
	    
		String healthNum = record.getHealthCardNum();
	    File file = new File(context.getFilesDir(), healthNum);
	    StringBuilder text = new StringBuilder();
	    
	    try {
	    	Scanner scanner = new Scanner(new FileInputStream(file));
	    	String line;
	    	
	    	while(scanner.hasNextLine()) {
	    		line = scanner.nextLine();
	    		text.append(line);
	    		text.append("\n");
	    	}
	    	
	    } catch (Exception e) { // FIX THIS LATER - If record does not exist
	    	e.printStackTrace();
	    }
	    
	    return(text);
	}

	/**
	 * Returns a StringBuilder consisting of all patients sorted by urgency.
	 * @return a StringBuilder consisting of all patients sorted by urgency.
	 	*/
	public StringBuilder getUrgencyInfo() {
		LinkedList<String> urgencyRecord = records.getUrgencyRecords();
		StringBuilder urgency = new StringBuilder();
		
		for(String healthCardNumber: urgencyRecord) {
			Record record = records.getRecord(healthCardNumber);
			String[] name = record.getName();
			urgency.append(name[0] + " " + name[1] + "\n");
			urgency.append("Urgency: " + record.getUrgencyRating() + "\n");
			urgency.append("Health Card Number: " + record.getHealthCardNum()
					+ "\n");
			urgency.append("Arrival Time: " + record.getArrivalTime() + "\n");
			urgency.append("\n");
		}
	
	return(urgency);		
	}

	/** Updates a Record's latest recording. 
	 * @param The Record in question.
	 * @param heartRate The new symptoms.
	 * @throws NoRecordSpecifiedException If this record does not exist.
	 * @throws FileNotFoundException If the file does not exist.
	 */
	public void setPrescription(Record record, String prescriptionName, String
		prescriptionInstructions, Context context) throws
		NoRecordSpecifiedException, FileNotFoundException {
	
		if (record == null) {
			throw new NoRecordSpecifiedException("No record has been specified.");
		}
	
		record.setPrescriptionName(prescriptionName);
		record.setPrescriptionInstructions(prescriptionInstructions);
	
		records.addPrescription("Patient: " + record.getHealthCardNum() + "\n" 
		+ prescriptionName + ":\n\t" + prescriptionInstructions, context);
	}
	
	/** Updates a Record's latest urgency rating. 
	 * @param The Record in question,
	 */
	public void updateUrgency(Record record) {
		record.updateUrgencyRating();
	}

	/** Returns the first prescription to be filled. 
	 * @return A String of the first prescription to be filled.
	 * @throws FileNotFoundException If the file does not exist.
	 */
	public String getPrescription(Context context) throws 
	FileNotFoundException {
		return StaffMember.getRecords().getPrescription(context);
	}
}