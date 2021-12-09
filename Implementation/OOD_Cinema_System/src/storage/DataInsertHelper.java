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

public class DataInsertHelper {

    //instances of mappers
    static ScreeningMapper sgm = ScreeningMapper.getInstance();
    static MovieMapper mm = MovieMapper.getInstance();
    static ScreenMapper sm = ScreenMapper.getInstance();

    public static void main(String[] args) {
        //add some predefined movies
        insertMovies();
        System.out.println(">>> Movie insert finished");
    }


    /**
     * This method is used to insert some predefined movies into the database
     */
    private static void insertMovies(){
        //IMDB Top250 * 40
        mm.getMovie("The Shawshank Redemption", 2*60+22, 1994);
        mm.getMovie("The Godfather", 2*60+55, 1972);
        mm.getMovie("The Godfather: Part II", 3*60+22, 1974);
        mm.getMovie("The Dark Knight", 2*60+32, 2008);
        mm.getMovie("12 Angry Men", 60+36, 1957);
        mm.getMovie("Schindlers List", 3*60+15, 1993);
        mm.getMovie("The Lord of the Rings: The Return of the King", 3*60+21, 2003);
        mm.getMovie("Pulp Fiction", 2*60+34, 1994);
        mm.getMovie("Il buono, il brutto, il cattivo", 2*60+41, 1996);
        mm.getMovie("The Lord of the Rings: The Fellowship of the Ring", 2*60+58, 2001);
        mm.getMovie("Fight Club", 2*60+19, 1999);
        mm.getMovie("Forrest Gump", 2*60+22, 1994);
        mm.getMovie("Inception", 2*60+38, 2010);
        mm.getMovie("The Lord of the Rings: The Two Towers", 2*60+59, 2002);
        mm.getMovie("Star Wars: Episode V - The Empire Strikes Back", 2*60+4, 1980);
        mm.getMovie("The Matrix", 2*60+16, 1999);
        mm.getMovie("Goodfellas", 2*60+26, 1990);
        mm.getMovie("One Flew Over the Cuckoos Nest", 2*60+13, 1975);
        mm.getMovie("Shichinin no samurai", 3*60+27, 1954);
        mm.getMovie("Se7en", 2*60+7, 1995);
        mm.getMovie("The Silence of the Lambs", 60+58, 1991);
        mm.getMovie("Cidade de Deus", 2*60+10, 2002);
        mm.getMovie("La vita è bella", 60+56, 1997);
        mm.getMovie("It is a Wonderful Life", 2*60+10, 1946);
        mm.getMovie("Saving Private Ryan", 2*60+49, 1998);
        mm.getMovie("Star Wars", 2*60+1, 1977);
        mm.getMovie("Interstellar", 2*60+49, 2014);
        mm.getMovie("Sen to Chihiro no kamikakushi", 2*60+5, 2001);
        mm.getMovie("The Green Mile", 3*60+9, 1999);
        mm.getMovie("Gisaengchung", 2*60+12, 2019);
        mm.getMovie("Léon", 60+50, 1994);
        mm.getMovie("Seppuku", 2*60+13, 1962);
        mm.getMovie("The Pianist", 2*60+30, 2002);
        mm.getMovie("Terminator 2: Judgment Day", 2*60+17, 1991);
        mm.getMovie("Back to the Future", 60+56, 1985);
        mm.getMovie("The Usual Suspects", 60+46, 1995);
        mm.getMovie("Psycho", 60+49, 1960);
        mm.getMovie("The Lion King", 60+28, 1994);
        mm.getMovie("Modern Times", 60+27, 1936);
        mm.getMovie("American History X", 60+59, 1998);

        //IMDB most popular * 40
        mm.getMovie("House of Gucci", 2*60+38, 2021);
        mm.getMovie("The Power of the Dog", 2*60+6, 2021);
        mm.getMovie("Spider-Man: No Way Home", 2*60+30, 2021);
        mm.getMovie("The Last Duel", 2*60+32, 2021);
        mm.getMovie("Ghostbusters: Afterlife", 2*60+4, 2021);
        mm.getMovie("Dune: Part One", 2*60+35, 2021);
        mm.getMovie("Encanto", 60+42, 2021);
        mm.getMovie("Red Notice", 60+58, 2021);
        mm.getMovie("tick, tick...BOOM!", 60+55, 2021);
        mm.getMovie("The Matrix Resurrections", 2*60+28, 2021);
        mm.getMovie("Shang-Chi and the Legend of the Ten Rings", 2*60+12, 2021);
        mm.getMovie("No Time to Die", 2*60+43, 2021);
        mm.getMovie("Venom: Let There Be Carnage", 60+37, 2021);
        mm.getMovie("A Castle for Christmas", 60+38, 2021);
        mm.getMovie("Resident Evil: Welcome to Raccoon City", 60+47, 2021);
        mm.getMovie("A Boy Called Christmas", 60+46, 2021);
        mm.getMovie("Eternals", 2*60+36, 2021);
        mm.getMovie("Antim: The Final Truth", 2*60+18, 2021);
        mm.getMovie("Bruised", 2*60+9, 2020);
        mm.getMovie("Last Night in Soho", 60+56, 2021);
        mm.getMovie("West Side Story", 2*60+36, 2021);
        mm.getMovie("Licorice Pizza", 2*60+13, 2021);
        mm.getMovie("Spencer", 60+57, 2021);
        mm.getMovie("King Richard", 2*60+24, 2021);
        mm.getMovie("National Lampoons Christmas Vacation", 60+37, 1989);
        mm.getMovie("8-Bit Christmas", 60+37, 2021);
        mm.getMovie("Home Alone", 60+43, 1990);
        mm.getMovie("Nightmare Alley", 2*60+30, 2021);
        mm.getMovie("Belfast", 60+38, 2021);
        mm.getMovie("Free Guy", 60+55, 2021);
        mm.getMovie("Silent Night", 60+32, 2021);
        mm.getMovie("The French Dispatch", 60+47, 2021);
        mm.getMovie("Love Hard", 60+44, 2021);
        mm.getMovie("Jai Bhim", 2*60+44, 2021);
        mm.getMovie("The Harder They Fall", 2*60+19, 2021);
        mm.getMovie("Love Actually", 2*60+15, 2003);
        mm.getMovie("Single All the Way", 60+39, 2021);
        mm.getMovie("Omicron", 60+42, 1963);
        mm.getMovie("Home Sweet Home Alone", 60+33, 2021);
        mm.getMovie("Finch", 60+55, 2021);
    }

}
