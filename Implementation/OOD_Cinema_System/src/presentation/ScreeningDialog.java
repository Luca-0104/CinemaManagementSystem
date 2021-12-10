package presentation;

import application.domain.ManagementSystem;
import application.domain.Movie;
import application.domain.Screening;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ScreeningDialog extends Dialog<ScreeningInfo>{
    private LocalDate displayedDate = LocalDate.now();
    // This list represents all available time selections for adding a new screening
    private String[] times = { "12:00", "12:15", "12:30", "12:45",
                               "13:00", "13:15", "13:30", "13:45",
                               "14:00", "14:15", "14:30", "14:45",
                               "15:00", "15:15", "15:30", "15:45",
                               "16:00", "16:15", "16:30", "16:45",
                               "17:00", "17:15", "17:30", "17:45",
                               "18:00", "18:15", "18:30", "18:45",
                               "19:00", "19:15", "19:30", "19:45",
                               "20:00", "20:15", "20:30", "20:45",
                               "21:00", "21:15", "21:30", "21:45",
                               "22:00"
    };
    private ChoiceBox<String> timeBox = new ChoiceBox<String>(FXCollections.observableArrayList(times));
    // ten screens at all
    private Integer[] screenList = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    private ChoiceBox<Integer> screenBox = new ChoiceBox<Integer>(FXCollections.observableArrayList(screenList));
    private List<Movie> movies;
    private String[] movieList = new String[ManagementSystem.getMovies().size()];
    private ChoiceBox<String> movieBox;
    private ButtonType buttonTypeOk = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);

    /**
     * Constructor with parameter.
     * If the input does not pass the test, they should be set in the new-popped dialog
     * @param s
     */
    public ScreeningDialog(ScreeningInfo s) {
        this();
        timeBox.getSelectionModel().select(s.time.toString());
        screenBox.getSelectionModel().select(Integer.valueOf(s.screenNumber));
        movieBox.getSelectionModel().select(s.movieNumber);
    }

    /**
     * Constructor
     */
    public ScreeningDialog() {
        super();
        // Get all movies in database and display in a readable way
        movies = ManagementSystem.getMovies();
        for (int i = 0; i < movies.size(); i++) {
            movieList[i] = movies.get(i).toString();
        }
        movieBox = new ChoiceBox<String>(FXCollections.observableArrayList(movieList));

        Label label1 = new Label("Time: ");
        Label label2 = new Label("Screen: ");
        Label label3 = new Label("Movie: ");
        getDialogPane().getButtonTypes().add(buttonTypeOk);

        setResultConverter(new Callback<ButtonType, ScreeningInfo>() {
            @Override public ScreeningInfo call(ButtonType b) {

                if (b == buttonTypeOk) {
                    return new ScreeningInfo(displayedDate, LocalTime.parse(timeBox.getValue()),
                            screenBox.getValue(), Integer.parseInt(movieBox.getValue().split(" ")[movieBox.getValue().split(" ").length - 1]));
                }

                return null;
            }
        });
        // set dialog instructions and validate the input info
        setTitle("New Screening");
        setHeaderText("Please enter the details for the new Screening");
        timeBox.getSelectionModel().selectedIndexProperty().addListener((e) -> {
            validateInput();
        });
        screenBox.getSelectionModel().selectedIndexProperty().addListener((e) -> {
            validateInput();
        });
        movieBox.getSelectionModel().selectedIndexProperty().addListener((e) -> {
            validateInput();
        });
        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(timeBox, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(screenBox, 2, 2);
        grid.add(label3, 1, 3);
        grid.add(movieBox, 2, 3);
        getDialogPane().setContent(grid);

        getDialogPane().lookupButton(buttonTypeOk).setDisable(true);

    }

    /**
     * Validate the input of user
     */
    private void validateInput() {
        boolean disable = !timeBox.getSelectionModel().isEmpty() && !screenBox.getSelectionModel().isEmpty()
                && !movieBox.getSelectionModel().isEmpty();
        getDialogPane().lookupButton(buttonTypeOk).setDisable(!disable);
    }
}
