package application.persistency;

import application.domain.Movie;

public class PersistentMovie extends Movie {
    private int oid;

    public PersistentMovie(int oid, String title, int runningTime) {
        super(title, runningTime);
        this.oid = oid;
    }

    int getOid() {
        return oid;
    }

    @Override
    public String toString() {
        return "PersistentMovie{" +
                "oid=" + oid +
                ", title=" + super.getTitle() +
                ", runningTime=" + super.getRunningTime() +
                '}';
    }
}
