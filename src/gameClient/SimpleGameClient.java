package gameClient;

import myUtils.MyServer;

/**
 * This class represents a simple example for using the GameServer API:
 * the main file performs the following tasks:
 * 1. Creates a game_service [0,23] (line 36)
 * 2. Constructs the graph from JSON String (lines 37-39)
 * 3. Gets the scenario JSON String (lines 40-41)
 * 4. Prints the fruits data (lines 44-45)
 * 5. Add a single robot (line 48) // note: in general a list of robots should be added
 * 6. Starts game (line 49) 
 * 7. Main loop (should be a thread)
 * 8. move the robot along the current edge (line 54)
 * 9. direct to the next edge (if on a node) (line 68)
 *  
 * @author boaz.benmoshe
 *
 */

public class SimpleGameClient {
	private static MyServer server;

	public static void main(String[] a) {
		test1();
//	buildKmlFiles();
	}
	private static void buildKmlFiles() {
		final int NumberOFScenario = 23;
		for (int i = 0; i < NumberOFScenario; i++) {
			runScenario run = new runScenario(i);
			Thread t1 = new Thread(run);
			t1.start();
			try {t1.join();
			} catch (InterruptedException e) {e.printStackTrace();}
			System.out.println("finish playing scenario "+i);
		}
		System.out.println("finish playing all scenarios");
	}
	public static void test1() {
		MyGameGUI.buildScenario(); // you have [0,23] games
		server = MyServer.getServer();
		server.game.startGame();
	}
}