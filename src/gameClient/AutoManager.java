package gameClient;

import java.util.List;

import algorithms.MovingAlgo;
import dataStructure.DGraph;
import myUtils.MyServer;

public class AutoManager implements Runnable {

	private static MyServer server;
	private static MovingAlgo moveRobots;

	public AutoManager(int scenarioNumber) {
		server = MyServer.getServer(scenarioNumber);
		server.game.startGame();
		
	}

	@Override
	public void run() {
		int i=0;
		String gJason = server.game.getGraph();
		DGraph g = new DGraph();
		g.init(gJason); //case that we aren't using gui
		moveRobots = MovingAlgo.getMovingAlgo(g);
		moveRobots.addRobot();
		server.game.startGame();
		MyGameGUI.autoManagerIsReady = true;
//		double speed;
		System.out.println("start playing...");
		while(server.game.isRunning()) {
			//				System.out.println("The game is running");
			i++;
			List<String> log = server.game.move();
//			speed = moveRobots.logicWalk(log);
			moveRobots.logicWalk(log);
			//	System.out.println(-start+end);
//			try {
//				if(speed==1)
//					Thread.sleep(250);//(int)(100/speed));
//				if(speed==2)
//					Thread.sleep(210);
//				if(speed==3)
//					Thread.sleep(200);
//				if(speed==4)
//					Thread.sleep(200);
//				if(speed==5)
//					Thread.sleep(150);
//				if(speed> 5)
//					System.out.println(speed);
//				if(speed==-1)
//					Thread.sleep(100);
//
//			} catch (InterruptedException e) {
//				System.out.println("the error is here");
//			}
		}
		System.out.println("finish playing... moves = "+i);
	}
}