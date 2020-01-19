package myUtils;

import Server.robot;
import oop_utils.OOP_Point3D;
import utils.Point3D;

public class myRobot {
	 private int id;
	 private double value;
	 private int src;
	 private int dest;
	 private Point3D pos;
	 private double speed;
	 private boolean hasDest;
	 
	public int getID() {
		return id;
	}

	public Point3D getLocation() {
		return pos;
	}

	public double getMoney() {
		return value;
	}

	public int getNextNode() {
		return dest;
	}

	public double getSpeed() {
		return speed;
	}

	public int getSrcNode() {
		return src;
	}

	public boolean isMoving() {
		if(dest !=-1)
			return true;
		return false;
	}

	public boolean setNextNode(int arg0) {
		dest=arg0;
		return true;
	}

	public void setSpeed(double arg0) {
		speed= arg0;
	}
	
	public void setLocation(Point3D pos1) {
		pos= pos1;
	}

	public boolean isHasDest() {
		return hasDest;
	}

	public void setHasDest(boolean hasDest) {
		this.hasDest = hasDest;
	}



}
