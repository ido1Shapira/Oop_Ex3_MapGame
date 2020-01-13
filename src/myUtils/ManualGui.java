package myUtils;

import java.util.List;

import algorithms.MovingAlgo;

/**
 * This class represents the manual mode
 * @author ido shapira & edut cohen
 */
public class ManualGui implements Runnable {

	private static int prev_key;
	private static int prev_idRobot;

	private int key;
	private int idRobot;

	/**
	 * @param key
	 * @param idRobot
	 */
	public ManualGui(int key, int idRobot) {
		this.key = key;
		this.idRobot = idRobot;
	}

	public synchronized void setKey(int key) {
		this.key = key;
	}

	public synchronized void setIdRobot(int idRobot) {
		this.idRobot = idRobot;
	}

	@Override
	public void run() {
		while(MovingAlgo.game.isRunning()) {
			List<String> log = MovingAlgo.game.move();
			if(key != -1 && idRobot != -1) {
//				if(prev_key != key || prev_idRobot != idRobot) {
//					MyGameGUI.game.chooseNextEdge(idRobot,key);
//					System.out.println("robot "+ idRobot+ " moved to: " +key +"\n" +log);
//					prev_key = key;
//					prev_idRobot = idRobot;
//				}
				if(prev_key != key || prev_idRobot != idRobot) {
					MovingAlgo.game.chooseNextEdge(idRobot,key);
					System.out.println("robot "+ idRobot+ " moved to: " +key +"\n" +log);
					if(prev_key != key) {
					prev_key = key;}
					if(prev_idRobot != idRobot) {
					prev_idRobot = idRobot;}
				}
			}
		}
	}
}
