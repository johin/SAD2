package dk.itu;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: jonashinge
 * Date: 25/11/2013
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
public class Imdb {

    private static Connection con = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    private static String url = "jdbc:mysql://localhost:3306/imdb";
    private static String user = "root";
    private static String password = "geekchamp3!";


    public static Set getGenres() {
        HashSet<String> genres = new HashSet<String>();
        try {
            con = DriverManager.getConnection(url, user, password);
            pst = con.prepareStatement("SELECT DISTINCT genre FROM directors_genres WHERE genre != 'Adult'");
            rs = pst.executeQuery();

            while (rs.next()) {
                //System.out.print(rs.getString(1));
                genres.add(rs.getString(1));
            }

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
        return genres;
    }

    public static HashMap<Integer,Director> getDirectors() {
        HashMap<Integer,Director> dirs = new HashMap<Integer,Director>();

        try {
            con = DriverManager.getConnection(url, user, password);
            pst = con.prepareStatement("SELECT director_id,first_name,last_name,genre FROM directors_genres JOIN directors ON (director_id = directors.id)");
            rs = pst.executeQuery();

            Director dir;
            while (rs.next()) {
                if(dirs.containsKey(rs.getInt(1))) {
                    dir = dirs.get(rs.getInt(1));
                }
                else {
                    dir = new Director();
                    dirs.put(rs.getInt(1), dir);
                }
                dir.setId(rs.getInt(1));
                dir.setName(rs.getString(2).concat(" ").concat(rs.getString(3)));
                dir.addGenre(rs.getString(4));

                //System.out.print(rs.getString(1));
            }

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
        return dirs;
    }
}
