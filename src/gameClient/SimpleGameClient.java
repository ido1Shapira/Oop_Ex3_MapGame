package gameClient;

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
		MyGameGUI.game.startGame();
		GameManager man = new GameManager();
//		nearestFruit(0);
	}


	

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