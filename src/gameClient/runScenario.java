package gameClient;

import myUtils.MyServer;

public class runScenario implements Runnable {
	private int NumberScenario;
	private static MyServer server;

	public runScenario(int NumberScenario) {
		this.NumberScenario = NumberScenario;
	}
	@Override
	public void run() {
		server = new MyServer(NumberScenario); // you have [0,23] games
		server.game.startGame();
		AutoManager man = new AutoManager(NumberScenario,server); //calling the game manager to start the game
		Thread t1 = new Thread(man);
		t1.start();
		Logger_KML kml = new Logger_KML(NumberScenario); //starts the KML recording of the game
		try { t1.join();
		} catch (InterruptedException e) {e.printStackTrace();}
		kml.writeToFile();
		System.out.println("Exclude KML file: "+NumberScenario+" ...");
		
	}

}
