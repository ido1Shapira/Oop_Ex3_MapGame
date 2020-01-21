package algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import dataStructure.edge_data;
import dataStructure.graph;
import myUtils.MyParser;
import myUtils.MyServer;
import myUtils.myFruit;
import myUtils.myRobot;
import utils.Point3D;
/**
 * this Class is  responsible of all the moving algorithms needed for the game
 * main 2 algorithms are randomWalk and logicNewWalk
 * @author Ido Shapira & Edut Cohen
 *
 */
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
	 *  this method's goal is to move the robots in a random way.
	 * @param log the current state of robots and fruits given from the server
	 */
	public void randomWalk(List<String> log) {
		for (int j = 0; j < server.game.getRobots().size(); j++) {
			myRobot r= new myRobot(log.get(j));
			int rid = r.getID();
			int src = r.getSrcNode();
			int dest = r.getDest();
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
	public List<Integer> nodesByValue(List<Integer> nodes){
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
		List<Integer> nodesByVal= nodesByValue(this.getFruitSrc());
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
			myFruit fr= new myFruit(f);
			if(findFruitSrc(f)==node)
				ans+=fr.getValue();
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
	 * this method return the nearest node from fruitSrc list to src node. in the method near means the nodes keys difference is the smallest
	 * @param src
	 * @param fruitSrc
	 * @return
	 */
	private int myNearestFruit(int src, List<Integer> fruitSrc) {
		int minDist=Integer.MAX_VALUE;
		int togo=-1;
		for (int i = 0; i < fruitSrc.size(); i++) {
			int current= fruitSrc.get(i);
			int currDist= Math.abs(src- current) ;
			if(currDist<minDist) {
				minDist=currDist;
				togo=current;
			}
		}
		return togo;
	}


	/**
	 * this method calculate and return the first step toward the nearest fruit to src node
	 * @param src
	 * @param fruitSrc
	 * @return the first step toward the nearest fruit to src node
	 */
	public int whereToGo(int src, List<Integer> fruitSrc) {
		double minDist=Double.MAX_VALUE;
		int togo=-1;
		for (int i = 0; i < fruitSrc.size(); i++) {
			int current= fruitSrc.get(i);
			double currDist= MovingAlgo.algo.shortestPathDist(src, current);
			if(currDist<minDist) {
				minDist=currDist;
				togo=current;
				//System.out.println("src is = "+src+" togo= "+togo);
			}
		}
		try {
			return MovingAlgo.algo.shortestPath(src, togo).get(1).getKey(); //get the second node on the list going from src to the edge has a fruit
		}
		catch (Exception e) {
			return MovingAlgo.algo.shortestPath(src, togo).get(1).getKey();
		}
	}
/**
 * find the most worthwhile fruit calculated by (fruit value/fruit distance from src) and return the first step toward it
 * @param src
 * @param fruits
 * @return
 */
	private int whereToGoByVal(int src, List<String> fruits) {
		double bestR=-1;
		int togo=31;
		for (String jfruit : fruits) {
			int fSrc= this.findFruitSrc(jfruit);
			double currR=  this.getNodeVal(fSrc)/ algo.shortestPathDist(src, fSrc);
			if(currR>bestR && this.getNodeVal(fSrc)>0) {
				bestR=currR;
				togo= fSrc;
			}
		}
		if(bestR==-1) {
			if(src!=0) {
				return MovingAlgo.algo.shortestPath(src, src-1).get(1).getKey();
			}
			else
				return MovingAlgo.algo.shortestPath(src, src+1).get(1).getKey();
		}
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
		myFruit f= new myFruit(jfruit);
		Point3D fruit=f.getLocation();
		double type=f.getType();
		if ( Math.abs(fruit.distance2D(src) + fruit.distance2D(dest)-src.distance2D(dest))<0.000001) { //fruit is on the edge
			if((e.getSrc()>e.getDest() && type==-1)||(e.getSrc()<e.getDest() && type==1)) //type of fruit matches edge
				return f.getValue();
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

	/**
	 * adding algorithm for level 23 (more info can be given in logicWalkFor23)
	 */
	public void addRobotFor23() {
		server.game.addRobot(0);	
		server.game.addRobot(39);		
		server.game.addRobot(17);			
	}

	/**
	 * this method was build in order to pass level 23. After trying many times with our main "logicWalk" 
	 * method we found out that our greedy algorithm cannot achieve the needed results to pass lever 23 so we built a new algorithm 
	 * Specialize in level 23.
	 * the main thinking is to split the graph to 3 sub-graphs and give each robot one of the sub-graphs
	 * every robot is in charge of eating the fruits that are in its subgraph. if there is no fruit in its
	 * sub graph the robot waits till one is created
	 * @param log game log from the server
	 * @return the recommended sleeping time for the thread
	 */
	public double logicWalkFor23(List<String> log) {
		ArrayList<Integer> listFor0= new ArrayList<Integer>();
		for (int i = 0; i < 15; i++) { //nodes for robot 0
			listFor0.add(i);
		}
		ArrayList<Integer> listFor1= new ArrayList<Integer>();
		for (int i = 39; i < 48; i++) { //nodes for robot 1
			listFor1.add(i);
		}
		listFor1.add(14); listFor1.add(15);listFor1.add(27);listFor1.add(28);listFor1.add(29);
		ArrayList<Integer> listFor2= new ArrayList<Integer>();
		for (int i = 15; i < 40; i++) { //nodes for robot 2
			listFor2.add(i);
		}
		double minSpeed=10;
		boolean allGood= true;
		ArrayList<Integer> fruits =(ArrayList<Integer>) getFruitSrc();
		for (int k = 0; k < server.game.getRobots().size(); k++) {
			String robot_json = log.get(k);
			myRobot r= new myRobot(robot_json);
			double currSpeed= r.getSpeed();;
			if(currSpeed<minSpeed)
				minSpeed=currSpeed;
			int dest = r.getDest();
			if(dest==-1) {
				allGood=false;
			}
			if(fruits.contains(r.getSrcNode())) {
				allGood=false;
			}
		}
		if(allGood) {	//there isn;t a robot on a fruity edge or a robots that needs redirections
			double minTime=Double.MAX_VALUE;
			for (int i = 0; i < server.game.getRobots().size(); i++) {
				myRobot r= new myRobot(log.get(i));
				Point3D rob=r.getLocation();
				Point3D src= algo.myGraph.getNode(r.getSrcNode()).getLocation();
				Point3D dest= algo.myGraph.getNode(r.getDest()).getLocation();
				double distRobDest= rob.distance2D(dest);
				double edgeDist= src.distance2D(dest);
				double mid= distRobDest/edgeDist;
				double timeToGoEdge= algo.myGraph.getEdge(r.getSrcNode(),r.getDest()).getWeight();
				double reltiveTime=((timeToGoEdge*mid)/r.getSpeed())*1000;
				if(reltiveTime<minTime)
					minTime=reltiveTime; //minimum time the thread can sleep without missing something important
			}
			return minTime;
		}
		for (int j = 0; j < server.game.getRobots().size(); j++) {
			myRobot rob= new myRobot(log.get(j));
			int dest = rob.getDest();
			if(dest==-1) {// if the robot needs redirection
				int rid = rob.getID();
				int src = rob.getSrcNode();
				minSpeed=-1;
				if(this.iHaveFruits(src)) { //the robot stands on a node that has a fruit on one of its edges
					server.game.chooseNextEdge(rid, this.bestNeighbor(src));
				}
				else //all fruits are more then one step fur
				{
					ArrayList<Integer> list = new ArrayList<Integer>();
					for (int i = 0; i < getFruitSrc().size(); i++) {
						if(rid == 0) {
							if(listFor0.contains(getFruitSrc().get(i)))
								list.add(getFruitSrc().get(i));
						}
						if(rid == 1) {
							if(listFor1.contains(getFruitSrc().get(i)))
								list.add(getFruitSrc().get(i));
						}
						if(rid == 2) {
							if(listFor2.contains(getFruitSrc().get(i)))
								list.add(getFruitSrc().get(i));
						}
					}
					if(list.size() !=0) {
						int favNode= whereToGo(src, list); //first step toward the nearest fruit 
						server.game.chooseNextEdge(rid, favNode); 
					}
				}
			}
		}
		return minSpeed;
	}


	/**
	 * adding algorithm for level 16 (more info can be given in logicWalkFor16)
	 */
	public void addRobotFor16() {
		server.game.addRobot(16);	
		server.game.addRobot(12);	
	}

	/**
	 * this method was build in order to pass level 16. After trying many times with our main "logicNewWalk" 
	 * method we found out that our greedy algorithm cannot achieve the needed results to pass lever 16' so we built a new algorithm 
	 * Specialize in level 16.
	 * the main thinking is to split the graph to 2 sub-graphs and give each robot one of the sub-graphs
	 * every robot is in charge of eating the fruits that are in its subgraph. if there is no fruit in its
	 * sub graph it can go the other sub-graph and eat fruits from there. 
	 * @param log game log from the server
	 * @return 100 as a arbitrary value, meaning the thread can sleep 100 milliseconds
	 */
	public double logicWalkFor16(List<String> log) {
		ArrayList<Integer> listFor0= new ArrayList<Integer>();
		for (int i = 0; i < 7; i++) {   //nodes list for robot 0
			listFor0.add(i);
		}
		for (int i = 20; i < 33; i++) {
			listFor0.add(i);
		}
		ArrayList<Integer> listFor1= new ArrayList<Integer>();
		for (int i = 7; i < 20; i++) {  // //nodes list for robot 1
			listFor1.add(i);
		}
		for (int i = 33; i < 40; i++) {
			listFor1.add(i);
		}
		ArrayList<String> lfFor0= new ArrayList<String>() ; //json fruits list for robot 0
		ArrayList<String> lfFor1= new ArrayList<String>() ;  //json fruits list for robot 1
		for (String f : server.game.getFruits()) {
			int fSrc=this.findFruitSrc(f);
			if(listFor0.contains(fSrc))
				lfFor0.add(f);
			if(listFor1.contains(fSrc))
				lfFor1.add(f);	
		}
		for (int j = 0; j < server.game.getRobots().size(); j++) {
			myRobot rob= new myRobot(log.get(j));
			int dest = rob.getDest();
			if(dest==-1) {// if the robot needs redirection
				int rid = rob.getID();
				int src = rob.getSrcNode();
				List<Integer> fruits =getFruitSrc();
				//edge case in this algorithm I want to manually control
				if(src == 36 && fruits.contains(37))
					server.game.chooseNextEdge(rid, 37);
				if(src ==39)
					server.game.chooseNextEdge(rid, 37);
				if(src ==37)
					server.game.chooseNextEdge(rid, 36);
				if((src ==30 || src == 31 ) && !fruits.contains(29) && fruits.contains(7))
					server.game.chooseNextEdge(rid, src+1);
				if(src == 16 && fruits.contains(6) && fruits.contains(14))
					server.game.chooseNextEdge(rid, 17);
				if(src == 3 && fruits.contains(4))
					server.game.chooseNextEdge(rid, 4);
				if(src==2 && fruits.contains(0) && fruits.contains(29))
					server.game.chooseNextEdge(rid, 30);

				else {
					if(this.iHaveFruits(src)) { //the robot stands on a node that has a fruit on one of its edges
						server.game.chooseNextEdge(rid, this.bestNeighbor(src));
					}
					else {
						int myDest=this.myNearestFruit(src, fruits);
						if( Math.abs(src-myDest) < 3) { //the nearest fruit is less than 3 nodes fur
							server.game.chooseNextEdge(rid, algo.shortestPath(src, myDest).get(1).getKey());
						}
						else //all fruits are more then one step fur
						{
							if(rid == 0) {
								if(lfFor0.size()!=0) {
									int favNode= whereToGoByVal(src, lfFor0); //first step toward the nearest fruit 
									server.game.chooseNextEdge(rid, favNode); 
								}
								else 
									server.game.chooseNextEdge(rid, whereToGo(src, fruits)); //no fruits on its subgraph 
							}
							if(rid== 1) {
								if( lfFor1.size() !=0) {
									int favNode= whereToGoByVal(src, lfFor1); //first step toward the nearest fruit 
									server.game.chooseNextEdge(rid, favNode); 
								}
								else
									server.game.chooseNextEdge(rid, whereToGo(src, fruits));  //no fruits on its subgraph
							}
						}
					}
				}
			}
		}
		return 100;
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
	 * @return
	 */
	public double logicNewWalk(List<String> log) {
		ArrayList<Integer> destList= new ArrayList<Integer>(); //list of nodes we know that there is a robot going toward them
		boolean needRedirect= false; //at the moment there isn't a robot needs redirections
		double goToFruit=100000; //if someone is going on a fruity 
		//edge we calculate the recommended sleeping time in order to get the fruit considering the robot's speed
		boolean onWay2fruit=false; //at the moment we don't know if there is a robot on a fruity edge
		ArrayList<Integer> fruits =(ArrayList<Integer>) getFruitSrc();
		for (int k = 0; k < server.game.getRobots().size(); k++) {  //checks whether we need to change needRedirect or onWay2fruit
			String robot_json = log.get(k);
			myRobot r= new myRobot(robot_json);
			int dest = r.getDest();
			if(dest==-1) {
				needRedirect=true;
			}
			else
				destList.add(dest);
			double currSpeed= r.getSpeed();
			if(fruits.contains(r.getSrcNode())) {
				onWay2fruit=true;
				if(50/currSpeed < goToFruit)
					goToFruit=100/currSpeed;
			}
		}
		if(!needRedirect && onWay2fruit) {
			return goToFruit;
		}
		double minTime=Double.MAX_VALUE;
		if(!needRedirect && !onWay2fruit) {
			for (int i = 0; i < server.game.getRobots().size(); i++) {
				myRobot r= new myRobot(log.get(i));
				Point3D rob=r.getLocation();
				Point3D src= algo.myGraph.getNode(r.getSrcNode()).getLocation();
				Point3D dest= algo.myGraph.getNode(r.getDest()).getLocation();
				double distRobDest= rob.distance2D(dest);
				double edgeDist= src.distance2D(dest);
				double mid= distRobDest/edgeDist;
				double timeToGoEdge= algo.myGraph.getEdge(r.getSrcNode(),r.getDest()).getWeight();
				double reltiveTime=((timeToGoEdge*mid)/r.getSpeed())*1000;
				if(reltiveTime<minTime)
					minTime=reltiveTime;
			}
			return minTime; //minimum time the thread can sleep without missing something important
		}
		else {
			for (int j = 0; j < server.game.getRobots().size(); j++) {
				myRobot rob= new myRobot(log.get(j));
				int dest = rob.getDest();
				if(dest==-1) {// if the robot needs redirection
					int rid = rob.getID();
					int src = rob.getSrcNode();
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
			return 100; //Arbitrary value
		}
	}


















































	// Walking method for ex3
	//
	//	public double logicWalk(List<String> log) {
	//		ArrayList<Integer> destList= new ArrayList<Integer>(); //list of nodes we know that there is a robot going toward them
	//		double minSpeed=10;
	//		boolean allGood= true;
	//		double goToFruit=100000;
	//		boolean onWay2fruit=false;
	//		ArrayList<Integer> fruits =(ArrayList<Integer>) getFruitSrc();
	//		for (int k = 0; k < server.game.getRobots().size(); k++) {
	//			String robot_json = log.get(k);
	//			myRobot r= new myRobot(robot_json);
	//			double currSpeed= r.getSpeed();;
	//			if(currSpeed<minSpeed)
	//				minSpeed=currSpeed;
	//			int dest = r.getDest();
	//			destList.add(dest);
	//			if(dest==-1) {
	//				allGood=false;
	//			}
	//			if(fruits.contains(r.getSrcNode())) {
	//				allGood=false;
	//				onWay2fruit=true;
	//				if(50/currSpeed < goToFruit)
	//					goToFruit=100/currSpeed;
	//			}
	//		}
	//		if(allGood) {
	//			double minTime=Double.MAX_VALUE;
	//			for (int i = 0; i < server.game.getRobots().size(); i++) {
	//				myRobot r= new myRobot(log.get(i));
	//				Point3D rob=r.getLocation();
	//				Point3D src= algo.myGraph.getNode(r.getSrcNode()).getLocation();
	//				Point3D dest= algo.myGraph.getNode(r.getDest()).getLocation();
	//				double distRobDest= rob.distance2D(dest);
	//				double edgeDist= src.distance2D(dest);
	//				double mid= distRobDest/edgeDist;
	//				double timeToGoEdge= algo.myGraph.getEdge(r.getSrcNode(),r.getDest()).getWeight();
	//				double reltiveTime=((timeToGoEdge*mid)/r.getSpeed())*1000;
	//				if(reltiveTime<minTime)
	//					minTime=reltiveTime;
	//			}
	//			System.out.println("I can wait "+minTime+" milliseconds");
	//			return minTime;
	//		}
	//		for (int j = 0; j < server.game.getRobots().size(); j++) {
	//			myRobot rob= new myRobot(log.get(j));
	//			int dest = rob.getDest();
	//			if(dest==-1) {// if the robot needs redirection
	//				int rid = rob.getID();
	//				int src = rob.getSrcNode();
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
	//		if(onWay2fruit)
	//			return goToFruit;
	//		return minSpeed;
	//	}

}