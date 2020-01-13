package gameClient;

import java.util.List;

import algorithms.MovingAlgo;

public class GameManager implements Runnable {

	public static boolean isManual = false;
	
	public GameManager() {
		Thread Master = new Thread(this);
		Master.start();
	} 


	@Override
	public void run() {
		if(!isManual) {
			while(MovingAlgo.game.isRunning()) {
				List<String> log = MovingAlgo.game.move();
				MovingAlgo.logicWalk(log);
			}
		}
	}


}