package application.persistency;

import application.domain.Movie;

public class PersistentMovie extends Movie {
    private int oid;    //object id

    public PersistentMovie(int oid, String title, int runningTime, int year) {
        super(title, runningTime, year);
        this.oid = oid;
    }

    int getOid() {
        return oid;
    }

    public String toString() {
        return super.getTitle() + " " + super.getRunningTime() + " " + super.getYear() + " -> id: " + oid;
    }

}
