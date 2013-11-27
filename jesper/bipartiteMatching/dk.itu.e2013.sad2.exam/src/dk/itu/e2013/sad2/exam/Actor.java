package dk.itu.e2013.sad2.exam;

public class Actor {

	private int id;
	private int film_count;
	private String gender;
	private String firstName;
	private String lastName;
	private String role;
	
	
	
	public Actor(int id, int filmCount, String gender, String firstName, String lastName){
		this.id = id;
		this.gender = gender;
		this.film_count = filmCount;
		this.firstName = firstName;
		this.lastName = lastName;
//		this.role = role;
	}
	
	//Getters
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public int getId() {
		return id;
	}
	
	public String getGender() {
		return gender;
	}
	
	public int getFilm_count() {
		return film_count;
	}
	
	public String getRole() {
		return role;
	}
	
}
