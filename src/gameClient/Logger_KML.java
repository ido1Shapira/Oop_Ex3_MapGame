package gameClient;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import myUtils.HelpMe;
import myUtils.myServer;
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
			"      <TimeStamp>\r\n" + 
			"        <when>date</when>\r\n" + 
			"      </TimeStamp>\r\n" + 
			"      <styleUrl>#icon</styleUrl>\r\n" + 
			"      <Point>\r\n" + 
			"        <coordinates>(x,y)</coordinates>\r\n" + 
			"      </Point>\r\n" + 
			"    </Placemark>\n";


	// static variable single_instance of server 
		private static Logger_KML single_instance = null; 

		private static myServer server;


		 
		/**
		 * private constructor restricted to this class itself
		 * building a kml file and insert the standard header kml and the style for every object
		 * @param fileName
		 */
		private Logger_KML(int scenarioNumber) {
			server = myServer.getServer(scenarioNumber);
			this.fileName = scenarioNumber+ ".kml";
			this.content = new StringBuilder(headerFile);
			this.content.append(Logger_KML.appleStyle);
			this.content.append(Logger_KML.bananaStyle);
			this.content.append(Logger_KML.robotStyle);
			Thread kmlThread = new Thread(this);
			kmlThread.start();
		}
		/**
		*  static method to create instance of myServer class 
		*/
		public static Logger_KML getLogger_KML(int scenarioNumber) 
		{ 
			if (single_instance == null) {
				synchronized (Logger_KML.class) {
					if (single_instance == null)
						single_instance = new Logger_KML(scenarioNumber); 
				}
			}
			return single_instance;
		}
	
	
	
		/**
		 * this method updates the data to the current date in a kml format
		 */
	private static void getKmlFormatDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date = new Date();  
		String currentTime = formatter.format(date);
		currentTime = currentTime.replace(" ", "T");
		currentTime += 'Z';
		data = currentTime;
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
	public void addPlaceMark(Point3D p , String icon) {
		String placeMark = Logger_KML.placeMark;
		placeMark = placeMark.replace("(x,y)",p.x()+","+p.y());
		placeMark = placeMark.replace("date", data); //replace date to the current data kml format
		placeMark = placeMark.replace("icon", icon); //replace the word "icon" to specific icon match the current object
		this.content.append(placeMark); //adds placeMark to the logger
	}
	/**
	 * five times in a second this class takes a screenshot of the game converts it to a kml format and adds it the the logger
	 */
	@Override
	public void run() {
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
	private void screenShot() {
		Logger_KML.getKmlFormatDate(); //update current date
		//fruits
		Iterator<String> f_iter = server.game.getFruits().iterator();
		while(f_iter.hasNext()) {
			String f = f_iter.next();
			Point3D p = HelpMe.getFruitPosition(f);
			int type = HelpMe.getFruitType(f);
			if(type == 1)
				this.addPlaceMark(p, "apple");
			else
				this.addPlaceMark(p, "banana");

		}
		//robots
		Iterator<String> r_iter = server.game.getRobots().iterator();
		while(r_iter.hasNext()) {
			Point3D p = HelpMe.getRobotPosition(r_iter.next());
			this.addPlaceMark(p, "robot");
		}

	}

	public static void main(String args[]) throws FileNotFoundException {
		String info = server.game.toString();
		System.out.println(info);
//		Logger_KML kml = new Logger_KML("test");
//		MyGameGUI.buildScenario(); // you have [0,23] games	
//		server.game.startGame();
	}


}