package dk.itu.e2013.sad2.exam;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;

public class main {

	private static ResultSet resultSet = null;
	private static Map<Integer, Movie> movies = new HashMap<Integer, Movie>();
	private static final String connString =  "jdbc:mysql://localhost/imdb?user=root&password=tattoo3s";
	//private static Map<Integer, Actor> actors = new HashMap<Integer, Actor>();
	private static ArrayList<Map<Integer, Actor>> ActorList = new ArrayList<>();
	
	public static void main(String[] args) throws Exception {
		MySQLConnector dao = new MySQLConnector();
		Class.forName("com.mysql.jdbc.Driver");
		try{
		    movies = getMoviesByRank(dao, (float) 9.0, connString);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		
		//ActorList.add(getActorsInMovie(dao, 281907, connString));
		for(Entry<Integer, Movie> entry : movies.entrySet()) {	
			Integer key = entry.getKey();
			
//			try{
				ActorList.add(getActorsInMovie(dao, key, connString));
//			}
//			catch(Exception e){
//				System.out.println("inner exception: " + e.getMessage());
//				System.out.println("Exception from movie ID:" + key);
//			}
//			System.out.println("Inner Debug");
	    }
		
		//Close up DataObject
		dao.close();
		dao = null;
		
		System.out.println("DEBUG");
		
	}
	private static Map<Integer, Movie> getMoviesByRank(MySQLConnector dao, float rank, String connString){
		ResultSet rs;
		
		Map<Integer,Movie> moviesByRank = new HashMap<Integer, Movie>();
		try{

		    rs = dao.readMovies(connString, rank);
		    
		    while (rs.next()) {
		    	moviesByRank.put(rs.getInt("id"), new Movie(rs.getInt("id"), 
		    			rs.getString("name"),
		    			rs.getInt("year"),
		    			rs.getFloat("rank")));
		    }
		    rs = null;
		    //dao.close();
		}
		catch(SQLException se){
			System.out.println(se.getMessage());
			System.out.println(se.getSQLState());
			System.out.println(se.getNextException());
			rs = null;
		}
		
	    //for(Entry<Integer, Movie> entry : moviesByRank.entrySet()) {
	    //    Integer key = entry.getKey();
	    //    Movie value = entry.getValue();
	    //    System.out.println("Movie id: " + key);
	    //    System.out.println("Movie name: " + value.getName());
	    //    System.out.println("Movie rank: " + value.getRank() + "\n");
	    //}
		
	    return moviesByRank;
    }
	
	private static Map<Integer, Actor> getActorsInMovie(MySQLConnector dao, int movieId, String connString){
		ResultSet rs;
		Map<Integer, Actor> actors = new HashMap<Integer, Actor>();
		
		try{
			
		    rs = dao.readActors(connString, movieId);
		    if (resultSet!= null) {    
		    	while (rs.next()) {
			    	
			    	//Debugging:
			    	String role = rs.getString("role");
			    	int id = rs.getInt("id");
			    	int film_count = rs.getInt("film_count");
			    	String gender = rs.getString("gender");
			    	String first_name = rs.getString("first_name");
			    	String last_name = rs.getString("last_name");
			    	
			    	if(role == null)
			    		System.out.println("NULL!");
			    	if(id <= 0)
			    		System.out.println("NULL!");
			    	if(film_count <= 0)
			    		System.out.println("NULL!");
			    	if(gender == null)
			    		System.out.println("NULL!");
			    	if(first_name == null)
			    		System.out.println("NULL!");
			    	if(last_name == null)
			    		System.out.println("NULL!");
			    	
			    	
			    	actors.put(rs.getInt("id"), 
			    			new Actor(rs.getInt("id"), 
			    					rs.getInt("film_count"), 
			    					rs.getString("gender"), 
			    					rs.getString("first_name"), 
			    					rs.getString("last_name")));
			    	
		    }
		    }
		    else{
		    	System.out.println("Error: empty result set!");
		    }
//		    rs = null;
		  //  dao.close();
		}
		catch(SQLException se){
			System.out.println(se.getMessage());
			System.out.println(se.getSQLState());
			System.out.println(se.getNextException());
//			rs = null;
		}
		
	    //for(Entry<Integer, Movie> entry : moviesByRank.entrySet()) {
	    //    Integer key = entry.getKey();
	    //    Movie value = entry.getValue();
	    //    System.out.println("Movie id: " + key);
	    //    System.out.println("Movie name: " + value.getName());
	    //    System.out.println("Movie rank: " + value.getRank() + "\n");
	    //}
		
	    return actors;
    }
}
//We could find the maximum pairs that when appearing together have movie ratings above/below x. Maybe the pairs could be man/woman?
//Strategy/algorithm:
//	Build bipartite graph with actors that have gained ratings over x. Then use network flow to find the maximum number of couples.
//	Thoughts:
//	What would journalists benefit from this??