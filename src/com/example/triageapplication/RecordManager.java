package com.example.triageapplication;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.Scanner;

import android.content.Context;

/**
 * A class that manages patient Records. RecordManager knows how to read Record
 * objects from a file and how to write its collection of Records to a file.
 * Part of this code and its overall structure comes from lecturers Anya 
 * Tafliovich's and Jen Campbell's CSC207 Fall 2013 class notes/code. 
 */
public class RecordManager implements Serializable {

    /** A unique ID for serialization. */
	private static final long serialVersionUID = -5456807997690411743L;
	
	/** A mapping of health card numbers to Records. */
	private Map<String, Record> records;
	
	/** A linked list of patients in order of most to least urgent. */
	private LinkedList<Record> recordsByUrgency;	
	
    /**
     * Constructs a new RecordManager that manages a collection of 
     * Records stored in directory dir in a file named fileName.
     * @param dir The directory in which the data file is stored.
     * @param fileName The data file containing Record information
     * @param context The context of this Android application.
     * @throws IOException
     */
    public RecordManager(File dir, String fileName, Context context)
    		throws IOException {
    	
    	records = new HashMap<String, Record>();
    	recordsByUrgency = new LinkedList<Record>();
    	
    	// Populates the Record list using stored data, if it exists.
        File file = new File(dir, fileName);
        if (file.exists()) {
            this.populate(file.getPath(), context);
        }	
        else {
            file.createNewFile();
        }
    }
    
    /**
     * Adds a Record to this RecordManager.
     * @param record The Record to be added.
     */
    public void add(Record record) {
        records.put(record.getHealthCardNum(), record);
        int position = 0;
        if (recordsByUrgency.size() == 1) {
        		recordsByUrgency.add(record);
        }
        else {
	        
	        while (position + 1 < recordsByUrgency.size()) {
		        	
	        		// Does the new record have the same urgency as this one?
	        		Record thisRecord = (Record) recordsByUrgency.get(position);
		        	if (thisRecord.getUrgencyRating() == record.getUrgencyRating()) {
		        		int[] thisArraivalTime = (int[]) thisRecord.getDobAsIntArray();
		        		int[] newArrivalTime = (int[]) record.getDobAsIntArray();
		        		
		        		// Which patient arrived first?	
		        		boolean newOneComesfirst = true;
		        		for (int i = 0; i < newArrivalTime.length; i++) {
		        			if (thisArraivalTime[i] < newArrivalTime[i]) {
		        				newOneComesfirst = false;
		        				break;
		        			}
		        		}
		        		// The record should be added before the next one	
		        		if (newOneComesfirst) {
		        			break;
		        		}
	        		
	    		}
	        			 
		    		else {
		    			position =  position + 1;
		    			}
	        }
        	
	        recordsByUrgency.add(position, record);
        }
    }
    
    /**
     * Removes the given health card number from the RecordManager
     * @param healthcardnum the health card number of a patient
     */
    public void remove(String healthcardnum) {
        recordsByUrgency.remove(records.get(healthcardnum));
        records.remove(healthcardnum);
    }

    
    /**
     * Returns the Records managed by this RecordManager.
     * @return A map of patient health card numbers to Record objects.
     */
    public Map<String, Record> getRecords() {
        return records;
    }
    
    /**
     * Returns the Patient with the highest priority.
     * @return the Patient with the highest priority.
     */
    public Record getRecord() {
		return this.recordsByUrgency.removeFirst();    	
    }
    
    /**
     * Return the patient Record which corresponds to a given health card
     * number.
     * @param healthCardNum The given health card number.
     * @return The Record in question.
     */
    public Record getRecord(String healthCardNum) {
    	return records.get(healthCardNum);
    }
    
    /**
     * Returns a string representation of all Records of patients who have been
     * at the hospital.
     * @return A string representation of all patient Records.
     */
    @Override
    public String toString() {
    	String result = "";
    	for (Record r : records.values()) {
    		result += r.toString() + "\n";
    		}
    	return result;
    }

    /** Saves the Record objects to file outputStream.
     * @param outputStream The output stream.
     */
    public void saveToFile(FileOutputStream outputStream) {
        try {
        	// write record info one per line into outputStream
        	for (Record r : records.values()) {
        		outputStream.write((r.toString() + "\n").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /** Populates the map of Records from the file at path filePath.
     * @param filePath The filepath of the data file.
     * @throws FileNotFoundException If record file doesn't exist 
     */
    private void populate(String filePath, Context context) throws 
    FileNotFoundException {
        
        Scanner scanner = new Scanner(new FileInputStream(filePath));
        String[] record;
        
        while (scanner.hasNextLine()) {
        	record = scanner.nextLine().split(",");
        	String[] name = {record[0], record[1]};
        	String healthCardNumber = record[2];
        	String[] dob = {record[3], record[4], record[5]};
        	Double temperature = Double.parseDouble(record[6]);
        	int bloodPressure = Integer.decode(record[7]);
        	int heartRate = Integer.decode(record[8]);
        	String symptoms = record[9];
        	Boolean seenByDoctor = Boolean.getBoolean(record[10]);
        	String arrivalTime  = record[11];
        	
    		Record r = new Record(name, healthCardNumber, dob);
    		r.setBloodPressure(bloodPressure);
    		r.setHeartRate(heartRate);
    		r.setSeenByDoctor(seenByDoctor);
    		r.setSymptoms(symptoms);
    		r.setTemperature(temperature);
    		r.setArrivalTime(arrivalTime);
    		r.updateUrgencyRating();
    		
        	add(r);
        }
        scanner.close();
    }   
	
}

