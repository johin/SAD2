package dk.itu.e2013.sad2.exam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.SQLException;
import java.sql.Statement;
//import java.util.Map;
//import java.util.HashMap;

public class MySQLConnector {
	
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	public ResultSet readMovies(String connString, float rank){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(connString);
		      preparedStatement = connect
		          .prepareStatement("SELECT * FROM imdb.movies where rank > ? ; ");
		      preparedStatement.setFloat(1, rank);
		      resultSet = preparedStatement.executeQuery();

			return resultSet;
		}
		catch(Exception ex){
		
			return null;
		}		
	}
	
	public ResultSet readActors(String connString, int movieId){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(connString);
		      preparedStatement = connect
		          .prepareStatement("SELECT id, first_name, last_name, gender, film_count, role FROM imdb.actors actors LEFT OUTER JOIN imdb.roles roles ON actors.id=roles.actor_id WHERE roles.movie_id = ? ; ");
		      preparedStatement.setInt(1, movieId);
		      resultSet = preparedStatement.executeQuery();

			return resultSet;
		}
		catch(Exception ex){
		
			return null;
		}		
	}
	
	 public void close() {
		    try {
		      if (resultSet != null) {
		        resultSet.close();
		      }

		      if (statement != null) {
		        statement.close();
		      }

		      if (connect != null) {
		        connect.close();
		      }
		    } catch (Exception e) {

		    }
		  }
}
