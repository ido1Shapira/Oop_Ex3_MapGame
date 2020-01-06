package gameClient;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Fruit;
import Server.Game_Server;
import Server.RobotG;
import Server.game_service;
import Server.robot;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;
import utils.StdDraw;

public class MyGameGUI {
	game_service game;
	public void buildScenario(int scenarioNumber) {
		if(scenarioNumber < 0 || scenarioNumber>23) {
			throw new RuntimeException("scenarioNumber must be between 0 to 23");
		}
		this.game = Game_Server.getServer(scenarioNumber); // you have [0,23] games
		String gJason = game.getGraph();
		DGraph g = new DGraph();
		g.init(gJason);
		Random r = new Random();
		int key= r.nextInt(g.nodeSize());
		while(game.addRobot(key)) {key = r.nextInt(g.nodeSize());}
		for (Iterator<String> iterator = game.getFruits().iterator(); iterator.hasNext();) {
			String fruit = (String) iterator.next();
			System.out.println(fruit);
		}
		//		for (int i = 0; i < game.getRobots().size(); i++) {
		//			Random r = new Random();
		//			int node= r.nextInt(g.nodeSize());
		//			this.game.addRobot(node);
		//		}
		paint(g,this.game.getRobots(),this.game.getFruits());		
	}


	public void playManual() {

	}
	public void playAuto() {

	}
	public void getTimeOut() {

	}

	////////////

	//for scale
	private static double xmin, ymin, xmax, ymax;

	private static Graph_Algo algo;

	static double Xmin,Xmax,Ymin,Ymax;
	static boolean firstPaint = true;

	private static void paint(graph g, List<String> robots, List<String> fruits) {
		if(firstPaint) { StdDraw.setCanvasSize(800,600); firstPaint = false;}
		if(g != null) {
			StdDraw.clear();
			algo = new Graph_Algo(g);
			if(g.nodeSize() > 0) {
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
			try {
				StdDraw.setXscale(Xmin,Xmax);
				StdDraw.setYscale(Ymin,Ymax);
			}
			catch (Exception e) {
				StdDraw.setScale();
				Xmin = 0;
				Xmax = 100;
				Ymin = 0;
				Ymax = 100;
			}
		}
		//paint graph
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
		//paint robots
		for (int i = 0; i <robots.size() ; i++) {
			RobotG r=new RobotG (g, i+4);
			drawRobot(r);
		}
		//paint fruits
		for (int i = 0; i < fruits.size(); i++) {
			Random val=new Random();
			String fs= fruits.get(i);
//			fs.
//			Fruit f = new Fruit(val.nextDouble(10.0), 1, e)
		}
		StdDraw.show();
	}

	private static void drawRobot(RobotG r) {
		StdDraw.setPenColor(Color.pink);
		StdDraw.setPenRadius(0.06);
		Point3D src=algo.myGraph.getNode(r.getSrcNode()).getLocation();
		StdDraw.point(src.x(), src.y());
	}
	
	private static void drawFruit(Fruit f) {
//		if()
		StdDraw.setPenColor(Color.pink);
		StdDraw.setPenRadius(0.06);
		StdDraw.point(f.getLocation().x(), f.getLocation().y());
	}

	private static void drawEdge(edge_data edge) {
		double rangeX=xmax-xmin;
		double rangeY=ymax-ymin;
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
		StdDraw.text(relativex+(10*rangeX/600.0), relativey+(10*rangeY/600.0),""+roundafter);
	}

	private static void drawNode(node_data node) {
		double rangeX=xmax-xmin;
		double rangeY=ymax-ymin;
		StdDraw.setPenRadius(0.0255);
		StdDraw.setPenColor(node.getInfo().equals("selected") ? StdDraw.GREEN : StdDraw.CYAN);
		StdDraw.point(node.getLocation().x(), node.getLocation().y());
		StdDraw.setPenColor(StdDraw.BLUE);
		StdDraw.text(node.getLocation().x()+(10.0*(rangeX/600.0)), node.getLocation().y()+(10.0*(rangeY)/600.0),""+node.getKey());
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
	public static JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu robots = new JMenu("robots");
		menuBar.add(robots);
		return menuBar;
	}

	public static void main(String[] a) {
		test1();
	}
	public static void test1() {
		MyGameGUI gui = new MyGameGUI();
		gui.buildScenario(16);
		System.out.println(gui.game.getRobots().size());
		gui.game.addRobot(5);
		System.out.println(gui.game.getRobots().size());
		gui.game.addRobot(5);
		System.out.println(gui.game.getRobots());

		//		gui.game.move();
	}
}
