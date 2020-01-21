package gameClient;

import java.util.List;

import algorithms.MovingAlgo;
import dataStructure.DGraph;
import myUtils.MyServer;
/**
 * this class controls the automatic game manager Thread.
 * its only method is run that moves the robots in the game and sleep specific 
 * recommended time it calculate using MovingAlgo class.
 * @author Ido Shapira & Edut Cohen 
 *
 */
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
		switch (stage)
		{
		case 16:
			moveRobots.addRobotFor16();
			break;
		case 23:
			moveRobots.addRobotFor23();
			break;
		default:
			moveRobots.addRobot();
			break;
		}
		server.game.startGame();
		MyGameGUI.autoManagerIsReady = true;
		System.out.println("start playing...");
		System.out.println("you are now playing level number "+stage);
		double speed;
		while(server.game.isRunning()) {      //sleeping routine
			i++;
			List<String> log = server.game.move();
			switch(stage) {
			case 0:
				speed = moveRobots.logicNewWalk(log);			
				try { 	Thread.sleep(Math.min((long) speed, 150)); }
				 catch (InterruptedException e1) { e1.printStackTrace();}
				break;
			case 1:
				speed = moveRobots.logicNewWalk(log);			
				try { 	Thread.sleep(Math.min((long) speed, 200)); }
				 catch (InterruptedException e1) { e1.printStackTrace();}
				break;
			case 3:
				speed = moveRobots.logicNewWalk(log);			
				try { 	Thread.sleep(Math.min((long) speed, 190)); }
				 catch (InterruptedException e1) { e1.printStackTrace();}
				break;
			case 9:
				speed = moveRobots.logicNewWalk(log);			
				try { 	Thread.sleep(Math.min((long) speed, 135)); }
				 catch (InterruptedException e1) { e1.printStackTrace();}
				break;
			case 11:
				speed = moveRobots.logicNewWalk(log);			
				try { Thread.sleep(100);}
				 catch (InterruptedException e1) { e1.printStackTrace();}
				break;
			case 13:
				speed = moveRobots.logicNewWalk(log);			
				try { 	Thread.sleep(Math.min((long) speed, 125)); }
				 catch (InterruptedException e1) { e1.printStackTrace();}
				break;	
			case 16:
				speed = moveRobots.logicWalkFor16(log);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case 19:
				speed = moveRobots.logicNewWalk(log);			
				try { 	Thread.sleep(Math.min((long) speed, 200)); }
				 catch (InterruptedException e1) { e1.printStackTrace();}
				break;	
			case 20:
				speed = moveRobots.logicNewWalk(log);			
				try { 	Thread.sleep(Math.min((long) speed, 150)); }
				 catch (InterruptedException e1) { e1.printStackTrace();}
				break;	
			case 23:
				speed = moveRobots.logicWalkFor23(log);
				try { Thread.sleep(30);}
				catch (InterruptedException e) { e.printStackTrace();}
				break;
						default:
				speed = moveRobots.logicNewWalk(log);			
				if(speed!=-1 && speed>10) { //the robot is very close to a node
					try { Thread.sleep(Math.min((long) speed, 200));} 
					catch (InterruptedException e) { e.printStackTrace();}
				}
				else {
					if(speed ==-1) //there is a robot on a node and it needs redirections
						try { Thread.sleep(150);} 
					catch (InterruptedException e) { e.printStackTrace();}
					else //all the robots are on an edge 
						try { Thread.sleep(50);} 
					catch (InterruptedException e) { e.printStackTrace();}
				}
				break;
			}
		}
		System.out.println("finish playing... moves = "+i);
	}
}