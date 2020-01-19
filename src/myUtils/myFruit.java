package myUtils;

import Server.fruits;
import Server.robot;
import oop_utils.OOP_Point3D;
import utils.Point3D;

public class myFruit  {
	
	private Point3D pos;
	private int type;
	private double value;
	private boolean haveRobot;
	
	public Point3D getLocation() {
		return pos;
	}

	public int getType() {
		return type;
	}

	public double getValue() {
		return value;
	}

	public boolean isHaveRobot() {
		return haveRobot;
	}

	public void setHaveRobot(boolean haveRobot) {
		this.haveRobot = haveRobot;
	}
	
	

}
