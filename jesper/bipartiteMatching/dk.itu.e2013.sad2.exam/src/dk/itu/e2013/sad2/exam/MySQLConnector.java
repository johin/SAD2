package dk.itu.e2013.sad2.exam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.HashMap;

public class MySQLConnector {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedstatement = null;
	private ResultSet resultSet = null;
	private Map<Integer , Actor> maleActors;
	private Map<Integer , Actor> femaleActors;
	
	public void readDatabase() throws Exception {
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			connect = DriverManager.getConnection("jdbc:mysql://localhost/imdb?"
					+ "user=root&password=tattoo3s");
			
			statement = connect.createStatement();
			
			resultSet = statement.executeQuery("Select * from actors where gender like 'M'");
			
			maleActors = writeResultSet(resultSet);
			
			//resultSet = null;
			
			resultSet = statement.executeQuery("Select * from actors where gender like 'F'");
			
			femaleActors = writeResultSet(resultSet);
			
			System.out.println("DEUG");
			
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	 private Map<Integer, Actor> writeResultSet(ResultSet resultSet) throws SQLException {
		 Map<Integer, Actor> destMap = new HashMap<Integer, Actor>();
		 while (resultSet.next()) {
			 destMap.put(resultSet.getInt("id"), new Actor(resultSet.getInt("id"), resultSet.getInt("film_count"), resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("gender")));
			 //int id = resultSet.getInt("id");
			 //String firstName = resultSet.getString("first_name");
			 //String lastName = resultSet.getString("last_name");
			 //String gender = resultSet.getString("gender");
			 //int filmCount = resultSet.getInt("film_count");
			 //System.out.println("first name: " + firstName);
			 //System.out.println("last name: " + lastName);
			 //System.out.println("gender: " + gender);
			 //System.out.println("id: " + id);
			 //System.out.println("film count: " + filmCount);
		 }
		 
		 return destMap;
	 }
}
