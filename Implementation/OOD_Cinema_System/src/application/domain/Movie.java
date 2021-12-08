package application.domain;

public class Movie {

    private String title;
    private int runningTime;    //seconds
    private int year;

    public Movie(String title, int runningTime, int year) {
        this.title = title;
        this.runningTime = runningTime;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public String toString() {
        return this.title + " " + this.runningTime + " " + this.year;
    }
}
