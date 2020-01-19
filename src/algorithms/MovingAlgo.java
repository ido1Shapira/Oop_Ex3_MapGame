package algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import myUtils.MyParser;
import myUtils.MyServer;
import utils.Point3D;

public class MovingAlgo {

	private static MyServer server = MyServer.getServer();

	// static variable single_instance of server 
	private static MovingAlgo single_instance = null; 

	public static Graph_Algo algo;
	// private constructor restricted to this class itself 
	private MovingAlgo(graph g) 
	{ 
		algo = new Graph_Algo(g);
	} 

	// static method to create instance of MovingAlgo class 
	public static MovingAlgo getMovingAlgo(graph g) 
	{ 
		if (single_instance == null) {
			synchronized (MovingAlgo.class) {
				if (single_instance == null)
					single_instance = new MovingAlgo(g); 
			}
		}
		return single_instance;
	}
	/**
	 * this method's goal is to move the robots in a logical way.
	 * logical way means that after the game ends the robots' value would be the biggest under the given circumstances.
	 * of course that our algorithm is not optimal, but it is yet a good algorithm.
	 * the main thinking of moving the robots is a greedy algorithm 
	 * that moves every robot one step toward the nearest fruit from where he stands at the moment
	 * Our way of preventing robots from uniting is to make sure that every robot has a different destination node.
	 * 
	 * @param log the current state of robots and fruits given from the server
	 */
	public double logicWalk(List<String> log)  {
		ArrayList<Integer> destList= new ArrayList<Integer>(); //list of nodes we know that there is a robot going toward them
		for (int j = 0; j < server.game.getRobots().size(); j++) {
			String robot_json = log.get(j);
			int dest = MyParser.getRobotDest(robot_json);
			destList.add(dest);
		}
		double minSpeed=10;
		for (int j = 0; j < server.game.getRobots().size(); j++) {
			String robot_json = log.get(j);
			int dest = MyParser.getRobotDest(robot_json);
			double currspeed=MyParser.getRobotSpeed(robot_json);
			if(currspeed<minSpeed )
				minSpeed= currspeed;
			if(dest==-1) {// if the robot needs redirection
				int rid = MyParser.getRobotId(robot_json);
				int src = MyParser.getRobotSrc(robot_json);
				minSpeed=-1;
				if(this.iHaveFruits(src)) { //the robot stands on a node that has a fruit on one of its edges
					server.game.chooseNextEdge(rid, this.bestNeighbor(src));
					destList.add(bestNeighbor(src)); //adding the new dest to the list
				}
				else //all fruits are more then one step fur
				{
					int favNode= whereToGo(src, getFruitSrc());//, table); //first step toward the nearest fruit 
					if(!destList.contains(favNode)) { //there isn't a robot going there
						server.game.chooseNextEdge(rid, favNode); 
						destList.add(favNode); //adding the new dest to the list
					}
					else { //there isn't a robot going there already
						int secondOption=second(src,favNode, destList);  //go the other way
						server.game.chooseNextEdge(rid, secondOption);
						destList.add(secondOption); ////adding the new dest to the list
					}
				}
			}
		}

		return minSpeed;
	}
	
	//please ignore!!
//	public double logicWalk4(List<String> log) {//, double [][] table) {
//		ArrayList<Integer> destList= new ArrayList<Integer>(); //list of nodes we know that there is a robot going toward them
//		double minSpeed=10;
//	
//		ArrayList<Integer> fruits =(ArrayList<Integer>) getFruitSrc();
//		for (int k = 0; k < server.game.getRobots().size(); k++) {
//			String robot_json = log.get(k);
////			double currSpeed=MyParser.getRobotSpeed(robot_json);
////			if(currSpeed<minSpeed)
////				minSpeed=currSpeed;
//			
//			int dest = MyParser.getRobotDest(robot_json);
//			destList.add(dest);
////			if(dest==-1) {
////				onANode=true;
////				allGood=false;
////			}
////			if(fruits.contains(MyParser.getRobotSrc(robot_json)))
////				allGood=false;
//		}
//		double minTime=Double.MAX_VALUE;
////		boolean allGood=true;
////		boolean onANode= false;
////		if(allGood && !onANode) {
////			for (int i = 0; i < server.game.getRobots().size(); i++) {
////				String robot_json = log.get(i);
////				Point3D rob= MyParser.getRobotPosition(robot_json);
////				
////				Point3D src= algo.myGraph.getNode(MyParser.getRobotSrc(robot_json)).getLocation();
////				Point3D dest= algo.myGraph.getNode(MyParser.getRobotDest(robot_json)).getLocation();
////				double distRobDest= rob.distance2D(dest);
////				double edgeDist= src.distance2D(dest);
////				double mid= distRobDest/edgeDist;
////				double timeToGoEdge= algo.myGraph.getEdge(MyParser.getRobotSrc(robot_json),MyParser.getRobotDest(robot_json)).getWeight();
////				double reltiveTime=1000*((timeToGoEdge*mid)/MyParser.getRobotSpeed(robot_json));
////				if(reltiveTime<minTime)
////					minTime=reltiveTime;
////			}
//////			System.out.println(minTime);
////			return minTime;
////		}
//		for (int j = 0; j < server.game.getRobots().size(); j++) {
//			String robot_json = log.get(j);
//			int dest = MyParser.getRobotDest(robot_json);
//			double currspeed=MyParser.getRobotSpeed(robot_json);
//			if(currspeed<minSpeed )
//				minSpeed= currspeed;
//			if(dest==-1) {// if the robot needs redirection
//				int rid = MyParser.getRobotId(robot_json);
//				int src = MyParser.getRobotSrc(robot_json);
//				minSpeed=-1;
//				if(this.iHaveFruits(src)) { //the robot stands on a node that has a fruit on one of its edges
//					server.game.chooseNextEdge(rid, this.bestNeighbor(src));
//					destList.add(bestNeighbor(src)); //adding the new dest to the list
//				}
//				else //all fruits are more then one step fur
//				{
//					int favNode= whereToGo(src, getFruitSrc());//, table); //first step toward the nearest fruit 
//					if(!destList.contains(favNode)) { //there isn't a robot going there
//						server.game.chooseNextEdge(rid, favNode); 
//						destList.add(favNode); //adding the new dest to the list
//					}
//					else { //there isn't a robot going there already
//						int secondOption=second(src,favNode, destList);  //go the other way
//						server.game.chooseNextEdge(rid, secondOption);
//						destList.add(secondOption); ////adding the new dest to the list
//					}
//				}
//			}
//		}
//
//		return minSpeed;
//	}

//	public double logicWalkAlone(List<String> log) {
//		String robot_json = log.get(0);
//		int dest = MyParser.getRobotDest(robot_json);
//		if(dest==-1) {// if the robot needs redirection
//			int rid = MyParser.getRobotId(robot_json);
//			int src = MyParser.getRobotSrc(robot_json);
//			if(this.iHaveFruits(src)) { //the robot stands on a node that has a fruit on one of its edges
//				server.game.chooseNextEdge(rid, this.bestNeighbor(src));
//			}
//			else //all fruits are more then one step fur
//			{
//				int favNode= whereToGo(src, getFruitSrc());//, table); //first step toward the nearest fruit 
//				server.game.chooseNextEdge(rid, favNode); 
//			}
//		}
//		return 0;
//	}
	/**
	 *  this method's goal is to move the robots in a random way.
	 * @param log the current state of robots and fruits given from the server
	 */
	public void randomWalk(List<String> log) {
		for (int j = 0; j < server.game.getRobots().size(); j++) {
			String robot_json = log.get(j);
			int rid = MyParser.getRobotId(robot_json);
			int src = MyParser.getRobotSrc(robot_json);
			int dest = MyParser.getRobotDest(robot_json);
			if(dest==-1) 
				server.game.chooseNextEdge(rid, randomNextNode(src));
		}
	}

	/**
	 * this method chooses the robot next node randomly according to its src node
	 * @param src the node the robot is at the moment
	 * @return random neighbor of src
	 */
	private static int randomNextNode(int src) {
		int ans = -1;
		Collection<edge_data> ee = MovingAlgo.algo.myGraph.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}

	/**
	 * this method make a list of nodes arranged by value according of where the fruits are at the moment
	 * first at the list is the node with the biggest fruits value
	 * @return list of nodes arranged by value.
	 * 
	 */
	public List<Integer> nodesByValue(){
		List<Integer> nodes=getFruitSrc(); //nodes with fruit on their edges
		List<Integer> ans= new ArrayList<Integer>(); 
		int differanteNodes=nodes.size();
		for(int i=0; i<differanteNodes; i++) { //gets the most valuable node and removes it from the list
			ans.add(getMax(nodes));
			nodes.remove((Integer)getMax(nodes));	
		}
		return ans;
	}

	/**
	 * gets the most valuable node from the given list
	 * @param list the list to choose from
	 * @return the best choice from the list
	 */
	private int getMax(List<Integer> list) {
		int maxNode=0;
		double maxVal=0;
		for (int i = 0; i < list.size(); i++) {
			int curr=list.get(i);
			double currVal=getNodeVal(curr);
			if(maxVal< currVal) {
				maxVal=currVal;
				maxNode=curr;
			}
		}
		return maxNode;
	}

	/**
	 * adds all the robots needed for the current scenario to the best place given from nodesByValue list
	 */
	public void addRobot() {
		int robotSize = MyParser.getRobotsNum(server.game.toString());
		List<Integer> nodesByVal= nodesByValue();
		for (int i = 0; i < robotSize; i++) {
			server.game.addRobot(nodesByVal.get(i));			
		}
	}

	/**
	 * calculate the value of all fruits can be reached from this node
	 * @param node the current node we now check
	 * @return the node's value
	 */
	private double getNodeVal(int node) {
		double ans=0;
		for(int i=0; i<getFruitSrc().size();i++) {
			String f= server.game.getFruits().get(i);
			if(findFruitSrc(f)==node)
				ans+=MyParser.getFruitValue(f);
		}
		return ans;
	}

	/**
	 * @param src
	 * @return true if there is a fruit on one of the edges starting from src
	 */
	private boolean iHaveFruits(int src) {				
		return getFruitSrc().contains(src);
	}

	/**
	 * 
	 * @param src
	 * @param fruitSrc
	 * @return the first step toward the nearest fruit to src node
	 */
	public int whereToGo(int src, List<Integer> fruitSrc) {//, double [][] table) {
		double minDist=Double.MAX_VALUE;
		int togo=-1;
		for (int i = 0; i < fruitSrc.size(); i++) {
			int current= fruitSrc.get(i);
			double currDist= MovingAlgo.algo.shortestPathDist(src, current);//table[src][current];
			if(currDist<minDist) {
				minDist=currDist;
				togo=current;
			}
		}
		//		List<node_data> listToGo=MovingAlgo.algo.shortestPath(src, togo);
		//		for (int j = 1; j <listToGo.size(); j++) {
		//			if( destList.contains(listToGo.get(j).getKey()))
		//				return -1;
		//		}
		try {
			return MovingAlgo.algo.shortestPath(src, togo).get(1).getKey(); //get the second node on the list going from src to the edge has a fruit
		}
		catch (Exception e) {
			return MovingAlgo.algo.shortestPath(src, togo).get(1).getKey();
		}
	}

	/**
	 * checks whether the given fruit on the given edge 
	 * @param jfruit string representing a fruit
	 * @param e the edge we now check
	 * @return the value of the fruit if its on the edge, 0 otherwise
	 */
	public double isOnEdge(String jfruit,  edge_data e ) {
		Point3D src=MovingAlgo.algo.myGraph.getNode(e.getSrc()).getLocation();
		Point3D dest=MovingAlgo.algo.myGraph.getNode(e.getDest()).getLocation();
		Point3D fruit=MyParser.getFruitPosition(jfruit);
		double type=MyParser.getFruitType(jfruit);
		if ( Math.abs(fruit.distance2D(src) + fruit.distance2D(dest)-src.distance2D(dest))<0.000001) { //fruit is on the edge
			if((e.getSrc()>e.getDest() && type==-1)||(e.getSrc()<e.getDest() && type==1)) //type of fruit matches edge
				return MyParser.getFruitValue(jfruit);
		}
		return 0;
	}

	/**
	 * 
	 * @param src the node we are on
	 * @param list list of nodes we don't want to get to (there is a robot going there already)
	 * @return a neighbor of src that is not on the list if exist  
	 */
	private static int second(int src,int lastOption, List<Integer> list) {
		Collection<edge_data> edges= MovingAlgo.algo.myGraph.getE(src);
		for (Iterator<edge_data> it = edges.iterator(); it.hasNext();) {
			edge_data e = (edge_data) it.next();
			int dest=e.getDest();
			if(!list.contains(dest))
				return dest;
		}
		return lastOption;
	}

	/**
	 * 
	 * @return list of keys representing the nodes that the fruits are on (the src nodes of the edges) 
	 */
	public List<Integer> getFruitSrc(){
		Collection<String> fruits=server.game.getFruits();
		ArrayList<Integer> list= new ArrayList<Integer>();
		for (String f : fruits) {
			int src= findFruitSrc(f);
			if(!list.contains(src))
				list.add(src);
		}
		return list;
	}

	/**
	 * calculate the src node of the edge the fruit is on
	 * @param jfruit string representing the fruit
	 * @return src node key
	 */
	public int findFruitSrc(String jfruit) {
		int ans=-1;
		for (int i = 0; i < MovingAlgo.algo.myGraph.nodeSize(); i++) {
			Collection<edge_data> e =MovingAlgo.algo.myGraph.getE(i);
			for (Iterator<edge_data> iterator = e.iterator(); iterator.hasNext();) {
				edge_data currE = (edge_data) iterator.next();
				if(this.isOnEdge(jfruit, currE)!=0)
					return i;
			}
		}
		return ans;
	}

	/**
	 * considering there is one or more fruits can be directly reached from src we calculate the dest node to the most worthy edge
	 * @param src the node we are on
	 * @return dest node of best edge
	 */
	private int bestNeighbor(int src) {
		double maxEvalue=0;
		int maxEidDest=-1;
		Collection<edge_data> ee = MovingAlgo.algo.myGraph.getE(src);
		for (Iterator<edge_data> iterator = ee.iterator(); iterator.hasNext();) {
			edge_data edge = (edge_data) iterator.next();
			double edgeVal=0;
			Collection<String> fruits=server.game.getFruits();
			for (String f : fruits) {
				edgeVal+=this.isOnEdge(f, edge);
			}			
			if(maxEvalue<edgeVal) {
				maxEvalue=edgeVal;
				maxEidDest=edge.getDest();
			}
		}
		return maxEidDest;
	}
}