package presentation;

// Movie info to burden the result of user input, transiting them to system backend
public class MovieInfo {
    String title;
    int runningTime;
    int year;

    /**
     * constructor for movie info
     * @param title
     * @param runningTime
     * @param year
     */
    MovieInfo(String title, int runningTime, int year) {
        this.title = title;
        this.runningTime = runningTime;
        this.year = year;
    }

    public String toString() {
        return title + " " + runningTime + " " + year;
    }
}
