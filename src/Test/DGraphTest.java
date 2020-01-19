package Test;

import static org.junit.Assert.*;

import org.junit.Test;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Edge;
import dataStructure.Vertex;
import dataStructure.edge_data;
import utils.Point3D;

public class DGraphTest {


	public static DGraph g = new DGraph();
	public final static int numberOfVertexs = 100;
	public final static int numberOfEdge = 1000;
	int i,j;
//	@Test
	public void testDGraph() {
		long startTime = System.currentTimeMillis();
		DGraph g = new DGraph();
		Point3D p= new Point3D(0,0);
		for(int l = 1; l<=numberOfVertexs;l++) {
			g.addNode (new Vertex(p,0));
		}
		for( i = 1; i<=numberOfVertexs;i++) {
			if(i<numberOfVertexs-10) {
				for(j=1; j<11; j++) 			
					g.connect(i, i+j, 10);
				}
			else {
				for(j=1; j<11; j++) 
					g.connect(i, i-j, 10);
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("That took " + (endTime - startTime) + " milliseconds");
		System.out.println("number of edges= "+g.edgeSize());
		System.out.println("number of nodes= "+g.nodeSize());
	}

	@Test
	public void testAddNode() {
		DGraph dg= new DGraph();
		Vertex v1=new Vertex(new Point3D(50,5,1),0,10);
		Vertex v2=new Vertex(new Point3D(10,80,1),1,20);
		Vertex v3=new Vertex(new Point3D(10,20,1),2,30);
		Vertex v4=new Vertex(new Point3D(7,65,1),3,40);
		Vertex v5=new Vertex(new Point3D(80,10,1),4,50);
		dg.addNode(v1);
		dg.addNode(v2);
		dg.addNode(v3);
		dg.addNode(v4);
		if(dg.nodeSize()!=4)
			fail("add node failed");
		dg.addNode(v5);
		if(dg.nodeSize()!=5)
			fail("add node failed");
		dg.removeNode(4);
		if(dg.nodeSize()!=4)
			fail("add node failed");
		dg.addNode(v1);
		if(dg.nodeSize()!=5)
			fail("add node failed");
	}
	//@Test
	public void testConnect() {
		DGraph dg= new DGraph();
		Vertex v1=new Vertex(new Point3D(50,5,1),0,10);
		Vertex v2=new Vertex(new Point3D(10,80,1),1,20);
		Vertex v3=new Vertex(new Point3D(10,20,1),2,30);
		dg.addNode(v1);
		dg.addNode(v2);
		dg.addNode(v3);
		dg.connect(v1.getKey(), v2.getKey(), 10);
		dg.connect(v2.getKey(), v3.getKey(), 15);
		edge_data e12=dg.getEdge(0, 1);
		if(e12.getWeight()!=10)
			fail("testConnect failed");
		dg.removeEdge(0, 1);
		e12=dg.getEdge(0, 1);
		if(e12!=null)
			fail("testConnect failed");
	}

	@Test
	public void testIsConnected() {
		DGraph dg= new DGraph();
		Vertex v1=new Vertex(new Point3D(50,5,1),0,10);
		Vertex v2=new Vertex(new Point3D(10,80,1),1,20);
		Vertex v3=new Vertex(new Point3D(10,20,1),2,30);
		Vertex v4=new Vertex(new Point3D(7,65,1),3,40);
		Vertex v5=new Vertex(new Point3D(80,10,1),4,50);
		dg.addNode(v1);
		dg.addNode(v2);
		dg.addNode(v3);
		dg.addNode(v4);
		dg.addNode(v5);
		dg.connect(v1.getKey(), v2.getKey(), 10);
		dg.connect(v2.getKey(), v3.getKey(), 15);
		dg.connect(v3.getKey(), v4.getKey(), 20);
		dg.connect(v4.getKey(), v1.getKey(), 10);
		dg.connect(v5.getKey(), v1.getKey(), 10);
		Graph_Algo al= new Graph_Algo();
		al.init(dg);
		if(al.isConnected()) {
			fail("isConnected failed");
		}
		dg.connect(v4.getKey(), v5.getKey(), 10);
		al.init(dg);
		if(!al.isConnected()) {
			fail("isConnected failed");
		}
	}

	@Test
	public void testGetV() {
		DGraph dg= new DGraph();
		Vertex v1=new Vertex(new Point3D(50,5,1),1,10);
		Vertex v2=new Vertex(new Point3D(10,80,1),2,20);
		Vertex v3=new Vertex(new Point3D(10,20,1),3,30);
		Vertex v4=new Vertex(new Point3D(7,65,1),4,40);
		Vertex v5=new Vertex(new Point3D(80,10,1),5,50);
		Vertex v6=new Vertex(new Point3D(4, 6),6,34);
		dg.addNode(v1);
		dg.addNode(v2);
		dg.addNode(v3);
		dg.addNode(v4);
		dg.addNode(v5);
		if(dg.getV().size()!=5)
			fail("getV failed");
		dg.addNode(v6);
		if(dg.getV().size()!=6)
			fail("getV failed");

	}


	@Test
	public void testRemoveNode() {
		DGraph dg= new DGraph();
		Vertex v1=new Vertex(new Point3D(50,5,1),1,10);
		Vertex v2=new Vertex(new Point3D(10,80,1),2,20);
		Vertex v3=new Vertex(new Point3D(10,20,1),3,30);
		Vertex v4=new Vertex(new Point3D(7,65,1),4,40);
		Vertex v5=new Vertex(new Point3D(80,10,1),5,50);
		dg.addNode(v1);
		dg.addNode(v2);
		dg.addNode(v3);
		dg.addNode(v4);
		dg.addNode(v5);
		dg.connect(v1.getKey(), v2.getKey(), 10);
		dg.connect(v2.getKey(), v3.getKey(), 15);
		dg.connect(v3.getKey(), v4.getKey(), 20);
		dg.connect(v4.getKey(), v1.getKey(), 10);
		dg.connect(v5.getKey(), v1.getKey(), 10);
		dg.removeNode(1);
		if(dg.nodeSize()!=4)
			fail("remove node failed");
		if(dg.edgeSize()!=2)
			fail("remove node failed");
	}

	@Test
	public void testRemoveEdge() {
		DGraph dg= new DGraph();
		Vertex v1=new Vertex(new Point3D(50,5,1),0,0);
		Vertex v2=new Vertex(new Point3D(10,80,1),1,0);
		dg.addNode(v1);
		dg.addNode(v2);
		dg.connect(v1.getKey(), v2.getKey(), 10);
		dg.connect(v2.getKey(), v1.getKey(), 10);
		dg.removeEdge(0, 1);
		if(dg.edgeSize()!=1)
			fail("fail remove edge");
		dg.removeEdge(1, 0);
		if(dg.edgeSize()!=0)
			fail("fail remove edge");
	}


	@Test
	public void testEdgeSize() {
		DGraph dg= new DGraph();
		Vertex v1=new Vertex(new Point3D(50,5,1),0,10);
		Vertex v2=new Vertex(new Point3D(10,80,1),1,20);
		Vertex v3=new Vertex(new Point3D(10,80,1),2,54);
		dg.addNode(v1);
		dg.addNode(v2);
		dg.addNode(v3);
		dg.connect(v1.getKey(), v2.getKey(), 10);
		dg.connect(v1.getKey(), v3.getKey(), 10);
		dg.connect(v2.getKey(), v3.getKey(), 10);
		dg.connect(v3.getKey(), v1.getKey(), 10);
		if(dg.edgeSize()!=4)
			fail("fail edgesize");
		dg.removeEdge(v1.getKey(), v2.getKey());
		if(dg.edgeSize()!=3)
			fail("fail edgesize");
		dg.removeEdge(v2.getKey(), v3.getKey());
		if(dg.edgeSize()!=2)
			fail("fail edgesize");
		dg.removeEdge(v1.getKey(), v3.getKey());
		if(dg.edgeSize()!=1)
			fail("fail edgesize");
	}

	@Test
	public void testGetNode() {
		DGraph dg= new DGraph();
		Vertex v1=new Vertex(new Point3D(50,5,1),0,10);
		Vertex v2=new Vertex(new Point3D(10,80,1),1,20);
		Vertex v3=new Vertex(new Point3D(10,80,1),2,54);
		dg.addNode(v1);
		dg.addNode(v2);
		dg.addNode(v3);
		if(!dg.getNode(0).getLocation().equalsXY(v1.getLocation()))
			fail("get node failed");
	}

	@Test
	public void testGetEdge() {
		DGraph dg= new DGraph();
		Vertex v1=new Vertex(new Point3D(50,5,1),0,10);
		Vertex v2=new Vertex(new Point3D(10,80,1),1,20);
		dg.addNode(v1);
		dg.addNode(v2);
		Edge e=new Edge(0, 1, 10);
		dg.connect(v1.getKey(), v2.getKey(), 10);
		if(e.getWeight()!=dg.getEdge(0, 1).getWeight())
			fail("get edge failed");
	}

	@Test
	public void testGetE() {
		DGraph dg= new DGraph();
		Vertex v1=new Vertex(new Point3D(50,5,1),1,10);
		Vertex v2=new Vertex(new Point3D(10,80,1),2,20);
		Vertex v3=new Vertex(new Point3D(10,80,1),3,54);
		dg.addNode(v1);
		dg.addNode(v2);
		dg.addNode(v3);
		dg.connect(v1.getKey(), v2.getKey(), 10);
		dg.connect(v1.getKey(), v3.getKey(), 10);
		dg.connect(v2.getKey(), v3.getKey(), 10);
		dg.connect(v3.getKey(), v1.getKey(), 10);		
		if(!dg.getE(v1.getKey()).contains(dg.getEdge(1, 2)))
			fail("test getE failed");
	}
	@Test
	public void testGetMC() {
		DGraph dg= new DGraph();
		Vertex v1=new Vertex(new Point3D(50,5,1),1,10);
		Vertex v2=new Vertex(new Point3D(10,80,1),2,20);
		Vertex v3=new Vertex(new Point3D(10,80,1),3,54);
		dg.addNode(v1);
		dg.addNode(v2);
		dg.addNode(v3);
		dg.connect(v1.getKey(), v2.getKey(), 10);
		dg.connect(v1.getKey(), v3.getKey(), 10);
		dg.connect(v2.getKey(), v3.getKey(), 10);
		dg.connect(v3.getKey(), v1.getKey(), 10);	
		int oldMC= dg.getMC();
		dg.removeNode(1);
		if(dg.getMC()==oldMC)
			fail("get MC failed");
	}

}