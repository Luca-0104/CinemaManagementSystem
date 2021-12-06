package application.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    }

    public void cancelSelected(){

    }

    public void scheduleScreening(){

    }

    public void cancelScreening(){

    }

    public void updateScreening(){

    }


    // operations on tickets:

    public void sellTickets(){

    }


    // operations on movies:
    public void addMovie(String title, int runningTime, int year){

    }


    // checking methods:

    // check if a movie is already added
    private boolean checkDoubleAdded(String title, int runningTime, int year){
        return false;
    }

    private boolean checkLongerScreening(){
        return false;
    }

    private boolean checkDoubleScreening(LocalTime time, String screenName, Screening ignore){
        return false;
    }

    private boolean checkTimeAvailable(){
        return false;
    }

    // check if a screening has already sold tickets
    private boolean checkSold(Screening sg){
        return false;
    }

    // check if the number of tickets we want to sell more than the rest of seats in that screen
    private boolean checkTicketOverSold(int ticketNum, Screening sg){
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
}
