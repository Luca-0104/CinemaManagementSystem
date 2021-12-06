package application.persistency;

import application.domain.Movie;
import application.domain.Screen;
import application.domain.Screening;

import java.time.LocalDate;
import java.time.LocalTime;

public class PersistentScreening extends Screening {
    private int oid;

    public PersistentScreening(int oid, LocalDate date, LocalTime time, Movie movie, Screen screen) {
        super(date, time, movie, screen);
        this.oid = oid;
    }

    int getOid() {
        return oid;
    }
}
