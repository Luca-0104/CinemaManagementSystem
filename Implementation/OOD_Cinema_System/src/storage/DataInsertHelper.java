package storage;

import application.domain.Movie;
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

public class DataInsertHelper {

    //instances of mappers
    static ScreeningMapper sgm = ScreeningMapper.getInstance();
    static MovieMapper mm = MovieMapper.getInstance();
    static ScreenMapper sm = ScreenMapper.getInstance();

    //movies
    private static PersistentMovie movie1, movie2, movie3, movie4, movie5, movie6, movie7, movie8, movie9, movie10, movie11, movie12, movie13, movie14, movie15, movie16, movie17, movie18, movie19, movie20, movie21, movie22, movie23, movie24, movie25, movie26, movie27, movie28, movie29, movie30, movie31, movie32, movie33, movie34, movie35, movie36, movie37, movie38, movie39, movie40, movie41, movie42, movie43, movie44, movie45, movie46, movie47, movie48, movie49, movie50, movie51, movie52, movie53, movie54, movie55, movie56, movie57, movie58, movie59, movie60, movie61, movie62, movie63, movie64, movie65, movie66, movie67, movie68, movie69, movie70, movie71, movie72, movie73, movie74, movie75, movie76, movie77, movie78, movie79, movie80;

    public static void main(String[] args) {
        //add some predefined movies
        insertMovies();
        System.out.println(">>> Movie insert finished");


//        //add some predefined screenings
//        insertScreenings();
//        System.out.println(">>> Screening insert finished");

    }

//    /**
//     * This method is used to insert some predefined screenings into the database
//     */
//    private static void insertScreenings() {
//        /** 2021-12-09*/
//        //screen1
//        String[] times = new String[]{"12:30", "15:30", "18:30", "21:30"};
//        Movie[] movies = new Movie[]{movie1, movie2, movie3, movie4};
//        for (int i = 0; i < times.length; i++){
//            sgm.scheduleScreening(LocalDate.parse("2021-12-09"), LocalTime.parse(times[i]), movies[i], sm.getScreen("screen1"));
//        }
//
//        //screen2
//        times = new String[]{"12:00", "18:00"};
//        movies = new Movie[]{movie5, movie6};
//        for (int i = 0; i < times.length; i++){
//            sgm.scheduleScreening(LocalDate.parse("2021-12-09"), LocalTime.parse(times[i]), movies[i], sm.getScreen("screen2"));
//        }
//
//    }


    /**
     * This method is used to insert some predefined movies into the database
     */
    private static void insertMovies() {
        //IMDB Top250 * 40
        movie1 = mm.getMovie("The Shawshank Redemption", 2 * 60 + 22, 1994);
        movie2 = mm.getMovie("The Godfather", 2 * 60 + 55, 1972);
        movie3 = mm.getMovie("The Godfather: Part II", 3 * 60 + 22, 1974);
        movie4 = mm.getMovie("The Dark Knight", 2 * 60 + 32, 2008);
        movie5 = mm.getMovie("12 Angry Men", 60 + 36, 1957);
        movie6 = mm.getMovie("Schindlers List", 3 * 60 + 15, 1993);
        movie7 = mm.getMovie("The Lord of the Rings: The Return of the King", 3 * 60 + 21, 2003);
        movie8 = mm.getMovie("Pulp Fiction", 2 * 60 + 34, 1994);
        movie9 = mm.getMovie("Il buono, il brutto, il cattivo", 2 * 60 + 41, 1996);
        movie10 = mm.getMovie("The Lord of the Rings: The Fellowship of the Ring", 2 * 60 + 58, 2001);
        movie11 = mm.getMovie("Fight Club", 2 * 60 + 19, 1999);
        movie12 = mm.getMovie("Forrest Gump", 2 * 60 + 22, 1994);
        movie13 = mm.getMovie("Inception", 2 * 60 + 38, 2010);
        movie14 = mm.getMovie("The Lord of the Rings: The Two Towers", 2 * 60 + 59, 2002);
        movie15 = mm.getMovie("Star Wars: Episode V - The Empire Strikes Back", 2 * 60 + 4, 1980);
        movie16 = mm.getMovie("The Matrix", 2 * 60 + 16, 1999);
        movie17 = mm.getMovie("Goodfellas", 2 * 60 + 26, 1990);
        movie18 = mm.getMovie("One Flew Over the Cuckoos Nest", 2 * 60 + 13, 1975);
        movie19 = mm.getMovie("Shichinin no samurai", 3 * 60 + 27, 1954);
        movie20 = mm.getMovie("Se7en", 2 * 60 + 7, 1995);
        movie21 = mm.getMovie("The Silence of the Lambs", 60 + 58, 1991);
        movie22 = mm.getMovie("Cidade de Deus", 2 * 60 + 10, 2002);
        movie23 = mm.getMovie("La vita è bella", 60 + 56, 1997);
        movie24 = mm.getMovie("It is a Wonderful Life", 2 * 60 + 10, 1946);
        movie25 = mm.getMovie("Saving Private Ryan", 2 * 60 + 49, 1998);
        movie26 = mm.getMovie("Star Wars", 2 * 60 + 1, 1977);
        movie27 = mm.getMovie("Interstellar", 2 * 60 + 49, 2014);
        movie28 = mm.getMovie("Sen to Chihiro no kamikakushi", 2 * 60 + 5, 2001);
        movie29 = mm.getMovie("The Green Mile", 3 * 60 + 9, 1999);
        movie30 = mm.getMovie("Gisaengchung", 2 * 60 + 12, 2019);
        movie31 = mm.getMovie("Léon", 60 + 50, 1994);
        movie32 = mm.getMovie("Seppuku", 2 * 60 + 13, 1962);
        movie33 = mm.getMovie("The Pianist", 2 * 60 + 30, 2002);
        movie34 = mm.getMovie("Terminator 2: Judgment Day", 2 * 60 + 17, 1991);
        movie35 = mm.getMovie("Back to the Future", 60 + 56, 1985);
        movie36 = mm.getMovie("The Usual Suspects", 60 + 46, 1995);
        movie37 = mm.getMovie("Psycho", 60 + 49, 1960);
        movie38 = mm.getMovie("The Lion King", 60 + 28, 1994);
        movie39 = mm.getMovie("Modern Times", 60 + 27, 1936);
        movie40 = mm.getMovie("American History X", 60 + 59, 1998);

        //IMDB most popular * 40
        movie41 = mm.getMovie("House of Gucci", 2 * 60 + 38, 2021);
        movie42 = mm.getMovie("The Power of the Dog", 2 * 60 + 6, 2021);
        movie43 = mm.getMovie("Spider-Man: No Way Home", 2 * 60 + 30, 2021);
        movie44 = mm.getMovie("The Last Duel", 2 * 60 + 32, 2021);
        movie45 = mm.getMovie("Ghostbusters: Afterlife", 2 * 60 + 4, 2021);
        movie46 = mm.getMovie("Dune: Part One", 2 * 60 + 35, 2021);
        movie47 = mm.getMovie("Encanto", 60 + 42, 2021);
        movie48 = mm.getMovie("Red Notice", 60 + 58, 2021);
        movie49 = mm.getMovie("tick, tick...BOOM!", 60 + 55, 2021);
        movie50 = mm.getMovie("The Matrix Resurrections", 2 * 60 + 28, 2021);
        movie51 = mm.getMovie("Shang-Chi and the Legend of the Ten Rings", 2 * 60 + 12, 2021);
        movie52 = mm.getMovie("No Time to Die", 2 * 60 + 43, 2021);
        movie53 = mm.getMovie("Venom: Let There Be Carnage", 60 + 37, 2021);
        movie54 = mm.getMovie("A Castle for Christmas", 60 + 38, 2021);
        movie55 = mm.getMovie("Resident Evil: Welcome to Raccoon City", 60 + 47, 2021);
        movie56 = mm.getMovie("A Boy Called Christmas", 60 + 46, 2021);
        movie57 = mm.getMovie("Eternals", 2 * 60 + 36, 2021);
        movie58 = mm.getMovie("Antim: The Final Truth", 2 * 60 + 18, 2021);
        movie59 = mm.getMovie("Bruised", 2 * 60 + 9, 2020);
        movie60 = mm.getMovie("Last Night in Soho", 60 + 56, 2021);
        movie61 = mm.getMovie("West Side Story", 2 * 60 + 36, 2021);
        movie62 = mm.getMovie("Licorice Pizza", 2 * 60 + 13, 2021);
        movie63 = mm.getMovie("Spencer", 60 + 57, 2021);
        movie64 = mm.getMovie("King Richard", 2 * 60 + 24, 2021);
        movie65 = mm.getMovie("National Lampoons Christmas Vacation", 60 + 37, 1989);
        movie66 = mm.getMovie("8-Bit Christmas", 60 + 37, 2021);
        movie67 = mm.getMovie("Home Alone", 60 + 43, 1990);
        movie68 = mm.getMovie("Nightmare Alley", 2 * 60 + 30, 2021);
        movie69 = mm.getMovie("Belfast", 60 + 38, 2021);
        movie70 = mm.getMovie("Free Guy", 60 + 55, 2021);
        movie71 = mm.getMovie("Silent Night", 60 + 32, 2021);
        movie72 = mm.getMovie("The French Dispatch", 60 + 47, 2021);
        movie73 = mm.getMovie("Love Hard", 60 + 44, 2021);
        movie74 = mm.getMovie("Jai Bhim", 2 * 60 + 44, 2021);
        movie75 = mm.getMovie("The Harder They Fall", 2 * 60 + 19, 2021);
        movie76 = mm.getMovie("Love Actually", 2 * 60 + 15, 2003);
        movie77 = mm.getMovie("Single All the Way", 60 + 39, 2021);
        movie78 = mm.getMovie("Omicron", 60 + 42, 1963);
        movie79 = mm.getMovie("Home Sweet Home Alone", 60 + 33, 2021);
        movie80 = mm.getMovie("Finch", 60 + 55, 2021);
    }

}
