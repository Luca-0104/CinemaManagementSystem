package application.domain;

import application.persistency.MovieMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class ManagementSystem {
    //Attributes:

    private LocalDate currentDate;
    //Associations:
    private Cinema cinema = null;
    private List<Screening> currentScreenings;
    private Screening selectedScreening;

    private List<ManagementObserver> observers = new ArrayList<>();

    //Singleton:
    private static ManagementSystem  uniqueInstance;

    private ManagementSystem() {
        cinema = new Cinema();
    }

    public static ManagementSystem getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ManagementSystem();
        }
        return uniqueInstance;
    }

    // operations on observers:

    public void addObserver(ManagementObserver o) {
        observers.add(o);
    }

    public void notifyObservers() {
        // loop through to tell all the  observers to update
        for (ManagementObserver mo : observers) {
            mo.update();
        }
    }

    public boolean observerMessage(String message, boolean confirm) {
        ManagementObserver mo = (ManagementObserver) observers.get(0);
        return mo.message(message, confirm);
    }

    // set the date and update the information
    public void setDate(LocalDate date) {
        // update the current date
        currentDate = date;
        // update the current screenings by the given date
        currentScreenings = cinema.getScreenings(currentDate);
        selectedScreening = null;
        // tell all the observers to update
        notifyObservers();
    }

    // operations on screenings:

    public void selectScreening(String screenName, LocalTime time){
        for(Screening s : currentScreenings){
            if(s.getScreen().getName().equals(screenName)){
                if(s.getTime().isBefore(time) && s.getEndTime().isAfter(time)){
                    selectedScreening = s;
                }
            }
        }
        notifyObservers();
    }

    public void noSelectScreening(){
        selectedScreening = null;
        notifyObservers();
    }

    public boolean cancelSelected(){
        if(this.observerMessage("Confirm Cancelling Screening", true)){
            if(!this.checkSold(this.selectedScreening)){
                currentScreenings.remove(this.selectedScreening);
                cinema.cancelScreening(this.selectedScreening);
                selectedScreening = null;
                this.notifyObservers();
                return true;
            }
        }
        return false;
    }

    public boolean scheduleScreening(LocalDate date, LocalTime time, String title, int runningTime, int year, String screenName){
        Movie movie = MovieMapper.getInstance().getMovie(title, runningTime, year);

        if(!checkDoubleScreening(time, runningTime, screenName, null) &&
                checkTimeAvailable(date, time, movie.getRunningTime(), screenName, null)){
            Screening s = cinema.scheduleScreening(date, time, title, runningTime, year, screenName);
            currentScreenings.add(s);
            this.notifyObservers();
            return true;
        }
        return false;
    }


    public void changeSelected(LocalTime time, String screenName){
        if (selectedScreening != null){

            if(!checkSold(selectedScreening) && !checkDoubleScreening(time, selectedScreening.getMovie().getRunningTime(), screenName, selectedScreening)
                    && checkTimeAvailable(currentDate, time, selectedScreening.getMovie().getRunningTime(), screenName, selectedScreening)){
                Screen screen = cinema.getScreen(screenName);
                selectedScreening.setTime(time);
                System.out.println(time);
                selectedScreening.setScreen(screen);
                cinema.updateScreening(selectedScreening);
                selectedScreening = null;
                notifyObservers();
            }
        }
    }


    // operations on tickets:

    public boolean sellTickets(int ticketNum){
        int nts = selectedScreening.getTicketsSold();
        Screen sc = selectedScreening.getScreen();
        int cp = sc.getCapacity();
        if(!this.checkTicketOverSold(ticketNum, selectedScreening)){
            selectedScreening.setTicketsSold(nts + ticketNum);
            cinema.updateScreening(selectedScreening);
            this.notifyObservers();
            return true;
        }else{
            return false;
        }
    }


    // operations on movies:
    public boolean addMovie(String title, int runningTime, int year){
        if(!this.checkDoubleAdded(title, runningTime, year)){
            cinema.addMovie(title, runningTime, year);
            this.notifyObservers();
            return true;
        }else{
            return false;
        }
    }


    // checking methods:

    // check if a movie is already added
    private boolean checkDoubleAdded(String title, int runningTime, int year){
        if (cinema.checkExistedMovid(title, runningTime, year)){
            this.observerMessage("Existed movie", false);
            return true;
        }
        return false;
    }

    private boolean checkDoubleScreening(LocalTime time, int runningTime, String screenName, Screening sg){
        for (Screening s : currentScreenings){
            if(s != sg && s.getScreen().getName().equals(screenName) && s.getTime().equals(time)){
                observerMessage("Double Screening", false);
                return true;
            }
            if(s != sg && s.getScreen().getName().equals(screenName) && s.getEndTime().equals(time.plusMinutes(runningTime))){
                observerMessage("Double Screening", false);
                return true;
            }
        }
        return false;

//        if(cinema.checkExistedScreening(time, screenName, sg)){
//            return this.observerMessage("Existed Screening", false);
//        }
//        return true;
    }

    private boolean checkTimeAvailable(LocalDate date, LocalTime time, int length, String screenName, Screening screening){
        length += 15;
        LocalTime endBoundary = time.plusMinutes(length);
        LocalTime startBoundary = time.minusMinutes(15);

        List<Screening> screenings = cinema.getScreenings(date);
        for (Screening s : screenings){
            if (s.getScreen().getName().equals(screenName) && s != screening) {
                if (s.getTime().isAfter(startBoundary) && s.getTime().isBefore(endBoundary)){
                    observerMessage("Time is not available", false);
                    return false;
                }
                if (s.getEndTime().isAfter(startBoundary) && s.getEndTime().isBefore(endBoundary)){
                    observerMessage("Time is not available", false);
                    return false;
                }
                if ((s.getEndTime().isAfter(endBoundary) && s.getTime().isBefore(startBoundary))){
                    observerMessage("Time is not available", false);
                    return false;
                }
            }
        }
        return true;
    }

    // check if a screening has already sold tickets
    private boolean checkSold(Screening sg){
        if(sg.getTicketsSold() > 0){
            this.observerMessage("This screening has been sold", false);
            return true;
        }
        return false;
    }

    // check if the number of tickets we want to sell more than the rest of seats in that screen
    private boolean checkTicketOverSold(int ticketNum, Screening sg){
        int nts = sg.getTicketsSold();
        Screen sc = sg.getScreen();
        int cp = sc.getCapacity();
        if(nts + ticketNum > cp){
            this.observerMessage("Over Sold", false);
            return true;
        }
        return false;
    }


    // some 'get' methods

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public  List<Screening> getScreenings(){
        return new ArrayList<>(currentScreenings);
    }

    public Screening getSelectedScreening(){
        return selectedScreening;
    }

    public static List<Screen> getScreens(){
        return Cinema.getScreens();
    }

    public static List<Movie> getMovies() {
        return Cinema.getMovies();
    }
}
