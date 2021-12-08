package storage;

import java.sql.*;

/**
 * (Singleton Pattern)
 */
public class Database {
    private static Connection con;

    // Singleton:

    private static Database   uniqueInstance;

    public static Database getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Database();
        }
        return uniqueInstance;
    }

    private Database() {
        try {
//            con = DriverManager.getConnection("jdbc:sqlite:./cinema.db");
            con = DriverManager.getConnection("jdbc:sqlite:./Implementation/OOD_Cinema_System/cinema.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return con;
    }

    /**
     * Get the next oid that should be used to insert new data in Movie table
     * @return Get a fresh object ID
     */
    public int getNextMovieId() {
        int id = 0;
        try {
            Statement stmt = con.createStatement();

            // However, there will be always a single row in OidMovie table
            ResultSet rset = stmt.executeQuery("SELECT * FROM OidMovie");
            while (rset.next()) {
                id = rset.getInt(1);
            }
            rset.close();

            id++;

            stmt.executeUpdate("UPDATE OidMovie SET last_id = '" + id + "'");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    /**
     * Get the next oid that should be used to insert new data in Screening table
     * @return Get a fresh object ID
     */
    public int getNextScreeningId() {
        int id = 0;
        try {
            Statement stmt = con.createStatement();

            // However, there will be always a single row in OidScreening table
            ResultSet rset = stmt.executeQuery("SELECT * FROM OidScreening");
            while (rset.next()) {
                id = rset.getInt(1);
            }
            rset.close();

            id++;

            stmt.executeUpdate("UPDATE OidScreening SET last_id = '" + id + "'");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }
}
