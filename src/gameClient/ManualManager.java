package gameClient;

import java.util.List;

import myUtils.MyServer;

/**
 * This class represents the manual mode
 * @author ido shapira & edut cohen
 */
public class ManualManager implements Runnable {

	private static int prev_key;
	private static int prev_idRobot;

	private int key;
	private int idRobot;

	// static variable single_instance of server 
	private static ManualManager single_instance = null; 

	private static MyServer server;


	/** private constructor restricted to this class itself
	 * @param key
	 * @param idRobot
	 */
	private ManualManager(int key, int idRobot) {
		this.key = key;
		this.idRobot = idRobot;
		server = MyServer.getServer();
	}
	
	/**
	 * static method to create instance of myServer class  
	 * @param key
	 * @param idRobot
	 */
	public static ManualManager getManualManager(int key, int idRobot) 
	{ 
		if (single_instance == null) {
			synchronized (Logger_KML.class) {
				if (single_instance == null)
					single_instance = new ManualManager(key,idRobot); 
			}
		}
		return single_instance;
	}
	/**
	 * update key to the given parameter
	 * @param key
	 */
	public synchronized void setKey(int key) {
		this.key = key;
	}
	/**
	 * update idRobot to the given parameter
	 * @param idRobot
	 */
	public synchronized void setIdRobot(int idRobot) {
		this.idRobot = idRobot;
	}
	/**
	 * moves the robot we now working at to the node we last clicked on
	 */
	@Override
	public void run() {
		while(server.game.isRunning()) {
			List<String> log = server.game.move();
			if(key != -1 && idRobot != -1) { //one click was made
				//				if(prev_key != key || prev_idRobot != idRobot) {
				//					MyGameGUI.game.chooseNextEdge(idRobot,key);
				//					System.out.println("robot "+ idRobot+ " moved to: " +key +"\n" +log);
				//					prev_key = key;
				//					prev_idRobot = idRobot;
				//				}
				if(prev_key != key || prev_idRobot != idRobot) {  //change the current robot or the current node key
					server.game.chooseNextEdge(idRobot,key); //moves the current robot to the current node
					System.out.println("robot "+ idRobot+ " moved to: " +key +"\n" +log);
					if(prev_key != key) {  //update prev_key to be the current node
						prev_key = key;}
					if(prev_idRobot != idRobot) { //update prev_idRobot to be the  current robot
						prev_idRobot = idRobot;}
				}
			}
		}
	}
}