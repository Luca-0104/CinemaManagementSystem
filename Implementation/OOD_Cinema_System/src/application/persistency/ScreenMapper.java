package application.persistency;

import application.domain.Screen;
import storage.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenMapper {
    // Implementation of hidden cache

    private Map<Integer, PersistentScreen> cache;   //<oid, PersistentScreen>

    /**
     * Search for a specific PersistentScreen object from cache according to the oid of the screen
     * @param oid   the object id of the screen we want
     * @return  an object of PersistentScreen, which wraps the screen object we want
     */
    private PersistentScreen getFromCache(int oid) {
        return cache.get(oid);
    }

    /**
     * Search for a specific PersistentScreen object from cache according to the name of the screen
     * @param name The name of the screen
     * @return an object of PersistentScreen, which wraps the screen object we want
     */
    private PersistentScreen getFromCacheByName(String name) {
        //loop through the cache
        for (PersistentScreen ps : cache.values()) {
            //check whether the name of the screen in cache is same as the name of the one we want (screen name is unique)
            if (ps.getName().equals(name)) {
                return ps;
            }
        }
        return null;
    }

    /**
     * Add the PersistentScreen object into the cache (map)
     * @param ps an object of PersistentScreen class, which wraps a screen object
     */
    private void addToCache(PersistentScreen ps) {
        cache.put(ps.getOid(), ps);
    }

    // Constructor:

    private ScreenMapper() {
        cache = new HashMap<Integer, PersistentScreen>();
        getScreens();   // we do not need the return value here, just use this to initialize the cache at the beginning
    }

    // Singleton:

    private static ScreenMapper uniqueInstance;

    /**
     * This method is used to get the unique instance of ScreenMapper in our system
     * @return  the unique instance of ScreenMapper
     */
    public static ScreenMapper getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ScreenMapper();
        }
        return uniqueInstance;
    }

    /**
     * Get a specific screen by screen name
     * @param name the name of the screen (this is unique)
     * @return a PersistentScreen object that wraps the screen we want
     */
    public PersistentScreen getScreen(String name) {
        //we only need to get the screen from cache
        //This is because, every time we initialize the ScreenMapper, the cache will be synchronized with database.
        PersistentScreen ps = getFromCacheByName(name);
        return ps;
    }

    /**
     * Get a specific screen by its object id
     * @param oid the object id of a specific screen we want
     * @return a PersistentScreen object that wraps the screen we want
     */
    PersistentScreen getScreenForOid(int oid) {
        //we only need to get the screen from cache
        //This is because, every time we initialize the ScreenMapper, the cache will be synchronized with database.
        PersistentScreen ps = getFromCache(oid);
        return ps;
    }

    /**
     * Get all the screen objects in our system
     * @return a list contains all the screen objects in our system
     */
    public List<Screen> getScreens() {
        // if cache is empty, we query a list of screens from the database
        if (cache.size() == 0) {
            List<Screen> screenList = new ArrayList<Screen>();
            try {
                //query all the screens from the database
                Database.getInstance();
                Statement stmt = Database.getConnection().createStatement();
                ResultSet rset = stmt.executeQuery("SELECT * FROM Screens;");
                // loop through all the screens (row)
                while (rset.next()) {
                    // get the info from result, then pack them up into an PersistentScreen object
                    PersistentScreen ps = new PersistentScreen(rset.getInt("oid"), rset.getString("name"), rset.getInt("capacity"));
                    screenList.add(ps);
                    // add this new screen instance into the cache
                    addToCache(ps);
                }
                //close all the resources we have used
                rset.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return screenList;

        // if the cache is not empty, we return the screens in the cache
        } else {
            return new ArrayList<Screen>(cache.values());
        }
    }
}
