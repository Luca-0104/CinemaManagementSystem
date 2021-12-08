package presentation;

import application.domain.ManagementObserver;
import application.domain.ManagementSystem;
import application.domain.Screen;
import application.domain.Screening;
import application.persistency.PersistentScreen;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StaffUI implements ManagementObserver {
    final static int         LEFT_MARGIN   = 50;
    final static int         TOP_MARGIN    = 50;
    final static int         BOTTOM_MARGIN = 50;
    final static int         ROW_HEIGHT    = 30;
    final static int         COL_WIDTH     = 60;
    final static int         PPM           = 2;                     // Pixels per minute
    final static int         PPH           = 60 * PPM;              // Pixels per hours
    final static int         TZERO         = 18;                    // Earliest time shown
    final static int         SLOTS         = 12;                    // Number of booking slots shown
    private ManagementSystem ms;
    private LocalDate displayedDate;
    private List<Screen> screens = new ArrayList<Screen>();
    private int firstX, firstY, currentX, currentY;
    private boolean mouseDown;

    @FXML
    private DatePicker datePicker;
    @FXML private Canvas canvas;
    @FXML private VBox box;

    public void initialize() {
        ms = ManagementSystem.getInstance();
        ms.setDate(LocalDate.now());
        ms.addObserver(this);
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

        box.layout();
        update();

    }

    @Override
    public void update() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        screens = ManagementSystem.getScreens();
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
        //
        for (int i = 0; i < screens.size(); i++) {
            int y = TOP_MARGIN + (i + 1) * ROW_HEIGHT;
            // frontend
            gc.fillText(((PersistentScreen)screens.get(i)).getOid() + " (" + screens.get(i).getCapacity() + ")", 0, y - ROW_HEIGHT / 3);
            gc.strokeLine(LEFT_MARGIN, y, canvas.getWidth(), y);
        }
        LocalTime start = LocalTime.of(18, 0);
        for (int i = 0; i < SLOTS + 1; i++) {
            LocalTime show = start.plusMinutes(i * 30);
            String tmp = show.getHour() + ":" + (show.getMinute() > 9 ? show.getMinute() : "0" + show.getMinute());
            int x = LEFT_MARGIN + i * COL_WIDTH;
            gc.fillText(tmp, x + 15, 40);
            gc.strokeLine(x, TOP_MARGIN, x, canvas.getHeight());
        }
        List<Screening> enumV = ms.getScreenings();
        for (Screening s : enumV) {
            int x = timeToX(s.getTime());
            int y = screenToY(((PersistentScreen)s.getScreen()).getOid());
            gc.setFill(Color.GRAY);
            // frontend
            gc.fillRect(x, y, 4 * COL_WIDTH, ROW_HEIGHT);
            if (s == ms.getSelectedScreening()) {
                gc.setStroke(Color.RED);
                gc.strokeRect(x, y, 4 * COL_WIDTH, ROW_HEIGHT);
                gc.setStroke(Color.BLACK);
            }
            gc.setFill(Color.WHITE);
            // frontend
            gc.fillText(s.getDetails(), x, y + ROW_HEIGHT / 2); // need to distinguish ticket sold condition
        }
        Screening sg = ms.getSelectedScreening();
        if (mouseDown && sg != null) {
            int x = timeToX(sg.getTime()) + currentX - firstX;
            int y = screenToY(((PersistentScreen)sg.getScreen()).getOid()) + currentY - firstY;
            gc.setStroke(Color.RED);
            // frontend
            gc.strokeRect(x, y, 4 * COL_WIDTH, ROW_HEIGHT);
            gc.setStroke(Color.BLACK);
        }
    }

    private int timeToX(LocalTime time) {
        return LEFT_MARGIN + PPH * (time.getHour() - TZERO) + PPM * time.getMinute();
    }

    private LocalTime xToTime(int x) {
        x -= LEFT_MARGIN;
        int h = Math.max(0, (x / PPH) + TZERO);
        int m = Math.max(0, (x % PPH) / PPM);
        return LocalTime.of(h, m);
    }

    private int screenToY(int screen) {// this assumes that the tables are continuously numbered from 1 to n-1
        return TOP_MARGIN + (ROW_HEIGHT * (screen - 1));
    }

    private int yToScreen(int y) {
        return ((y - TOP_MARGIN) / ROW_HEIGHT) + 1;
    }

    public void nextDay() {
        displayedDate = datePicker.getValue();
        displayedDate = displayedDate.plusDays(1);
        datePicker.setValue(displayedDate);
        ms.setDate(displayedDate);
    }

    public void prevDay() {
        displayedDate = datePicker.getValue();
        displayedDate = displayedDate.minusDays(1);
        datePicker.setValue(displayedDate);
        ms.setDate(displayedDate);
    }

    public void showDate() {
        displayedDate = datePicker.getValue();
        ms.setDate(displayedDate);
    }

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

    public void showSellTicketDialog() {
        Dialog sellTicket = new Dialog();
        sellTicket.setTitle("Sell Tickets");
        sellTicket.setHeaderText("Please enter the details for the ticket selling");

        Label label = new Label("Ticket Number: ");
        Integer[] ticketChoice = { 1, 2, 3, 4, 5 };
        ChoiceBox<Integer> cb = new ChoiceBox<Integer>(FXCollections.observableArrayList(ticketChoice));

        GridPane grid = new GridPane();
        grid.add(label, 1, 1);
        grid.add(cb, 2, 1);
        sellTicket.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        sellTicket.getDialogPane().getButtonTypes().add(buttonTypeOk);

        Optional<ScreeningInfo> result = sellTicket.showAndWait();

        if (result.isPresent()) {
            ms.sellTickets(cb.getValue());
        }
    }

}
