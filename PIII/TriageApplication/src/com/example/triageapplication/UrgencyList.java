package com.example.triageapplication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.Collator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

import android.content.Context;

public class UrgencyList {
	private Queue urgencyList;

	
	public UrgencyList(){
		this.urgencyList = new PriorityQueue(); 
	}

	//add patients by decreasing order
	//
	public void addPatient(Record patient){
		if (!patient.getSeenByDoctor()){
			PriorityQueue tempListBefore = new PriorityQueue();
			PriorityQueue tempListAfter = new PriorityQueue();
			
			while (this.urgencyList.peek()!= null){//run through all of the objects
					if (((Record) this.urgencyList.peek()).getUrgencyRating() 
							== patient.getUrgencyRating()){
						
						while (this.urgencyList.peek()!= null){
							tempListAfter.add(this.urgencyList.poll());//grabs remaining objects
						
						}
						
						break;
					} else{
						tempListBefore.add(this.urgencyList.poll());//Removes from the original list places it into storage
						
					}
			
					this.urgencyList.clear();//empties to prevent duplicates
					
					this.urgencyList = tempListBefore;
					this.urgencyList.add(patient);
					
					
					//Queue don't allow popping from the back this reorders 
					Record [] tempListAfterArray = (Record[]) tempListAfter.toArray();
					
					for (int i = 0; tempListAfterArray.length> i; i++){
						this.urgencyList.add(tempListAfterArray[tempListAfterArray.length-i]);
						
					}
			}}
	}
	
	
	// also remvoes
	//probably gets called when seen by doctor 
	
	/** Grabs the lowest element
	 * @return
	 */
	public Record nextPatient(){
		if (this.urgencyList.size()>0){		
			return (Record) this.urgencyList.poll();
		} else {
			//I think I need to raise an exception here 
			return;
		}
	}

	/** Grabs a particular patient for a given health card number
	 * @param healthCardNum
	 * @return
	 */
	public Record grabPatient(String healthCardNum){
		if (this.urgencyList.contains(StaffMember.getRecord(healthCardNum))){//need to modify once erin is done
			Record [] iteratorArray = (Record[]) this.urgencyList.toArray();
			
			for (int i=0; i< iteratorArray.length; i++){
				if (iteratorArray[i] == StaffMember.getRecord(healthCardNum)){
					return StaffMember.getRecord(healthCardNum);
				} else {
					
				}
				
			}
		}
	}
	
	//kind of extra but it easy implement remvoes a given patietn
	//probably gets called when seen by doctor 
	public void removePatient(String healthCardNum){
		if (this.urgencyList.contains(healthCardNum)){//need to modify once erin is done
			this.urgencyList.remove(healthCardNum);
		}		
	}
	
	public void sort(){
		Record [] sortingArray = (Record[]) this.urgencyList.toArray() 
				[this.urgencyList.size()];
		
		
		Record smallest = sortingArray[0];
		Record temp;
		
		for (int z=0; sortingArray.length>z; z++){
			for (int y=0; sortingArray.length-z>y; y++)
			if (sortingArray[y].getUrgencyRating() <smallest){
				temp = sortingArray[y];
				
				sortingArray[y] = smallest;
				
				smallest = temp;
			}
		}	
		
		
		
		//grab a pivot
	}
	
	
	public void populate(String filePath, Context context) throws 
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
        	
        	if (seenByDoctor != true){
        		Record r = new Record(name, healthCardNumber, dob);
        		r.setBloodPressure(bloodPressure);
        		r.setHeartRate(heartRate);
        		r.setSeenByDoctor(seenByDoctor);
        		r.setSymptoms(symptoms);
        		r.setTemperature(temperature);
        		r.setArrivalTime(arrivalTime);
    		
        		urgencyList.add(r);
        	}
        }
        scanner.close();
        
        sort();
    }
}
