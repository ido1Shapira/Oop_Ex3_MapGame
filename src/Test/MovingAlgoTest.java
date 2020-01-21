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
		MyServer.getServer(2);
		DGraph g= new DGraph();
		g.init(MyServer.getServer().game.getGraph());
		MovingAlgo m = MovingAlgo.getMovingAlgo(g);
		int nodeMax=0;
		int nodeMin=0;
		double valMax=0;
		double valMin=100;
		for(int i=0; i<MyServer.getServer().game.getFruits().size(); i++) {
			int currLoc=m.getFruitSrc().get(i);
			double currVal= MyParser.getFruitValue(MyServer.getServer().game.getFruits().get(i));
			if(currVal>= valMax) {
				valMax= currVal;
				nodeMax=currLoc;
			}
			if(currVal <= valMin) {
				valMin= currVal;
				nodeMin=currLoc;
			}
		}
		if(m.nodesByValue(m.getFruitSrc()).get(0)!=nodeMax || m.nodesByValue(m.getFruitSrc()).get(m.nodesByValue(m.getFruitSrc()).size()-1)!=nodeMin)
			fail("nodes by value failed");
		
	}
	
	@Test
	void testGetFruitSrc() {
		MyServer.getServer(2);
		DGraph g= new DGraph();
		g.init(MyServer.getServer().game.getGraph());
		MovingAlgo m =MovingAlgo.getMovingAlgo(g);
		ArrayList<Integer> list =new ArrayList<Integer>();
		list.add(9);
		list.add(4);
		list.add(3);
		if(!m.getFruitSrc().containsAll(list))
			fail("get fruit src failed");
	}
	@Test
	void testIsOnEdge() {
		MyServer.getServer(2);
		DGraph g= new DGraph();
		g.init(MyServer.getServer().game.getGraph());
		MovingAlgo m =MovingAlgo.getMovingAlgo(g);
		if(m.isOnEdge(MyServer.getServer().game.getFruits().get(0), new Edge(9,8,0))==0)
			fail("is on edge failed");
		if(m.isOnEdge(MyServer.getServer().game.getFruits().get(0), new Edge(8,9,0))!=0)
			fail("is on edge failed");
	}
	@Test
	void testFindFruitSrc() {
		MyServer.getServer(2);
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
		MyServer.getServer(2);
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
	
}
