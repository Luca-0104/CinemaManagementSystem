package application.domain;

import application.persistency.MovieMapper;
import application.persistency.ScreenMapper;
import application.persistency.ScreeningMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Cinema {

    // mappers for operate the database
    ScreeningMapper sgm = ScreeningMapper.getInstance();
    MovieMapper mm = MovieMapper.getInstance();
    ScreenMapper sm = ScreenMapper.getInstance();

    /*
        'get' methods (wrapping the get methods in mappers)
     */

    List<Screening> getScreenings(LocalDate currentDate){
        return sgm.getScreenings(currentDate);
    }

    Movie getMovie(String title, int runningTime, int year){
        return mm.getMovie(title, runningTime, year);
    }

    Screen getScreen(String name){
        return sm.getScreen(name);
    }

    static List<Screen> getScreens(){
        return ScreenMapper.getInstance().getScreens();
    }

    /*
        operations on screening
     */

    /**
     * The method for schedule a screening
     * @param date data of this screening
     * @param time time of this screening
     * @param title title of the movie
     * @param runningTime running time of the movie
     * @param name name of the screen
     * @return the instance of the newly scheduled screening
     */
    public Screening scheduleScreening(LocalDate date, LocalTime time, String title, int runningTime, int year, String name){
        // get the instance of movie and screen by their details
        Movie movie = getMovie(title, runningTime, year);
        Screen screen = getScreen(name);
        // schedule this new screening
        return sgm.scheduleScreening(date, time, movie, screen);
    }

    public void updateScreening(Screening sg){
        sgm.updateScreening(sg);
    }

    public void cancelScreening(Screening sg){
        sgm.deleteScreening(sg);
    }

//    public boolean checkExistedScreening(LocalTime time, String screenName, Screening sg){
//        return sgm.checkExistedScreening(time, screenName, sg);
//    }

    /*
        operations on movie !!!!!
     */
    public static List<Movie> getMovies(){
        return null;
    }

    public void addMovie(String title, int runningTime, int year){
        mm.addMovie(title, runningTime);
    }

    public boolean checkExistedMovid(String title, int runningTime, int year){
        return mm.checkExistedMovie(title, runningTime, year);
    }

    /*
        operations on ticket !!!!!
     */

}
