package myUtils;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Point3D;

public final class MyParser {

	/*
	 * Returns the requested field from the object
	 */
	private static String Json2value(final String json ,
			final String object , final String field) throws JSONException {
		JSONObject line = new JSONObject(json);
		JSONObject jsonforObject = line.getJSONObject(object);
		return ""+ jsonforObject.get(field);
	}
	
	//getters for server 
	public static int getRobotsNum(String json) {
		try {
			return Integer.parseInt(Json2value(json,"GameServer","robots"));
		}
		catch (JSONException e) {e.printStackTrace();}
		return -1;
	}

	public static int getMoves(String json) {
		try {
			return Integer.parseInt(Json2value(json,"GameServer","moves"));
		}
		catch (JSONException e) {e.printStackTrace();}
		return -1;
	}

	public static String getBackGround(String json) {
		try {
			return Json2value(json,"GameServer","graph");
		}
		catch (JSONException e) {e.printStackTrace();}
		return null;
	}

	
	//getters for robot
	
	public static int getRobotId(String json){
		try {
			return Integer.parseInt(Json2value(json,"Robot","id"));
		}
		catch (JSONException e) {e.printStackTrace();} 
		return -1;
	}

	public static Point3D getRobotPosition(String json) {
		try {
			return new Point3D(Json2value(json,"Robot","pos"));
		}
		catch (JSONException e) {e.printStackTrace();}
		return null;
	}

	public static int getRobotSrc(String json){
		try {
			return Integer.parseInt(Json2value(json,"Robot","src"));
		}
		catch (JSONException e) {e.printStackTrace();}
		return -1;
	}
	public static int getRobotDest(String json){
		try {
			return Integer.parseInt(Json2value(json,"Robot","dest"));
		}
		catch (JSONException e) {e.printStackTrace();}
		return -1;
	}
	public static double getRobotValue(String json){
		try {
			return Double.valueOf(Json2value(json,"Robot","value"));
		}
		catch (JSONException e) {e.printStackTrace();}
		return -1;
	}
	public static double getRobotSpeed(String json){
		try {
			return Double.valueOf(Json2value(json,"Robot","speed"));
		}
		catch (JSONException e) {e.printStackTrace();}
		return 1;
	}
	
	
	///getters for fruit
	public static double getFruitValue(String json){
		try {
			return Double.valueOf(Json2value(json,"Fruit","value"));
		}
		catch (JSONException e) {e.printStackTrace();}
		return -1;
	}
	public static int getFruitType(String json){
		try {
			return Integer.parseInt(Json2value(json,"Fruit","type"));
		}
		catch (JSONException e) {e.printStackTrace();}
		return -1;
	}
	public static Point3D getFruitPosition(String json){
		try {
			return new Point3D(Json2value(json,"Fruit","pos"));
		}
		catch (JSONException e) {e.printStackTrace();}
		return null;
	}



}
