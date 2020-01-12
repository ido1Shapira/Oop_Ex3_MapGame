package algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import dataStructure.edge_data;
import gameClient.GameManager;
import gameClient.MyGameGUI;
import myUtils.HelpMe;
import utils.Point3D;

public class MovingAlgo {


	public static void logicWalk(List<String> log) {
		ArrayList<Integer> destList= new ArrayList<Integer>();
		for (int k = 0; k < MyGameGUI.game.getRobots().size(); k++) {
			String rob_json = log.get(k);
			destList.add(HelpMe.getRobotDest(rob_json));	
		}
		for (int j = 0; j < MyGameGUI.game.getRobots().size(); j++) {
			String robot_json = log.get(j);
			int rid = HelpMe.getRobotId(robot_json);
			int src = HelpMe.getRobotSrc(robot_json);
			int dest = HelpMe.getRobotDest(robot_json);
			if(dest==-1) {
				if(MovingAlgo.iHaveFruits(src)) {
					//System.out.println("I have a fruit "+src +" ans i need to go to"+bestNeighbor(src));
					MyGameGUI.game.chooseNextEdge(rid, MovingAlgo.bestNeighbor(src));
					destList.add(bestNeighbor(src));
				}
				else
				{
					int favNode= whereToGo(src, getFruitSrc());
					if(!destList.contains(favNode)) {
						MyGameGUI.game.chooseNextEdge(rid, favNode);
						destList.add(favNode);
					}
					else {
						int secondOption=second(src, destList);
						//System.out.println("heading to "+favNode);
						MyGameGUI.game.chooseNextEdge(rid, secondOption);
						destList.add(secondOption);
					}
				}
			}
		}
	}

	public static void randomWalk(List<String> log) {
		for (int j = 0; j < MyGameGUI.game.getRobots().size(); j++) {
			String robot_json = log.get(j);
			int rid = HelpMe.getRobotId(robot_json);
			int src = HelpMe.getRobotSrc(robot_json);
			int dest = HelpMe.getRobotDest(robot_json);
			if(dest==-1) 
				MyGameGUI.game.chooseNextEdge(rid, randomNextNode(src));
		}
	}
	
	
	private static int randomNextNode(int src) {
		int ans = -1;
		Collection<edge_data> ee = MyGameGUI.algo.myGraph.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}

	private static List<Integer> nodesByValue(){
		List<Integer> nodesToCheck=getFruitSrc();
		List<Integer> nodes=new ArrayList<Integer>();
		for (Integer currNode : nodesToCheck) {
			if(!nodes.contains(currNode))
				nodes.add(currNode);
		}
		List<Integer> ans= new ArrayList<Integer>();
		int differanteNodes=nodes.size();
		for(int i=0; i<differanteNodes; i++) {
			ans.add(getMax(nodes));
			nodes.remove((Integer)getMax(nodes));	
		}
		return ans;
	}


	private static int getMax(List<Integer> list) {
		int maxNode=0;
		double maxVal=0;
		for (int i = 0; i < list.size(); i++) {
			int curr=list.get(i);
			if(maxVal< getNodeVal(curr)) {
				maxVal=getNodeVal(curr);
				maxNode=curr;
			}
		}
		return maxNode;
	}


	public static void addRobot() {
		int robotSize = HelpMe.getRobotsNum(MyGameGUI.game.toString());
		//	System.out.println("robots num="+robotSize);
		List<Integer> nodesByVal= nodesByValue();
		//	System.out.println(nodesByVal);
		for (int i = 0; i < robotSize; i++) {
			MyGameGUI.game.addRobot(nodesByVal.get(i));			
		}
	}


	private static double getNodeVal(int node) {
		double ans=0;
		for(int i=0; i<getFruitSrc().size();i++) {
			if(getFruitSrc().get(i)==node)
				ans+=HelpMe.getFruitValue(MyGameGUI.game.getFruits().get(i));
		}
		return ans;
	}


	private static boolean iHaveFruits(int src) {				
		return getFruitSrc().contains(src);
	}
	
	
	private static int whereToGo(int src, List<Integer> fruitSrc) {
		double minDist=Double.MAX_VALUE;
		int togo=-1;
		for (int i = 0; i < fruitSrc.size(); i++) {
			double currDest= MyGameGUI.algo.shortestPathDist(src, fruitSrc.get(i));
			if(currDest<minDist) {
				minDist=currDest;
				togo=fruitSrc.get(i);
			}
		}
		int firstStep;
		try {
			//System.out.println(MyGameGUI.algo.shortestPath(src, togo).size());
			firstStep=MyGameGUI.algo.shortestPath(src, togo).get(1).getKey(); //get the second node on the list going from src to the edge has a fruit
			//System.out.println("i go to "+firstStep);
			return firstStep;
		}
		catch (Exception e) {
			//			System.out.println("src "+src+" fruit are in "+fruitSrc+" i go to "+togo);
			//			System.out.println("i have "+MyGameGUI.algo.shortestPath(src, togo).size()+" nodes on my way");
			//			System.out.println("which are: ");
			for (int i = 0; i < MyGameGUI.algo.shortestPath(src, togo).size(); i++) {
				//				System.out.print(MyGameGUI.algo.shortestPath(src, togo).get(i).getKey()+" ");
			}
			//			System.out.println("\nERRORRRRRR");
			//			System.out.println("after all I go to "+MyGameGUI.algo.shortestPath(src, togo).get(1).getKey());
			return MyGameGUI.algo.shortestPath(src, togo).get(1).getKey();
		}
	}


	private static double isOnEdge(String jfruit,  edge_data e ) {
		Point3D src=MyGameGUI.algo.myGraph.getNode(e.getSrc()).getLocation();
		Point3D dest=MyGameGUI.algo.myGraph.getNode(e.getDest()).getLocation();
		Point3D fruit=HelpMe.getFruitPosition(jfruit);
		double type=HelpMe.getFruitType(jfruit);
		if ( Math.abs(fruit.distance2D(src) + fruit.distance2D(dest)-src.distance2D(dest))<0.000001) { //fruit is on the edge
			if((e.getSrc()>e.getDest() && type==-1)||(e.getSrc()<e.getDest() && type==1)) //type of fruit matches edge
				return HelpMe.getFruitValue(jfruit);
		}
		return 0;
	}


	private static int second(int src, List<Integer> list) {
		Collection<edge_data> edges= MyGameGUI.algo.myGraph.getE(src);
		for (Iterator<edge_data> it = edges.iterator(); it.hasNext();) {
			edge_data e = (edge_data) it.next();
			if(!list.contains(e.getDest()))
				return e.getDest();
		}
		return whereToGo(src, getFruitSrc());
	}


	private static List<Integer> getFruitSrc(){
		Collection<String> fruits=MyGameGUI.game.getFruits();
		ArrayList<Integer> list= new ArrayList<Integer>();
		for (String f : fruits) {
			list.add(findFruitSrc(f));
		}
		return list;
	}


	private static int findFruitSrc(String jfruit) {
		int ans=-1;
		for (int i = 0; i < MyGameGUI.algo.myGraph.nodeSize(); i++) {
			Collection<edge_data> e =MyGameGUI.algo.myGraph.getE(i);
			for (Iterator<edge_data> iterator = e.iterator(); iterator.hasNext();) {
				edge_data currE = (edge_data) iterator.next();
				if(isOnEdge(jfruit, currE)!=0)
					return i;
			}
		}
		return ans;
	}


	private static int bestNeighbor(int src) {
		double maxEvalue=0;
		int maxEidDest=-1;
		Collection<edge_data> ee = MyGameGUI.algo.myGraph.getE(src);
		for (Iterator<edge_data> iterator = ee.iterator(); iterator.hasNext();) {
			edge_data edge = (edge_data) iterator.next();
			double edgeVal=0;
			Collection<String> fruits=MyGameGUI.game.getFruits();
			for (String f : fruits) {
				edgeVal+=isOnEdge(f, edge);
			}			
			if(maxEvalue<edgeVal) {
				maxEvalue=edgeVal;
				maxEidDest=edge.getDest();
			}
		}
		return maxEidDest;
	}
}