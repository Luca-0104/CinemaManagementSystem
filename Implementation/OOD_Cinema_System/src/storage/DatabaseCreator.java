package storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCreator {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:./Implementation/OOD_Cinema_System/cinema.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                Statement stmt;
                String sql;

                /*
                  create tables
                */
                //OidMovie
                stmt = conn.createStatement();
                sql = "CREATE TABLE  IF NOT EXISTS OidMovie (\r\n" +
                        "       last_id      INT NOT NULL\r\n" +
                        ") ;";
                stmt.execute(sql);

                //OidScreening
                stmt = conn.createStatement();
                sql = "CREATE TABLE  IF NOT EXISTS OidScreening (\r\n" +
                        "       last_id      INT NOT NULL\r\n" +
                        ") ;";
                stmt.execute(sql);

                //Movies
                stmt = conn.createStatement();
                sql = "CREATE TABLE IF NOT EXISTS Movies (\r\n" +
                        "       oid             int NOT NULL PRIMARY KEY,\r\n" +
                        "       title           VARCHAR(64) NOT NULL,\r\n" +
                        "       runningTime     int NOT NULL,\r\n" +
                        "       `year`     int NOT NULL\r\n" +
                        ") ;";
                stmt.execute(sql);

                //Screens
                stmt = conn.createStatement();
                sql = "CREATE TABLE IF NOT EXISTS Screens (\r\n" +
                        "       oid             int  NOT NULL PRIMARY KEY,\r\n" +
                        "       name            VARCHAR(32) NOT NULL UNIQUE,\r\n" +
                        "       capacity        int NOT NULL\r\n" +
                        ") ;";
                stmt.execute(sql);

                //Screenings
                stmt = conn.createStatement();
                sql = "CREATE TABLE IF NOT EXISTS Screenings (\r\n" +
                        "       oid             int  NOT NULL PRIMARY KEY,\r\n" +
                        "       date            VARCHAR(32) NOT NULL,\r\n" +
                        "       time            VARCHAR(32) NOT NULL,\r\n" +
                        "       ticketsSold     int NOT NULL,\r\n" +
                        "       movie_id        int NOT NULL,\r\n" +
                        "       screen_id       int NOT NULL\r\n" +
                        ") ;";
                stmt.execute(sql);


                /*
                  insert example data
                 */
                stmt = conn.createStatement();
                sql = "INSERT INTO OidMovie VALUES (0) ;";
                stmt.execute(sql);

                stmt = conn.createStatement();
                sql = "INSERT INTO OidScreening VALUES (0) ;";
                stmt.execute(sql);

                stmt = conn.createStatement();
                sql = "INSERT INTO Screens (oid, name, capacity) VALUES (1, 'screen1', 30) ;";
                stmt.execute(sql);

                stmt = conn.createStatement();
                sql = "INSERT INTO Screens (oid, name, capacity) VALUES (2, 'screen2', 30) ;";
                stmt.execute(sql);

                stmt = conn.createStatement();
                sql = "INSERT INTO Screens (oid, name, capacity) VALUES (3, 'screen3', 50) ;";
                stmt.execute(sql);

                stmt = conn.createStatement();
                sql = "INSERT INTO Screens (oid, name, capacity) VALUES (4, 'screen4', 50) ;";
                stmt.execute(sql);

                stmt = conn.createStatement();
                sql = "INSERT INTO Screens (oid, name, capacity) VALUES (5, 'screen5', 70) ;";
                stmt.execute(sql);

                stmt = conn.createStatement();
                sql = "INSERT INTO Screens (oid, name, capacity) VALUES (6, 'screen6', 70) ;";
                stmt.execute(sql);

                stmt = conn.createStatement();
                sql = "INSERT INTO Screens (oid, name, capacity) VALUES (7, 'screen7', 90) ;";
                stmt.execute(sql);

                stmt = conn.createStatement();
                sql = "INSERT INTO Screens (oid, name, capacity) VALUES (8, 'screen8', 90) ;";
                stmt.execute(sql);

                stmt = conn.createStatement();
                sql = "INSERT INTO Screens (oid, name, capacity) VALUES (9, 'screen9', 100) ;";
                stmt.execute(sql);

                stmt = conn.createStatement();
                sql = "INSERT INTO Screens (oid, name, capacity) VALUES (10, 'screen10', 100) ;";
                stmt.execute(sql);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
