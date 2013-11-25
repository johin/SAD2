package dk.itu;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    Logger lgr = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        // test set cover algorithm
        //Integer[] genres = {1,5,9,2,4,10,34,62,29,50,38};
        //Integer[][] genreSets = {{1,5,9,38},{34,1,9},{4,10,62},{2,29,50},{38,29,50,62,1,5,9},{1,5,34,4,10,2}};

        Main m = new Main();

        Set genres = Imdb.getGenres();
        //Director[] directors = (Director[]) Imdb.getDirectors().values().toArray();
        List<Director> directors = new ArrayList<Director>(Imdb.getDirectors().values());
        //System.out.print(Imdb.getDirectors().values());

        List<Director> result = m.greedySetCover(genres, directors);
        System.out.println("Set(s) covering all genres:");
        for(Director d : result) {
            System.out.println(d.getName().concat(" (").concat(Integer.toString(d.getId())).concat(") ").concat(" : ").concat(d.getGenres().toString()));
        }

    }

    /*private List greedySetCover(Integer[] X, Integer[][] F) {
        // convert arrays to sets
        Set<Integer> x = new HashSet<Integer>(Arrays.asList(X));
        ArrayList<Set<Integer>> f = new ArrayList<Set<Integer>>();
        for(Integer[] S : F) {
            f.add(new HashSet<Integer>(Arrays.asList(S)));
        }
        Logger lgr = Logger.getLogger(Main.class.getName());
        lgr.log(Level.INFO, "Genres are: ".concat(Arrays.toString(x.toArray())));
        lgr.log(Level.INFO, "Genre sets are: ".concat(Arrays.toString(f.toArray())));
        return greedySetCover(x,f);
    }*/

    private List<Director> greedySetCover(Set<String> X, List<Director> F) {
        Set<String> U = X;
        //ArrayList<Set<String>> C = new ArrayList<Set<String>>();
        ArrayList<Director> C = new ArrayList<Director>();
        while(U.size() > 0) {
            // greedy step: maximize union of U and S(in F)
            lgr.log(Level.INFO, "Maximizing; U is now of size: ".concat(Integer.toString(U.size())));
            Set<String> V = new HashSet<String>(U);
            //Set<String> s = null;
            Director d = null;
            for(Director D : F) {
                Set<String> v = new HashSet<String>(U);
                Set S = D.getGenres();
                //lgr.log(Level.INFO, "Missing genres: ".concat(U.toString()));
                //lgr.log(Level.INFO, "Examining genres: ".concat(D.getGenres().toString()));
                v.removeAll(S);
                if(v.size() < V.size()) {
                    V = v;
                    //s = S;
                    d = D;
                }
            }
            // update U, remove genres from U that is in S
            U = V;
            // update C, add genre set
            C.add(d);
        }
        return C;
    }
}
