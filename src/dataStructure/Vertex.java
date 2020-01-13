package dataStructure;

import java.io.Serializable;

import utils.Point3D;

/**
 * This class represents a node (vertex) in a (directional) weighted graph.
 * @author ido shapira & edut cohen
 */
public class Vertex implements node_data,Serializable {
	private int id;
	private Point3D location;
	private double weight;
	private String info;
	private int tag;
	
	///////////////////constructors/////////////////////
	public Vertex(Point3D location,int id) {
		this.id = id;
		this.setLocation(location);
		this.setWeight(0);
		this.setInfo("");
		this.setTag(0);
	}
	public Vertex(Point3D location) { //for stddraw
		this.setLocation(location);
		this.setWeight(0);
		this.setInfo("");
		this.setTag(0);
	}
	public Vertex(Point3D location,int id, double weight) {
		this.id = id;
		this.setLocation(location);
		this.setWeight(weight);
		this.info = "";
		this.tag = 0;
	}
	public Vertex(node_data n) { //copy constractor
		this.id = n.getKey();
		this.info = this.getInfo();
		this.location = new Point3D(n.getLocation());
		this.weight = n.getWeight();
		this.tag = n.getTag();
	}
	/**
	 * Return the key (id) associated with this node.
	 * @return
	 */
	@Override
	public int getKey() {
		return this.id;
	}
	/** Return the location (of applicable) of this node, if
	 * none return null.
	 * @return
	 */
	@Override
	public Point3D getLocation() {
		return this.location;
	}
	/** Allows changing this node's location.
	 * @param p - new new location  (position) of this node.
	 */
	@Override
	public void setLocation(Point3D p) {
		this.location = new Point3D(p);
	}
	/**
	 * Return the weight associated with this node.
	 * @return
	 */
	@Override
	public double getWeight() {
		return this.weight;
	}
	/**
	 * Allows changing this node's weight.
	 * @param w - the new weight
	 * @throws Exception 
	 */
	@Override
	public void setWeight(double w) {
		if (w >= 0) {
			this.weight = w;
		}
		else
			{throw new RuntimeException("weight must be positive");}
	}
	/**
	 * return the remark (meta data) associated with this node.
	 * @return
	 */
	@Override
	public String getInfo() {
		return this.info;
	}
	/**
	 * Allows changing the remark (meta data) associated with this node.
	 * @param s
	 */
	@Override
	public void setInfo(String s) {
		this.info = s;
	}
	/**
	 * Temporal data (aka color: e,g, white, gray, black) 
	 * which can be used be algorithms 
	 * @return
	 */
	@Override
	public int getTag() {
		return this.tag;
	}
	/** 
	 * Allow setting the "tag" value for temporal marking an node - common 
	 * practice for marking by algorithms.
	 * @param t - the new value of the tag
	 */
	@Override
	public void setTag(int t) {
		if(t >=0) {
			this.tag = t;
		}
	}
}
