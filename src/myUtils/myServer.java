package myUtils;

import Server.Game_Server;
import Server.game_service;

public class MyServer {

	// static variable single_instance of server 
	private static MyServer single_instance = null; 

	public game_service game;

	// private constructor restricted to this class itself 
	public MyServer(int scenarioNumber) 
	{ 
		game = Game_Server.getServer(scenarioNumber); // you have [0,23] games
//		game.startGame();
	} 

	// static method to create instance of myServer class 
	public static synchronized MyServer getServer(int scenarioNumber) 
	{ 
		if (single_instance == null) {
			synchronized (MyServer.class) {
				if (single_instance == null)
					single_instance = new MyServer(scenarioNumber); 
			}
		}
		return single_instance;
	}
	public static MyServer getServer() 
	{
		return single_instance;
	}


}
