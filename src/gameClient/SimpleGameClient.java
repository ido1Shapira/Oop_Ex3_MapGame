package gameClient;

/**
* This class represents a simple example for using the GameServer API:
* the main file performs the following tasks:
* 0. login as a user ("999") for testing - do use your ID.
* 1. Creates a game_service [0,23] (user "999" has stage 10, can play in scenarios [0,10] not above
* 2. Constructs the graph from JSON String
* 3. Gets the scenario JSON String 
* 5. Add a set of robots  // note: in general a list of robots should be added
* 6. Starts game 
* 7. Main loop (vary simple thread)
* 8. move the robot along the current edge 
* 9. direct to the next edge (if on a node) 
* 10. prints the game results (after "game over"), and write a KML: 
*  
* @author boaz.benmoshe
*
*/

public class SimpleGameClient {
	public static void main(String[] a) {
		MyGameGUI gui = MyGameGUI.getGui(); //starts the gui thread 
		Thread toPaint = new Thread(gui);
		toPaint.start();
	}
}