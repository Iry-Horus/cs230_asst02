package comSys;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class A2TaskA {
	public static ArrayList<PoliceUnit> policeUnits = new ArrayList<PoliceUnit>();
	public static ArrayList<Suspect> suspects = new ArrayList<Suspect>();
	public static ArrayList<Station> stations = new ArrayList<Station>();
	public static Kennel kennel;
	public static int time;
	public static int dogsInKennel;
	
	public static void main(String[] args) {	
		///generate list of Station objects
		stations.add(new Station("downtown", new Point(25, 5)));
		stations.add(new Station("midtown", new Point(80, 30)));
		stations.add(new Station("uptown", new Point(10, 90)));
		stations.add(new Station("lazytown", new Point(70, 80)));
		
		//read police.csv and generate list of PoliceUnit objects
		policeUnits = readPoliceCSV("police.CSV");
		
		//read police.csv and generate list of Suspect objects
		suspects = readSuspectCSV("suspects.CSV");
		
		//sets max units in stations
		for(Station currentStation: stations)
			currentStation.setMaxUnits((int)Math.ceil(policeUnits.size() / (double)4));
			
		//assigns station to all police units
		System.out.println("-------- POLICE UNIT'S ASSIGNED STATIONS --------\n");
		
		for(PoliceUnit currentPoliceUnit: policeUnits)
			currentPoliceUnit.setAssignedStation();
		
		System.out.println("\n------------------------------------------------\n------------------------------------------------\n");
		
		//creates kennel
		kennel = new Kennel();
		
		//sets number of dogs in kennel
		dogsInKennel = (int)Math.ceil(suspects.size() / (double)2);
		
		//service executor 
		ExecutorService pool = Executors.newFixedThreadPool(policeUnits.size());
		time = 0;
		while (time < 120) {
			for (PoliceUnit currentPoliceUnit: policeUnits)
				pool.execute(currentPoliceUnit);
			time++;
			System.out.println("\n-------------------- TRIAL " + time + " -------------------\n" + "--------------- DOGS IN KENNEL: " + dogsInKennel + "---------------\n");
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		//System.out.println("Finished");
		pool.shutdown();
		
		//prints to output files
		writePoliceCSV();
		writeSuspectCSV();
		
	}
	
	//prints all current PoliceUnit data to a .csv file
	public static void writePoliceCSV() {
		try {
			//opens file or creates new file if file does not exist
			File file = new File("police-output.csv");
			if(!file.exists()) {
				file.createNewFile();
			}
			
			//creates PrintWriter object
			PrintWriter pw = new PrintWriter(file);
			
			//prints column titles 
			pw.println("id,x.location,y.location,status,dog,suspect");
			
			//prints PoliceUnit information
			for (PoliceUnit unit: policeUnits) {
				String suspect;
				if (unit.getAssignedSuspect() == null)
					suspect = "";
				else 
					suspect = unit.getAssignedSuspect().getID();
				String currentLine = String.format("%s,%s,%s,%s,%s,%s", unit.getID(), unit.getLocation().getX(), unit.getLocation().getY(), unit.getStatus(), unit.getHasDog(), suspect);
				pw.println(currentLine);
			}
			
			pw.close();
			System.out.println("\n------------------------------------------------\n------------------------------------------------\n\npolice-output.csv created successfully.");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//prints all current Suspect data to a .csv file
	public static void writeSuspectCSV() {
		try {
			//opens file or creates new file if file does not exist
			File file = new File("suspects-output.csv");
			if(!file.exists()) {
				file.createNewFile();
			}
			
			//creates PrintWriter object
			PrintWriter pw = new PrintWriter(file);
			
			//prints column titles 
			pw.println("id,x.location,y.location,status,police unit");
			
			//prints PoliceUnit information
			for (Suspect suspect: suspects) {
				String policeUnit;
				if (suspect.getAssignedPoliceUnit() == null)
					policeUnit = "";
				else 
					policeUnit = suspect.getAssignedPoliceUnit().getID();
				String currentLine = String.format("%s,%s,%s,%s,%s", suspect.getID(), suspect.getLocation().getX(), suspect.getLocation().getY(), suspect.getStatus(), policeUnit);
				pw.println(currentLine);
			}
			
			pw.close();
			System.out.println("suspects-output.csv created successfully.");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//reads police.csv and returns ArrayList of PoliceUnit objects
	public static ArrayList<PoliceUnit> readPoliceCSV (String filename){
		try {
		    Scanner scanner = new Scanner(new File(filename));
	    	scanner.nextLine();
	    	while (scanner.hasNextLine()) {
	    		String[] unitData = scanner.nextLine().split(",");
	    		Point location = new Point(Integer.parseInt(unitData[1]), Integer.parseInt(unitData[2]));
	    		PoliceUnit newUnit = new PoliceUnit(unitData[0], location, unitData[3], unitData[4], null, null);	
	    		policeUnits.add(newUnit);
	    	}
		
		} catch(FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    return policeUnits;
	}
	
	//reads suspects.csv and returns ArrayList of Suspect objects
	public static ArrayList<Suspect> readSuspectCSV (String filename){
		try {
		    Scanner scanner = new Scanner(new File(filename));
	    	scanner.nextLine();
	    	while (scanner.hasNextLine()) {
	    		String[] suspectData = scanner.nextLine().split(",");
	    		Point location = new Point(Integer.parseInt(suspectData[1]), Integer.parseInt(suspectData[2]));
	    		Suspect newUnit = new Suspect(suspectData[0], location, suspectData[3], null);
	    		suspects.add(newUnit);
	    	}
		
		} catch(FileNotFoundException e) {
	        e.printStackTrace();
	    }
		
	    return suspects;
	}
}