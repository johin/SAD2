package dk.itu.e2013.sad2.exam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class main {

	private static final String connString =  "jdbc:mysql://localhost/imdb?user=root&password=tattoo3s";

	
	public static void main(String[] args){
		
		Map<Integer, Movie> movies = new LinkedHashMap<Integer, Movie>();
		
		List<Integer> Edges = new ArrayList<Integer>();
		
		MySQLConnector dao = new MySQLConnector();
		try{
			Class.forName("com.mysql.jdbc.Driver");	
			movies = dao.getData(connString, (float) 9.8);
		}
		catch(Exception e){
			System.out.println("Message: ");
			System.out.println(e.getMessage());
			System.out.println("Stack: ");
			System.out.println(e.getStackTrace());
		}
		
		List<ArrayList<Object>> tmpLst = new ArrayList<ArrayList<Object>>();
		
		//Get List of actors in movie
		for(Entry<Integer, Movie> entry : movies.entrySet()) {
			ArrayList<Object> tmp = new ArrayList<Object>();
			tmp.add(0, entry.getKey());
			tmp.add(1, entry.getValue().getActors());
			tmpLst.add(tmp);
        }
		
		//Actor matching
		for(int i = 1; i < tmpLst.size(); i++){
			int j = i-1;
			ArrayList<Object> mov1 = tmpLst.get(j);
			ArrayList<Object> mov2 = tmpLst.get(i);
			System.out.println("DEBUG");
		}
		
		System.out.println("DEBUG");
			
	}
}
//We could find the maximum pairs that when appearing together have movie ratings above/below x. Maybe the pairs could be man/woman?
//Strategy/algorithm:
//	Build bipartite graph with actors that have gained ratings over x. Then use network flow to find the maximum number of couples.
//	Thoughts:
//	What would journalists benefit from this??