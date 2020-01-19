package gameClient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
/**
 * This class represents a simple example of using MySQL Data-Base.
 * Use this example for writing solution. 
 * @author boaz.benmoshe
 *
 */
public class DBquery {
	public static final String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser="student";
	public static final String jdbcUserPassword="OOP2020student";

	private String id;
	
	public DBquery(String idForInfo) {
		this.id = idForInfo;
	}
	/** simply prints all the games as played by the users (in the database).
	 * 
	 */
	public static void printLog() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			int i = 0;
			while(resultSet.next())
			{
				System.out.println(i++ + ") Id: " + resultSet.getInt("UserID")+","
						+ "Level: "+resultSet.getInt("levelID")+","
								+ "Moves: "+resultSet.getInt("moves")+","
										+ "Time: "+resultSet.getDate("time"));
			}
			resultSet.close();
			statement.close();		
			connection.close();		
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("finish print log ...");
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
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}

	public static int allUsers() {
		int ans = 0;
		String allCustomersQuery = "SELECT * FROM Users;";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()) {
				System.out.println("Id: " + resultSet.getInt("UserID"));
				ans++;
			}
			resultSet.close();
			statement.close();		
			connection.close();
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("finish all users ...");
		return ans;
	}
	private String get1_2InfoById(String id)
	{
		StringBuilder ans = new StringBuilder();
		String allCustomersQuery = "SELECT * FROM Logs where UserID="+id+";";
		int i = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);

			while(resultSet.next())
			{
				ans.append(resultSet.getInt("levelID") + "\n");
				i++;
			}
			resultSet.close();
			statement.close();		
			connection.close();		
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return i + "{" + ans.substring(0) + "}";
	}
	/**
	 * 
	 * @return for the database the next details:
	 * 		1) Number of games
	 *		2) Current level
	 * 		3) Best out come in each scenario
	 */
	public String getInfo() {
		String resultFromTable = this.get1_2InfoById(this.id);
		int NumberOFGames = Integer.parseInt(resultFromTable.substring(0, resultFromTable.indexOf('{')));
		StringBuilder ans = new StringBuilder("1) Number of games are: "+ NumberOFGames + '\n');
		resultFromTable = resultFromTable.substring(resultFromTable.indexOf('{') +1);
		System.out.println(resultFromTable);
		
		int start = 0;
		int current = 0;
		ArrayList<Integer> allLevelInfoById = new ArrayList<Integer>();
		while(resultFromTable.charAt(current) != '}') {
			if(resultFromTable.charAt(current) == '\n') {
				allLevelInfoById.add(Integer.parseInt(resultFromTable.substring(start, current)));
				start = current +1;
			}
			current++;
		}
		ans.append("2) Current level is: "+ Collections.max(allLevelInfoById) + '\n');
		resultFromTable = resultFromTable.substring(0, resultFromTable.indexOf('}'));

		return ans.substring(0);
	}
	
	public String getPosition() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Simple main for demonstrating the use of the Data-base
	 * @param args
	 */
	public static void main(String[] args) {
//		int id1 = 999;  // "dummy existing ID  
//		int level = 0;
//		allUsers();
//		printLog();
//		String kml = getKML(id1,level);
//		System.out.println("***** KML file example: ******");
//		System.out.println(kml);
		
		DBquery q = new DBquery("316334440");
		System.out.println(q.getInfo());		
	}
	
	
}