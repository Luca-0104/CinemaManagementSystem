package application.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Screening {

    private LocalDate date; // start date
    private LocalTime time; //start time
    private int ticketsSold; //cannot surpass the capacity of the screen
    private Movie movie;
    private Screen screen;

    public Screening(LocalDate date, LocalTime time, int ticketsSold, Movie movie, Screen screen) {
        this.date = date;
        this.time = time;
        this.ticketsSold = ticketsSold;
        this.movie = movie;
        this.screen = screen;
    }

    //calculate the end time (does not include the 15 extra minutes for customers to leave and arrive)
    public LocalTime getEndTime(){
        return time.plusMinutes(movie.getRunningTime());
    }

    //get the detailed information of the screening
    public String getDetails() {
        StringBuffer details = new StringBuffer(128);
        details.append(movie.getTitle());
        details.append(" ");
        details.append(movie.getRunningTime());
        details.append(" ");
        details.append(date);
        details.append(time);
        details.append(" ");
        details.append(getEndTime());
        details.append(" ");
        details.append(screen.getName());
        details.append(" ");
        details.append(ticketsSold);

        return details.toString();
    }

    /* --------- getter and setters --------- */

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(int ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

}
