package presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CinemaSystemApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        try {
            Button button_admin = new Button("I am Administrator");
            button_admin.setLayoutX(300);
            button_admin.setLayoutY(300);
            button_admin.setPrefSize(200, 50);

            Button button_staff = new Button("I am Staff");
            button_staff.setLayoutX(600);
            button_staff.setLayoutY(300);
            button_staff.setPrefSize(200, 50);

            button_admin.setOnAction(event -> {
                try {
                    URL url_admin = new File("Implementation/OOD_Cinema_System/src/guiAdmin.fxml").toURI().toURL();
                    VBox box_admin = (VBox) FXMLLoader.load(url_admin);// loads the GUI from the file and creates the StaffUI controller
                    Scene scene_admin = new Scene(box_admin, StaffUI.LEFT_MARGIN + (StaffUI.SLOTS * StaffUI.COL_WIDTH), StaffUI.TOP_MARGIN + 10 * StaffUI.ROW_HEIGHT);
                    scene_admin.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

                    primaryStage.setScene(scene_admin);
                    primaryStage.setHeight(StaffUI.TOP_MARGIN + 10 * StaffUI.ROW_HEIGHT + 120);
                    primaryStage.setWidth(StaffUI.LEFT_MARGIN + (StaffUI.SLOTS * StaffUI.COL_WIDTH) + 60);
                    primaryStage.setResizable(true);
                    primaryStage.show();
                    scene_admin.setFill(Color.BROWN);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            button_staff.setOnAction(event -> {
                try {
                    URL url_staff = new File("Implementation/OOD_Cinema_System/src/guiStaff.fxml").toURI().toURL();
                    VBox box_staff = (VBox) FXMLLoader.load(url_staff);// loads the GUI from the file and creates the StaffUI controller
                    Scene scene_staff = new Scene(box_staff, StaffUI.LEFT_MARGIN + (StaffUI.SLOTS * StaffUI.COL_WIDTH), StaffUI.TOP_MARGIN + 10 * StaffUI.ROW_HEIGHT);
                    scene_staff.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

                    primaryStage.setScene(scene_staff);
                    primaryStage.setHeight(StaffUI.TOP_MARGIN + 10 * StaffUI.ROW_HEIGHT + 120);
                    primaryStage.setWidth(StaffUI.LEFT_MARGIN + (StaffUI.SLOTS * StaffUI.COL_WIDTH) + 60);
                    primaryStage.setResizable(true);
                    primaryStage.show();
                    scene_staff.setFill(Color.BROWN);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            Group group = new Group();
            group.getChildren().add(button_admin);
            group.getChildren().add(button_staff);

            Scene scene = new Scene(group);
            primaryStage.setScene(scene);
            primaryStage.setHeight(600);
            primaryStage.setWidth(600);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
