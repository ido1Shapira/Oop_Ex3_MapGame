package gameClient;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;


import org.json.JSONException;
import org.json.JSONObject;

import dataStructure.edge_data;
import utils.Point3D;
/**
 * This class represents a simple example for using the GameServer API:
 * the main file performs the following tasks:
 * 1. Creates a game_service [0,23] (line 36)
 * 2. Constructs the graph from JSON String (lines 37-39)
 * 3. Gets the scenario JSON String (lines 40-41)
 * 4. Prints the fruits data (lines 44-45)
 * 5. Add a single robot (line 48) // note: in genera a list of robots should be added
 * 6. Starts game (line 49)
 * 7. Main loop (should be a thread)
 * 8. move the robot along the current edge (line 54)
 * 9. direct to the next edge (if on a node) (line 68)
 *  
 * @author boaz.benmoshe
 *
 */
public class SimpleGameClient {
	public static void main(String[] a) {
		test1();
	}
	public static void test1() {
		MyGameGUI.buildScenario(); // you have [0,23] games
		//		String info = MyGameGUI.game.toString();
		//		System.out.println(info);
		//		the list of fruits should be considered in your solution
		//		Iterator<String> f_iter = MyGameGUI.game.getFruits().iterator();
		//		while(f_iter.hasNext()) {System.out.println(f_iter.next());}				
		MyGameGUI.game.startGame();
		int i=0;
		nextDest(1);
		System.out.println( MyGameGUI.game.move());
		System.out.println("Start game:");
		while(MyGameGUI.game.isRunning()) {
			List<String> log = MyGameGUI.game.move();
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
								dest = nextNode(src);
								MyGameGUI.game.chooseNextEdge(rid, dest);
								//						System.out.println("Turn to node: "+dest);
								//						System.out.println(ttt);
							}
						}

					} catch (JSONException e) {e.printStackTrace();}


				}
				i++;
			}
		}
	}
	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(int src) {
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
	private static int smartNextNode(int src) {
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

//	private double nearestFruit (int src) {
//
//	}
	//		private edge_data isOn(String f) {
	//			for (int i = 0; i < MyGameGUI.algo.myGraph.nodeSize(); i++) {
	//				Collection<edge_data> ee = MyGameGUI.algo.myGraph.getE(i);
	//				for (Iterator<edge_data> iterator = ee.iterator(); iterator.hasNext();) {
	//					edge_data edge = (edge_data) iterator.next();
	//					if(edge.getSrc()-edge.getDest()<0 && f.getType()==-1) {
	//					}
	//	
	//					}
	//				}
	//			}
	//			
	//			Iterator<edge_data> itr = ee.iterator();
	//			return null;
	//		}
}