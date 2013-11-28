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
import java.util.Queue;
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
	private LinkedList<String> recordsByUrgency;	
	
	/** A queue of prescriptions to be filled. */
	private LinkedList<String> prescriptions;	
	
	/** The directory that all RecordManager files are saved to. */
	private File directory;
	
    /**
     * Constructs a new RecordManager that manages a collection of 
     * Records stored in directory dir in a file named fileName.
     * @param dir The directory in which the data file is stored.
     * @param fileName The data file containing Record information
     * @param context The context of this Android application.
     * @throws IOException
     */
    public RecordManager(File dir, String fileName1, String fileName2, Context context)
    		throws IOException {
    	
    	records = new HashMap<String, Record>();
    	recordsByUrgency = new LinkedList<String>();
    	prescriptions = new LinkedList<String>();
    	
    	// Populates the Record list using stored data, if it exists.
        File file1 = new File(dir, fileName1);
        if (file1.exists()) {
            this.populateRecords(file1.getPath(), context);
        }	
        else {
            file1.createNewFile();
        }
        
        File file2 = new File(dir, fileName2);
        if (file2.exists()) {
            this.populatePrescriptions(file2.getPath(), context);
        }	
        else {
            file2.createNewFile();
        }
    }
    
    /**
     * Adds a Record to this RecordManager.
     * @param record The Record to be added.
     */
    public void add(Record record) {
        records.put(record.getHealthCardNum(), record);
        int position = 0;
        
        if (record.isCheckedOut() == false) {
        	
	        //Add if the patient has a lower priority and was came after all the other patients 
        	// or if the List is of size 0.
        	if (position >= recordsByUrgency.size() - 1) {
        		recordsByUrgency.add(position, record.getHealthCardNum());
        		return;
	        	}
		        
	        	else {		        
			        while (position < recordsByUrgency.size()) {
				    		Record thisRecord = records.get((String) recordsByUrgency.get(position));
				    		
				        	// If the new patient has a higher urgency rating than this one, add it here.
				        	if (thisRecord.getUrgencyRating() < record.getUrgencyRating()) {
				        		recordsByUrgency.add(position, record.getHealthCardNum());
				        		return;
				        	}
				    		
				        	
							// Does the new record have the same urgency as this one?
				        	if (thisRecord.getUrgencyRating() == record.getUrgencyRating()) {
				        		recordsByUrgency.add(position, record.getHealthCardNum());	
				        		break;
//				        		int[] thisArraivalTime = (int[]) thisRecord.getDobAsIntArray();
//				        		int[] newArrivalTime = (int[]) record.getDobAsIntArray();
//				        		
//				        		// Which patient arrived first?	
//				        		for (int i = 0; i < 6; i++) {
//				        			if (thisArraivalTime[i] < newArrivalTime[i]) {
//				        				position = position + 1;
//				        				break;
//				        			}
//				        			
//					        		// The record should be added before the next one	
//					        		if (thisArraivalTime[i] > newArrivalTime[i]){
//				        				recordsByUrgency.add(position, record.getHealthCardNum());		        			
//					        			return;
//					        			}
//					        		}	
				        		}
				    			        		
				        	// The new patient has a lower urgency than this one, try the next one ie. advance.        	        			 
				    		else {
				    			position =  position + 1;
				    			}
				        	}
			        }
        }
        }
		    
    /**
     * Removes the given health card number from the RecordManager
     * @param healthcardnum the health card number of a patient
     */
    public void removePatient(String healthcardnum) {
        //recordsByUrgency.remove(records.get(healthcardnum));
        records.remove(healthcardnum);
    }
    
    public void removePatientFromUrgency(Record record) {
    	int location = recordsByUrgency.indexOf(record.getHealthCardNum());
    	if (location != -1){
    		recordsByUrgency.remove();
    	}
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
    public String getFirst() {
    	return this.recordsByUrgency.removeFirst();
    }
    
    /**
     * Returns the list of Patients sorted by Urgency.
     * @return the list of Patients sorted by Urgency
     */
    public LinkedList<String> getUrgencyRecords() {
		return this.recordsByUrgency;    	
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
    /**
     * Deletes the file filename from internal storage. Then creates a new file.
     * @param filename the file that is to be deleted.
     */
    public void clearFile(String filename){
    	File file = new File(this.getDirectory(), filename);
        if (file.exists()) {
	        file.delete();
        }
        new File(this.getDirectory(), filename);
        
    }

    /** Saves the Record objects to file outputStream.
     * @param outputStream The output stream.
     */
    public void saveRecordsToFile(String filename, FileOutputStream outputStream) {
    	clearFile(filename);
        try {
        	// write record info one per line into outputStream
        	for (Record r : records.values()) {
        		outputStream.write((r.toString() + "\n").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /** Saves the prescriptions to file outputStream.
     * @param outputStream The output stream.
     */
    public void savePerscriptionsToFile(String filename, FileOutputStream outputStream) {
    	clearFile(filename);  	
        try {
        	// write record info one per line into outputStream
        	for (String r : prescriptions) {
        		outputStream.write((r + "\n").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   
    
    /** Populates the map of Records from the file at path filePath.
     * @param filePath The filepath of the data file.
     * @throws FileNotFoundException If record file doesn't exist 
     */
    private void populateRecords(String filePath, Context context) throws 
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
         	boolean checkedOut = Boolean.valueOf(record[12]);
         	
     		Record r = new Record(name, healthCardNumber, dob);
     		r.setBloodPressure(bloodPressure);
     		r.setHeartRate(heartRate);
     		r.setSeenByDoctor(seenByDoctor);
     		r.setSymptoms(symptoms);
     		r.setTemperature(temperature);
     		r.setArrivalTime(arrivalTime);
     		r.updateUrgencyRating();
     		r.setCheckedOut(checkedOut);
     		
         	add(r); 
         }
         scanner.close();
     }   

 
    
    /** Populates the Queue of Prescriptions from the file at path filePath.
     * @param filePath The filepath of the data file.
     * @throws FileNotFoundException If record file doesn't exist 
     */
    private void populatePrescriptions(String filePath, Context context) throws 
    FileNotFoundException {
        
        Scanner scanner = new Scanner(new FileInputStream(filePath));
        String perscription;
        
        while (scanner.hasNextLine()) {
        	String prescription = scanner.nextLine();
        	prescription = prescription + "\n" + scanner.nextLine();
        	prescription = prescription + "\n" + scanner.nextLine();

        	addPrescription(prescription, context); 
        }
        
        scanner.close();
        
    }    
    
    /**
     * Adds the specified prescription to the list of prescriptions to be filled.
     * @param prescription the prescription to be added.
     */
    public void addPrescription(String prescription, Context context) throws FileNotFoundException{
    	this.prescriptions.add(prescription);

		savePerscriptionsToFile("Prescriptions", context.openFileOutput(
				"Prescriptions",Context.MODE_PRIVATE));
       
    }
    
    /**
     * Returns the next prescription to fill.
     * @return the next prescription to fill.
     * @throws FileNotFoundException 
     */
	public String getPrescription(Context context) throws FileNotFoundException {
		if (prescriptions.isEmpty()){
			return "There currently aren't any prescriptions to be filled!";
		}
		
		String patient = prescriptions.remove();
		savePerscriptionsToFile("Prescriptions", context.openFileOutput(
				"Prescriptions",Context.MODE_PRIVATE));		
		return patient;


		
	}

	/**
	 * returns the directory files are saved to in this RecordManager.
	 * @return the directory files are saved to in this RecordManager.
	 */
	public File getDirectory() {
		return directory;
	}
	
}

