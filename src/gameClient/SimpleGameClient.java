package gameClient;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import dataStructure.edge_data;
import dataStructure.node_data;
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
		String info = MyGameGUI.game.toString();
		System.out.println(info);
		// the list of fruits should be considered in your solution
		Iterator<String> f_iter = MyGameGUI.game.getFruits().iterator();
		while(f_iter.hasNext()) {System.out.println(f_iter.next());}				
		MyGameGUI.game.startGame();
		int i=0;
		System.out.println("Start game:");
		while(MyGameGUI.game.isRunning()) {
			long t = MyGameGUI.game.timeToEnd();
			//System.out.println("roung: "+i+"  seconds to end:"+(t/1000));
			List<String> log = MyGameGUI.game.move();
			MyGameGUI.paint(MyGameGUI.game.getRobots(), MyGameGUI.game.getFruits());

			if(log!=null) {
				String robot_json = log.get(0);
				JSONObject line;
				try {
					line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");
					
					if(dest==-1) {	
						dest = nextNode(src);
						MyGameGUI.game.chooseNextEdge(rid, dest);
						System.out.println("Turn to node: "+dest);
						System.out.println(ttt);
					}
					
				} catch (JSONException e) {e.printStackTrace();}
	
				
				}
			i++;
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

}
