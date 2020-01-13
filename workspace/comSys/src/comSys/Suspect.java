package comSys;

import java.awt.Point;

public class Suspect {
	private String id;
	private Point location;
	private String status;
	private PoliceUnit policeUnit;
	
	public Suspect(String id, Point location, String status, PoliceUnit policeUnit) {
		this.id = id;
		this.location = location;
		this.status = status;
		this.policeUnit = policeUnit;
	}
	
	public String getID() {
		return id;
	}
	
	public Point getLocation() {
		return location;
	}
	
	public void setStatus(String newStatus) {
		status = newStatus;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setAssignedPoliceUnit(PoliceUnit assignedPoliceUnit) {
		policeUnit = assignedPoliceUnit;
	}
	
	public PoliceUnit getAssignedPoliceUnit() {
		return policeUnit;
	}

}
