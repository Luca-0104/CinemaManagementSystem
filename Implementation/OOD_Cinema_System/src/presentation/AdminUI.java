package presentation;

import application.domain.*;
import application.persistency.PersistentScreen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminUI implements ManagementObserver {
    final static int         LEFT_MARGIN   = 100;
    final static int         TOP_MARGIN    = 50;
    final static int         BOTTOM_MARGIN = 50;
    final static int         ROW_HEIGHT    = 50;
    final static int         COL_WIDTH     = 60;
    final static int         PPM           = 2;                     // Pixels per minute
    final static int         PPH           = 60 * PPM;              // Pixels per hours
    final static int         TZERO         = 12;                    // Earliest time shown
    final static int         SLOTS         = 24;                    // Number of booking slots shown
    private ManagementSystem ms;
    private LocalDate displayedDate;
    private List<Movie> movies = new ArrayList<Movie>();
    private List<Screen> screens = new ArrayList<Screen>();
    private int              firstX, firstY, currentX, currentY;
    private boolean          mouseDown;

    @FXML
    private DatePicker datePicker;
    @FXML private Canvas canvas;
    @FXML private VBox box;

    /**
     * This methods is called after the constructor and after any FXML instance variable have been injected
     */
    public void initialize() {
        ms = ManagementSystem.getInstance();
        ms.setDate(LocalDate.now());
        ms.addObserver(this);
        movies = ManagementSystem.getMovies();
        screens = ManagementSystem.getScreens();
        datePicker.setValue(LocalDate.now());
        displayedDate = LocalDate.now();

        // code to be executed when mouse is pressed
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            currentX = firstX = (int) e.getX();
            currentY = firstY = (int) e.getY();
            if (e.getButton() == MouseButton.PRIMARY) {
                mouseDown = true;
                ms.selectScreening(screens.get(yToScreen(firstY) - 1).getName(), xToTime(firstX));
            }
        });
        // code to be executed when mouse is released
        canvas.addEventFilter(MouseEvent.MOUSE_RELEASED, (e) -> {
            mouseDown = false;
            Screening s = ms.getSelectedScreening();
            if (s != null && (currentX != firstX || yToScreen(currentY) != ((PersistentScreen)s.getScreen()).getOid())) {
                ms.changeSelected(xToTime(timeToX(s.getTime()) + currentX - firstX), screens.get(yToScreen(currentY) - 1).getName());
            }
        });
        // code to be executed when mouse is dragged
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, (e) -> {
            currentX = (int) e.getX();
            currentY = (int) e.getY();
            update();
        });
        box.layout();
        update();

    }

    /**
     * This is the method that draws the management system canvas (admin)
     */
    @Override
    public void update() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        screens = ManagementSystem.getScreens();
        movies = ManagementSystem.getMovies();
        canvas.setHeight(TOP_MARGIN + screens.size() * ROW_HEIGHT);
        canvas.setWidth(LEFT_MARGIN + (SLOTS * COL_WIDTH));
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setLineWidth(2.0);
        gc.setFill(Color.BLACK);
        //
        // // Draw screen outlines
        //
        gc.strokeLine(LEFT_MARGIN, 0, LEFT_MARGIN, canvas.getHeight());
        gc.strokeLine(0, TOP_MARGIN, canvas.getWidth(), TOP_MARGIN);
        for (int i = 0; i < screens.size(); i++) {
            int y = TOP_MARGIN + (i + 1) * ROW_HEIGHT;
            // The left-most column, presenting the information of screen (oid, name, capacity)
            gc.fillText("("+((PersistentScreen)screens.get(i)).getOid()+") "+ screens.get(i).getName() +
                    "\nCapacity: " + screens.get(i).getCapacity(), 5, y - ROW_HEIGHT / 3 - 10);
            gc.strokeLine(LEFT_MARGIN, y, canvas.getWidth(), y);
        }
        LocalTime start = LocalTime.of(12, 0);
        for (int i = 0; i < SLOTS + 1; i++) {
            LocalTime show = start.plusMinutes(i * 30);
            String tmp = show.getHour() + ":" + (show.getMinute() > 9 ? show.getMinute() : "0" + show.getMinute());
            int x = LEFT_MARGIN + i * COL_WIDTH;
            gc.fillText(tmp, x, 40);
            gc.strokeLine(x, TOP_MARGIN, x, canvas.getHeight());
        }
        List<Screening> enumV = ms.getScreenings();
        // draw the figure representing a screening. The duration is represented by a rectangle and information will be
        // written on the pink rectangle.
        for (Screening s : enumV) {
            int x = timeToX(s.getTime());
            int y = screenToY(((PersistentScreen)s.getScreen()).getOid());
            gc.setFill(Color.rgb(244, 143, 177 ));
            float proportion = (float) s.getMovie().getRunningTime()/120;
            gc.fillRect(x, y, 4 * COL_WIDTH * proportion, ROW_HEIGHT);
            // If this screening is selected, use red border to symbolize that
            if (s == ms.getSelectedScreening()) {
                gc.setStroke(Color.RED);
                gc.strokeRect(x, y, 4 * COL_WIDTH * proportion, ROW_HEIGHT);
                gc.setStroke(Color.BLACK);
            }
            gc.setFill(Color.WHITE);
            // set the text on the rectangle
            gc.fillText("Title: "+s.getMovie().getTitle()+" Length: "+s.getMovie().getRunningTime()+"mins"
                    +"\nStart from: "+s.getTime(), x, y + ROW_HEIGHT / 2);
        }
        Screening sg = ms.getSelectedScreening();
        // Once a screening is dragged, use another red border to show where it is moving to
        if (mouseDown && sg != null) {
            int x = timeToX(sg.getTime()) + currentX - firstX;
            int y = screenToY(((PersistentScreen)sg.getScreen()).getOid()) + currentY - firstY;
            gc.setStroke(Color.RED);
            float proportion = (float) sg.getMovie().getRunningTime()/120;
            gc.strokeRect(x, y, 4 * COL_WIDTH * proportion, ROW_HEIGHT);
            gc.setStroke(Color.BLACK);
        }
    }

    /**
     * Transform time to x coordinate value
     * @param time
     * @return integer x
     */
    private int timeToX(LocalTime time) {
        return LEFT_MARGIN + PPH * (time.getHour() - TZERO) + PPM * time.getMinute();
    }

    /**
     * Transform x coordinate value to time
     * @param x
     * @return time
     */
    private LocalTime xToTime(int x) {
        x -= LEFT_MARGIN;
        int h = Math.max(0, (x / PPH) + TZERO);
        int m = Math.max(0, (x % PPH) / PPM);
        return LocalTime.of(h, m);
    }

    /**
     * Transform screen to y coordinate value
     * @param screen
     * @return y
     */
    private int screenToY(int screen) {// this assumes that the tables are continuously numbered from 1 to n-1
        return TOP_MARGIN + (ROW_HEIGHT * (screen - 1));
    }

    /**
     * Transform y coordinate to screen
     * @param y
     * @return screen
     */
    private int yToScreen(int y) {
        return ((y - TOP_MARGIN) / ROW_HEIGHT) + 1;
    }

    /**
     * Display the next day
     */
    public void nextDay() {
        displayedDate = datePicker.getValue();
        displayedDate = displayedDate.plusDays(1);
        datePicker.setValue(displayedDate);
        ms.setDate(displayedDate);
    }

    /**
     * Display the previous day
     */
    public void prevDay() {
        displayedDate = datePicker.getValue();
        displayedDate = displayedDate.minusDays(1);
        datePicker.setValue(displayedDate);
        ms.setDate(displayedDate);
    }

    /**
     * Display the date picker to choose date
     */
    public void showDate() {
        displayedDate = datePicker.getValue();
        ms.setDate(displayedDate);
    }

    /**
     * Transit the request to Management System of canceling a screening
     */
    public void cancelScreening() {
        ms.cancelSelected();
    }

    /**
     * Transit the request to Management System of canceling selecting currently selected screening
     */
    public void cancelSelection() {
        ms.noSelectScreening();
    }

    /**
     * Set the message content
     * @param s
     * @param confirm
     * @return boolean
     */
    @Override
    public boolean message(String s, boolean confirm) {
        Alert alert;
        if (confirm) {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
        } else {
            alert = new Alert(Alert.AlertType.WARNING);
        }
        alert.setContentText(s);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Apply a separate class to create a dialog for adding movie.
     * If the result presented does not meet the requirement, a new movie dialog will appear
     * to let user re-enter the information.
     */
    public void showAddMovieDialog() {
        MovieDialog addMovie = new MovieDialog();
        Optional<MovieInfo> result = addMovie.showAndWait();

        if (result.isPresent()) {
            MovieInfo m = result.get();
            // transit the info, let management system check whether this movie can be added
            if (!ms.addMovie(m.title, m.runningTime, m.year)) {
                showAddMovieDialog(m);
            }
            ;
        }
    }

    /**
     * Once the result presented does not meet the requirement, a new movie dialog will be created
     * by this method to let user re-enter the information to add movie.
     * @param m2
     */
    private void showAddMovieDialog(MovieInfo m2) {
        MovieDialog addMovie = new MovieDialog(m2);
        Optional<MovieInfo> result = addMovie.showAndWait();

        if (result.isPresent()) {
            MovieInfo m = result.get();
            // transit the info, let management system check whether this movie can be added
            if (!ms.addMovie(m.title, m.runningTime, m.year)) {
                showAddMovieDialog(m);
            }
            ;
        }
    }

    /**
     * Apply a separate class to create a dialog for adding screening.
     * If the result presented does not meet the requirement, a new screening dialog will appear
     * to let user re-enter the information.
     */
    public void showAddScreeningDialog() {
        ScreeningDialog addScreening = new ScreeningDialog();
        Optional<ScreeningInfo> result = addScreening.showAndWait();

        if (result.isPresent()) {
            ScreeningInfo s = result.get();
            // transit the info, let management system check whether this screening can be added
            if (!ms.scheduleScreening(displayedDate,
                                      s.time,
                                      movies.get(s.movieNumber - 1).getTitle(),
                                      movies.get(s.movieNumber - 1).getRunningTime(),
                                      movies.get(s.movieNumber - 1).getYear(),
                                      screens.get(s.screenNumber - 1).getName())) {
                showAddScreeningDialog(s);
            }
        }
    }

    /**
     * Once the result presented does not meet the requirement, a new screening dialog will be created
     * by this method to let user re-enter the information to add screening.
     * @param s2
     */
    private void showAddScreeningDialog(ScreeningInfo s2) {
        ScreeningDialog addScreening = new ScreeningDialog(s2);
        Optional<ScreeningInfo> result = addScreening.showAndWait();

        if (result.isPresent()) {
            ScreeningInfo s = result.get();
            // transit the info, let management system check whether this screening can be added
            if (!ms.scheduleScreening(displayedDate,
                                      s.time,
                                      movies.get(s.movieNumber - 1).getTitle(),
                                      movies.get(s.movieNumber - 1).getRunningTime(),
                                      movies.get(s.movieNumber - 1).getYear(),
                                      screens.get(s.screenNumber - 1).getName())) {
                showAddScreeningDialog(s);
            }
        }
    }


}
