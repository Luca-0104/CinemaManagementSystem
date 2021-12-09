package presentation;

import java.time.LocalDate;
import java.time.LocalTime;

// Screening info to burden the result of user input, transiting them to system backend
public class ScreeningInfo {
    LocalDate date;
    LocalTime time;
    int screenNumber;
    int movieNumber;

    /**
     * Constructor
     * @param date
     * @param time
     * @param screenNumber
     * @param movieNumber
     */
    public ScreeningInfo(LocalDate date, LocalTime time, int screenNumber, int movieNumber) {
        this.date = date;
        this.time = time;
        this.screenNumber = screenNumber;
        this.movieNumber = movieNumber;
    }
}
