package Test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Vertex;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;
public class graph_algorithmsTest {

	@Test
	public void testInitGraph() {
		DGraph g = new DGraph();
		Vertex v1=new Vertex(new Point3D(50,5,1),1,0);
		Vertex v2=new Vertex(new Point3D(10,80,1),2,0);
		g.addNode(v1);
		g.connect(v1.getKey(), v2.getKey(), 10);
		g.connect(v2.getKey(), v1.getKey(), 15);
		Graph_Algo al= new Graph_Algo();
		al.init(g);
		g.addNode(v2);
		if(!g.equals(al.myGraph))
			fail("init failed");
		if(!al.myGraph.getV().equals(g.getV()))
			fail("init failed");
	}

	@Test
	public void testCopy() {
		DGraph g = new DGraph();
		graph g2 = new DGraph();
		Vertex v1=new Vertex(new Point3D(50,5,1),1,0);
		Vertex v2=new Vertex(new Point3D(10,80,1),2,0);
		g.addNode(v1);
		Graph_Algo al= new Graph_Algo();
		al.init(g);
		g2 = al.copy();
		g2.addNode(v2);
		g2.connect(v1.getKey(), v2.getKey(), 10);
		g2.connect(v2.getKey(), v1.getKey(), 15);
		if(g2.nodeSize()==al.myGraph.nodeSize())
			fail("copy falied");
	}

	@Test
	public void testInitSave() {
		DGraph g = new DGraph();
		Vertex v1=new Vertex(new Point3D(50,5,1),1,0);
		Vertex v2=new Vertex(new Point3D(10,80,1),2,0);
		g.addNode(v1);
		g.addNode(v2);
		g.connect(v1.getKey(), v2.getKey(), 10);
		g.connect(v2.getKey(), v1.getKey(), 15);
		Graph_Algo al= new Graph_Algo();
		al.init(g);
		al.save("myTest.txt");
		Graph_Algo al2= new Graph_Algo();
		al2.init("myTest.txt");
		if(al.myGraph.nodeSize()!=al2.myGraph.nodeSize())
			fail("init/ save failed");
		if(al.myGraph.edgeSize()!=al2.myGraph.edgeSize())
			fail("init/ save failed");

	}

	@Test
	public void testIsConnected() {
		DGraph g = new DGraph();
		Vertex v1=new Vertex(new Point3D(50,5,1),0,10);
		Vertex v2=new Vertex(new Point3D(10,80,1),1,20);
		Vertex v3=new Vertex(new Point3D(10,20,1),2,30);
		Vertex v4=new Vertex(new Point3D(7,65,1),3,40);
		Vertex v5=new Vertex(new Point3D(80,10,1),4,50);
		g.addNode(v1);
		g.addNode(v2);
		g.addNode(v3);
		g.addNode(v4);
		g.addNode(v5);
		g.connect(v1.getKey(), v2.getKey(), 10);
		g.connect(v2.getKey(), v3.getKey(), 15);
		g.connect(v3.getKey(), v1.getKey(), 20);
		g.connect(v5.getKey(), v4.getKey(), 10);
		g.connect(v4.getKey(), v5.getKey(), 10);
		Graph_Algo al= new Graph_Algo();
		al.init(g);
		if(al.isConnected())
			fail("isConnected failed");
		g.connect(v1.getKey(), v4.getKey(), 10);
		g.connect(v5.getKey(), v2.getKey(), 10);
		al.init(g);
		if(!al.isConnected())
			fail("isConnected failed");
	}

	@Test
	public void testShortestPathDist() {
		DGraph g = new DGraph();
		Vertex v1=new Vertex(new Point3D(50,5,1),0,10);
		Vertex v2=new Vertex(new Point3D(10,80,1),1,20);
		Vertex v3=new Vertex(new Point3D(10,20,1),2,30);
		Vertex v4=new Vertex(new Point3D(7,65,1),3,40);
		g.addNode(v1);
		g.addNode(v2);
		g.addNode(v3);
		g.addNode(v4);
		g.connect(v1.getKey(), v2.getKey(), 30);
		g.connect(v1.getKey(), v3.getKey(), 10);
		g.connect(v3.getKey(), v4.getKey(), 5);
		g.connect(v4.getKey(), v2.getKey(), 7);
		Graph_Algo al= new Graph_Algo();
		al.init(g);
		if(al.shortestPathDist(v1.getKey(), v2.getKey())!=22)
			fail("shortest path failed");
		if(al.shortestPathDist(v2.getKey(), v1.getKey())!=Integer.MAX_VALUE-1)
			fail("shortest path failed"); 
		System.out.println("2 ERROR massages expected:");
		if(al.shortestPathDist(0, 4)!=-1)
			fail("shortest path failed");
		if(al.shortestPathDist(4, 4)!=-1)
			fail("shortest path failed");
		if(al.shortestPathDist(3, 3)!=0)
			fail("shortest path failed");
	}

	@Test
	public void testShortestPath() {
		DGraph g = new DGraph();
		Vertex v1=new Vertex(new Point3D(50,5,1),0,10);
		Vertex v2=new Vertex(new Point3D(10,80,1),1,20);
		Vertex v3=new Vertex(new Point3D(10,20,1),2,30);
		Vertex v4=new Vertex(new Point3D(7,65,1),3,40);
		g.addNode(v1);
		g.addNode(v2);
		g.addNode(v3);
		g.addNode(v4);
		g.connect(v1.getKey(), v2.getKey(), 30);
		g.connect(v1.getKey(), v3.getKey(), 10);
		g.connect(v3.getKey(), v4.getKey(), 5);
		g.connect(v4.getKey(), v2.getKey(), 7);
		Graph_Algo al= new Graph_Algo();
		al.init(g);
		if(al.shortestPath(v1.getKey(), v2.getKey()).size()!=4)//v1 -> v3 -> v4 ->v2
			fail("shortest path failed");
	}

	@Test
	public void testTSP() {
		DGraph g = new DGraph();
		Vertex v1=new Vertex(new Point3D(50,5,1),0,10);
		Vertex v2=new Vertex(new Point3D(10,80,1),1,20);
		Vertex v3=new Vertex(new Point3D(10,20,1),2,30);
		Vertex v4=new Vertex(new Point3D(7,65,1),3,40);
		g.addNode(v1);
		g.addNode(v2);
		g.addNode(v3);
		g.addNode(v4);
		g.connect(v1.getKey(), v2.getKey(), 30);
		g.connect(v1.getKey(), v3.getKey(), 10);
		g.connect(v3.getKey(), v4.getKey(), 5);
		g.connect(v4.getKey(), v2.getKey(), 7);
		Graph_Algo al= new Graph_Algo();
		al.init(g);
		ArrayList<Integer> list= new ArrayList<Integer>();
		list.add(0); list.add(1); list.add(1);list.add(3);list.add(4);list.add(0);list.add(3);
		List<node_data> ans= al.TSP(list);
		String path="";
		for (int i = 0; i < ans.size(); i++) {
			path= path+ans.get(i).getKey()+" ";
		}
		if(!path.equalsIgnoreCase("0 2 3 1 "))
			fail("TSP failed");
	}
}
