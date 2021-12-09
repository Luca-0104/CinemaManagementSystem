package application.domain;

import application.persistency.PersistentMovie;

public class Movie {

    private String title;
    private int runningTime;    //unit: minutes
    private int year;

    public Movie(String title, int runningTime, int year) {
        this.title = title;
        this.runningTime = runningTime;
        this.year = year;
    }

    // get methods
    public String getTitle() {
        return title;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public int getYear() {
        return year;
    }

    public String toString() {
        return this.title + " " + this.runningTime + " " + this.year;
    }
}
