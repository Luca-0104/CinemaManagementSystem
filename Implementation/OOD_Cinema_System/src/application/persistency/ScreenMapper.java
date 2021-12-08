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

    private PersistentScreen getFromCache(int oid) {
        return cache.get(oid);
    }

    private PersistentScreen getFromCacheByName(String name) {
        for (PersistentScreen ps : cache.values()) {
            if (ps.getName().equals(name)) {
                return ps;
            }
        }
        return null;
    }

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

    public static ScreenMapper getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ScreenMapper();
        }
        return uniqueInstance;
    }

    public PersistentScreen getScreen(String name) {
        PersistentScreen ps = getFromCacheByName(name);
        return ps;
    }

    PersistentScreen getScreenForOid(int oid) {
        PersistentScreen ps = getFromCache(oid);
        return ps;
    }

    public List<Screen> getScreens() {
        // if cache is empty, we query a list of screens from the database
        if (cache.size() == 0) {
            List<Screen> screenList = new ArrayList<Screen>();
            try {
                Database.getInstance();
                Statement stmt = Database.getConnection().createStatement();
                ResultSet rset = stmt.executeQuery("SELECT * FROM Screens;");
                // loop through all the screens (row)
                while (rset.next()) {
                    // get the info from result, then pack them up into an object
                    PersistentScreen ps = new PersistentScreen(rset.getInt("oid"), rset.getString("name"), rset.getInt("capacity"));
                    screenList.add(ps);
                    // add this new screen instance into the cache
                    addToCache(ps);
                }
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
