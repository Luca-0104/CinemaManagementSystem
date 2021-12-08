package presentation;

public class MovieInfo {
    String title;
    int runningTime;
    int year;

    MovieInfo(String title, int runningTime, int year) {
        this.title = title;
        this.runningTime = runningTime;
        this.year = year;
    }

    public String toString() {
        return title + " " + runningTime + " " + year;
    }
}
