package gameClient;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import Server.Game_Server;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import myUtils.MyParser;
import myUtils.MyServer;
import utils.Point3D;
import utils.StdDraw;

public class MyGameGUI implements Runnable {

	private static Color robotColor = Color.BLACK;
	private static int scenarioNumber = 0;
	private static int points = 0;
	public static graph g = new DGraph();
	private static Logger_KML kml;

	private static MyServer server = MyServer.getServer();

	public static boolean autoManagerIsReady = false; 

	// static variable single_instance of server 
	private static MyGameGUI single_instance = null;

	public static DBquery q = new DBquery("0"); //Default id in case the client  not to log in
	// private constructor restricted to this class itself 
	private MyGameGUI() {
		String id = JOptionPane.showInputDialog("Log in:","enter id");
		q = new DBquery(id);
		Game_Server.login(Integer.parseInt(id));
		buildScenario();
	} 

	/** 
	 * static method to create instance of myServer class 
	 * @return the single instance of this class
	 */
	public static MyGameGUI getGui() 
	{ 
		if (single_instance == null) {
			synchronized (MyGameGUI.class) {
				if (single_instance == null)
					single_instance = new MyGameGUI(); 
			}
		}
		return single_instance;
	}
	/**
	 * random color for the robots
	 */
	static {
		Random random = new Random();
		int r = random.nextInt(256); int g = random.nextInt(256); int b = random.nextInt(256);
		StdDraw.setPenColor(r,g,b);
		robotColor = new Color(r, g, b);
	}

	/**
	 * this method creates the opening window for starting the game.
	 * here the client chooses the scenario he wants to play and the way of moving the robots (manual or automatic)
	 * according to the client choice the game begins
	 * 
	 * 
	 */
	public static void buildScenario() {
		try {
			Object[]scenarioOptions = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23};
			scenarioNumber = (Integer)JOptionPane.showInputDialog(null, "Pick the scenario number:",
					"scenario options", JOptionPane.QUESTION_MESSAGE, null, scenarioOptions, null);
		}
		catch (NullPointerException e) {
			return;
		}
		server = MyServer.getServer(scenarioNumber); // you have [0,23] games

		String gJason = server.game.getGraph();
		((DGraph) g).init(gJason);
		MyGameGUI.paint(server.game.getRobots(), server.game.getFruits());
		Object option[] = { "Manual", "Automatic" };
		int kindOfGame = JOptionPane.showOptionDialog(null, "Are you want to play manual or automatic?", "Select an Option",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option,null);
		StdDraw.enableDoubleBuffering();
		if(kindOfGame == 0) { //Manual management
			playManual();
		}
		else { //Automatic management
			playAuto(scenarioNumber);
		}
		kml = new Logger_KML(scenarioNumber); //starts the KML recording of the game
		Thread kmlThread = new Thread(kml);
		kmlThread.start();
	}

	/**
	 * 
	 * @return an array of the node's keys
	 */
	private static Object[] keysList() {
		Object[] keys = new Object[g.nodeSize()];
		int i = 0;
		for (Iterator<node_data> iterator = g.getV().iterator(); iterator.hasNext();) {
			node_data v = (node_data) iterator.next();
			keys[i++] = v.getKey();
		}
		return keys;
	}

	/**
	 * Initiate a game according to the client choice of the robots location
	 */	
	private static void playManual() {
		String info = server.game.toString();
		MyGameGUI.paint(server.game.getRobots(), server.game.getFruits());
		int numberOfRobots = MyParser.getRobotsNum(info);
		for (int i = 1; i <= numberOfRobots; i++) {
			Object[] tempkeys = keysList();
			int src_node = (Integer)JOptionPane.showInputDialog(null, "Pick a vertex to put robot "+i +":",
					"Add robot", JOptionPane.QUESTION_MESSAGE, null, tempkeys, null);
			server.game.addRobot(src_node);
		}
	}
	/**
	 * starts a game played automatically by our moving algorithm
	 * @param scenarioNumber the scenario to begin
	 */
	private static void playAuto(int scenarioNumber) {
		AutoManager auto = new AutoManager(scenarioNumber); //calling the auto game manager to start the game
		Thread Master = new Thread(auto);
		Master.start();
		while(!autoManagerIsReady ) {
			try {Thread.sleep(50); //estimated time to locate all the robots in order to begin
			} catch(InterruptedException e) {e.printStackTrace();}
		}
	}
	/**
	 * prints some information about the game on the screen
	 * 1) time left to play
	 * 2) current point state
	 * 3) scenario number
	 */
	private static void paintInfoGame() {
		long t = server.game.timeToEnd();
		points =0;
		for (Iterator<String> iterator = server.game.getRobots().iterator(); iterator.hasNext();) {
			String robotj = (String) iterator.next();
			points += MyParser.getRobotValue(robotj);
		}
		StdDraw.setPenColor(Color.black);
		StdDraw.text((Xmax+Xmin)/2, Ymax-0.001, "points: "+points);
		StdDraw.textRight(Xmax-0.002, Ymax-0.001, "time to end: "+t/1000);
		StdDraw.textLeft(Xmin+0.002, Ymax-0.001, "Number scenario: "+scenarioNumber);
	}

	public static double Xmin;
	public static double Xmax;
	public static double Ymin;
	public static double Ymax;
	private static boolean firstPaint = true;
	/**
	 * Illustrate the game on the screen showing the graph we play on, robots and fruits
	 * all according to the current state given from the server
	 * @param robots robots objects in a json format
	 * @param fruits fruits objects in a json format
	 */
	private static void paint(List<String> robots, List<String> fruits) {
		if(firstPaint) {
			StdDraw.setCanvasSize(1239,595);
			firstPaint = false;
		}
		if(g != null) {
			if(g.nodeSize() > 0) {
				Xmin=Double.POSITIVE_INFINITY;
				Xmax=Double.NEGATIVE_INFINITY;
				Ymin=Double.POSITIVE_INFINITY;
				Ymax=Double.NEGATIVE_INFINITY;
				for (Iterator<node_data> iterator = g.getV().iterator(); iterator.hasNext();) {
					node_data v = (node_data) iterator.next();
					if(v.getLocation().x() < Xmin) Xmin = v.getLocation().x();
					if(v.getLocation().x() > Xmax) Xmax = v.getLocation().x();
					if(v.getLocation().y() < Ymin) Ymin = v.getLocation().y();
					if(v.getLocation().y() > Ymax) Ymax = v.getLocation().y();
				}
				Xmin -= 0.002;
				Xmax += 0.002;
				Ymin -= 0.002;
				Ymax += 0.002;
			}
			StdDraw.setXscale(Xmin,Xmax);
			StdDraw.setYscale(Ymin,Ymax);
		}
		StdDraw.clear(); //clear previous screen
		//				String background = MyParser.getBackGround(server.game.toString());
		//				StdDraw.picture((Xmax+Xmin)/2,(Ymax+Ymin)/2,"C:/Users/idsha/eclipse-workspace/OOP_ex3/"+background+".png");
		paintInfoGame();
		//draw edges
		for (Iterator<node_data> iterator = g.getV().iterator(); iterator.hasNext();) {
			node_data node = (node_data) iterator.next();
			if(g.getE(node.getKey()) != null) {
				for (Iterator<edge_data> iterator2 = g.getE(node.getKey()).iterator(); iterator2.hasNext();) {
					edge_data edge = (edge_data) iterator2.next();
					drawEdge(edge);
				}
			}
		}
		//draw nodes
		for (Iterator<node_data> iterator = g.getV().iterator(); iterator.hasNext();) {
			node_data node = (node_data) iterator.next();
			drawNode(node);
		}
		//draw fruits
		for (Iterator<String> iterator = server.game.getFruits().iterator(); iterator.hasNext();) {
			String fruit = (String) iterator.next();
			drawFruits(fruit);
		}
		//draw robots
		for (Iterator<String> iterator = robots.iterator(); iterator.hasNext();) {
			String robotJ = (String) iterator.next();
			drawRobot(robotJ);
		}
		StdDraw.show();
	}
	/**
	 * draw robots according to the string given
	 * @param robotJ a robot object in a json format
	 */
	private static void drawRobot(String robotJ) {
		StdDraw.setPenColor(robotColor);
		StdDraw.setPenRadius(0.06);
		Point3D pos = MyParser.getRobotPosition(robotJ);
		StdDraw.point(pos.x(),pos.y());
	}
	/**
	 * draw fruit according to the string given
	 * @param fruitJ a fruit object in a json format
	 */
	private static void drawFruits(String fruitJ) {
		Point3D pos = MyParser.getFruitPosition(fruitJ);
		//find type
		int type = MyParser.getFruitType(fruitJ);
		StdDraw.picture(pos.x(),pos.y(),(type == -1) ? "data/banana.jpeg" :"data/apple.jpeg", 0.00075, 0.00055);
	}
	/**
	 * draw edge according to the edge_data given
	 * @param edge the edge to draw
	 */
	private static void drawEdge(edge_data edge) {
		StdDraw.setPenRadius(0.005);
		StdDraw.setPenColor(StdDraw.BLACK);
		node_data src = g.getNode(edge.getSrc());
		node_data dest = g.getNode(edge.getDest());
		StdDraw.line(src.getLocation().x(),src.getLocation().y(),dest.getLocation().x() , dest.getLocation().y());
	}
	/**
	 * draw node according to the node_data given
	 * @param node the node to draw
	 */
	private static void drawNode(node_data node) {
		StdDraw.setPenRadius(0.03);
		StdDraw.setPenColor(StdDraw.CYAN);
		StdDraw.point(node.getLocation().x(), node.getLocation().y());
		StdDraw.setPenColor(StdDraw.BLUE);
		StdDraw.text(node.getLocation().x(), node.getLocation().y(),""+node.getKey());
	}
	/**
	 * the thread routine during the game
	 * display the current state of the game every 100 milliseconds
	 * after the game ends give the user the option to save the game as a KML file 
	 */
	@Override
	public void run() {
		if(server != null) {
			while(server.game.isRunning()) {
				paint(server.game.getRobots(), server.game.getFruits());
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
//			Object[] option = {"Yes","No"};
//			String info = server.game.toString();
//			int moves = MyParser.getMoves(info);
//			int toKML = JOptionPane.showOptionDialog(null, "Game over:\nyou got "+points+" points with "+ moves+" moves\n"
//					+ "Do you want to save this game to a kml file?","Game over",
//					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option,null);
//			if(toKML == 0) {
//				kml.writeToFile();
//			}
			String res = server.game.toString();
			String remark = kml.writeToFile();
			server.game.sendKML(remark); // Should be your KML
			System.out.println(res);
		}
	}
}
