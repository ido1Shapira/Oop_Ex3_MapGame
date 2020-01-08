package gameClient;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;
import utils.StdDraw;

public class MyGameGUI implements Runnable,MouseListener {
	public static game_service game;
	public static Graph_Algo algo;
	private static Color robotColor;
	public static boolean isManual;
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
		int scenarioNumber = (Integer)JOptionPane.showInputDialog(null, "Pick the scenario number:",
				"scenario options", JOptionPane.QUESTION_MESSAGE, null, scenarioOptions, null);
		game = Game_Server.getServer(scenarioNumber); // you have [0,23] games
		String gJason = game.getGraph();
		DGraph g = new DGraph();
		g.init(gJason);
		algo = new Graph_Algo(g);
		Object stringArray[] = { "Manual", "Automatic" };
		int kindOfGame = JOptionPane.showOptionDialog(null, "Are you want to play manual or automatic?", "Select an Option",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, stringArray,
				null);
		if(kindOfGame == 0) { //Manual
			playManual();
		}
		else { //Automatic
			playAuto();
		}
		String info = MyGameGUI.game.toString();
		try {
			JSONObject infoJson = new JSONObject(info);
			JSONObject jsonforRobot = infoJson.getJSONObject("GameServer");
			String strNumber =""+ jsonforRobot.get("robots");
			int numberOfRobots = Integer.parseInt(strNumber);
			for (int i = 1; i <= numberOfRobots; i++) {
				Object[] tempkeys = keysList();
				int src_node = (Integer)JOptionPane.showInputDialog(null, "Pick a vertex to put robot "+i +":",
						"Add robot", JOptionPane.QUESTION_MESSAGE, null, tempkeys, null);
				MyGameGUI.game.addRobot(src_node);
			}
		} catch (JSONException e1) {e1.printStackTrace();}
		StdDraw.enableDoubleBuffering();
		MyGameGUI gui = new MyGameGUI();
	}

	private static Object[] keysList() {
		Object[] keys = new Object[algo.myGraph.nodeSize()];
		int i = 0;
		for (Iterator<node_data> iterator = algo.myGraph.getV().iterator(); iterator.hasNext();) {
			node_data v = (node_data) iterator.next();
			keys[i++] = v.getKey();
		}
		return keys;
	}

	private static void playManual() {
		isManual = true;
	}
	private static void playAuto() {
		isManual = false;
	}
	private static void paintTimeOut() {
		long t = game.timeToEnd();
		StdDraw.setPenColor(Color.black);
		StdDraw.textRight(Xmax-0.002, Ymax-0.001, "time to end: "+t/1000);
	}

	static double Xmin,Xmax,Ymin,Ymax;
	static boolean firstPaint = true;

	public static void paint(List<String> robots, List<String> fruits) {
		if(firstPaint) { StdDraw.setCanvasSize(800,600); firstPaint = false;}
		StdDraw.clear();
		if(algo.myGraph != null) {
			if(algo.myGraph.nodeSize() > 0) {
				Xmin=Double.POSITIVE_INFINITY;
				Xmax=Double.NEGATIVE_INFINITY;
				Ymin=Double.POSITIVE_INFINITY;
				Ymax=Double.NEGATIVE_INFINITY;
				for (Iterator<node_data> iterator = algo.myGraph.getV().iterator(); iterator.hasNext();) {
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
			clearSelected();
			StdDraw.setXscale(Xmin,Xmax);
			StdDraw.setYscale(Ymin,Ymax);
		}
		paintTimeOut();
		for (Iterator<node_data> iterator = algo.myGraph.getV().iterator(); iterator.hasNext();) {
			node_data node = (node_data) iterator.next();
			drawNode(node);
			if(algo.myGraph.getE(node.getKey()) != null) {
				for (Iterator<edge_data> iterator2 = algo.myGraph.getE(node.getKey()).iterator(); iterator2.hasNext();) {
					edge_data edge = (edge_data) iterator2.next();
					drawEdge(edge);
				}
			}
		}
		//paint fruits
		for (Iterator<String> iterator = game.getFruits().iterator(); iterator.hasNext();) {
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
		try {
			JSONObject line = new JSONObject(robotJ);
			JSONObject jsonforRobot = line.getJSONObject("Robot");
			String strPos =""+ jsonforRobot.get("pos");
			String[] xyz = strPos.split(",");
			double x = Double.valueOf(xyz[0]);
			double y = Double.valueOf(xyz[1]);
			StdDraw.point(x,y);
		} catch (JSONException e) {e.printStackTrace();}

	}

	private static void drawFruits(String fruit) {
		try {
			JSONObject line = new JSONObject(fruit);
			JSONObject fruitJ = line.getJSONObject("Fruit");
			//find pos
			String strPos =""+ fruitJ.get("pos");
			String[] xyz = strPos.split(",");
			double x = Double.valueOf(xyz[0]);
			double y = Double.valueOf(xyz[1]);
			//find type
			int strType = fruitJ.getInt("type");
			StdDraw.picture(x,y,(strType == -1) ? "banana.jpeg" :"apple.jpeg", 0.00075, 0.00055);
		} catch (JSONException e) {e.printStackTrace();}
	}

	private static void drawEdge(edge_data edge) {
		double rangeX=Xmax-Xmin;
		double rangeY=Ymax-Ymin;
		StdDraw.setPenRadius(0.005);
		StdDraw.setPenColor(edge.getInfo().equals("shortest path") ? StdDraw.YELLOW : StdDraw.BLACK);
		node_data src = algo.myGraph.getNode(edge.getSrc());
		node_data dest = algo.myGraph.getNode(edge.getDest());
		StdDraw.line(src.getLocation().x(),src.getLocation().y(),dest.getLocation().x() , dest.getLocation().y());
		StdDraw.setPenRadius(0.02);
		StdDraw.setPenColor(StdDraw.ORANGE);
		double relativex=(src.getLocation().x()+dest.getLocation().x()*7)/8;
		double relativey=(src.getLocation().y()+dest.getLocation().y()*7)/8;
		StdDraw.point(relativex, relativey);
		int round=(int)(edge.getWeight()*100);
		double roundafter=round;
		roundafter=roundafter/100;
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.setPenRadius(0.02);
		StdDraw.text(relativex+(10*rangeX/800.0), relativey+(10*rangeY/600.0),""+roundafter);
	}

	private static void drawNode(node_data node) {
		double rangeX=Xmax-Xmin;
		double rangeY=Ymax-Ymin;
		StdDraw.setPenRadius(0.0255);
		StdDraw.setPenColor(node.getInfo().equals("selected") ? StdDraw.GREEN : StdDraw.CYAN);
		StdDraw.point(node.getLocation().x(), node.getLocation().y());
		StdDraw.setPenColor(StdDraw.BLUE);
		StdDraw.text(node.getLocation().x()+(10.0*(rangeX/800.0)), node.getLocation().y()+(10.0*(rangeY)/600.0),""+node.getKey());
	}

	private static void clearSelected() {
		for (Iterator<node_data> iterator = algo.myGraph.getV().iterator(); iterator.hasNext();) {
			node_data v = (node_data) iterator.next();
			v.setInfo("");
			for (Iterator<edge_data> iterator2 = algo.myGraph.getE(v.getKey()).iterator(); iterator2.hasNext();) {
				edge_data e = (edge_data) iterator2.next();
				e.setInfo("");
			}
		}
	}


	@Override
	public void run() {
		while(game.isRunning()) {
			MyGameGUI.paint(game.getRobots(), game.getFruits());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {e.printStackTrace();}
		}

	}

	//not working yet
	int key = -1;
	public static final double EPSILON = 10;


	private boolean similar(Point3D p1 , Point3D p2) {
		return (Math.abs(p1.x() - p2.x()) <= EPSILON)
				&& (Math.abs(p1.y() - p2.y()) <= EPSILON)
				&& (Math.abs(p1.z() - p2.z()) <= EPSILON);
	}
	private int findVertexWhenClicked(Point3D p) {
		for (Iterator<node_data> iterator = algo.myGraph.getV().iterator(); iterator.hasNext();) {
			node_data v = (node_data) iterator.next();
			if(similar(v.getLocation(),p)) {return v.getKey();}
		}
		return -1;
	}

	private Point3D getCordinateOnScreen(Point3D PbyPixle) {
		double XPixle=PbyPixle.x();
		double YPixle=PbyPixle.y();

		double mX = (Xmax-Xmin)/800.0;
		double mY=(Ymin-Ymax)/600.0;
		return new Point3D((mX*XPixle+Xmin), (mY*YPixle+Ymax));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(e.getClickCount());
		if(isManual) {
			Point3D p = getCordinateOnScreen(new Point3D (e.getX(),e.getY()));
			System.out.println(p);
			key = findVertexWhenClicked(p);
			System.out.println(key);
			if(key != -1) {
				if(algo.myGraph.getNode(key).getInfo().equals("selected")) {
					algo.myGraph.getNode(key).setInfo("");
				}
				else {
					algo.myGraph.getNode(key).setInfo("selected");
				}
			}
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}


	public static void main(String[] a) {
		test1();
	}
	public static void test1() {
		buildScenario();
		String g = game.getGraph();
		DGraph gg = new DGraph();
		gg.init(g);
		game.addRobot(1);
		game.addRobot(0);
		paint(game.getRobots(), game.getFruits());

	}
}
