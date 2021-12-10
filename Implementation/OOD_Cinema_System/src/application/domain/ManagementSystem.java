package application.domain;

import application.persistency.MovieMapper;
import application.persistency.PersistentScreening;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class ManagementSystem {
    //Attributes:

    private LocalDate currentDate;
    //Associations:
    private Cinema cinema = null;
    // currentScreenings is a list of screenings in a specific day
    private List<Screening> currentScreenings;
    // selectedScreening represents the screening selected by user in UI
    private Screening selectedScreening;
    // store observers needed to be notified
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

    // add objects needed to be notified to the observer list,
    // so that they will be notified after changes are made
    public void addObserver(ManagementObserver o) {
        observers.add(o);
    }

    // after making changes, this method will be called to notify all the added overserver
    public void notifyObservers() {
        // loop through to tell all the  observers to update
        for (ManagementObserver mo : observers) {
            mo.update();
        }
    }

    // used to send message to observer
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

    // set selectedScreening to an exist screening with given screen name and time
    // the parameter time needs to be inside the running period of the going to be selected screening
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

    // unselect a screening
    public void noSelectScreening(){
        selectedScreening = null;
        notifyObservers();
    }

    /*
    cancel the selected screening from database
    if the selected screening has been sold, the cancel operation will be invalid and a warning will be sent
     */
    public boolean cancelSelected(){
        if (selectedScreening != null) {
            // user confirms the cancelling operation
            if (this.observerMessage("Confirm Cancelling Screening", true)) {
                // confirmed and check whether been sold
                if (!this.checkSold(this.selectedScreening)) {
                    // remove screening
                    currentScreenings.remove(this.selectedScreening);
                    cinema.cancelScreening(this.selectedScreening);
                    selectedScreening = null;
                    this.notifyObservers();
                    return true;
                }
            }
        } else {
            this.observerMessage("Please Select a Screening First", false);
        }
        return false;
    }

    /*
    Used to schedule a new Screening with given date, time, movie title, running time, made year, screen name
    If there are conflicts with other existed screenings or there already is a same screening,
    this screening will not be added, and the corresponding error will be popped out
     */
    public boolean scheduleScreening(LocalDate date, LocalTime time, String title, int runningTime, int year, String screenName){
        // get movie object
        Movie movie = MovieMapper.getInstance().getMovie(title, runningTime, year);
        // doing checks
        if(!checkDoubleScreening(time, runningTime, screenName, null) &&
                checkTimeAvailable(date, time, movie.getRunningTime(), screenName, null)){
            // no conflict, ask cinema to add screening
            Screening s = cinema.scheduleScreening(date, time, title, runningTime, year, screenName);
            currentScreenings.add(s);
            this.notifyObservers();
            return true;
        }
        return false;
    }

    /*
    This method is used to change the selected screening to the given time and screen
    However, if tickets of this screening already have been sold, this screening cannot be modified
    The rearrenged time also cannot have conflict with other screenings, such as double added, and not available time
     */
    public void changeSelected(LocalTime time, String screenName){
        if (selectedScreening != null){
            // check whether tickets were sold, whether there is the same screen, and whether the changed time is available
            if(!checkSold(selectedScreening) && !checkDoubleScreening(time, selectedScreening.getMovie().getRunningTime(), screenName, selectedScreening)
                    && checkTimeAvailable(currentDate, time, selectedScreening.getMovie().getRunningTime(), screenName, selectedScreening)){
                Screen screen = cinema.getScreen(screenName);
                selectedScreening.setTime(time);
                selectedScreening.setScreen(screen);
                cinema.updateScreening(selectedScreening);
                selectedScreening = null;
                notifyObservers();
            }
        }
    }


    // used to reduce the remaining ticket number if the left tickets number is greater than ticketNum
    public boolean sellTickets(int ticketNum){
        if (selectedScreening != null) {
            int nts = selectedScreening.getTicketsSold();
            if (!this.checkTicketOverSold(ticketNum, selectedScreening)) {
                selectedScreening.setTicketsSold(nts + ticketNum);
                cinema.updateScreening(selectedScreening);
                this.notifyObservers();
                return true;
            } else {
                return false;
            }
        } else {
            this.observerMessage("Please Select a Screening First", false);
        }
        return false;
    }

    // add movie to the database, the unit of runningTime is minute
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

    /*
    check if a movie is already added
    return true if already existed
    return false if there isn't a same movie
     */
    private boolean checkDoubleAdded(String title, int runningTime, int year){
        if (cinema.checkExistedMovid(title, runningTime, year)){
            this.observerMessage("Existed movie", false);
            return true;
        }
        return false;
    }

    /*
    check whether there will be double added situation
    return true, if a same screening was existed
    otherwise, return false
     */
    private boolean checkDoubleScreening(LocalTime time, int runningTime, String screenName, Screening sg){
        //if this checking happens in "changScreening" operation
        if (sg != null){
            for (Screening s : currentScreenings){
                //we will check if the screening overlaps with itself
                if (((PersistentScreening)s).getOid() != ((PersistentScreening)sg).getOid()) {
                    if (!s.equals(sg) && s.getScreen().getName().equals(screenName) && s.getTime().equals(time)) {
                        observerMessage("Double Screening", false);
                        return true;
                    }
                    if (!s.equals(sg) && s.getScreen().getName().equals(screenName) && s.getEndTime().equals(time.plusMinutes(runningTime))) {
                        observerMessage("Double Screening", false);
                        return true;
                    }
                }
            }
            return false;

        //if this checking happens in "scheduleScreening" operation
        }else{
            for (Screening s : currentScreenings){
                //we do not need to check if the screening overlaps with itself
                if (!s.equals(sg) && s.getScreen().getName().equals(screenName) && s.getTime().equals(time)) {
                    observerMessage("Double Screening", false);
                    return true;
                }
                if (!s.equals(sg) && s.getScreen().getName().equals(screenName) && s.getEndTime().equals(time.plusMinutes(runningTime))) {
                    observerMessage("Double Screening", false);
                    return true;
                }
            }
            return false;
        }


    }

    /*
    check whether the selected time has overlap with other arranged screenings
    if overlap exists, will return false
    otherwise will return true
     */
    private boolean checkTimeAvailable(LocalDate date, LocalTime time, int length, String screenName, Screening screening){
        length += 15;
        LocalTime endBoundary = time.plusMinutes(length);
        LocalTime startBoundary = time.minusMinutes(15);
        //get all the screenings on a particular date
        List<Screening> screenings = cinema.getScreenings(date);

        //if this checking happens in "changScreening" operation
        if(screening != null){
            //iterate the list of screenings
            for (Screening s : screenings){
                //we will check if the screening overlaps with itself
                if (((PersistentScreening)s).getOid() != ((PersistentScreening)screening).getOid()) {
                    if (s.getScreen().getName().equals(screenName) && !s.equals(screening)) {
                        //check whether the start time of an existed screening is in the not allowed boundary of the going to be added screening
                        if (s.getTime().isAfter(startBoundary) && s.getTime().isBefore(endBoundary)){
                            observerMessage("Time is not available", false);
                            return false;
                        }
                        //check whether the end time of an existed screening is in the not allowed boundary of the going to be added screening
                        if (s.getEndTime().isAfter(startBoundary) && s.getEndTime().isBefore(endBoundary)){
                            observerMessage("Time is not available", false);
                            return false;
                        }
                        //check whether an existed screening completely covers the timespan
                        if ((s.getEndTime().isAfter(endBoundary) && s.getTime().isBefore(startBoundary))){
                            observerMessage("Time is not available", false);
                            return false;
                        }
                    }
                }
            }
            return true;
        //if this checking happens in "scheduleScreening" operation
        }else{
            //iterate the list of screenings
            for (Screening s : screenings){
                //we do not need to check if the screening overlaps with itself
                if (s.getScreen().getName().equals(screenName) && !s.equals(screening)) {
                    //check whether the start time of an existed screening is in the not allowed boundary of the going to be added screening
                    if (s.getTime().isAfter(startBoundary) && s.getTime().isBefore(endBoundary)){
                        observerMessage("Time is not available", false);
                        return false;
                    }
                    //check whether the end time of an existed screening is in the not allowed boundary of the going to be added screening
                    if (s.getEndTime().isAfter(startBoundary) && s.getEndTime().isBefore(endBoundary)){
                        observerMessage("Time is not available", false);
                        return false;
                    }
                    //check whether an existed screening completely covers the timespan
                    if ((s.getEndTime().isAfter(endBoundary) && s.getTime().isBefore(startBoundary))){
                        observerMessage("Time is not available", false);
                        return false;
                    }
                }
            }
            return true;
        }



    }

    /*
    check if a screening has already sold tickets
    if tickets have been sold, this method will return true
    if haven't been sold, will return false
     */
    private boolean checkSold(Screening sg){
        if(sg.getTicketsSold() > 0){
            this.observerMessage("This screening has been sold", false);
            return true;
        }
        return false;
    }

    /*
    check if the number of tickets we want to sell more than the rest of seats in that screen
    if the number of ordered tickets is greater than remaining tickets, this method will return true
    otherwise, this returns false
     */
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


    // 'get' methods

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
