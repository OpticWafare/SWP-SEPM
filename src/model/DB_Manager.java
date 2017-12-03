package model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class DB_Manager {

	private static Connection con;
	private static String pfad = "C:\\Users\\user\\Desktop\\USB Update 15.11.16\\"
			+ "USB Update 14.6.16\\USB Uptate 19.12.15\\USB ganz neu\\workspace\\Poker\\" + "Datenbank.db"; 
	private static PreparedStatement UserErstellen;
	private static PreparedStatement ResultErstellen;
	private static PreparedStatement AusgabeResults;
	
	public static Connection getCon() {
		if(con == null)
		{
			try {
				con = DriverManager.getConnection("jdbc:sqlite:" + pfad);
				UserErstellen = getCon().prepareStatement("insert into User(userName) values (?)");
				ResultErstellen = getCon().prepareStatement("insert into Results values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return con;
	}

	/*public static void createDatabase()
	{
		try {
			Statement stm = getCon().createStatement();
			stm.execute("create database Poker");
			Statement stm1 = getCon().createStatement();
			stm1.execute("use Poker");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	public static void createTables()
	{
		try {
			Statement stm = getCon().createStatement();
			stm.execute("create table if not exists User("
					+ "userID integer primary key autoincrement,"
					+ "userName varchar(50) NOT NULL,"
					+ "userRegistrationDate date NOT NULL default current_timestamp)");
			stm.execute("create table if not exists Results("
					+ "beginDateTimeOfTest datetime,"
					+ "endDateTimeOfTest datetime,"
					+ "userID int NOT NULL,"
					+ "nrPairs int NOT NULL,"
					+ "nrTripples int NOT NULL,"
					+ "nrFourOfaKind int NOT NULL,"
					+ "nrFullHouse int NOT NULL,"
					+ "nrFlush int NOT NULL,"
					+ "nrStraight int NOT NULL,"
					+ "nrStraightFlush int NOT NULL,"
					+ "nrRoyalFlush int NOT NULL,"
					+ "primary key(beginDateTimeOfTest, endDateTimeOfTest)"
					+ "foreign key(userID) references User(userID))");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void showTables()
	{
		try {
			
			DatabaseMetaData md = getCon().getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);
			while(rs.next())
			{
				System.out.println(rs.getString(3));
				DatabaseMetaData md1 = getCon().getMetaData();
				ResultSet rs1 = md1.getColumns(null, null, rs.getString(3), null);
				while(rs1.next())
				{
					System.out.print(rs1.getString("COLUMN_NAME") + " ");
					System.out.println(rs1.getString("TYPE_NAME"));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void UserErstellen(String userName)
	{
		getCon();
		try {			
			UserErstellen.setString(1, userName);
			UserErstellen.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void AddResult(long beginTimeOfTest, long endTimeOfTest, int UserID,  int nrPairs, int nrTripples,
			int nrFourOfaKind, int nrFullHouse, int nrFlush, int nrStraight,  int nrStraightFlush, int nrRoyalFlush)
	{
		getCon();
		try {
			ResultErstellen.setLong(1, beginTimeOfTest);
			ResultErstellen.setLong(2, endTimeOfTest);
			ResultErstellen.setInt(3, UserID);
			ResultErstellen.setInt(4, nrPairs);
			ResultErstellen.setInt(5, nrTripples);
			ResultErstellen.setInt(6, nrFourOfaKind);
			ResultErstellen.setInt(7, nrFullHouse);
			ResultErstellen.setInt(8, nrFlush);
			ResultErstellen.setInt(9, nrStraight);
			ResultErstellen.setInt(10, nrStraightFlush);
			ResultErstellen.setInt(11, nrRoyalFlush);
			
			ResultErstellen.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void AusgabeResults()
	{
		try {
			AusgabeResults = getCon().prepareStatement("Select * from Results");
			
		
			ResultSet rs = AusgabeResults.executeQuery();
			while(rs.next())
			{
				System.out.println("----------------------------------------------------------");
				Timestamp ts = new Timestamp(rs.getLong(1));
				Timestamp ts1 = new Timestamp(rs.getLong(2));
				System.out.println("Beginnzeit: "  + ts.toLocalDateTime());
				System.out.println("Endzeit: " + ts1.toLocalDateTime());
				System.out.println("UserID: " + rs.getInt(3));
				System.out.println("Anzahl der Paare: " + rs.getInt(4));
				System.out.println("Anzahl der Drillinge: " + rs.getInt(5));
				System.out.println("Anzahl der Vierlinge: " + rs.getInt(6));
				System.out.println("Anzahl der FullHouses: " + rs.getInt(7));
				System.out.println("Anzahl der Flushes: " + rs.getInt(8));
				System.out.println("Anzahl der Straights: " + rs.getInt(9));
				System.out.println("Anzahl der StraightFlushes: " + rs.getInt(10));
				System.out.println("Anzahl der RoyalFlushes: " + rs.getInt(11));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//createDatabase();
		createTables();
		showTables();
	}
}