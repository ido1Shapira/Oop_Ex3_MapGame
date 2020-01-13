package dataStructure;

import java.io.Serializable;

public class Edge implements edge_data, Serializable{	
	private int idSrc;
	private int idDest;
	private double weight;
	private String info;
	private int tag;
	///////////////////constructors/////////////////////
	public Edge(int idSrc, int idDest, double weight, String info, int tag) {
		this.idSrc = idSrc;
		this.idDest = idDest;
		if(weight >= 0) this.weight = weight;
		else throw new RuntimeException("weight must be a positive value");
		this.setInfo(info);
		this.setTag(tag);
	}
	public Edge(int idSrc, int idDest, double weight, String info) {
		this.idSrc = idSrc;
		this.idDest = idDest;
		if(weight >= 0) this.weight = weight;
		else throw new RuntimeException("weight must be a positive value");
		this.setInfo(info);
		this.tag = 0;
	}
	public Edge(int idSrc, int idDest, double weight) {
		this.idSrc = idSrc;
		this.idDest = idDest;
		if(weight >= 0) this.weight = weight;
		else throw new RuntimeException("weight must be a positive value");
		this.info = "";
		this.tag = 0;
	}
	/**
	 * The id of the source node of this edge.
	 * @return
	 */
	@Override
	public int getSrc() {
		return this.idSrc;
	}
	/**
	 * The id of the destination node of this edge
	 * @return
	 */
	@Override
	public int getDest() {
		return this.idDest;
	}
	/**
	 * @return the weight of this edge (positive value).
	 */
	@Override
	public double getWeight() {
		return this.weight;
	}
	/**
	 * return the remark (meta data) associated with this edge.
	 * @return
	 */
	@Override
	public String getInfo() {
		return this.info;
	}
	/**
	 * Allows changing the remark (meta data) associated with this edge.
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
	 * Allow setting the "tag" value for temporal marking an edge - common 
	 * practice for marking by algorithms.
	 * @param t - the new value of the tag
	 */
	@Override
	public void setTag(int t) {
		if (t >= 0) this.tag = t;
	}
}
