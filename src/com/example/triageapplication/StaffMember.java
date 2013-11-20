package com.example.triageapplication;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import android.content.Context;

/** A StaffMember. */
public class StaffMember implements Serializable {
	
	/** A unique ID used for serialization. */
	private static final long serialVersionUID = 4154792375501851873L;
	
	/** A manager of Records for this hospital. */
	private static RecordManager records;
	
	/** Valid days for a date of birth.*/
	public static final String[] VALID_DAYS = {"01", "02", "03", "04", "05",
		"06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16",
		"17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
		"28", "29", "30", "31"};
	
	/** Valid months for a date of birth.*/
	public static final String[] VALID_MONTHS = {"01", "02", "03", "04", "05",
		"06", "07", "08", "09", "10", "11", "12"};
	
	/** Valid years for a date of birth.*/
	public static final String[] VALID_YEARS = {"1950", "1951", "1952", "1953",
		"1954", "1955", "1956", "1957", "1958", "1959", "1960", "1961", "1962",
		"1963", "1964", "1965", "1966", "1967", "1968", "1969", "1970", "1971",
		"1972", "1973", "1974", "1975", "1976", "1977", "1978", "1979", "1980",
		"1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989",
		"1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998",
		"1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007",
		"2008", "2009", "2010", "2011", "2012", "2013"};
	
	/** This StaffMember's username. */
	private String username;

	/** Constructs a StaffMember with a username.
	 * @param username This Nurse's username.
	 */
	public StaffMember(String username){
		this.username = username;
	}
	
	/**
	 * Creates a new RecordManager that all StaffMembers have access to that manages
	 * a collection of Records stored in directory dir in a file named fileName.
	 * @param dir The directory in which the data file is stored.
	 * @param fileName The data file containing Record information
	 * @param context The context of this Android application.
	 * @throws IOException 
	 */
	public void createRecordManager(File dir, String fileName, Context context)
	throws IOException {
		records = new RecordManager(dir, fileName, context);
	}

	/** Creates a Record for a patient with with a name, a date of birth and a
	 * health card number.
	 * @param name The Patient's name.
	 * @param dob The Patient's date of birth.
	 * @param healthCardNumber The Patient's health card number.
	 * @param context The context of the Android application.				
	 * @throws InvalidDayOfBirthException 
	 * @throws InvalidDayOfBirthException, InvalidMonthOfBirthException,
	 * InvalidYearOfBirthException, InvalidHealthCardNumberException 
	 * @throws InvalidMonthOfBirthException 
	 * @throws InvalidYearOfBirthException 
	 * @throws InvalidHealthCardNumberException 
	 */	
	public void addPatient(String[] name, String[] dob, String 
			healthCardNumber, Context context) throws 
			InvalidDayOfBirthException, InvalidMonthOfBirthException, 
			InvalidYearOfBirthException, InvalidHealthCardNumberException {

		if (!Arrays.asList(VALID_DAYS).contains(dob[0])) {
			throw new InvalidDayOfBirthException();
		}
		if (!Arrays.asList(VALID_MONTHS).contains(dob[1])) {
			throw new InvalidMonthOfBirthException();
		}
		if (!Arrays.asList(VALID_YEARS).contains(dob[2])) {
			throw new InvalidYearOfBirthException();
		}
		if (!(healthCardNumber.length() == 11)) {
			throw new InvalidHealthCardNumberException();
		}
		else {
			Record r = new Record(name, healthCardNumber, dob);
			r.setupFile(context);
			r.updateRecordAdmitted(context);
			records.add(r);
			try {
				records.saveToFile(context.openFileOutput(
						"PatientsAndRecords", Context.MODE_PRIVATE));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	} 

	/**
	 * Gets the username of this Nurse.
	 * @return Username of this Nurse.
	 */
	public String getUsername() {
		return username;
	}

	/** Updates a Record's latest temperature measurement. 
	 * @param The Record in question.
	 * @param temperature The new temperature.
	 * @throws NoRecordSpecifiedException 
	 */
	public void setTemperature(Record record, double temperature) throws
	NoRecordSpecifiedException {
		if (record == null) {
			throw new NoRecordSpecifiedException("No record has been specified.");
		}
		record.setTemperature(temperature);
	}
	
	/** Updates a Record's latest blood pressure measurement. 
	 * @param The Record in question.
	 * @param bloodPressure The new blood pressure.
	 * @throws NoRecordSpecifiedException 
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
	 * @throws NoRecordSpecifiedException 
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
	 * @throws NoRecordSpecifiedException 
	 */
	public void setSeenByDoctor(Record record, boolean seenByDoctor, 
			Context context) throws 	NoRecordSpecifiedException {			
		if (record == null) {
			throw new NoRecordSpecifiedException(
					"No record has been specified.");
		}
		
		if (seenByDoctor == true){
		record.setSeenByDoctor(seenByDoctor);		
		record.updateRecordSeenByDoctor(context);
		}
	}
	
	/** Updates a Record's latest recording. 
	 * @param The Record in question.
	 * @param heartRate The new symptoms.
	 * @throws NoRecordSpecifiedException 
	 */
	public void setSymptoms(Record record, String symptoms) throws
	NoRecordSpecifiedException {
		
		if (record == null) {
			throw new NoRecordSpecifiedException("No record has been specified.");
		}
		
		record.setSymptoms(symptoms);
	}
		
	/** Save the information about vitals in a Record to disk.
	 * @param The Record in question.
	 * @throws NoRecordSpecifiedException 
	 */
	public void updateVitals(Record record, Context context) throws
	NoRecordSpecifiedException{
		
		if (record == null) {
			throw new NoRecordSpecifiedException(
					"No record has been specified.");
		}
		
		record.updateRecordVitalsSymptoms(context);
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
	 * Gets record manager of this StaffMember.
	 * @return Record Manager "records". 
	 */
	public static RecordManager getRecords() {
		return records;
	}
}
