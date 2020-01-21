package gameClient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

/**
 * This class is responsible of the communication with the server's DataBase 
 * in this class every method is in charge of a DB query or organizing information we got from the DB 
 * To shorten the waiting time, the class will keep a copy of the Logs table.
 * This class helps the client watch database information about his grades in the game 
 * and his position in relation to rest of the class
 * @author Ido Shapira & Edut Cohen
 */
public class DBquery {
	public static final String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser="student";
	public static final String jdbcUserPassword="OOP2020student";

	private static final int NumberOfLevels = 23;
	//To shorten the waiting time, the class will keep a copy of the Logs table
	private static ArrayList<ArrayList<String>> logsTable = DBquery.getTable("Logs"); 
	private String id; //id student
	private static enum col{ //all the columns in the Logs table
		levelID, moves, time, score, UserID, logID,
	}

	public DBquery(String idForInfo) {
		this.id = idForInfo;
	}
	/**
	 * in the start this method is activated in order to get a quick access to the table
	 * @param table the name of the table
	 * @return a copy of the table from the server
	 */
	private static ArrayList<ArrayList<String>> getTable(String table) {
		ArrayList<ArrayList<String>> ans = new ArrayList<ArrayList<String>>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM "+table);

			while(resultSet.next())
			{
				ArrayList<String> tmp = new ArrayList<String>();
				tmp.add(""+resultSet.getInt("levelID"));
				tmp.add(""+resultSet.getInt("moves"));
				tmp.add(""+resultSet.getDate("time"));
				tmp.add(""+resultSet.getDouble("score"));
				tmp.add(""+resultSet.getInt("UserID"));
				tmp.add(""+resultSet.getInt("logID"));
				ans.add(tmp);
			}
			resultSet.close();
			statement.close();		
			connection.close();		
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: "  + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}
	/**
	 * this function returns the KML string as stored in the database (userID, level);
	 * @param id
	 * @param level
	 * @return
	 */
	public static String getKML(int id, int level) {
		String ans = null;
		String allCustomersQuery = "SELECT * FROM Users where userID="+id+";";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			if(resultSet!=null && resultSet.next()) {
				ans = resultSet.getString("kml_"+level);
			}
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out   .println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}
	/**
	 * this method get an id and return a list of his best score
	 * if the student has not gotten to a level it return -1 for the specific level.
	 */
	private ArrayList<Double> getBestScoreById(String id){
		ArrayList<Double> ans = new ArrayList<Double>();
		for (int i = 0; i <= NumberOfLevels; i++) {			
			ArrayList<Double> allscoreByIdLevel = new ArrayList<Double>();
			for (Iterator<ArrayList<String>> iterator = logsTable.iterator(); iterator.hasNext();) {
				ArrayList<String> row = (ArrayList<String>) iterator.next();
				if(row.get(col.levelID.ordinal()).equals(""+i) && row.get(col.UserID.ordinal()).equals(id)) {
					allscoreByIdLevel.add(Double.parseDouble(row.get(col.score.ordinal())));
				}
			}
			if(allscoreByIdLevel.size() > 0) {
				ans.add(Collections.max(allscoreByIdLevel));
			}
			else {
				ans.add(-1.0);
			}
			allscoreByIdLevel.clear();
		}
		return ans;
	}

	/**
	 * 
	 * @return for the database the next details:
	 * 		1) Number of games
	 *		2) Current level
	 * 		3) Best out come in each scenario
	 */
	public String getInfo() {
		ArrayList<Integer> allLevelInfoById = new ArrayList<Integer>();
		for (Iterator<ArrayList<String>> iterator = logsTable.iterator(); iterator.hasNext();) {
			ArrayList<String> row = (ArrayList<String>) iterator.next();
			if(row.get(col.UserID.ordinal()).equals(this.id)) {
				allLevelInfoById.add(Integer.parseInt(row.get(col.levelID.ordinal())));
			}
		}
		StringBuilder ans = new StringBuilder("statiscis for: "+this.id+"\n\t1) Number of games are: "+ allLevelInfoById.size() + '\n');

		if(allLevelInfoById.size() > 0) {
			if(allLevelInfoById.contains(23)) {
				ans.append("\t2) Current level is: finish !!!\n");
			}
			else {
				ans.append("\t2) Current level is: "+ (Collections.max(allLevelInfoById)+1) + '\n');
			}
		}
		else {
			ans.append("\t2) Current level is: 0\n");
		}
		int level = 0;
		ArrayList<Double> allBestscoreByIdLevel = this.getBestScoreById(this.id);
		ans.append("\t3) Best out come in each level:\n");
		for (Iterator<Double> iterator = allBestscoreByIdLevel.iterator(); iterator.hasNext();) {
			Double score = (Double) iterator.next();
			if(score != -1) {
				ans.append("\t\tLevel "+level +") best score: "+ score + '\n');
			}
			else {
				ans.append("\t\tLevel "+level +") not yet arrived\n");
			}
			level++;
		}
		return ans.substring(0);
	}
	/**
	 * for each level this method return his position in a relation to the rest of the class
	 */
	public String getPosition() {
		StringBuilder ans = new StringBuilder();
		ArrayList<Double> myBestScoreByLevel = this.getBestScoreById(this.id);
		ans.append("Position of "+this.id+" on each level:\n");
		HashSet<String> users = this.getUsersId();
		ArrayList<ArrayList<Double>> allusersBestScores = new ArrayList<ArrayList<Double>>();
		for (Iterator<String> iterator = users.iterator(); iterator.hasNext();) {
			String id = (String) iterator.next();
			allusersBestScores.add(this.getBestScoreById(id));
		}
		for (int i = 0; i <= NumberOfLevels; i++) {
			int countGreater = 1;
			if(myBestScoreByLevel.get(i) != -1.0) {
				for (Iterator<ArrayList<Double>> iterator = allusersBestScores.iterator(); iterator.hasNext();) {
					ArrayList<Double> allLevelsCurrentPlayer = (ArrayList<Double>) iterator.next();
					Double currentLevel = allLevelsCurrentPlayer.get(i);
					if(currentLevel >= myBestScoreByLevel.get(i)) {
						countGreater++;
					}
				}
				ans.append("\tLevel "+i +") you are in place: "+ countGreater + '\n');
			}
			else { 
				ans.append("\tLevel "+i +") not yet arrived\n");
			}
		}
		return ans.substring(0);
	}
	/**
	 * @return all the user id with no repetition
	 */
	private HashSet<String> getUsersId() {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (Iterator<ArrayList<String>> iterator = logsTable.iterator(); iterator.hasNext();) {
			ArrayList<String> row = (ArrayList<String>) iterator.next();
			temp.add(Integer.parseInt(row.get(col.UserID.ordinal())));
		}
		HashSet<String> ans = new HashSet<String>();
		for (Iterator<Integer> iterator = temp.iterator(); iterator.hasNext();) {
			Integer id = (Integer) iterator.next();
			ans.add(""+id);
		}
		return ans;
	}

	/**
	 * Simple main for demonstrating the use this class
	 * @param args
	 */
	public static void main(String[] args) {
		String id = "205666407";
		DBquery q = new DBquery(id);
		//print the table from the database
		System.out.println("levelID, moves, time, score, UserID, logID"); //the columns
		for (Iterator<ArrayList<String>> iterator = logsTable.iterator(); iterator.hasNext();) {
			ArrayList<String> row = (ArrayList<String>) iterator.next();
			System.out.println(row);
		}
		//print the info of the student with this id
		System.out.println(q.getInfo());
		//print in each level the student's position
		System.out.println(q.getPosition());
	}
}

