package com.example.gpa2207020;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class HelloController {

    @FXML private TableView<Course> table;
    @FXML private TableColumn<Course, String> nameCol;
    @FXML private TableColumn<Course, Double> creditsCol;
    @FXML private TableColumn<Course, String> gradeCol;

    @FXML private TextField nameField;
    @FXML private TextField creditsField;
    @FXML private ComboBox<String> gradeBox;
    @FXML private Label gpaLabel;

    private final ObservableList<Course> courses = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Table columns mapping
        nameCol.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        creditsCol.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getCredits()));
        gradeCol.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getLetterGrade()));

        table.setItems(courses);

        // Grade options
        gradeBox.getItems().addAll(
                "A+", "A", "A-",
                "B+", "B", "B-",
                "C+", "C", "C-",
                "D", "F"
        );

        gpaLabel.setText("Current GPA: 0.00");
    }

    @FXML
    private void handleAddCourse() {
        String name = nameField.getText().trim();
        String creditsText = creditsField.getText().trim();
        String grade = gradeBox.getValue();

        if (name.isEmpty() || creditsText.isEmpty() || grade == null) {
            showAlert(Alert.AlertType.WARNING, "Input Error",
                    "Please enter course name, credits, and select a grade.");
            return;
        }

        double credits;
        try {
            credits = Double.parseDouble(creditsText);
            if (credits <= 0) {
                showAlert(Alert.AlertType.WARNING, "Input Error",
                        "Credits must be a positive number.");
                return;
            }
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.WARNING, "Input Error",
                    "Credits must be a number (e.g. 3 or 3.5).");
            return;
        }

        Course course = new Course(name, credits, grade);
        courses.add(course);

        nameField.clear();
        creditsField.clear();
        gradeBox.setValue(null);

        updateGpa();
    }

    @FXML
    private void handleRemoveCourse() {
        Course selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.INFORMATION, "No Selection",
                    "Please select a course to remove.");
            return;
        }
        courses.remove(selected);
        updateGpa();
    }

    private void updateGpa() {
        if (courses.isEmpty()) {
            gpaLabel.setText("Current GPA: 0.00");
            return;
        }

        double totalQualityPoints = 0.0;
        double totalCredits = 0.0;

        for (Course c : courses) {
            double gradePoint = letterToPoint(c.getLetterGrade());
            totalQualityPoints += gradePoint * c.getCredits();
            totalCredits += c.getCredits();
        }

        if (totalCredits == 0) {
            gpaLabel.setText("Current GPA: 0.00");
        } else {
            double gpa = totalQualityPoints / totalCredits;
            gpaLabel.setText(String.format("Current GPA: %.2f", gpa));
        }
    }

    private double letterToPoint(String grade) {
        if (grade == null) return 0.0;

        switch (grade) {
            case "A+":
            case "A":
                return 4.0;
            case "A-":
                return 3.7;
            case "B+":
                return 3.3;
            case "B":
                return 3.0;
            case "B-":
                return 2.7;
            case "C+":
                return 2.3;
            case "C":
                return 2.0;
            case "C-":
                return 1.7;
            case "D":
                return 1.0;
            case "F":
            default:
                return 0.0;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
