package myUtils;

import gameClient.MyGameGUI;

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
		while(MyGameGUI.game.isRunning()) {
			System.out.println(key+ " "+ idRobot);
			System.out.println(prev_key+ " "+ prev_idRobot);
			if(prev_key != key || prev_idRobot != idRobot) {

				System.out.println(key + " "+ idRobot);

				prev_key = key;
				prev_idRobot = idRobot;
			}
		}

	}


}
