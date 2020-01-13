package comSys;

import java.awt.Point;

public class Kennel {
	private Point location = new Point(50, 50);
	
	public synchronized void addDog() {
		A2TaskA.dogsInKennel ++;
		//print dogsInKennel
		//System.out.println(A2TaskA.dogsInKennel);
	}
	
	public synchronized void removeDog() {
		A2TaskA.dogsInKennel --;
		//print dogsInKennel
		//System.out.println(A2TaskA.dogsInKennel);
	}
	
	public int getDogsInKennel() {
		return A2TaskA.dogsInKennel;
	}
	
	public boolean getIsEmpty() {
		if (A2TaskA.dogsInKennel > 0)
			return false;
		else 
			return true;
	}
	
	public Point getLocation() {
		return location;
	}
}
