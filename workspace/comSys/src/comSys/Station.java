package comSys;

import java.awt.Point;

public class Station {
	private String name;
	private Point location;
	private boolean space = true;
	private int numberOfPoliceUnits = 0;
	private int maxUnits = 0;
	
	public Station(String name, Point location) {
		this.name = name;
		this.location = location;
	}
	
	public void setMaxUnits(int maxUnits) {
		this.maxUnits = maxUnits;
	}
	
	public Point getLocation() {
		return location;
	}
	
	public String getName() {
		return name;
	}
	
	public void addPoliceUnit() {
		numberOfPoliceUnits ++;
	}
	
	public boolean hasSpace() {
		if (numberOfPoliceUnits >= maxUnits)
			space = false;
		return space;
	}
}
