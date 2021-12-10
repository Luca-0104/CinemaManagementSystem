package application.persistency;


import application.domain.Movie;
import application.domain.Screen;
import application.domain.Screening;
import storage.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ScreeningMapper {
    // Singleton:

    private static ScreeningMapper uniqueInstance;

    /**
     * This method is used to get the unique instance of ScreeningMapper in our system
     * @return  the unique instance of ScreeningMapper
     */
    public static ScreeningMapper getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ScreeningMapper();
        }
        return uniqueInstance;
    }

    /**
     * Get a list of screenings that are on a specific day
     * @param currentDate a LocalDate object for filtering the screenings
     * @return  a list of all the screenings on a specific day
     */
    public List<Screening> getScreenings(LocalDate currentDate) {
        List<Screening> v = new ArrayList<Screening>();
        try {
            Database.getInstance();
            Statement stmt = Database.getConnection().createStatement();

            // query all the screenings on this day
            ResultSet rset = stmt.executeQuery("SELECT * FROM Screenings WHERE date='" + currentDate + "'");

            // loop through all the screenings (rows) queried from the database
            while (rset.next()) {
                // get the infos of a screening from the result set
                int oid = rset.getInt("oid");
                LocalDate sgDate = LocalDate.parse(rset.getString("date"));
                LocalTime sgTime = LocalTime.parse(rset.getString("time"));
                int ticketsSold = rset.getInt("ticketsSold");
                int movieID = rset.getInt("movie_id");
                int screenID = rset.getInt("screen_id");

                // get the movie and screen instances (for creating the screening)
                PersistentMovie pMovie = MovieMapper.getInstance().getMovieForOid(movieID);
                PersistentScreen pScreen = ScreenMapper.getInstance().getScreenForOid(screenID);

                // pack up the infos into an PersistentScreening object then add it to list
                PersistentScreening pScreening = new PersistentScreening(oid, sgDate, sgTime, ticketsSold, pMovie, pScreen);
                v.add(pScreening);
            }
            //close all the resources we have used
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return v;
    }

    /**
     * Schedule (add) a new screening
     * (no need to give ticketsSold number when adding a new screening, default value will be 0 automatically)
     * @param date the date of the new screening
     * @param time the time of the new screening
     * @param movie the movie of the new screening
     * @param screen the screen of the new screening
     * @return  a PersistentScreening object, which wraps the screening we had just scheduled
     */
    public PersistentScreening scheduleScreening(LocalDate date, LocalTime time, Movie movie, Screen screen) {
        /* insert new row into database */
        int oid = Database.getInstance().getNextScreeningId();  // get the oid for this new instance in the database
        performUpdate("INSERT INTO Screenings (oid, date, time, ticketsSold, movie_id, screen_id) " + "VALUES ('" + oid + "', '" + date + "', '" + time + "', '" + 0 + "', '" + ((PersistentMovie) movie).getOid() + "', '" + ((PersistentScreen) screen).getOid() + "')");

        /* return a new instance of this screening */
        return new PersistentScreening(oid, date, time, 0, movie, screen);
    }


    /**
     * Update the screening
     * @param sg A Screening instance, which has already been changed
     */
    public void updateScreening(Screening sg) {
        PersistentScreening pScreening = (PersistentScreening) sg;
        StringBuffer sql = new StringBuffer(256);

        // create the sql query string
        sql.append("UPDATE Screenings");
        sql.append(" SET");
        sql.append(" date = '");
        sql.append(pScreening.getDate());
        sql.append("', time = '");
        sql.append(pScreening.getTime());
        sql.append("', ticketsSold = ");
        sql.append(pScreening.getTicketsSold());
        sql.append(", movie_id = ");
        sql.append(((PersistentMovie) pScreening.getMovie()).getOid());
        sql.append(", screen_id = ");
        sql.append(((PersistentScreen) pScreening.getScreen()).getOid());
        sql.append(" WHERE oid = ");
        sql.append(pScreening.getOid());

        // execute sql
        performUpdate(sql.toString());
    }

    /**
     * Delete a specific screen from the database
     * (ScreeningMapper do not have the cache, only need to operate the database)
     * @param sg The instance of the screening that we want to delete
     */
    public void deleteScreening(Screening sg) {
        performUpdate("DELETE FROM Screenings" + " WHERE oid = '" + ((PersistentScreening) sg).getOid() + "'");
    }

    /**
     * This is used to execute sql with 'update' type, including
     * INSERT / UPDATE / DELETE (all the operation types besides query)
     * @param sql a String of sql statement
     */
    private void performUpdate(String sql) {
        try {
            //execute the update
            Database.getInstance();
            Statement stmt = Database.getConnection().createStatement();
            stmt.executeUpdate(sql);
            //close the resource we have used
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
