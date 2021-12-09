package presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
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
            button_admin.setLayoutX(100);
            button_admin.setLayoutY(100);
            button_admin.setPrefSize(150, 50);

            Button button_staff = new Button("I am Staff(Teller)");
            button_staff.setLayoutX(350);
            button_staff.setLayoutY(100);
            button_staff.setPrefSize(150, 50);

            button_admin.setOnAction(event -> {
                try {
                    URL url_admin = new File("Implementation/OOD_Cinema_System/src/guiAdmin.fxml").toURI().toURL();
                    VBox box_admin = (VBox) FXMLLoader.load(url_admin);// loads the GUI from the file and creates the AdminUI controller
                    Scene scene_admin = new Scene(box_admin, AdminUI.LEFT_MARGIN + (AdminUI.SLOTS * AdminUI.COL_WIDTH), AdminUI.TOP_MARGIN + 10 * AdminUI.ROW_HEIGHT);
                    scene_admin.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

                    primaryStage.setScene(scene_admin);
                    primaryStage.setTitle("Administrator");
                    primaryStage.setX(300);
                    primaryStage.setY(200);
                    primaryStage.setHeight(AdminUI.TOP_MARGIN + 10 * AdminUI.ROW_HEIGHT + 100);
                    primaryStage.setWidth(AdminUI.LEFT_MARGIN + (AdminUI.SLOTS * AdminUI.COL_WIDTH) + 40);
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
                    primaryStage.setTitle("Teller");
                    primaryStage.setX(300);
                    primaryStage.setY(200);
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
            group.getChildren().addAll(button_admin, button_staff);

            Scene scene = new Scene(group);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Cinema Management System");
            primaryStage.setHeight(300);
            primaryStage.setWidth(600);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
