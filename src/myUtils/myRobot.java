package myUtils;

import org.json.JSONException;
import org.json.JSONObject;

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

	public myRobot(String jRobot) {
		JSONObject line;
		try {
			line = new JSONObject(jRobot);
			JSONObject robi= line.getJSONObject("Robot");
			id= robi.getInt("id");
			value= robi.getDouble("value");
			speed= robi.getDouble("speed");
			src= robi.getInt("src");
			dest= robi.getInt("dest");
			pos= new Point3D(robi.getString("pos"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public int getID() {
		return id;
	}

	public Point3D getLocation() {
		return pos;
	}

	public double getMoney() {
		return value;
	}

	public int getDest() {
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
