package gameClient;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import Server.Game_Server;
import algorithms.Graph_Algo;
import algorithms.MovingAlgo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import myUtils.HelpMe;
import utils.Point3D;
import utils.StdDraw;

public class MyGameGUI implements Runnable {
	
	private static Color robotColor = Color.BLACK;
	private static int scenarioNumber = 0;
	private static int points = 0;

	
	static {
		Random random = new Random();
		int r = random.nextInt(256); int g = random.nextInt(256); int b = random.nextInt(256);
		StdDraw.setPenColor(r,g,b);
		robotColor = new Color(r, g, b);
	}

	private MyGameGUI() {
		Thread toPaint = new Thread(this);
		toPaint.start();
	} 

	public static void buildScenario() {
		Object[]scenarioOptions = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23};
		scenarioNumber = (Integer)JOptionPane.showInputDialog(null, "Pick the scenario number:",
				"scenario options", JOptionPane.QUESTION_MESSAGE, null, scenarioOptions, null);
		MovingAlgo.game = Game_Server.getServer(scenarioNumber); // you have [0,23] games
		
		String gJason = MovingAlgo.game.getGraph();
		System.out.println(gJason);
		String[] NE= gJason.split("Nodes", 2);
		System.out.println(NE[1]);
		String [] nodes= NE[1].split("\\}\\,\\{");
		for (int i = 0; i < nodes.length; i++) {
			System.out.println(nodes[i]);
		}
		DGraph g = new DGraph();
		g.init(gJason);
		MovingAlgo.algo = new Graph_Algo(g);
		Object stringArray[] = { "Manual", "Automatic" };
		int kindOfGame = JOptionPane.showOptionDialog(null, "Are you want to play manual or automatic?", "Select an Option",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, stringArray,
				null);
		StdDraw.enableDoubleBuffering();
		if(kindOfGame == 0) { //Manual
			playManual();
		}
		else { //Automatic
			playAuto();
		}
		MyGameGUI gui = new MyGameGUI();
	}

	private static Object[] keysList() {
		Object[] keys = new Object[MovingAlgo.algo.myGraph.nodeSize()];
		int i = 0;
		for (Iterator<node_data> iterator = MovingAlgo.algo.myGraph.getV().iterator(); iterator.hasNext();) {
			node_data v = (node_data) iterator.next();
			keys[i++] = v.getKey();
		}
		return keys;
	}

	private static void playManual() {
		GameManager.isManual = true;
		String info = MovingAlgo.game.toString();
		MyGameGUI.paint(MovingAlgo.game.getRobots(), MovingAlgo.game.getFruits());
		int numberOfRobots = HelpMe.getRobotsNum(info);
		for (int i = 1; i <= numberOfRobots; i++) {
			Object[] tempkeys = keysList();
			int src_node = (Integer)JOptionPane.showInputDialog(null, "Pick a vertex to put robot "+i +":",
					"Add robot", JOptionPane.QUESTION_MESSAGE, null, tempkeys, null);
			MovingAlgo.game.addRobot(src_node);
		}
	}
	private static void playAuto() {
		GameManager.isManual = false;
		MovingAlgo.addRobot();
		GameManager man = new GameManager();
	}
	private static void paintInfoGame() {
		long t = MovingAlgo.game.timeToEnd();
		points =0;
		for (Iterator<String> iterator = MovingAlgo.game.getRobots().iterator(); iterator.hasNext();) {
			String robotj = (String) iterator.next();
			points += HelpMe.getRobotValue(robotj);
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

	public static void paint(List<String> robots, List<String> fruits) {
		if(firstPaint) {StdDraw.setCanvasSize(800,600); firstPaint = false;}
		StdDraw.clear();
		if(MovingAlgo.algo.myGraph != null) {
			if(MovingAlgo.algo.myGraph.nodeSize() > 0) {
				Xmin=Double.POSITIVE_INFINITY;
				Xmax=Double.NEGATIVE_INFINITY;
				Ymin=Double.POSITIVE_INFINITY;
				Ymax=Double.NEGATIVE_INFINITY;
				for (Iterator<node_data> iterator = MovingAlgo.algo.myGraph.getV().iterator(); iterator.hasNext();) {
					node_data v = (node_data) iterator.next();
					if(v.getLocation().x() < Xmin) Xmin = v.getLocation().x();
					if(v.getLocation().x() > Xmax) Xmax = v.getLocation().x();
					if(v.getLocation().y() < Ymin) Ymin = v.getLocation().y();
					if(v.getLocation().y() > Ymax) Ymax = v.getLocation().y();
				}
				Xmin -= 0.002;
				Xmax += 0.002;
				Ymin -= 0.0015;
				Ymax += 0.0015;
			}
			StdDraw.setXscale(Xmin,Xmax);
			StdDraw.setYscale(Ymin,Ymax);
		}
		paintInfoGame();
		for (Iterator<node_data> iterator = MovingAlgo.algo.myGraph.getV().iterator(); iterator.hasNext();) {
			node_data node = (node_data) iterator.next();
			if(MovingAlgo.algo.myGraph.getE(node.getKey()) != null) {
				for (Iterator<edge_data> iterator2 = MovingAlgo.algo.myGraph.getE(node.getKey()).iterator(); iterator2.hasNext();) {
					edge_data edge = (edge_data) iterator2.next();
					drawEdge(edge);
				}
			}
		}
		for (Iterator<node_data> iterator = MovingAlgo.algo.myGraph.getV().iterator(); iterator.hasNext();) {
			node_data node = (node_data) iterator.next();
			drawNode(node);
		}
		//paint fruits
		for (Iterator<String> iterator = MovingAlgo.game.getFruits().iterator(); iterator.hasNext();) {
			String fruit = (String) iterator.next();
			drawFruits(fruit);
		}
		//paint robots
		for (Iterator<String> iterator = robots.iterator(); iterator.hasNext();) {
			String robotJ = (String) iterator.next();
			drawRobot(robotJ);
		}
		StdDraw.show();
	}

	private static void drawRobot(String robotJ) {

		StdDraw.setPenColor(robotColor);
		StdDraw.setPenRadius(0.06);
		Point3D pos = HelpMe.getRobotPosition(robotJ);
		StdDraw.point(pos.x(),pos.y());

	}

	private static void drawFruits(String fruitJ) {
			Point3D pos = HelpMe.getFruitPosition(fruitJ);
			//find type
			int type = HelpMe.getFruitType(fruitJ);
			StdDraw.picture(pos.x(),pos.y(),(type == -1) ? "banana.jpeg" :"apple.jpeg", 0.00075, 0.00055);
	}

	private static void drawEdge(edge_data edge) {
		//		double rangeX=Xmax-Xmin;
		//		double rangeY=Ymax-Ymin;
		StdDraw.setPenRadius(0.005);
		StdDraw.setPenColor(StdDraw.BLACK);
		node_data src = MovingAlgo.algo.myGraph.getNode(edge.getSrc());
		node_data dest = MovingAlgo.algo.myGraph.getNode(edge.getDest());
		StdDraw.line(src.getLocation().x(),src.getLocation().y(),dest.getLocation().x() , dest.getLocation().y());
		//		myStdDraw.setPenRadius(0.02);
		//		myStdDraw.setPenColor(myStdDraw.ORANGE);
		//		double relativex=(src.getLocation().x()+dest.getLocation().x()*7)/8;
		//		double relativey=(src.getLocation().y()+dest.getLocation().y()*7)/8;
		//		myStdDraw.point(relativex, relativey);
		//		int round=(int)(edge.getWeight()*100);
		//		double roundafter=round;
		//		roundafter=roundafter/100;
		//		myStdDraw.setPenColor(myStdDraw.RED);
		//		myStdDraw.setPenRadius(0.02);
		//		myStdDraw.text(relativex+(10*rangeX/800.0), relativey+(10*rangeY/600.0),""+roundafter);
	}

	private static void drawNode(node_data node) {
		StdDraw.setPenRadius(0.03);
		StdDraw.setPenColor(StdDraw.CYAN);
		StdDraw.point(node.getLocation().x(), node.getLocation().y());
		StdDraw.setPenColor(StdDraw.BLUE);
		StdDraw.text(node.getLocation().x(), node.getLocation().y(),""+node.getKey());
	}

	@Override
	public void run() {
		while(MovingAlgo.game.isRunning()) {
			MyGameGUI.paint(MovingAlgo.game.getRobots(), MovingAlgo.game.getFruits());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		JOptionPane.showMessageDialog(null, "Game over:\nyou got "+points+" points","Game over",JOptionPane.INFORMATION_MESSAGE);
	}



//	public static void main(String[] a) {
//		//test unit for HelpMe
//		for (Iterator<String> iterator = game.getRobots().iterator(); iterator.hasNext();) {
//			String robotj = (String) iterator.next();
//			System.out.println(HelpMe.getRobotValue(robotj));
//			System.out.println(HelpMe.getRobotDest(robotj));
//			System.out.println(HelpMe.getRobotId(robotj));
//			System.out.println(HelpMe.getRobotSpeed(robotj));
//			System.out.println(HelpMe.getRobotSrc(robotj));
//			System.out.println(HelpMe.getRobotPosition(robotj));
//		}
//		for (Iterator<String> iterator = game.getFruits().iterator(); iterator.hasNext();) {
//			String robotj = (String) iterator.next();
//			System.out.println(HelpMe.getFruitType(robotj));
//			System.out.println(HelpMe.getFruitValue(robotj));
//			System.out.println(HelpMe.getFruitPosition(robotj));
//		}
//		test1();
//	}
	public static void test1() {
		buildScenario();
		String g = MovingAlgo.game.getGraph();
		DGraph gg = new DGraph();
		gg.init(g);
		MovingAlgo.game.addRobot(1);
		MovingAlgo.game.addRobot(0);
		paint(MovingAlgo.game.getRobots(), MovingAlgo.game.getFruits());

	}
}
