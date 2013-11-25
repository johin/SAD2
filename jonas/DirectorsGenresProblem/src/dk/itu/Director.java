package dk.itu;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jonashinge
 * Date: 25/11/2013
 * Time: 16:01
 * To change this template use File | Settings | File Templates.
 */
public class Director {

    private int id;
    private String name;
    private Set genres;

    public Director() {
        genres = new HashSet();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set getGenres() {
        return genres;
    }

    public void addGenre(String genre) {
        genres.add(genre);
    }
}
