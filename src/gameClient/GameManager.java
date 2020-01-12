package gameClient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import algorithms.MovingAlgo;
import dataStructure.edge_data;
import myUtils.HelpMe;
import utils.Point3D;

public class GameManager implements Runnable {

	public GameManager() {
		Thread Master = new Thread(this);
		Master.start();
	} 


	@Override
	public void run() {
		if(!MyGameGUI.isManual) {
			while(MyGameGUI.game.isRunning()) {
				List<String> log = MyGameGUI.game.move();
				MovingAlgo.logicWalk(log);
			}
		}
	}


}