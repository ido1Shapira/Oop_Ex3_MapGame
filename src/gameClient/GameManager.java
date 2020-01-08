package gameClient;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.edge_data;
import utils.Point3D;

public class GameManager implements Runnable {
	
	public GameManager() {
		Thread Master = new Thread(this);
		Master.start();
	} 

	private static double nearestFruit (int src) {
		int dest=nextDest(src);
		if(dest !=-1)
			return MyGameGUI.algo.myGraph.getEdge(src,dest).getWeight();
		else {
			double min=Double.POSITIVE_INFINITY;
			Collection <edge_data> edges=MyGameGUI.algo.myGraph.getE(src);
			for (Iterator<edge_data> iterator = edges.iterator(); iterator.hasNext();) {
				edge_data edge = (edge_data) iterator.next();
				double curr= nearestFruit(edge.getDest())+edge.getWeight();
				if(curr<min)
					min=curr;				
			}
			return min;
		}
	}
	private static int nextDest(int src) {
		Collection<edge_data> ee = MyGameGUI.algo.myGraph.getE(src);
		for (Iterator<edge_data> iterator = ee.iterator(); iterator.hasNext();) {
			edge_data edge = (edge_data) iterator.next();
			Collection<String> fruits=MyGameGUI.game.getFruits();
			for (String f : fruits) {
				JSONObject line;
				try {
					line = new JSONObject(f);
					JSONObject ttt = line.getJSONObject("Fruit");
					double val = ttt.getDouble("value");
					int type = ttt.getInt("type");
					String loc = (String) ttt.get("pos");
					String[] s= loc.split(",", 3);
					double x=Double.parseDouble(s[0]);
					double y=Double.parseDouble(s[1]);
					if((edge.getSrc()>edge.getDest() && type==-1)||(edge.getSrc()<edge.getDest() && type==1)) {
						double sepY;
						Point3D sl= MyGameGUI.algo.myGraph.getNode(src).getLocation();
						Point3D dl= MyGameGUI.algo.myGraph.getNode(edge.getDest()).getLocation();
						sepY= ((sl.y()-dl.y())/(sl.x()-dl.y())  *(x-sl.x())+sl.y());
						if(Math.abs(sepY-y)<0.000001)
							return edge.getDest();
					}

				}
				catch (Exception e) {
				}
			}
		}
		return -1;
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
			MyGameGUI.paint(MyGameGUI.game.getRobots(), MyGameGUI.game.getFruits());
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
							else {
								dest = randomNextNode(src);
								MyGameGUI.game.chooseNextEdge(rid, dest);
								//						System.out.println("Turn to node: "+dest);
								//						System.out.println(ttt);
							}
						}

					} catch (JSONException e) {e.printStackTrace();}
				}
			}
		}
	}
}

