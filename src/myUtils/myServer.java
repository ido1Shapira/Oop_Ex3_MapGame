package myUtils;

import Server.Game_Server;
import Server.game_service;

public class myServer {

	// static variable single_instance of server 
	private static myServer single_instance = null; 

	public game_service game;

	// private constructor restricted to this class itself 
	private myServer(int scenarioNumber) 
	{ 
		game = Game_Server.getServer(scenarioNumber); // you have [0,23] games
//		game.startGame();
	} 

	// static method to create instance of myServer class 
	public static synchronized myServer getServer(int scenarioNumber) 
	{ 
		if (single_instance == null) {
			synchronized (myServer.class) {
				if (single_instance == null)
					single_instance = new myServer(scenarioNumber); 
			}
		}
		return single_instance;
	}
	public static myServer getServer() 
	{
		return single_instance;
	}


}
