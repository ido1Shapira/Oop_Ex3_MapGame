package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import dataStructure.DGraph;
import myUtils.MyParser;
import myUtils.MyServer;
import utils.Point3D;

class MyParserTest {

	public MyServer s= MyServer.getServer(11);
	public DGraph g= new DGraph();



	@Test
	void testGetRobotId() {
		g.init(MyServer.getServer().game.getGraph());
		MyServer.getServer().game.addRobot(7);
		if(MyParser.getRobotId(MyServer.getServer().game.getRobots().get(0)) != 0 )
			fail("Not yet implemented");
	}

	@Test
	void testGetRobotPosition() {
		g.init(MyServer.getServer().game.getGraph());
		MyServer.getServer().game.addRobot(7);
		Point3D p= MyParser.getRobotPosition(MyServer.getServer().game.getRobots().get(0));
		Point3D p2 = (g.getNode(7).getLocation());
		if(!p.equals(p2))
			fail("Not yet implemented");
	}

	@Test
	void testGetRobotSrc() {
		g.init(MyServer.getServer().game.getGraph());
		MyServer.getServer().game.addRobot(7);
		if(MyParser.getRobotSrc(MyServer.getServer().game.getRobots().get(0)) != 7 )
			fail("Not yet implemented");
	}

	@Test
	void testGetRobotDest() {
		g.init(MyServer.getServer().game.getGraph());
		MyServer.getServer().game.addRobot(7);
		if(MyParser.getRobotDest(MyServer.getServer().game.getRobots().get(0)) !=  -1)
			fail("Not yet implemented");
	}

	@Test
	void testGetRobotValue() {		
		g.init(MyServer.getServer().game.getGraph());
		MyServer.getServer().game.addRobot(7);
		if(MyParser.getRobotValue(MyServer.getServer().game.getRobots().get(0)) !=  0)
			fail("Not yet implemented");
	}

	@Test
	void testGetRobotSpeed() {
		g.init(MyServer.getServer().game.getGraph());
		MyServer.getServer().game.addRobot(7);
		if(MyParser.getRobotSpeed(MyServer.getServer().game.getRobots().get(0)) !=  1)
			fail("Not yet implemented");
	}

	@Test
	void testGetFruitValue() {
		if(MyParser.getFruitValue(MyServer.getServer().game.getFruits().get(0)) !=  5)
			fail("Not yet implemented");
	}

	@Test
	void testGetFruitType() {
		if(MyParser.getFruitType(MyServer.getServer().game.getFruits().get(0)) !=  -1)
			fail("Not yet implemented");
	}

	@Test
	void testGetFruitPosition() {
		Point3D f= new Point3D("35.20273974670703,32.10439601193746,0.0");
		Point3D f2 = MyParser.getFruitPosition(MyServer.getServer().game.getFruits().get(0));
		if(!f.equals(f2) )
			fail("Not yet implemented");
	}

}
