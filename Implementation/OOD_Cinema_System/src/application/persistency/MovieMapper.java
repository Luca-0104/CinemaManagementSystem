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

    /**
     * Search for a specific PersistentMovie object from cache according to the oid of the movie
     * @param oid   the object id of the movie we want
     * @return  an object of PersistentMovie, which wraps the movie object we want
     */
    private PersistentMovie getFromCache(int oid) {
        return cache.get(oid);
    }

    /**
     * Search for a specific PersistentMovie object from cache according to the detailed info of the movie
     * @param title The title of the movie
     * @param runningTime The running time of the movie
     * @return an object of PersistentMovie, which wraps the movie object we want
     */
    private PersistentMovie getFromCacheByDetails(String title, int runningTime) {
        //loop through the cache
        for (PersistentMovie m : cache.values()) {
            //check whether the movie in cache is we want, according to the detailed info of this movie
            if (title.equals(m.getTitle()) && runningTime == m.getRunningTime()) {
                return m;
            }
        }
        return null;
    }

    /**
     * Add the PersistentMovie object into the cache (map)
     * @param m an object of PersistentMovie class, which wraps a movie object
     */
    private void addToCache(PersistentMovie m) {
        cache.put(m.getOid(), m);
    }

    // Constructor:

    private MovieMapper() {
        //initialize the cache
        cache = new HashMap<Integer, PersistentMovie>();

        //initialize the cache (This is because, we have some predefined movies added to db using script)
        //Therefore, we need to add all the predefined movies into the cache when initializing the MovieMapper
        initPredefinedMovies();
    }

    // Singleton:

    private static MovieMapper uniqueInstance;

    /**
     * This method is used to get the unique instance of MovieMapper in our system
     * @return  the unique instance of MovieMapper
     */
    public static MovieMapper getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new MovieMapper();
        }
        return uniqueInstance;
    }

    /**
     * Get all the movies in our system.
     * @return  a list contains all the Movie objects in our system
     */
    public List<Movie> getMovies(){
        // if cache is empty, we query a list of movies from the database
        if (cache.size() == 0) {
            List<Movie> movieList = new ArrayList<Movie>();
            try {
                //query all the movies in the database
                Database.getInstance();
                Statement stmt = Database.getConnection().createStatement();
                ResultSet rset = stmt.executeQuery("SELECT * FROM Movies;");
                // loop through all the movies (row)
                while (rset.next()) {
                    // get the info from result, then pack them up into an PersistentMovie object
                    PersistentMovie pm = new PersistentMovie(rset.getInt("oid"), rset.getString("title"), rset.getInt("runningTime"), rset.getInt("year"));
                    movieList.add(pm);
                    // add this new movie object into the cache
                    addToCache(pm);
                }
                //close all the resources we have used
                rset.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return movieList;

        } else {
            // if the cache is not empty, we do not need to query from the database, just return the movies in the cache
            return new ArrayList<Movie>(cache.values());
        } 
    }

    /**
     * Get a specific movie by using the detailed infos of this movie
     * @param title The title of the movie
     * @param runningTime   The running time of the movie
     * @param year  The year of this movie
     * @return  An PersistentMovie object that wraps the movie we want
     */
    public PersistentMovie getMovie(String title, int runningTime, int year) {
        //first we try to get the movie from the cache
        PersistentMovie m = getFromCacheByDetails(title, runningTime);

        //if no object in the cache, we try to get it from the database
        if (m == null) {
            m = getMovie("SELECT * FROM Movies WHERE title = '" + title + "' AND runningTime = '" + runningTime + "' AND year = '" + year + "'");

            //if no object in database as well, we just add this new movie into database
            if (m == null) {
                m = addMovie(title, runningTime, year);
            }

            //after getting movie from db, we add it into cache
            addToCache(m);
        }
        return m;
    }

    /**
     * Get a specific movie by its objet id
     * @param oid the object id of the movie we want
     * @return  a PersistentMovie object, which wraps the Movie we want
     */
    PersistentMovie getMovieForOid(int oid) {
        //first we try to get the movie from the cache
        PersistentMovie m = getFromCache(oid);
        //if no such movie object in the cache, we try to get it from the database
        if (m == null) {
            //query the movie from database
            m = getMovie("SELECT * FROM Movies WHERE oid ='" + oid + "'");
            if (m != null) {
                //after get from db, we add it into cache
                addToCache(m);
            }
        }
        return m;
    }

    /**
     * Add a new movie into our database
     * @param title the title of this movie
     * @param runningTime the running time of this movie
     * @param year the year of this movie
     * @return  A PersistentMovie object that wraps the movie we just added
     */
    public PersistentMovie addMovie(String title, int runningTime, int year) {
        //first we try to get the movie from the cache, if already in cache it must also be in database therefore, we do not need to do anything
        PersistentMovie m = getFromCacheByDetails(title, runningTime);

        //if no object in the cache, we insert a new row into the database
        if (m == null) {
            try {
                //execute the sql update command to insert this new movie into database
                Database.getInstance();
                Statement stmt = Database.getConnection().createStatement();
                stmt.executeUpdate("INSERT INTO Movies (oid, title, runningTime, year)" + "VALUES ('" + Database.getInstance().getNextMovieId() + "', '" + title + "', '" + runningTime + "', '" + year + "');");

                //close the resources we have used
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // get the movie we have just inserted. This operation will also add it to cache
            m = getMovie(title, runningTime, year);
        }
        return m;
    }

    /**
     * Query a movie from database using sql string
     * @param sql   a String of sql command
     * @return  a Persistent Movie object that wraps the movie we want
     */
    private PersistentMovie getMovie(String sql) {
        PersistentMovie m = null;

        try {
            //execute the sql query command from database
            Database.getInstance();
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rset = stmt.executeQuery(sql);

            while (rset.next()) {

                // get the infos of this movie from result set
                int oid = rset.getInt("oid");
                String title = rset.getString("title");
                int runningTime = rset.getInt("runningTime");
                int year = rset.getInt("year");

                // pack up info into PersistentMovie object
                m = new PersistentMovie(oid, title, runningTime, year);

            }

            //close all the resources we have used
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return m;
    }

    /**
     * This method checks the movie table in database to see whether this movie has already existed in the database.
     * Only when each one of [title, runningTime, year] are same, two movies will be considered as a same one.
     *
     * @param title The title of the movie that should be checked
     * @param runningTime The running time of the movie that should be checked
     * @param year The year of the movie that should be checked
     * @return true refers to there is a same movie has already been added in database, false refers to there is no same movie in database
     */
    public boolean checkExistedMovie(String title, int runningTime, int year){
        //the sql command for selecting the movie from database
        String sql = "SELECT * FROM Movies WHERE title = '" + title + "' AND runningTime = " + runningTime + ";";
        Database.getInstance();
        try {
            //execute the query
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rset = stmt.executeQuery(sql);
            //if there is a result, this means a same movie has already been added in database
            if(rset.next()){
                return true;
            }
            //close all the resources we have used
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //if we get here, this means no same movie in database
        return false;
    }

    /**
     * initialize the cache (This is because, we have some predefined movies added to db using script)
     * We need to add all the predefined movies into the cache at the beginning
     */
    private void initPredefinedMovies(){
        try {
            //select all the predefined movies from the Database
            Database.getInstance();
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rset = stmt.executeQuery("SELECT * FROM Movies;");
            // loop through all the movies (row)
            while (rset.next()) {
                // get the infos from result, then pack them up into an PersistentMovie object
                PersistentMovie pm = new PersistentMovie(rset.getInt("oid"), rset.getString("title"), rset.getInt("runningTime"), rset.getInt("year"));
                // add this new movies instance into the cache
                addToCache(pm);
            }
            //close all the resources we have used
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
