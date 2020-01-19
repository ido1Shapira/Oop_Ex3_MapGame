package gameClient;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import myUtils.MyParser;
import myUtils.MyServer;
import utils.Point3D;

/**
 * this class represent a kml convertor of this game.
 * during the game this class takes a screenshot and converts it to a kml format.
 * each object has his own icon
 */
public class Logger_KML implements Runnable{

	private static String data;
	private String fileName;
	private StringBuilder content;
	private MyServer server;

	//header file is the opening of the file required for every kml file
	private static final String headerFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
			"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" + 
			"  <Document>\r\n" + 
			"    <name>Matala 3</name>\n"
			+ "<Style id=\"check-hide-children\">" + 
			"      <ListStyle>\r\n" + 
			"        <listItemType>checkHideChildren</listItemType>\r\n" + 
			"      </ListStyle>\r\n" + 
			"    </Style>\r\n" + 
			"\r\n" + 
			"    <styleUrl>#check-hide-children</styleUrl>";
	//footer file is the ending of the file required for every kml file
	private static final String footerFile = "</Document>\r\n" + 
			"</kml>";
	// kml string representing an apple object
	private static final String appleStyle = " <Style id=\"apple\">\r\n" + 
			"     <IconStyle>\r\n" + 
			"    <scale>1.0</scale>\r\n" + 
			"       <Icon>\r\n" + 
			"         <href>apple.jpeg</href>\r\n" + 
			"       </Icon>\r\n" + 
			"     </IconStyle>\r\n" + 
			"   </Style>\n";
	// kml string representing a banana object
	private static final String bananaStyle = "<Style id=\"banana\">\r\n" + 
			"  <IconStyle>\r\n" + 
			"    <scale>1.0</scale>\r\n" + 
			"    <Icon>\r\n" + 
			"      <href>banana.jpeg</href>\r\n" + 
			"    </Icon>\r\n" + 
			"  </IconStyle>\r\n" + 
			"</Style>\n";
	// kml string representing a robot object
	private static final String robotStyle = "<Style id=\"robot\">\r\n" + 
			"  <IconStyle>\r\n" + 
			"    <scale>1.0</scale>\r\n" + 
			"    <Icon>\r\n" + 
			"      <href>robot.png</href>\r\n" + 
			"    </Icon>\r\n" + 
			"  </IconStyle>\r\n" + 
			"</Style>\n";
	
	// kml string representing a place mark and a time stamp 
	private static final String placeMark = "<Placemark>\r\n" + 
			" <TimeStamp>\r\n" + 
			"          <when>date</when>\r\n" + 
			"        </TimeStamp>" +
			"      <styleUrl>#icon</styleUrl>\r\n" + 
			"      <Point>\r\n" + 
			"        <coordinates>(x,y)</coordinates>\r\n" + 
			"      </Point>\r\n" + 
			"    </Placemark>\n";
	
	private static final String path = "  <Placemark>\r\n" + 
			"	   		<LineString>\r\n" + 
			"	   			<coordinates>\r\n" + 
			"	   				(x1,y1),0\n\r(x2,y2),0\r\n" + 
			"	   			</coordinates>\r\n" + 
			"	   		</LineString>\r\n" + 
			" 			<Style> \r\n" + 
			"  				<LineStyle>  \r\n" + 
			"   				<color>#ff00aaff</color>\r\n" + 
			"   				<width>3</width>\r\n" + 
			"  				</LineStyle> \r\n" + 
			" 			</Style>" +
			"	   	</Placemark>";
	
		 
		/**
		 * private constructor restricted to this class itself
		 * building a kml file and insert the standard header kml and the style for every object
		 * @param fileName
		 */
		public Logger_KML(int scenarioNumber) {
			server = MyServer.getServer(scenarioNumber);
			server.game.startGame();
			this.fileName = scenarioNumber+ ".kml";
			this.content = new StringBuilder(headerFile);
			this.content.append(Logger_KML.appleStyle);
			this.content.append(Logger_KML.bananaStyle);
			this.content.append(Logger_KML.robotStyle);
			
		}
	
		/**
		 * this method updates the data to the current date in a kml format
		 */
	private static void getKmlFormatDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Date date = new Date();  
//		String currentTime = formatter.format(date);
//		currentTime = currentTime.replace(" ", "T");
//		currentTime += 'Z';
//		data = currentTime;
		data = formatter.format(date);
	}
	/**
	 * Writes the log of the game in a KML format to a file 
	 * this method takes the string that the class built during the game and adds it to the kml file.
	 * it also adds the footer of the kml file
	 */
	public boolean writeToFile(){
		try {
			FileWriter fileWriter = new FileWriter(this.fileName, true);
			PrintWriter pw = new PrintWriter(fileWriter);
			pw.println(this.content); //content = header+ game screenshots
			pw.println(footerFile);
			pw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * add a place mark to the kml file
	 * @param p x and y coordinate of the place mark
	 * @icon to pick the place mark style
	 */
	private void addPlaceMark(Point3D p , String icon) {
		String placeMark = Logger_KML.placeMark;
		placeMark = placeMark.replace("(x,y)",p.x()+","+p.y());
		placeMark = placeMark.replace("date", data); //replace date to the current data kml format
//		Logger_KML.getKmlFormatDate();
//		placeMark = placeMark.replace("dateEnd", data); //replace date to the current data kml format
		placeMark = placeMark.replace("icon", icon); //replace the word "icon" to specific icon match the current object
		this.content.append(placeMark); //adds placeMark to the logger
	}
	/**
	 * add a path to the kml file
	 * @param p1 p2 points that represent the path
	 */
	private void addPath(Point3D p1, Point3D p2) {
		String path = Logger_KML.path;
		path = path.replace("(x1,y1)",p1.x()+","+p1.y());
		path = path.replace("(x2,y2)",p2.x()+","+p2.y());
		this.content.append(path); //adds path to the logger
	}
	
	
	
	/**
	 * five times in a second this class takes a screenshot of the game converts it to a kml format and adds it the the logger
	 */
	@Override
	public void run() {
		String jDgraph = this.server.game.getGraph();
		DGraph dg = new DGraph();
		dg.init(jDgraph);
		for (Iterator<node_data> iterator = dg.getV().iterator(); iterator.hasNext();) {
			node_data v = (node_data) iterator.next();
			for (Iterator<edge_data> iterator2 = dg.getE(v.getKey()).iterator(); iterator2.hasNext();) {
				edge_data e = (edge_data) iterator2.next();
				this.addPath(dg.getNode(e.getSrc()).getLocation(),dg.getNode(e.getDest()).getLocation());
			}
		}
		while(server.game.isRunning()) {
			this.screenShot();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		//this.writeToFile();
	}
	/**
	 * convert the positions of all the robots and fruits to a kml format and adds it to the logger
	 */
	public void screenShot() {
		Logger_KML.getKmlFormatDate(); //update current date
		//fruits
		Iterator<String> f_iter = server.game.getFruits().iterator();
		while(f_iter.hasNext()) {
			String f = f_iter.next();
			Point3D p = MyParser.getFruitPosition(f);
			int type = MyParser.getFruitType(f);
			if(type == 1)
				this.addPlaceMark(p, "apple");
			else
				this.addPlaceMark(p, "banana");

		}
		//robots
		Iterator<String> r_iter = server.game.getRobots().iterator();
		while(r_iter.hasNext()) {
			Point3D p = MyParser.getRobotPosition(r_iter.next());
			this.addPlaceMark(p, "robot");
		}

	}

	public static void main(String args[]) {
//		Logger_KML kml = new Logger_KML();
//		kml.addPath(new Point3D("35.19597880064568,32.10154696638656,0"), new Point3D("35.19597880064578,32.10154696638666,0"));
//		System.out.println(kml.content);
//		System.out.println(kml.footerFile);
		
	}


}