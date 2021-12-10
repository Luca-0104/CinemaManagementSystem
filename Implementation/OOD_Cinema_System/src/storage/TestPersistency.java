package storage;

import application.domain.Screen;
import application.domain.Screening;
import application.persistency.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TestPersistency {

    //instances of mappers
    static ScreeningMapper sgm = ScreeningMapper.getInstance();
    static MovieMapper mm = MovieMapper.getInstance();
    static ScreenMapper sm = ScreenMapper.getInstance();

    public static void main(String[] args) {
        String url = "jdbc:sqlite:./Implementation/OOD_Cinema_System/cinema.db";


        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                Statement stmt;
                String sql;

                /*
                    movie mapper -- insert movies (if no exists in db, getMovie will call addMovie)
                    COVERAGE:   getMovie
                                getFromCacheByDetails
                                getMovie(String sql)
                                addMovie
                                addToCache

                 */
                PersistentMovie movie1 = mm.getMovie("testMovie1", 120, 2021);
                PersistentMovie movie2 = mm.getMovie("testMovie2", 120, 2021);
                PersistentMovie movie3 = mm.getMovie("testMovie3", 120, 2021);
                PersistentMovie movie4 = mm.getMovie("testMovie4", 120, 2021);
                PersistentMovie movie5 = mm.getMovie("testMovie5", 120, 2021);

                /*
                    movie mapper -- getMovieForOid (!!!this method is "default", these tests should be commented eventually)
                    COVERAGE:   getMovieForOid
                                getFromCache
                                getMovie(String sql)
                                addToCache

                    NOW, movieMapper 100%
                 */
//                System.out.println(mm.getMovieForOid(1));
//                System.out.println(mm.getMovieForOid(2));
//                System.out.println(mm.getMovieForOid(3));
//                System.out.println(mm.getMovieForOid(4));
//                System.out.println(mm.getMovieForOid(5));


                /*
                    screen mapper --  getScreen (when initializing the ScreenMapper, all the screens are added to cache)
                    COVERAGE:   getScreen
                                getFromCacheByName
                 */
                System.out.println("------------getScreen----------");
                System.out.println(sm.getScreen("screen1").toString());
                System.out.println(sm.getScreen("screen2").toString());
                System.out.println(sm.getScreen("screen3").toString());
                System.out.println(sm.getScreen("screen4").toString());
                System.out.println(sm.getScreen("screen5").toString());
                System.out.println(sm.getScreen("screen6").toString());
                System.out.println(sm.getScreen("screen7").toString());
                System.out.println(sm.getScreen("screen8").toString());
                System.out.println(sm.getScreen("screen9").toString());
                System.out.println(sm.getScreen("screen10").toString());

                /*
                    screen mapper --  getScreenForOid (!!!this method is "default", these tests should be commented eventually)
                    COVERAGE:   getScreenForOid
                                getFromCache
                 */
//                System.out.println("------------getScreenForOid----------");
//                System.out.println(sm.getScreenForOid(1));
//                System.out.println(sm.getScreenForOid(2));
//                System.out.println(sm.getScreenForOid(3));
//                System.out.println(sm.getScreenForOid(4));
//                System.out.println(sm.getScreenForOid(5));
//                System.out.println(sm.getScreenForOid(6));
//                System.out.println(sm.getScreenForOid(7));
//                System.out.println(sm.getScreenForOid(8));
//                System.out.println(sm.getScreenForOid(9));
//                System.out.println(sm.getScreenForOid(10));

                /*
                    screen mapper --  getScreens
                    COVERAGE:   getScreens
                                addToCache

                    NOW, screenMapper 100%
                 */
                System.out.println("------------getScreens----------");
                List<Screen> screenList = sm.getScreens();
                for (Screen s : screenList){
                    System.out.println(s.toString());
                }

                /*
                    screening mapper --  scheduleScreening
                    COVERAGE:   scheduleScreening
                                performUpdate(String sql)
                 */
                //day1
                PersistentScreening sg1 = sgm.scheduleScreening(LocalDate.parse("2021-12-18"), LocalTime.parse("10:15"), movie1, sm.getScreen("screen1"));
                PersistentScreening sg2 = sgm.scheduleScreening(LocalDate.parse("2021-12-18"), LocalTime.parse("12:15"), movie2, sm.getScreen("screen3"));
                PersistentScreening sg3 = sgm.scheduleScreening(LocalDate.parse("2021-12-18"), LocalTime.parse("14:15"), movie3, sm.getScreen("screen5"));

                //day2
                PersistentScreening sg4 = sgm.scheduleScreening(LocalDate.parse("2021-12-29"), LocalTime.parse("10:15"), movie1, sm.getScreen("screen2"));
                PersistentScreening sg5 = sgm.scheduleScreening(LocalDate.parse("2021-12-29"), LocalTime.parse("12:15"), movie2, sm.getScreen("screen4"));
                PersistentScreening sg6 = sgm.scheduleScreening(LocalDate.parse("2021-12-29"), LocalTime.parse("14:15"), movie3, sm.getScreen("screen6"));
                PersistentScreening sg7 = sgm.scheduleScreening(LocalDate.parse("2021-12-29"), LocalTime.parse("16:15"), movie4, sm.getScreen("screen8"));
                //day3
                PersistentScreening sg8 = sgm.scheduleScreening(LocalDate.parse("2022-01-08"), LocalTime.parse("10:25"), movie1, sm.getScreen("screen10"));
                PersistentScreening sg9 = sgm.scheduleScreening(LocalDate.parse("2022-01-08"), LocalTime.parse("12:25"), movie2, sm.getScreen("screen8"));
                PersistentScreening sg10 = sgm.scheduleScreening(LocalDate.parse("2022-01-08"), LocalTime.parse("14:25"), movie3, sm.getScreen("screen6"));
                PersistentScreening sg11 = sgm.scheduleScreening(LocalDate.parse("2022-01-08"), LocalTime.parse("16:25"), movie4, sm.getScreen("screen4"));
                PersistentScreening sg12 = sgm.scheduleScreening(LocalDate.parse("2022-01-08"), LocalTime.parse("18:25"), movie5, sm.getScreen("screen2"));

                //day4
                PersistentScreening sg13 = sgm.scheduleScreening(LocalDate.parse("2022-01-18"), LocalTime.parse("05:30"), movie4, sm.getScreen("screen5"));
                PersistentScreening sg14 = sgm.scheduleScreening(LocalDate.parse("2022-01-18"), LocalTime.parse("08:30"), movie4, sm.getScreen("screen7"));
                PersistentScreening sg15 = sgm.scheduleScreening(LocalDate.parse("2022-01-18"), LocalTime.parse("10:30"), movie4, sm.getScreen("screen9"));

                //day5
                PersistentScreening sg16 = sgm.scheduleScreening(LocalDate.parse("2022-01-28"), LocalTime.parse("09:30"), movie5, sm.getScreen("screen5"));
                PersistentScreening sg17 = sgm.scheduleScreening(LocalDate.parse("2022-01-28"), LocalTime.parse("11:30"), movie5, sm.getScreen("screen5"));
                PersistentScreening sg18 = sgm.scheduleScreening(LocalDate.parse("2022-01-28"), LocalTime.parse("14:30"), movie5, sm.getScreen("screen5"));

                //day6
                PersistentScreening sg19 = sgm.scheduleScreening(LocalDate.parse("2022-02-08"), LocalTime.parse("10:50"), movie1, sm.getScreen("screen3"));
                PersistentScreening sg20 = sgm.scheduleScreening(LocalDate.parse("2022-02-08"), LocalTime.parse("13:50"), movie1, sm.getScreen("screen3"));
                PersistentScreening sg21 = sgm.scheduleScreening(LocalDate.parse("2022-02-08"), LocalTime.parse("16:50"), movie1, sm.getScreen("screen3"));
                PersistentScreening sg22 = sgm.scheduleScreening(LocalDate.parse("2022-02-08"), LocalTime.parse("23:50"), movie1, sm.getScreen("screen3"));

                /*
                    screening mapper --  getScreenings
                    COVERAGE:   getScreenings
                 */
                showScreenings();

                /*
                    screening mapper --  updateScreening
                    COVERAGE:   updateScreening
                                performUpdate(String sql)
                 */
                sg1.setDate(LocalDate.parse("2022-01-01")); //day7
                sg1.setTime(LocalTime.parse("12:00"));
                sg1.setMovie(movie2);
                sg1.setScreen(sm.getScreen("screen2"));
                sgm.updateScreening(sg1);
                System.out.println(">>>>>>>>>>>>>>>>> screening1 is updated <<<<<<<<<<<<<<<<<");
                showScreenings();

                /*
                    screening mapper --  deleteScreening
                    COVERAGE:   deleteScreening
                                performUpdate(String sql)

                   NOW, screenMapper 100%
                 */
                //test to delete the first screening of each day
                sgm.deleteScreening(sg2);
                sgm.deleteScreening(sg4);
                sgm.deleteScreening(sg8);
                sgm.deleteScreening(sg13);
                sgm.deleteScreening(sg16);
                sgm.deleteScreening(sg19);
                sgm.deleteScreening(sg1);

                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> first screenings of each day are deleted <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                showScreenings();



            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showScreenings(){
        List<Screening> screeningListDay1 = sgm.getScreenings(LocalDate.parse("2021-12-18"));
        List<Screening> screeningListDay2 = sgm.getScreenings(LocalDate.parse("2021-12-29"));
        List<Screening> screeningListDay3 = sgm.getScreenings(LocalDate.parse("2022-01-08"));
        List<Screening> screeningListDay4 = sgm.getScreenings(LocalDate.parse("2022-01-18"));
        List<Screening> screeningListDay5 = sgm.getScreenings(LocalDate.parse("2022-01-28"));
        List<Screening> screeningListDay6 = sgm.getScreenings(LocalDate.parse("2022-02-08"));
        List<Screening> screeningListDay7 = sgm.getScreenings(LocalDate.parse("2022-01-01"));
        System.out.println("------------screeningListDay1----------");
        for (Screening s : screeningListDay1){
            System.out.println(s.toString());
        }
        System.out.println("------------screeningListDay2----------");
        for (Screening s : screeningListDay2){
            System.out.println(s.toString());
        }
        System.out.println("------------screeningListDay3----------");
        for (Screening s : screeningListDay3){
            System.out.println(s.toString());
        }
        System.out.println("------------screeningListDay4----------");
        for (Screening s : screeningListDay4){
            System.out.println(s.toString());
        }
        System.out.println("------------screeningListDay5----------");
        for (Screening s : screeningListDay5){
            System.out.println(s.toString());
        }
        System.out.println("------------screeningListDay6----------");
        for (Screening s : screeningListDay6){
            System.out.println(s.toString());
        }
        System.out.println("------------screeningListDay7----------");
        for (Screening s : screeningListDay7){
            System.out.println(s.toString());
        }

    }
}
