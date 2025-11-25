package com.example.gpa2207020;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.net.URL;

public class HelloController {

    // Start GPA: go to GPA form (where you can add/update/delete)
    @FXML
    private void handleStartGpaCalculator(ActionEvent event) {
        try {
            URL fxml = HelloController.class.getResource("gpa-form-view.fxml");
            if (fxml == null) {
                showError("FXML Not Found", "Could not find gpa-form-view.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxml);
            Parent root = loader.load();

            // start a "new session" on GPA page (clear table, ready to add)
            GpaFormController controller = loader.getController();
            controller.startNewSession();

            Scene scene = new Scene(root, 1050, 650);
            URL css = HelloController.class.getResource("gpa-style.css");
            if (css != null) {
                scene.getStylesheets().add(css.toExternalForm());
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("GPA Calculator - Course Entry");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error Opening GPA Screen",
                    "Could not open GPA calculation page:\n" + e.getMessage());
        }
    }

    // Show History: go to separate history page
    @FXML
    private void handleShowHistory(ActionEvent event) {
        try {
            URL fxml = HelloController.class.getResource("history-view.fxml");
            if (fxml == null) {
                showError("FXML Not Found", "Could not find history-view.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxml);
            Parent root = loader.load();

            Scene scene = new Scene(root, 900, 600);
            URL css = HelloController.class.getResource("gpa-style.css");
            if (css != null) {
                scene.getStylesheets().add(css.toExternalForm());
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("GPA History");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error Opening History Screen",
                    "Could not open history page:\n" + e.getMessage());
        }
    }

    private void showError(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
