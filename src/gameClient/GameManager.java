package gameClient;

import java.util.List;

import algorithms.MovingAlgo;
import dataStructure.DGraph;
import myUtils.myServer;

public class GameManager implements Runnable {

	public static boolean isManual = false;

	private static myServer server;
	private static MovingAlgo moveRobots;
	// static variable single_instance of server 
	private static GameManager single_instance = null; 

	// private constructor restricted to this class itself 
	private GameManager(boolean isManual , int scenarioNumber) {
		GameManager.isManual = isManual;
		server = myServer.getServer(scenarioNumber);
		Thread Master = new Thread(this);
		Master.start();
	} 

	// static method to create instance of MovingAlgo class 
	public static GameManager getGameManager(boolean isManual , int scenarioNumber) 
	{ 
		if (single_instance == null) {
			synchronized (GameManager.class) {
				if (single_instance == null)
					single_instance = new GameManager(isManual ,scenarioNumber); 
			}
		}
		return single_instance;
	}

	@Override
	public void run() {
		int i=0;
		if(!isManual) {
			String gJason = server.game.getGraph();
			DGraph g = DGraph.getDGraph();
			g.init(gJason);
			moveRobots = MovingAlgo.getMovingAlgo(g);
			moveRobots.addRobot();
			server.game.startGame();
			while(server.game.isRunning()) {

				i++;
				List<String> log = server.game.move();
				moveRobots.logicWalk(log);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.out.println("the error is here");
				}
			}
			System.out.println("moves = "+i);
		}
		else {

		}
	}


}