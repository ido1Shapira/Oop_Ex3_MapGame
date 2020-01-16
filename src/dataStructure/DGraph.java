package dataStructure;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import utils.Point3D;

/**
 * This class represents a directional weighted graph.
 */
public class DGraph implements graph, Serializable{

	private int id;
	private HashMap<Integer,node_data> idToVertex;
	private HashMap<Integer,HashMap<Integer,edge_data>> idToEdge; 
	private int mc;
	private int edgeNum;


	// static variable single_instance of server 
	private static DGraph single_instance = null; 

	// private constructor restricted to this class itself 
	/////////Default constructor//////////
	private DGraph() {
		this.id =-1;
		this.idToVertex = new HashMap<Integer,node_data>();
		this.idToEdge = new HashMap<Integer,HashMap<Integer,edge_data>>();
		this.mc = 0;
		this.edgeNum=0;
	}

	// static method to create instance of MovingAlgo class 
	public static DGraph getDGraph() 
	{ 
		if (single_instance == null) {
			synchronized (DGraph.class) {
				if (single_instance == null)
					single_instance = new DGraph(); 
			}
		}
		return single_instance;
	}


	/**
	 * return the node_data by the node_id,
	 * @param key - the node_id
	 * @return the node_data by the node_id, null if none.
	 */
	@Override
	public node_data getNode(int key) {
		try {
			return this.idToVertex.get(key);
		}
		catch(Exception e) {
			return null;
		}
	}
	/**
	 * return the data of the edge (src,dest), null if none.
	 * Note: this method should run in O(1) time.
	 * @param src
	 * @param dest
	 * @return
	 */
	@Override
	public edge_data getEdge(int src, int dest) {
		try {
			return this.idToEdge.get(src).get(dest);
		}
		catch(Exception e) {
			return null;
		}
	}
	/**
	 * add a new node to the graph with the given node_data.
	 * Note: this method should run in O(1) time.
	 * @param n
	 */
	@Override
	public void addNode(node_data n) {
		id++;
		int sizeBefore = this.nodeSize();
		this.idToVertex.put(id,new Vertex(new Point3D(n.getLocation()),id));
		this.idToEdge.put(id,new HashMap<Integer,edge_data>());	
		if(this.nodeSize() -1 == sizeBefore) {this.mc++;}
	}

	/**
	 * Connect an edge with weight w between node src to node dest.
	 * * Note: this method should run in O(1) time.
	 * @param src - the source of the edge.
	 * @param dest - the destination of the edge.
	 * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
	 */
	@Override
	public void connect(int src, int dest, double w) {
		if(this.getNode(src)==null || this.getNode(dest)==null || w<0 || src == dest) return;
		int sizeBefore = this.edgeSize();
		try {
			if(this.idToEdge.get(src).get(dest)==null)
				this.edgeNum++;
			this.idToEdge.get(src).put(dest, new Edge(src,dest,w));
		}
		catch(NullPointerException e){
			this.idToEdge.put(src,new HashMap<Integer,edge_data>());	
			this.idToEdge.get(src).put(dest, new Edge(src,dest,w));
			this.edgeNum++;
		}
		if(this.edgeSize() -1 == sizeBefore) {this.mc++; }
	}
	/**
	 * This method return a pointer (shallow copy) for the
	 * collection representing all the nodes in the graph. 
	 * Note: this method should run in O(1) time.
	 * @return Collection<node_data>
	 */
	@Override
	public Collection<node_data> getV() {
		return this.idToVertex.values();
	}
	/**
	 * This method return a pointer (shallow copy) for the
	 * collection representing all the edges getting out of 
	 * the given node (all the edges starting (source) at the given node). 
	 * Note: this method should run in O(1) time.
	 * @return Collection<edge_data>
	 */
	@Override
	public Collection<edge_data> getE(int node_id) {
		return this.idToEdge.get(node_id).values();
	}
	/**
	 * Delete the node (with the given ID) from the graph -
	 * and removes all edges which starts or ends at this node.
	 * This method should run in O(n), |V|=n, as all the edges should be removed.
	 * @return the data of the removed node (null if none). 
	 * @param key
	 */
	@Override
	public node_data removeNode(int key) {
		node_data nodeToRemove = this.getNode(key);
		if(nodeToRemove != null) {
			for (Iterator<node_data> iterator = this.getV().iterator(); iterator.hasNext();) {
				node_data v = (node_data) iterator.next();
				this.removeEdge(v.getKey(), key);
			}
			if(this.idToEdge.get(key)!=null) {
				this.edgeNum-=this.idToEdge.get(key).size();
				this.mc+=this.idToEdge.get(key).size();
			}
			this.idToVertex.remove(key);
			this.idToEdge.remove(key);
			this.mc++;
		}

		return nodeToRemove;
	}
	/**
	 * Delete the edge from the graph, 
	 * Note: this method should run in O(1) time.
	 * @param src
	 * @param dest
	 * @return the data of the removed edge (null if none).
	 */
	@Override
	public edge_data removeEdge(int src, int dest) {
		edge_data edgeToRemove = this.getEdge(src, dest);
		if(edgeToRemove != null) {
			this.idToEdge.get(src).remove(dest);
			this.edgeNum-=1;
			this.mc++;
		}
		return edgeToRemove;
	}
	/** return the number of vertices (nodes) in the graph.
	 * Note: this method should run in O(1) time. 
	 * @return
	 */
	@Override
	public int nodeSize() {
		return this.idToVertex.size();
	}
	/** 
	 * return the number of edges (assume directional graph).
	 * Note: this method should run in O(1) time.
	 * @return
	 */
	@Override
	public int edgeSize() {
		return this.edgeNum;
	}
	/**
	 * return the Mode Count - for testing changes in the graph.
	 * @return
	 */
	@Override
	public int getMC() {
		return this.mc;
	}
	/**
	 * this method initiate a Dgraph from a given string representing the graph in a json version
	 * @param gJason the graph in a json string
	 */
	public void init(String gJason) {
		String[] NE= gJason.split("Nodes", 2); //splits the string to nodes and edges
		String [] nodes= NE[1].split("\\}\\,\\{"); //each string is a node
		String [] edges =NE[0].split("\\}\\,\\{");  //each string is an edge
		for (int i = 0; i < nodes.length; i++) {
			nodes[i]= nodes[i].replace('"', '#'); //cannot split according to the char "
			nodes[i]= nodes[i].substring(nodes[i].indexOf("#:#")+3, nodes[i].indexOf("#,#")); //Separate the location from the rest info
			this.addNode(new Vertex(new Point3D(nodes[i]))); //create a node and adds it to the graph
		}
		edges[0] = edges[0].substring(edges[0].indexOf('[')); //the first and last edges' strings are different so we change them
		edges[edges.length-1] = edges[edges.length-1].substring(0, edges[edges.length-1].indexOf('}'));
		for (int i = 0; i < edges.length; i++) {
			int src, dest;
			double w;
			String [] oneEdge= edges[i].split(",",3);
			oneEdge[0] = oneEdge[0].substring(oneEdge[0].lastIndexOf(':')+1); //string represents the src
			src=Integer.parseInt(oneEdge[0]);	
			oneEdge[1] = oneEdge[1].substring(oneEdge[1].indexOf(':')+1);  //string represents the weight
			w=Double.parseDouble(oneEdge[1]);	
			oneEdge[2] = oneEdge[2].substring(oneEdge[2].lastIndexOf(':')+1);  //string represents the dest
			dest= Integer.parseInt(oneEdge[2]);
			this.connect(src, dest, w);  //create an edge and adds it to the graph
		}		
	}
}