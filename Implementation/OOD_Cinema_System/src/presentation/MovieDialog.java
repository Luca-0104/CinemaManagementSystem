package presentation;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.time.LocalTime;

// An extra class to help create a movie dialog
@SuppressWarnings("restriction")
public class MovieDialog extends Dialog<MovieInfo> {
    private TextField titleField = new TextField();
    private TextField runningTimeField = new TextField();
    private TextField yearField = new TextField();
    private ButtonType buttonTypeOk = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);

    /**
     * Constructor with parameter.
     * If the input does not pass the test, they should be set in the new-popped dialog
     * @param m
     */
    public MovieDialog(MovieInfo m) {
        this();
        titleField.setText(m.title);
        runningTimeField.setText(String.valueOf(m.runningTime));
        yearField.setText(String.valueOf(m.year));
    }

    /**
     * Constructor
     */
    public MovieDialog() {
        super();
        Label label1 = new Label("Title: ");
        Label label2 = new Label("Running Time: ");
        Label label3 = new Label("Year: ");
        getDialogPane().getButtonTypes().add(buttonTypeOk);

        setResultConverter(new Callback<ButtonType, MovieInfo>() {
            @Override public MovieInfo call(ButtonType b) {

                if (b == buttonTypeOk) {
                    return new MovieInfo(titleField.getText(),
                            Integer.parseInt(runningTimeField.getText()),
                            Integer.parseInt(yearField.getText())
                    );
                }

                return null;
            }
        });
        // set dialog instructions and validate the input info
        setTitle("Add New Movie");
        setHeaderText("Please enter the details for the new Movie");
        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });
        runningTimeField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });
        yearField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });

        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(titleField, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(runningTimeField, 2, 2);
        grid.add(label3, 1, 3);
        grid.add(yearField, 2, 3);
        getDialogPane().setContent(grid);

        getDialogPane().lookupButton(buttonTypeOk).setDisable(true);

    }

    /**
     * Validate the input of user
     */
    private void validateInput() {
        boolean disable = titleField.getText().length() > 0 && runningTimeField.getText().length() > 0 && yearField.getText().length() > 0;
        getDialogPane().lookupButton(buttonTypeOk).setDisable(!disable);
    }
}
