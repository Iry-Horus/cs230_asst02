package comSys;

/* Zachary Bennett
 * zben475
 * 884799600
 * CompSci 230 A2TaskA
 */

import java.awt.Point;
import java.util.ArrayList;

public class PoliceUnit implements Runnable {
	private String id;
	private Point location;
	private String status;
	private String hasDog;
	private Suspect assignedSuspect;
	private Station assignedStation;
	private int timeAtScene = 0;
	
	public PoliceUnit(String id, Point location, String status, String hasDog, Suspect assignedSuspect, Station assignedStation) {
		this.id = id;
		this.location = location;
		this.status = status;
		this.hasDog = hasDog;
		this.assignedSuspect = assignedSuspect;
		this.assignedStation = assignedStation;		
	}
	
	public String getID() {
		return id;
	}
	
	public Point getLocation() {
		return location;
	}
	
	public String getStatus() { 
	    return status;
	}
	
	public String getHasDog() {
		return hasDog;
	}
	
	public Suspect getAssignedSuspect() {
		return assignedSuspect;
	}
	
	public synchronized void setAssignedStation() {
		assignedStation = getClosestHasSpaceStation(A2TaskA.stations);
		assignedStation.addPoliceUnit(); 
		log("assigned " + assignedStation.getName() + " station.");
	}
	
	public Station getAssignedStation() {
		return assignedStation;
	}
	
	public synchronized boolean moveToPoint(Point p, int moves) {
		while (location.getX() < p.getX() && moves != 0) {
			location.move((int)location.getX() + 1, (int)location.getY());
			moves --;
		} 
		while (location.getX() > p.getX() && moves != 0) {
				location.move((int)location.getX() - 1, (int)location.getY());
				moves --;
		}
		while (location.getY() < p.getY() && moves != 0) {
			location.move((int)location.getX(), (int)location.getY() + 1);
			moves --;
		} 
		while (location.getY() > p.getY() && moves != 0) {
			location.move((int)location.getX(), (int)location.getY() - 1);
			moves --;
		}
		boolean gotThere = location.equals(p);
		return gotThere;
	}
	
	public synchronized Suspect getClosestUnassignedSuspect(ArrayList<Suspect> suspects) { 
	    double shortestDistance=-1;
		Suspect target = null;	
		for (int i = 0; i < suspects.size(); i++) {
			double xDist = Math.abs(suspects.get(i).getLocation().getX() - location.getX());
			double yDist = Math.abs(suspects.get(i).getLocation().getY() - location.getY());
			
			if (shortestDistance < 0) {
				if (suspects.get(i).getStatus().equalsIgnoreCase("Unassigned")) {
					shortestDistance = Math.hypot(xDist, yDist);
					target = suspects.get(i);
				}
			} else if (Math.hypot(xDist, yDist) < shortestDistance) {
				if (suspects.get(i).getStatus().equalsIgnoreCase("Unassigned")) {
					shortestDistance = Math.hypot(xDist, yDist);
					target = suspects.get(i);
				}
			}
		}
		return target;
	}
	
	public synchronized Station getClosestHasSpaceStation(ArrayList<Station> stations) {
	    double shortestDistance = -1;
	    double xDist;
	    double yDist;
		Station target = null;	
		for (Station currentStation: stations) {
			xDist = Math.abs(currentStation.getLocation().getX() - location.getX());
			yDist = Math.abs(currentStation.getLocation().getY() - location.getY());
			if (shortestDistance < 0) {
				if (currentStation.hasSpace()) {
					shortestDistance = Math.hypot(xDist, yDist);
					target = currentStation;
				}
			} else if (Math.hypot(xDist, yDist) < shortestDistance) {
				if (currentStation.hasSpace()) {
					shortestDistance = Math.hypot(xDist, yDist);
					target = currentStation;
				}
			}
		}
		return target;
	}
	
	public synchronized void moveToSuspect() {
	    boolean gotThere = moveToPoint(assignedSuspect.getLocation(), 4);
	    if (gotThere == true) {
	    	status = "At Scene";
	    	assignedSuspect.setStatus("Caught");
	    }
	}
	
	
	public synchronized void moveToStation() {
	    boolean gotThere = moveToPoint(assignedStation.getLocation(), 3);
	    if (gotThere == true) {
    	    if (assignedSuspect.getStatus().equalsIgnoreCase("Caught")) {
    	        assignedSuspect.setStatus("Jailed");
    	        assignedSuspect.setAssignedPoliceUnit(null);
    	    	assignedSuspect = null;
    	    }
            status = "Standby";
	    }
	}
	
	public synchronized void moveToKennel() {
		boolean gotThere = moveToPoint(A2TaskA.kennel.getLocation(), 3);
		if (gotThere == true)
			status = "At Kennel";
	}
	
	public void log(String message)
	{
		System.out.println(id + "]> " + message);
	}

	@Override
	public synchronized void run() {
		log("start status: " + status + " | dog: " + hasDog);
		switch(status) {
			case "Standby" :
				assignedSuspect = getClosestUnassignedSuspect(A2TaskA.suspects);
				if (assignedSuspect == null) 
					break;	
				else {
					assignedSuspect.setStatus("Assigned");
					assignedSuspect.setAssignedPoliceUnit(this);
					status = "Approaching Kennel";
				}
				break;
			   
			case "Approaching Kennel" :
				moveToKennel();
				break;
			   
			case "At Kennel":
				if(hasDog.equalsIgnoreCase("No")) {
					if (A2TaskA.kennel.getIsEmpty() == false) {
						A2TaskA.kennel.removeDog();
						hasDog = "Yes";
						status  = "Approaching Suspect";
					} else if (A2TaskA.kennel.getIsEmpty() == true)
						break;
					
				} else if (hasDog.equalsIgnoreCase("Yes")) {
					A2TaskA.kennel.addDog();
					hasDog = "No";
					status = "Returning";
				}
				break;
			
			case "Approaching Suspect":
				moveToSuspect();
				break;
				
			case "At Scene":
				if (assignedSuspect.getStatus().equalsIgnoreCase("Assigned"))
					assignedSuspect.setStatus("Caught");
				if (timeAtScene < 4)
					timeAtScene ++;
				else {
					status = "Approaching Kennel";
					timeAtScene = 0;
				}	
				break;
			
			case "Returning":
				moveToStation();
				break;
				
		}
		log("end status: " + status + " | dog: " + hasDog);
	}
}
