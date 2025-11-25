package com.example.gpa2207020;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ResultController {

    @FXML private Label gpaLabel;
    @FXML private Label totalCreditsLabel;

    @FXML private TableView<Course> resultTable;
    @FXML private TableColumn<Course, String> nameCol;
    @FXML private TableColumn<Course, String> codeCol;
    @FXML private TableColumn<Course, Double> creditsCol;
    @FXML private TableColumn<Course, String> teacher1Col;
    @FXML private TableColumn<Course, String> teacher2Col;
    @FXML private TableColumn<Course, String> gradeCol;

    private final ObservableList<Course> courses = FXCollections.observableArrayList();

    @FXML
    public void initialize() {


        nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        codeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCode()));
        creditsCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getCredits()));
        teacher1Col.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTeacher1()));
        teacher2Col.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTeacher2()));
        gradeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLetterGrade()));

        resultTable.setItems(courses);
    }


    public void setData(List<Course> courseList, double gpa, double totalCredits) {
        courses.setAll(courseList);

        gpaLabel.setText(String.format("%.2f", gpa));
        totalCreditsLabel.setText(String.format("%.1f", totalCredits));
    }


    @FXML
    private void handleBackToHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 800, 500);
            scene.getStylesheets().add(getClass().getResource("gpa-style.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("GPA Calculator - Home");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}