package com.example.gpa2207020;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloController {

    @FXML
    private void handleStartGpaCalculator(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gpa-form-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1050, 650);
            scene.getStylesheets().add(getClass().getResource("gpa-style.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("GPA Calculator - Course Entry");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
