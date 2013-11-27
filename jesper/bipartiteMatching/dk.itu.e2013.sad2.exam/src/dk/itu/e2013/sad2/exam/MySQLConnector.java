package dk.itu.e2013.sad2.exam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MySQLConnector {
		
	public Map<Integer, Movie> getData(String connString, float rank) throws SQLException{
		ResultSet rs = null; 
		Map<Integer, Movie> movies = new LinkedHashMap<Integer, Movie>();
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection connect = null;
			connect = DriverManager.getConnection(connString);
			
			PreparedStatement preparedStatement = null;
			preparedStatement = connect.prepareStatement("SELECT movie_id, name, rank, year, actor_id, first_name, last_name, gender, film_count FROM imdb.movies movies LEFT OUTER JOIN imdb.roles roles ON movies.id=roles.movie_id LEFT OUTER JOIN imdb.actors actors ON actors.id=roles.actor_id WHERE rank > ?;");
			preparedStatement.setFloat(1, rank);
			
			rs = preparedStatement.executeQuery();
			
			Movie tmpMov;

			while (rs.next()) {
				if(movies.containsKey(rs.getInt("movie_id")) && !rs.wasNull()){
					tmpMov = movies.get(rs.getInt("movie_id"));
					tmpMov.addActor(rs.getInt("actor_id"));
					
					//todo: add Actor object insted of actor_id
				}
				else{
					
					if(!rs.wasNull()){
						tmpMov = new Movie(rs.getInt("movie_id"), rs.getString("name"), rs.getInt("year"), rs.getFloat("rank"), rs.getInt("actor_id"));
						movies.put(rs.getInt("movie_id"), tmpMov);
					}
				}
			}

			rs.close();
			preparedStatement.close();
			connect.close();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		return movies;
	}
	

}
