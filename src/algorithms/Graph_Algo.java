package algorithms;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;

/**
 * This class represents the set of graph-theory algorithms
 * @author Ido Shapira & Edut Cohen
 *
 */
public class Graph_Algo implements graph_algorithms{
	
	private int mcGraph;
	public graph myGraph;
	private HashMap<Integer,HashSet<Integer>> vertexToNeighbors; // v1--->V
	private HashMap<Integer,HashSet<Integer>> NeighborsToVertex; // V--->v1
	
	public Graph_Algo() {
		this.mcGraph = 0;
		this.myGraph = null;
		this.vertexToNeighbors = new HashMap<Integer,HashSet<Integer>>();
		this.NeighborsToVertex = new HashMap<Integer,HashSet<Integer>>();
	}
	public Graph_Algo(graph g) {
		this.init(g);
	}
	/**
	 * initiate the graph given into graph-algo object
	 * (NOT A DEEP COPY)- gets an actual pointer to graph g
	 * in addition adds 2 new fields to help control the graph.
	 * vertexToNeighbors- represents the connection between a vertex and his "kids"-
	 *  (all the vertices that end an edge that starts with the main vertex)
	 *  NeighborsToVertex- represents the connection between a vertex and his "dads"-
	 *  (all the vertices that start an edge that ends with the main vertex)
	 *  @param g the graph to insert
	 */
	@Override
	public void init(graph g) {
		myGraph = g;
		this.mcGraph = g.getMC();
		this.vertexToNeighbors = new HashMap<Integer,HashSet<Integer>>();
		for (Iterator<node_data> Nodeiter = this.myGraph.getV().iterator(); Nodeiter.hasNext();) {
			node_data n = (node_data) Nodeiter.next();
			if(this.myGraph.getE(n.getKey()) != null) {
				for (Iterator<edge_data> Edgeiter = this.myGraph.getE(n.getKey()).iterator(); Edgeiter.hasNext();) {
					edge_data edge = (edge_data) Edgeiter.next();
					try {
						this.vertexToNeighbors.get(edge.getSrc()).add(edge.getDest());
					}
					catch(NullPointerException e){
						this.vertexToNeighbors.put(edge.getSrc(),(new HashSet<Integer>()));			
						this.vertexToNeighbors.get(edge.getSrc()).add(edge.getDest());
					}
				}
			}
		}
		this.NeighborsToVertex = new HashMap<Integer,HashSet<Integer>>();
		for (Iterator<node_data> Nodeiter = this.myGraph.getV().iterator(); Nodeiter.hasNext();) {
			node_data n = (node_data) Nodeiter.next();
			if(this.myGraph.getE(n.getKey()) != null) {
				for (Iterator<edge_data> Edgeiter = this.myGraph.getE(n.getKey()).iterator(); Edgeiter.hasNext();) {
					edge_data edge = (edge_data) Edgeiter.next();
					try {
						this.NeighborsToVertex.get(edge.getDest()).add(edge.getSrc());
					}
					catch(NullPointerException e){
						this.NeighborsToVertex.put(edge.getDest(),(new HashSet<Integer>()));			
						this.NeighborsToVertex.get(edge.getDest()).add(edge.getSrc());
					}
				}
			}
		}
	}
	/**
	 * initiate a graph from a file into graph-algo object 
	 * uses init(graph) ^ the method above
	 * @param file_name the file with the graph's info
	 */
	@Override
	public void init(String file_name) {
		try
		{    
			FileInputStream file = new FileInputStream(!file_name.contains(".txt")? file_name+".txt" : file_name); 
			ObjectInputStream in = new ObjectInputStream(file); 
			this.init((graph)in.readObject());
			in.close(); 
			file.close(); 
		} 
		catch(IOException ex) 
		{ 
			ex.printStackTrace();
		} 
		catch(ClassNotFoundException ex) 
		{ 
			System.out.println("ClassNotFoundException is caught"); 
		} 
	}
	/**
	 * save the graph field from this graph-algo object to a file
	 * @param file_name the file to save our graph info to
	 */
	@Override
	public void save(String file_name) {
		try
		{    
			FileOutputStream file = new FileOutputStream(!file_name.contains(".txt")? file_name+".txt" : file_name); 
			ObjectOutputStream out = new ObjectOutputStream(file); 
			out.writeObject((graph) this.myGraph); 			
			out.close(); 
			file.close(); 
		}   
		catch(IOException ex) 
		{
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
	}
	/**
	 * @return a deep copy of this object's graph- save the graph to a file and initiate it from the file 
	 */
	@Override
	public graph copy() {
		Random r = new Random();
		String file_name = "file"+Math.abs(r.nextInt()); //save this graph to a file with a random name
		this.save(file_name);
		Graph_Algo ga=new Graph_Algo();
		ga.init(file_name); // initiate the info from the file to a new graph
		File file = new File(file_name+".txt"); 
		file.delete(); //delete the new file we have created
		return ga.myGraph;
	}
	/**
	 * Checks whether this graph is connected or not
	 * a connected graph means there is a valid path from EVREY node to each other node in this graph
	 * returns true if and only if (iff) the graph is connected
	 * 
	 */
	@Override
	public boolean isConnected() {
		if(this.myGraph == null) new RuntimeException("the graph is null");
		if(this.myGraph.nodeSize() <= 1) return true;
		if(this.mcGraph != this.myGraph.getMC()) this.init(myGraph);
		infoTagWeightReset();		
		if(!this.checkLegal())  return false; //there is a node that is not reachable to any other node
		int i=1;
		while (this.myGraph.getNode(i) == null) i++; //finds the first node that exists
		node_data mySrc= this.myGraph.getNode(i);
		this.tagKids(mySrc);
		if(this.countTags(1)!=this.myGraph.nodeSize()) { //if not all the vertices are reachable from src
			return false;
		}		
		this.tagDads(mySrc);
		if(this.countTags(2)!=this.myGraph.nodeSize()) { //if not all the vertices can reach to src
			return false;
		}
		return true;
	}
	/**
	 * tags all of src's kids (as defined above)
	 * @param src the vertex to start from
	 */
	private void tagKids(node_data src) {
		src.setTag(1);
		for (Iterator<Integer> iterator = this.vertexToNeighbors.get(src.getKey()).iterator(); iterator.hasNext();) {
			Integer sonKey = (Integer) iterator.next();
			node_data son= this.myGraph.getNode(sonKey);
			if(son.getTag()==0) {
				this.tagKids(son);
			}
		}
	}
	/**
	 * tags all of src's dads (as defined above)
	 * @param src the vertex to start from
	 */
	private void tagDads(node_data src) {
		src.setTag(2);
		for (Iterator<Integer> iterator = this.NeighborsToVertex.get(src.getKey()).iterator(); iterator.hasNext();) {
			Integer dadKey = (Integer) iterator.next();
			node_data dad= this.myGraph.getNode(dadKey);
			if(dad.getTag()==1) {
				this.tagDads(dad);
			}
		}
	}
	/**
	 * counts how many vertices's tag is =i
	 * @param i the tag we now counting
	 * @return the number of vertices with i tag
	 */
	private int countTags(int i) {
		int count=0;
		for (Iterator<node_data> iterator = this.myGraph.getV().iterator(); iterator.hasNext();) {
			node_data current = (node_data) iterator.next();
			if(current.getTag()==i)
				count++;
		}
		return count;
	}
	/**
	 * pretesting for isConnected 
	 * @return true iff every vertex has kids and dads (at least one)
	 * which means it can reach another vertex and can be reached from another vertex 
	 */
	private boolean checkLegal() {
		int count = 0;
		for (Iterator<node_data> it = this.myGraph.getV().iterator(); it.hasNext();) {
			node_data v = (node_data) it.next();
			if(!this.vertexToNeighbors.containsKey(v.getKey())) {
				count++;}
			if(!this.NeighborsToVertex.containsKey(v.getKey())) {
				count++;}
		}      
		if(count == 0) return true;
		return false;
	}
	/**
	 * reset all the tags to 0, the info to an empty string and the weights to 0
	 */
	private void infoTagWeightReset() {
		for (Iterator<node_data> init = this.myGraph.getV().iterator(); init.hasNext();) {
			node_data v = (node_data) init.next();
			v.setInfo("");
			v.setTag(0);
			v.setWeight(Integer.MAX_VALUE-1);
		}
	}
	/**
	 * Initiate all the vertices's weight to their distance from src node
	 * and all the vertices info to a string represents their path from src
	 * set tag to 1 if we finished with this vertex
	 * based on Dijkstra's algorithm
	 * https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm 
	 * @param src the node we start from
	 */
	private void shortPathGraph(int src) {
		infoTagWeightReset();
		this.myGraph.getNode(src).setTag(0);// starting point
		this.myGraph.getNode(src).setInfo(""+src);// starting point
		this.myGraph.getNode(src).setWeight(0);
		HashMap<Integer, node_data> hashCopy = new HashMap<Integer, node_data>();
		for (Iterator<node_data> init = this.myGraph.getV().iterator(); init.hasNext();) {
			node_data v = (node_data) init.next();
			hashCopy.put(v.getKey(), v);	//init copy list for all vertices we havent visited yet
		}
		for(int i=0; i<this.myGraph.nodeSize();i++) {
			node_data current =  this.findMin(hashCopy);
			int currentKey = current.getKey();
			hashCopy.remove(currentKey);
			if( this.vertexToNeighbors.get(currentKey) !=null) {
				for (Iterator<Integer> iterator = this.vertexToNeighbors.get(currentKey).iterator(); iterator.hasNext();) {
					Integer sonKey = (Integer) iterator.next();
					node_data son = ((graph) this.myGraph).getNode(sonKey);
					double edgeWeight = ((graph) this.myGraph).getEdge(currentKey, sonKey).getWeight();
					if(son.getWeight()>(current.getWeight()+edgeWeight) && son.getTag()==0) {
						son.setWeight(current.getWeight()+edgeWeight);
						son.setInfo(current.getInfo()+" "+sonKey);
					}
				}
			}
			current.setTag(1);
		}
	}
	/**
	 * finds the vertex which its weight is minimum
	 * @param hashNotVisited the hash-map we search in
	 * @return The least weighted node
	 */
	private node_data findMin (HashMap<Integer, node_data> hashNotVisited) {
//		node_data min=new Vertex(new Point3D(0,0,0), Integer.MAX_VALUE, Integer.MAX_VALUE);
		Iterator <node_data> first = hashNotVisited.values().iterator();
		node_data min = first.next();
		for (Iterator <node_data> it = hashNotVisited.values().iterator(); it.hasNext();) {
			node_data now = (node_data) it.next();
			if(now.getTag()==0 && now.getWeight() < min.getWeight())
				min = now;
		}
		return min;
	}
	/**
	 * Calculate the shortest path distance starting from src and ending with dest
	 * @param src the node to start from
	 * @param dest the node to end with
	 * @return the distance 
	 */
	@Override
	public double shortestPathDist(int src, int dest) {
		if(this.myGraph == null) new RuntimeException("the graph is null");
		if(this.mcGraph != this.myGraph.getMC()) this.init(myGraph);
		try {
			if(src==dest && this.myGraph.getNode(src)!=null)
				return 0;
			this.shortPathGraph(src);
			return this.myGraph.getNode(dest).getWeight();	
		}
		catch (NullPointerException e) {
			System.out.println("Vertex src or dest does not exist");
			return -1;
		}
	}

	/**
	 * gets a string of nodes keys and brings back list of those nodes 
	 * including nodes that are not on the list according to the shortest path between each 2 nodes in the list
	 * @param path the string of nodes
	 * @return the list of nodes to visit
	 */
	private List<node_data> string2listExtendad (String path){
		try {
			String [] pathSplit= path.split(" ");
			ArrayList <node_data> list= new ArrayList<node_data>();
			int prev=Integer.parseInt(pathSplit[0]);
			for (int i = 1; i < pathSplit.length; i++) {
				int current= Integer.parseInt(pathSplit[i]);
				List<node_data> listToAdd=this.shortestPath(prev, current);
				listToAdd.remove(listToAdd.size()-1);
				list.addAll(listToAdd);
				prev=current;
			}
			String end=pathSplit[pathSplit.length-1];
			list.add(this.myGraph.getNode(Integer.parseInt(end)));
			return list;
		}
		catch(Exception e) {
			return null;
		}
	}
	/**
	 * Calculate the shortest path starting from src and ending with dest
	 * @param src the node to start from
	 * @param dest the node to end with
	 * @return list of nodes which are the shortest path
	 */
	@Override
	public List<node_data> shortestPath(int src, int dest)  {
		if(this.myGraph == null) new RuntimeException("the graph is null");
		if(this.mcGraph != this.myGraph.getMC()) this.init(myGraph);
		try {
			if(src==dest) {
				ArrayList<node_data> list= new ArrayList<node_data>();
				list.add(this.myGraph.getNode(src));
				return list;
			}
			this.shortPathGraph(src);
			String path = this.myGraph.getNode(dest).getInfo();
			return this.string2list(path);
		}
		catch(Exception e) {
			return null;
		}
	}
	/**
	 * Translates the string of nodes' keys to actual list of nodes
	 * @param path the string to translate
	 * @return list of nodes according to the string
	 */
	private List<node_data> string2list(String path){ //gets a string of nodes keys and brings back list of those nodes
		try {
			String [] pathSplit= path.split(" ");
			ArrayList <node_data> list= new ArrayList<node_data>();
			for (int i = 0; i < pathSplit.length; i++) {
				int toAdd= Integer.parseInt(pathSplit[i]);
				list.add(this.myGraph.getNode(toAdd));
			}
			return list;
		}
		catch(Exception e) {
			return null;
		}
	}
	/**
	 * computes a relatively short path which visit each node in the targets List.
	 * @param targets the nodes to visit
	 * @return the relatively short path that was found
	 * 
	 */
	public  List<node_data> TSP(List<Integer> targets){
		if(this.myGraph == null) new RuntimeException("the graph is null");
		if(this.mcGraph != this.myGraph.getMC()) this.init(myGraph);
		int n=targets.size();
		ArrayList<node_data> nodeList= new ArrayList<node_data>();//same list as targets but with nodes
		for (Iterator<Integer> iterator = targets.iterator(); iterator.hasNext();) {
			Integer nodeKey = (Integer) iterator.next();
			node_data currNode=this.myGraph.getNode(nodeKey);
			if(currNode!=null && currNode.getTag()!=3) {
				nodeList.add(this.myGraph.getNode(nodeKey));
				this.myGraph.getNode(nodeKey).setTag(3); //to prevent inserting 2 equal nodes to the table
			}
			else {
				n-=1;
			}
		}
		double [][]table=new double [n][n]; 
		table=this.drawTable(nodeList);//distance table for only necessary targets
		String []ans=new String [2]; //answer is split to distance and list of keys string
		double min= Integer.MAX_VALUE;
		for (int i = 0; i < n; i++) {
			String [] curAns=this.TspSub(i, table, nodeList); //starts each time with a different vertex
			if(Double.parseDouble(curAns[0])<min) {
				ans=curAns; //save the smallest path value
				min=Double.parseDouble(curAns[0]); // changes the minimum path distance
			}
		}
		if(ans[0]==null ||ans[1]==null) //if no path is legal (infinity distance)
			return null;
		if(Double.parseDouble(ans[0])>=Integer.MAX_VALUE) //if path is infinity
			return null;
		else
			return this.string2listExtendad(ans[1]);
	}

	/**
	 * define a distance table to all nodes in the nodeList
	 * @param nodeList list of nodes to insert the table
	 * @return the table
	 */
	private double [] [] drawTable (List<node_data> nodeList) {
		int i=0;
		int j=0;
		int n= nodeList.size();
		node_data [] nodesByOrder= new node_data[n];
		double [][] table = new double[n][n];
		for (Iterator<node_data> iterator =nodeList.iterator(); iterator.hasNext();) {
			node_data out = (node_data) iterator.next();
			nodesByOrder[i]=out;
			j=0;
			for (Iterator<node_data> it = nodeList.iterator(); it.hasNext();) {
				node_data in = (node_data) it.next();
				this.shortPathGraph(out.getKey());
				table[i][j]=this.myGraph.getNode(in.getKey()).getWeight();
				j++;
			}
			i++;
		}
		//prints the table if necessary
		//		for (int a = 0; a < n; a++) {
		//			for (int b = 0; b < n; b++) {
		//				System.out.print(table[a][b] + "\t \t");
		//			}
		//			System.out.println();
		//		}
		return table;
	}

	/**
	 * returns string array
	 * ans[0]= minimum distance that goes from all the vertices starting with src
	 * ans [1]= a String of the nodes by passing order
	 * @param src from what line to start the search
	 * @param table
	 * @param nodesByOrder
	 * @return
	 */
	private String[] TspSub(int  src, double [][] table, List<node_data> nodesByOrder ) {
		String ans=""+src;
		String [] toreturn=new String[2];
		int n=table.length;
		double sum=0;
		String nodeskey=""+nodesByOrder.get(src).getKey();
		boolean [] nodes= new boolean [n];
		for (int i = 0; i < nodes.length; i++) { //reset array to false- all nodes are unvisited
			nodes[i]=false;
		}
		nodes[src]=true;
		int current=src;
		for (int i = 0; i < n-1; i++) {
			double min=Integer.MAX_VALUE;
			int j;
			int minplace=-1;
			for ( j = 0; j < n ; j++) { // after iteration - table [current][minplace] is selected
				if(table[current][j]<=min && nodes[j]==false ) {
					min=table[current][j];
					minplace=j;
				}
			}
			if(min==Integer.MAX_VALUE) { //no finite distance is found
				toreturn[0]=""+Integer.MAX_VALUE;
				toreturn[1]= nodeskey;
				return toreturn;
			}
			sum=sum+table[current][minplace];
			ans=ans+" "+minplace;
			nodeskey=nodeskey+" "+nodesByOrder.get(minplace).getKey();
			current=minplace;	
			nodes[minplace]=true; //marks this nodes as visited
		}
		toreturn[0]=""+sum;
		toreturn[1]= nodeskey;
		return toreturn;
	}
	
}