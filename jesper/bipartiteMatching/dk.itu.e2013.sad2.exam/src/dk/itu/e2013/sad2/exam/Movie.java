package dk.itu.e2013.sad2.exam;

public class Movie {

	private int id;
	private int year;
	private float rank;
	private String name;
	
	public Movie(int id, String name, int year, float rank){
		this.id = id;
		this.name = name;
		this.year = year;
		this.rank = rank;
	}

	//Getters
	public int getId() {
		return id;
	}

	public int getYear() {
		return year;
	}

	public float getRank() {
		return rank;
	}

	public String getName() {
		return name;
	}

}
