package gameClient;

import java.util.List;

import algorithms.MovingAlgo;
import dataStructure.DGraph;
import myUtils.MyServer;

public class AutoManager implements Runnable {

	public static boolean isManual = false;

	private static MyServer server;
	private static MovingAlgo moveRobots;
	// static variable single_instance of server 
	private static AutoManager single_instance = null; 

	// private constructor restricted to this class itself 
	private AutoManager(boolean isManual , int scenarioNumber) {
		AutoManager.isManual = isManual;
		server = MyServer.getServer(scenarioNumber);
		Thread Master = new Thread(this);
		Master.start();
	} 

	// static method to create instance of MovingAlgo class 
	public static AutoManager getGameManager(boolean isManual , int scenarioNumber) 
	{ 
		if (single_instance == null) {
			synchronized (AutoManager.class) {
				if (single_instance == null)
					single_instance = new AutoManager(isManual ,scenarioNumber); 
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
			double speed;
			while(server.game.isRunning()) {
				i++;
				List<String> log = server.game.move();
				long start = System.currentTimeMillis();
				speed = moveRobots.logicWalk(log);
				long end = System.currentTimeMillis();
			//	System.out.println(-start+end);
				try {
					if(speed==1)
						Thread.sleep(250);//(int)(100/speed));
					if(speed==2)
						Thread.sleep(210);
					if(speed==3)
						Thread.sleep(200);
					if(speed==4)
						Thread.sleep(200);
					if(speed==5)
						Thread.sleep(150);
					if(speed> 5)
						System.out.println(speed);
					if(speed==-1)
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