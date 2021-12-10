package application.persistency;

import application.domain.Movie;
import application.domain.Screen;
import application.domain.Screening;

import java.time.LocalDate;
import java.time.LocalTime;

public class PersistentScreening extends Screening {
    private int oid;    //object id

    public PersistentScreening(int oid, LocalDate date, LocalTime time, int ticketsSold, Movie movie, Screen screen) {
        super(date, time, ticketsSold, movie, screen);
        this.oid = oid;
    }

    public int getOid() {
        return oid;
    }

    @Override
    public String toString() {
        return "PersistentScreening{" +
                "oid=" + oid +
                ", date=" + getDate() +
                ", time=" + getTime() +
                ", endTime=" + getEndTime() +
                ", ticketsSold=" + getTicketsSold() +
                ", movieId=" + ((PersistentMovie) getMovie()).getOid() +
                ", ScreenId=" + ((PersistentScreen) getScreen()).getOid() +
                '}';
    }
}
