package application.persistency;

import application.domain.Movie;
import application.domain.Screen;
import storage.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieMapper {
    // Implementation of hidden cache
    
    private Map<Integer, PersistentMovie> cache;    //<oid, PersistentMovie>

    private PersistentMovie getFromCache(int oid) {
        return cache.get(oid);
    }

    private PersistentMovie getFromCacheByDetails(String title, int runningTime) {
        for (PersistentMovie m : cache.values()) {
            if (title.equals(m.getTitle()) && runningTime == m.getRunningTime()) {
                return m;
            }
        }
        return null;
    }

    private void addToCache(PersistentMovie m) {
        cache.put(m.getOid(), m);
    }

    // Constructor:

    private MovieMapper() {
        cache = new HashMap<Integer, PersistentMovie>();
    }

    // Singleton:

    private static MovieMapper uniqueInstance;

    public static MovieMapper getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new MovieMapper();
        }
        return uniqueInstance;
    }
    
    public List<Movie> getMovies(){
        // if cache is empty, we query a list of movies from the database
        if (cache.size() == 0) {
            System.out.println("---no cache");
            List<Movie> movieList = new ArrayList<Movie>();
            try {
                Database.getInstance();
                Statement stmt = Database.getConnection().createStatement();
                ResultSet rset = stmt.executeQuery("SELECT * FROM Movies;");
                // loop through all the movies (row)
                int i = 1;
                while (rset.next()) {
                    System.out.println(i);
                    i++;
                    // get the info from result, then pack them up into an object
                    PersistentMovie pm = new PersistentMovie(rset.getInt("oid"), rset.getString("title"), rset.getInt("runningTime"), rset.getInt("year"));
                    movieList.add(pm);
                    // add this new movies instance into the cache
                    addToCache(pm);
                }
                rset.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("--- movieList size: " + movieList.size());
            return movieList;

            // if the cache is not empty, we return the movies in the cache
        } else {
            System.out.println("---have cache");
            return new ArrayList<Movie>(cache.values());
        } 
    }

    public PersistentMovie getMovie(String title, int runningTime, int year) {
        //first we try to get the movie from the cache
        PersistentMovie m = getFromCacheByDetails(title, runningTime);

        //if no object in the cache, we try to get it from the database
        if (m == null) {
            m = getMovie("SELECT * FROM Movies WHERE title = '" + title + "' AND runningTime = '" + runningTime + "' AND year = '" + year + "'");

            //if no object in database as well, we just add this new movie
            if (m == null) {
                m = addMovie(title, runningTime, year);
            }

            //after get from db, we add it into cache
            addToCache(m);
        }
        return m;
    }

    PersistentMovie getMovieForOid(int oid) {
        //first we try to get the movie from the cache
        PersistentMovie m = getFromCache(oid);
        //if no object in the cache, we try to get it from the database
        if (m == null) {
            m = getMovie("SELECT * FROM Movies WHERE oid ='" + oid + "'");
            if (m != null) {
                //after get from db, we add it into cache
                addToCache(m);
            }
        }
        return m;
    }

    // Add a movie to the database

    public PersistentMovie addMovie(String title, int runningTime, int year) {
        //first we try to get the movie from the cache, if already in cache it must also be in database, we do not do anything
        PersistentMovie m = getFromCacheByDetails(title, runningTime);

        //if no object in the cache, we insert a new row into the database
        if (m == null) {
            try {
                Database.getInstance();
                Statement stmt = Database.getConnection().createStatement();
                stmt.executeUpdate("INSERT INTO Movies (oid, title, runningTime, year)" + "VALUES ('" + Database.getInstance().getNextMovieId() + "', '" + title + "', '" + runningTime + "', '" + year + "');");
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // get the movie we have just inserted. This will also add it to cache
            m = getMovie(title, runningTime, year);
        }
        return m;
    }

    /**
     * Query a movie from database
     */
    private PersistentMovie getMovie(String sql) {
        PersistentMovie m = null;

        try {
            //query from database
            Database.getInstance();
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            while (rset.next()) {
                // get info from result set
                int oid = rset.getInt("oid");
                String title = rset.getString("title");
                int runningTime = rset.getInt("runningTime");
                int year = rset.getInt("year");
                // pack up info into object
                m = new PersistentMovie(oid, title, runningTime, year);
            }

            rset.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return m;
    }

    public boolean checkExistedMovie(String title, int runningTime, int year){
        String sql = "SELECT * FROM Movies WHERE title = '" + title + "' AND runningTime = " + runningTime + ";";
        Database.getInstance();
        try {
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rset = stmt.executeQuery(sql);
            if(rset.next()){
                return true;
            }
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
