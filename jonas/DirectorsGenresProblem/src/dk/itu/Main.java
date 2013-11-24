package dk.itu;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    Logger lgr = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        String url = "jdbc:mysql://localhost:3306/imdb";
        String user = "root";
        String password = "geekchamp3!";

        try {
            con = DriverManager.getConnection(url, user, password);
            pst = con.prepareStatement("SELECT * FROM directors");
            rs = pst.executeQuery();

            /*while (rs.next()) {
                System.out.print(rs.getInt(1));
                System.out.print(": ");
                System.out.println(rs.getString(2).concat(" ").concat(rs.getString(3)));
            } */

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(Main.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Main.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }

        // test set cover algorithm
        Integer[] genres = {1,5,9,2,4,10,34,62,29,50,38};
        Integer[][] genreSets = {{1,5,9,38},{34,1,9},{4,10,62},{2,29,50},{38,29,50,62,1,5,9},{1,5,34,4,10,2}};

        Main m = new Main();

        List result = m.greedySetCover(genres, genreSets);
        System.out.print(Arrays.toString(result.toArray()));
    }

    private void parseFromDB() {

    }

    private List greedySetCover(Integer[] X, Integer[][] F) {
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
    }

    private List greedySetCover(Set<Integer> X, List<Set<Integer>> F) {
        Set<Integer> U = X;
        ArrayList<Set<Integer>> C = new ArrayList<Set<Integer>>();
        while(U.size() > 0) {
            // greedy step: maximize union of U and S(in F)
            lgr.log(Level.INFO, "Maximizing; U is now of size: ".concat(Integer.toString(U.size())));
            Set<Integer> V = new HashSet<Integer>(U);
            Set<Integer> s = null;
            for(Set<Integer> S : F) {
                Set<Integer> v = new HashSet<Integer>(U);
                v.removeAll(S);
                if(v.size() < V.size()) {
                    V = v;
                    s = S;
                }
            }
            // update U, remove genres from U that is in S
            U = V;
            // update C, add genres from S
            C.add(s);
        }
        return C;
    }
}
