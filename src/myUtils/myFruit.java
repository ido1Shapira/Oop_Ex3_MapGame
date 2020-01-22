package myUtils;

import org.json.JSONObject;

import Server.fruits;
import Server.robot;
import oop_utils.OOP_Point3D;
import utils.Point3D;
/**
 * this class represents a fruit in the game
 * @author Ido Shapira & Edut Cohen
 *
 */

public class myFruit  {
	
	private Point3D pos;
	private int type;
	private double value;
	
	/**
	 * constructor from a json string
	 * @param jfruit
	 */
	public myFruit(String jfruit) {
		JSONObject line;
		try {
			line = new JSONObject(jfruit);
			JSONObject f= line.getJSONObject("Fruit");
			pos= new Point3D (f.getString("pos"));
			type= f.getInt("type");
			value= f.getDouble("value");
		}
		catch (Exception e) { }
	}

	public Point3D getLocation() {
		return pos;
	}

	public int getType() {
		return type;
	}

	public double getValue() {
		return value;
	}


	
	

}
