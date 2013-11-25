package dk.itu.e2013.sad2.exam;

public class Actor {

	private String firstName;
	private String lastName;
	private int id;
	private String gender;
	private int film_count;
	
	public Actor(int id, int filmCount, String firstName, String lastName, String gender){
		this.id = id;
		this.film_count = filmCount;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
	}
	
	//Getters & Setters
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getFilm_count() {
		return film_count;
	}
	public void setFilm_count(int film_count) {
		this.film_count = film_count;
	}
	
}
