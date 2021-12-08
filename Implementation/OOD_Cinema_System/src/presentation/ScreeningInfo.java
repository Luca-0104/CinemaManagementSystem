package presentation;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScreeningInfo {
    LocalDate date;
    LocalTime time;
    int screenNumber;
    int movieNumber;

    public ScreeningInfo(LocalDate date, LocalTime time, int screenNumber, int movieNumber) {
        this.date = date;
        this.time = time;
        this.screenNumber = screenNumber;
        this.movieNumber = movieNumber;
    }
}
