package dk.itu.e2013.sad2.exam;

import java.util.ArrayList;
import java.util.List;

public class Movie {

	private int id;
	private int year;
	private float rank;
	private String name;
	private List<Integer> actors;
	
	public Movie(){
		this.actors =  new ArrayList<Integer>();
	}
	
	public Movie(int id, String name, int year, float rank, int actor){
		this.setId(id);
		this.setName(name);
		this.setYear(year);
		this.setRank(rank);
		this.actors =  new ArrayList<Integer>();
		this.actors.add(actor);
	}

//	Getter & Setter
	public void addActor(int actor){
		this.actors.add(actor);
	}
	
	public List<Integer> getActors(){
		return actors;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public float getRank() {
		return rank;
	}

	public void setRank(float rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
