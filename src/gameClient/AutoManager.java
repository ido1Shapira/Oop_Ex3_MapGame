package gameClient;

import java.util.List;

import algorithms.MovingAlgo;
import dataStructure.DGraph;
import myUtils.MyServer;

public class AutoManager implements Runnable {

	private static MyServer server;
	private static MovingAlgo moveRobots;
	private static int stage;
	public AutoManager(int scenarioNumber) {
		server = MyServer.getServer(scenarioNumber);
		server.game.startGame();
		stage=scenarioNumber;

	}

	@Override
	public void run() {
		int i=0;
		String gJason = server.game.getGraph();
		DGraph g = new DGraph();
		g.init(gJason); //case that we aren't using gui
		moveRobots = MovingAlgo.getMovingAlgo(g);
		if(stage!=23 && stage!=16)
			moveRobots.addRobot();
		if(stage ==16 )
			moveRobots.addRobotFor16();
		else
			moveRobots.addRobotFor23();
		server.game.startGame();
		MyGameGUI.autoManagerIsReady = true;
		double speed;
		System.out.println("start playing...");
		System.out.println("stage is "+stage);
		while(server.game.isRunning()) {
			i++;
			List<String> log = server.game.move();
			if(stage==23) {
				speed = moveRobots.logicWalkFor23(log);
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(stage==16) {
				speed = moveRobots.logicWalkFor16(log);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(stage !=23 && stage!=16) {
				speed = moveRobots.logicNewWalk(log);
				try {
					if(stage ==11)
						Thread.sleep(90);
					else {
						if(speed!=-1 && speed>10) {
							Thread.sleep(Math.min((long) speed, 200));
						}
						else {
							if(speed ==-1)
								Thread.sleep(150);
							else
								Thread.sleep(50);
						}
					}
				}
				catch (InterruptedException e) {
					System.out.println("the error is here");
				}
			}
		}
		System.out.println("finish playing... moves = "+i);
	}
}