package Test;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import algorithms.MovingAlgo;
import dataStructure.DGraph;
import dataStructure.Edge;
import myUtils.MyParser;
import myUtils.MyServer;

class MovingAlgoTest {

	@Test
	void testNodesByValue() {
		MyServer s= MyServer.getServer(2);
		DGraph g= new DGraph();
		g.init(MyServer.getServer().game.getGraph());
		MovingAlgo m = MovingAlgo.getMovingAlgo(g);
//		SysteMovingAlgo.out.println(MovingAlgo.getFruitSrc());
//		SysteMovingAlgo.out.println(MovingAlgo.nodesByValue());
		int nodeMax=0;
		int nodeMin=0;
		double valMax=0;
		double valMin=100;
		for(int i=0; i<MyServer.getServer().game.getFruits().size(); i++) {
			int currLoc=m.getFruitSrc().get(i);
			double currVal= MyParser.getFruitValue(MyServer.getServer().game.getFruits().get(i));
//			SysteMovingAlgo.out.print("the value of the fruit that is on "+currLoc+"  is ");
//			SysteMovingAlgo.out.println(currVal);
			if(currVal>= valMax) {
				valMax= currVal;
				nodeMax=currLoc;
			}
			if(currVal <= valMin) {
				valMin= currVal;
				nodeMin=currLoc;
			}
		}
		if(m.nodesByValue().get(0)!=nodeMax || m.nodesByValue().get(m.nodesByValue().size()-1)!=nodeMin)
			fail("nodes by value failed");
		
	}
	
	@Test
	void testGetFruitSrc() {
		MyServer s= MyServer.getServer(2);
		DGraph g= new DGraph();
		g.init(MyServer.getServer().game.getGraph());
		MovingAlgo m =MovingAlgo.getMovingAlgo(g);
	//	SysteMovingAlgo.out.println(MovingAlgo.getFruitSrc());
		ArrayList<Integer> list =new ArrayList<Integer>();
		list.add(9);
		list.add(4);
		list.add(3);
		if(!m.getFruitSrc().containsAll(list))
			fail("get fruit src failed");
	}
	@Test
	void testIsOnEdge() {
		MyServer s= MyServer.getServer(2);
		DGraph g= new DGraph();
		g.init(MyServer.getServer().game.getGraph());
		MovingAlgo m =MovingAlgo.getMovingAlgo(g);
	//	SysteMovingAlgo.out.println(MovingAlgo.getFruitSrc());
	//	SysteMovingAlgo.out.println(MyServer.getServer().game.getFruits().get(0));
		if(m.isOnEdge(MyServer.getServer().game.getFruits().get(0), new Edge(9,8,0))==0)
			fail("is on edge failed");
		if(m.isOnEdge(MyServer.getServer().game.getFruits().get(0), new Edge(8,9,0))!=0)
			fail("is on edge failed");
	}
	@Test
	void testFindFruitSrc() {
		MyServer s= MyServer.getServer(2);
		DGraph g= new DGraph();
		g.init(MyServer.getServer().game.getGraph());
		MovingAlgo m =MovingAlgo.getMovingAlgo(g);
		ArrayList<Integer> list =new ArrayList<Integer>();
		list.add(9);
		list.add(4);
		list.add(3);
		for(int i=0; i<MyServer.getServer().game.getFruits().size(); i++) {
			if(!list.contains(m.findFruitSrc(MyServer.getServer().game.getFruits().get(i))))
				fail("get fruit src failed");	
		}
	}
	
	@Test
	void testWhereToGo() {
		MyServer s= MyServer.getServer(2);
		DGraph g= new DGraph();
		g.init(MyServer.getServer().game.getGraph());
		MovingAlgo m =MovingAlgo.getMovingAlgo(g);
		if(m.whereToGo(8, m.getFruitSrc())!=9)
			fail("where to go failed");
		if(m.whereToGo(10, m.getFruitSrc())!=9)
			fail("where to go failed");
		if(m.whereToGo(2, m.getFruitSrc())!=3)
			fail("where to go failed");
		if(m.whereToGo(5, m.getFruitSrc())!=4)
			fail("where to go failed");
	}
	
//	@Test
//	public void testShortestPathDist() {
//		DGraph g = new DGraph();
//		Vertex v1=new Vertex(new Point3D(50,5,1),1,10);
//		Vertex v2=new Vertex(new Point3D(10,80,1),2,20);
//		Vertex v3=new Vertex(new Point3D(10,20,1),3,30);
//		Vertex v4=new Vertex(new Point3D(7,65,1),4,40);
//		g.addNode(v1);
//		g.addNode(v2);
//		g.addNode(v3);
//		g.addNode(v4);
//		g.connect(v1.getKey(), v2.getKey(), 30);
//		g.connect(v1.getKey(), v3.getKey(), 10);
//		g.connect(v3.getKey(), v4.getKey(), 5);
//		g.connect(v4.getKey(), v2.getKey(), 7);
//		Graph_Algo al= new Graph_Algo();
//		al.init(g);
//		SysteMovingAlgo.out.println(al.shortestPathDist(v1.getKey(), v2.getKey()));
//		if(al.shortestPathDist(v1.getKey(), v2.getKey())!=22)
//			fail("shortest path failed");
//		if(al.shortestPathDist(v2.getKey(), v1.getKey())!=Integer.MAX_VALUE-1)
//			fail("shortest path failed"); 
//		SysteMovingAlgo.out.println("2 ERROR massages expected:");
//		if(al.shortestPathDist(1, 5)!=-1)
//			fail("shortest path failed");
//		if(al.shortestPathDist(5, 5)!=-1)
//			fail("shortest path failed");
//		if(al.shortestPathDist(4, 4)!=0)
//			fail("shortest path failed");
//	}
//
////	@Test
//	public void testShortestPath() {
//		DGraph g = new DGraph();
//		Vertex v1=new Vertex(new Point3D(50,5,1),1,10);
//		Vertex v2=new Vertex(new Point3D(10,80,1),2,20);
//		Vertex v3=new Vertex(new Point3D(10,20,1),3,30);
//		Vertex v4=new Vertex(new Point3D(7,65,1),4,40);
//		g.addNode(v1);
//		g.addNode(v2);
//		g.addNode(v3);
//		g.addNode(v4);
//		g.connect(v1.getKey(), v2.getKey(), 30);
//		g.connect(v1.getKey(), v3.getKey(), 10);
//		g.connect(v3.getKey(), v4.getKey(), 5);
//		g.connect(v4.getKey(), v2.getKey(), 7);
//		Graph_Algo al= new Graph_Algo();
//		al.init(g);
//		SysteMovingAlgo.out.println(al.shortestPath(v1.getKey(), v2.getKey()));
//		if(al.shortestPath(v1.getKey(), v2.getKey()).size()!=4)//v1 -> v3 -> v4 ->v2
//			fail("shortest path failed");
//	}

}
