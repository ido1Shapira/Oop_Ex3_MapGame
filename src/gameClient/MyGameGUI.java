package gameClient;
import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.StdDraw;

public class MyGameGUI implements Runnable{
	game_service game;
	private static Graph_Algo algo;

	public void buildScenario(int scenarioNumber) {
		if(scenarioNumber < 0 || scenarioNumber>23) {
			throw new RuntimeException("scenarioNumber must be between 0 to 23");
		}
		this.game = Game_Server.getServer(scenarioNumber); // you have [0,23] games
		String gJason = game.getGraph();
		DGraph g = new DGraph();
		g.init(gJason);
		algo.init(g);
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

	static double Xmin,Xmax,Ymin,Ymax;
	static boolean firstPaint = true;

	public void paint(graph g, List<String> robots, List<String> fruits) {
		if(firstPaint) { StdDraw.setCanvasSize(800,600); firstPaint = false;}
		StdDraw.clear();
		if(g != null) {
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
		for (Iterator<String> iterator = robots.iterator(); iterator.hasNext();) {
			String robotJ = (String) iterator.next();
			drawRobot(robotJ);
		}
		//paint fruits
		for (Iterator<String> iterator = game.getFruits().iterator(); iterator.hasNext();) {
			String fruit = (String) iterator.next();
			drawFruits(fruit);
		}
		StdDraw.show();
	}

	private void drawRobot(String robotJ) {
		StdDraw.setPenColor(Color.pink);
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

	private void drawFruits(String fruit) {
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
			StdDraw.setPenRadius(0.04);
			StdDraw.picture(x,y,(strType == -1) ? "banana.jpeg" :"apple.jpeg", 0.00075, 0.00075);
		} catch (JSONException e) {e.printStackTrace();}
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
		StdDraw.text(relativex+(10*rangeX/800.0), relativey+(10*rangeY/600.0),""+roundafter);
	}

	private static void drawNode(node_data node) {
		double rangeX=xmax-xmin;
		double rangeY=ymax-ymin;
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
	
	public static void main(String[] a) {
		test1();
	}
	public static void test1() {
		MyGameGUI gui = new MyGameGUI();
		gui.buildScenario(16);
		String g = gui.game.getGraph();
		DGraph gg = new DGraph();
		gg.init(g);
		gui.game.addRobot(1);
		gui.game.addRobot(0);
		gui.paint(gg, gui.game.getRobots(), gui.game.getFruits());

	}
	private static int nextNode(graph g, int src) {
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}

	@Override
	public void run() {
		while(game.isRunning()) {
			this.paint(algo.myGraph, game.getRobots(), game.getFruits());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		
	}
}
