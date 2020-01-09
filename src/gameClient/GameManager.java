package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import dataStructure.edge_data;
import utils.Point3D;

public class GameManager implements Runnable {

	public GameManager() {
		Thread Master = new Thread(this);
		Master.start();
	} 

	private static List<Integer>  getFruitSrc(){
		Collection<String> fruits=MyGameGUI.game.getFruits();
		ArrayList<Integer> list= new ArrayList<Integer>();
		for (String f : fruits) {
			JSONObject line;
			try {
				line = new JSONObject(f);
				JSONObject ttt = line.getJSONObject("Fruit");
				double val = ttt.getDouble("value");
				int type = ttt.getInt("type");
				String loc = (String) ttt.get("pos");
				Point3D floc= new Point3D(loc);
				list.add(findFruitSrc(floc,val, type));
			} 
			catch (JSONException e) {e.printStackTrace();}
		}
		return list;
	}


	private static int findFruitSrc(Point3D floc,double val, int type) {
		int ans=-1;
		for (int i = 0; i < MyGameGUI.algo.myGraph.nodeSize(); i++) {
			Collection<edge_data> e =MyGameGUI.algo.myGraph.getE(i);
			for (Iterator<edge_data> iterator = e.iterator(); iterator.hasNext();) {
				edge_data currE = (edge_data) iterator.next();
				if(isOnEdge(floc, val, type, currE)!=0)
					return i;
			}
		}
		if (ans==-1)
			System.out.println("loc="+floc+" type="+type+" is not on an edge:(");
		return ans;
	}

	//		private static double nearestFruit (int src) {
	//			int dest=nextDest(src);
	//			if(dest !=-1) {
	//				System.out.println("dest is"+ dest);
	//				return MyGameGUI.algo.myGraph.getEdge(src,dest).getWeight();
	//			}
	//			else {
	//				double min=Double.POSITIVE_INFINITY;
	//				Collection <edge_data> edges=MyGameGUI.algo.myGraph.getE(src);
	//				for (Iterator<edge_data> iterator = edges.iterator(); iterator.hasNext();) {
	//					edge_data edge = (edge_data) iterator.next();
	//					double curr= nearestFruit(edge.getDest())+edge.getWeight();
	//					if(curr<min)
	//						min=curr;				
	//				}
	//				return min;
	//			}
	//			//return 10000;
	//		}
	private static int nextDest(int src) {
		int ans=-1;
		double maxVal=0;
		Collection<edge_data> ee = MyGameGUI.algo.myGraph.getE(src);
		for (Iterator<edge_data> iterator = ee.iterator(); iterator.hasNext();) {
			edge_data edge = (edge_data) iterator.next();
			double edgeVal=0;
			Collection<String> fruits=MyGameGUI.game.getFruits();
			for (String f : fruits) {
				JSONObject line;
				try {
					line = new JSONObject(f);
					JSONObject ttt = line.getJSONObject("Fruit");
					double val = ttt.getDouble("value");
					int type = ttt.getInt("type");
					String loc = (String) ttt.get("pos");
					Point3D floc= new Point3D(loc);
					edgeVal=+isOnEdge(floc, val, type, edge);
				}
				catch (Exception e) {System.out.println(e.toString());}
			}
			if(edgeVal>maxVal) {
				maxVal=edgeVal;
				ans=edge.getDest();
			}
		}
		return ans;
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

	@Override
	public void run() {
		while(MyGameGUI.game.isRunning()) {
			List<String> log = MyGameGUI.game.move();
			if(!MyGameGUI.isManual) {
				if(log!=null) {
					for (int j = 0; j < MyGameGUI.game.getRobots().size(); j++) {
						String robot_json = log.get(j);
						JSONObject line;
						try {
							line = new JSONObject(robot_json);
							JSONObject ttt = line.getJSONObject("Robot");
							int rid = ttt.getInt("id");
							int src = ttt.getInt("src");
							int dest = ttt.getInt("dest");
							if(dest==-1) {	
								if(nextDest(src)!=-1) {
									System.out.println("not randomm!!!!");
									MyGameGUI.game.chooseNextEdge(rid, nextDest(src));
								}
								else
								{
									System.out.println("fruits are in: "+getFruitSrc());
									int favNode= whereToGo(src, getFruitSrc());
									System.out.println("heading to "+favNode);
//									int firstStep= MyGameGUI.algo.shortestPath(src, favNode).get(1).getKey();
//									if(src==firstStep) {
//										System.out.println("ERROR!!!!!!");
									//	dest = randomNextNode(src);
									//	MyGameGUI.game.chooseNextEdge(rid, dest);
//									}
//									else {
//										if(MyGameGUI.algo.myGraph.getEdge(src, firstStep)==null) {
//											System.out.println("ERROR222222");
//											dest = randomNextNode(src);
//											MyGameGUI.game.chooseNextEdge(rid, dest);
//										}
//										else
											MyGameGUI.game.chooseNextEdge(rid, favNode);
//									}
								}
							}

						} catch (JSONException e) {e.printStackTrace();}
					}
				}
			}
		}
	}
	private int whereToGo(int src, List<Integer> fruitSrc) {
		double minDist=Double.MAX_VALUE;
		int togo=-1;
		for (int i = 0; i < fruitSrc.size(); i++) {
			double currDest= MyGameGUI.algo.shortestPathDist(src, fruitSrc.get(i));
			if(currDest<minDist) {
				minDist=currDest;
				togo=fruitSrc.get(i);
			}
		}
		//return togo;
				int firstStep;
				System.out.println("size of moves ="+MyGameGUI.algo.shortestPath(src, togo).size());
				if(MyGameGUI.algo.shortestPath(src, togo).size()>1)
					firstStep=MyGameGUI.algo.shortestPath(src, togo).get(1).getKey(); //get the second node on the list going from src to the edge has a fruit
				else
					firstStep=MyGameGUI.algo.shortestPath(src, togo).get(0).getKey();
				return firstStep;
	}

	private static double isOnEdge(Point3D fruit, double value, int type,  edge_data e ) {
		Point3D src=MyGameGUI.algo.myGraph.getNode(e.getSrc()).getLocation();
		Point3D dest=MyGameGUI.algo.myGraph.getNode(e.getDest()).getLocation();
		if ( Math.abs(fruit.distance2D(src) + fruit.distance2D(dest)-src.distance2D(dest))<0.001) { //fruit is on the edge
			if((e.getSrc()>e.getDest() && type==-1)||(e.getSrc()<e.getDest() && type==1)) //type of fruit matches edge
				return value;
		}
		return 0;
	}
}
