package application.persistency;

import storage.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
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

    PersistentMovie addMovie(String title, int runningTime, int year) {
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
                // pack up info into object
                m = new PersistentMovie(oid, title, runningTime);
            }

            rset.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return m;
    }

}
