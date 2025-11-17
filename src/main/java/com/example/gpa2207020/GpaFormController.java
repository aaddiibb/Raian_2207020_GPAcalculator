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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GpaFormController {

    @FXML private TextField targetCreditsField;
    @FXML private Label currentCreditsLabel;

    @FXML private TextField courseNameField;
    @FXML private TextField courseCodeField;
    @FXML private TextField courseCreditField;
    @FXML private TextField teacher1Field;
    @FXML private TextField teacher2Field;
    @FXML private ComboBox<String> gradeBox;

    @FXML private Label statusLabel;

    @FXML private Button calculateGpaButton;
    @FXML private Button updateButton;

    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, String> nameCol;
    @FXML private TableColumn<Course, String> codeCol;
    @FXML private TableColumn<Course, Double> creditsCol;
    @FXML private TableColumn<Course, String> teacher1Col;
    @FXML private TableColumn<Course, String> teacher2Col;
    @FXML private TableColumn<Course, String> gradeCol;

    private final ObservableList<Course> courses = FXCollections.observableArrayList();

    private double targetCredits = 15.0;
    private double currentCredits = 0.0;

    @FXML
    public void initialize() {


        nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        codeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCode()));
        creditsCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getCredits()));
        teacher1Col.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTeacher1()));
        teacher2Col.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTeacher2()));
        gradeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLetterGrade()));

        courseTable.setItems(courses);

        gradeBox.getItems().addAll(
                "A+", "A", "A-",
                "B+", "B", "B-",
                "C+", "C", "D", "F"
        );


        targetCreditsField.setText(String.valueOf(targetCredits));

        targetCreditsField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                targetCredits = Double.parseDouble(newVal);
                if (targetCredits <= 0) throw new NumberFormatException();

                updateCreditsAndButton();
            } catch (NumberFormatException e) {
                calculateGpaButton.setDisable(true);
            }
        });

        updateCreditsAndButton();

        updateButton.setDisable(true);

        courseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                fillForm(newSel);
                updateButton.setDisable(false);
            } else {
                updateButton.setDisable(true);
            }
        });
    }

    @FXML
    private void handleAddCourse() {
        CourseData d = readAndValidate();
        if (d == null) return;

        if (currentCredits + d.credits > targetCredits) {
            showAlert(Alert.AlertType.WARNING, "Credit Limit",
                    "Adding this course exceeds the target credits.");
            return;
        }

        Course c = new Course(d.name, d.code, d.credits, d.teacher1, d.teacher2, d.grade);
        courses.add(c);

        currentCredits += d.credits;
        clearFields();
        updateCreditsAndButton();

        statusLabel.setText("Course added successfully.");
    }


    @FXML
    private void handleRemoveCourse() {
        Course selected = courseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.INFORMATION, "Remove", "Select a course to remove.");
            return;
        }

        courses.remove(selected);
        currentCredits -= selected.getCredits();

        updateCreditsAndButton();
        statusLabel.setText("Course removed.");
    }


    @FXML
    private void handleUpdateCourse() {
        Course sel = courseTable.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert(Alert.AlertType.INFORMATION, "Update", "Select a course to update.");
            return;
        }

        CourseData d = readAndValidate();
        if (d == null) return;

        double newTotal = currentCredits - sel.getCredits() + d.credits;

        if (newTotal > targetCredits) {
            showAlert(Alert.AlertType.WARNING, "Credit Limit",
                    "Updating this course exceeds the target credits.");
            return;
        }

        sel.setName(d.name);
        sel.setCode(d.code);
        sel.setCredits(d.credits);
        sel.setTeacher1(d.teacher1);
        sel.setTeacher2(d.teacher2);
        sel.setLetterGrade(d.grade);

        currentCredits = newTotal;
        courseTable.refresh();
        updateCreditsAndButton();

        statusLabel.setText("Course updated successfully.");
    }


    @FXML
    private void handleReset() {
        courses.clear();
        currentCredits = 0;
        clearFields();
        updateCreditsAndButton();
        statusLabel.setText("Form reset.");
    }


    @FXML
    private void handleCalculateGpa(ActionEvent event) {
        double qp = 0, cr = 0;

        for (Course c : courses) {
            qp += gradePoint(c.getLetterGrade()) * c.getCredits();
            cr += c.getCredits();
        }

        double gpa = cr == 0 ? 0 : qp / cr;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("result-view.fxml"));
            Parent root = loader.load();

            ResultController rc = loader.getController();
            rc.setData(new ArrayList<>(courses), gpa, targetCredits);

            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("gpa-style.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("GPA Result");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Courses");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV File", "*.csv"));

        File file = fileChooser.showSaveDialog(null);
        if (file == null) return;

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Course Name,Course Code,Credits,Teacher 1,Teacher 2,Grade\n");
            for (Course c : courses) {
                writer.write(String.format("%s,%s,%.2f,%s,%s,%s\n",
                        c.getName(), c.getCode(), c.getCredits(),
                        c.getTeacher1(), c.getTeacher2(), c.getLetterGrade()));
            }

            showAlert(Alert.AlertType.INFORMATION, "Export Successful", "Data exported!");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Export Failed", "Unable to export file.");
        }
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



    private static class CourseData {
        String name, code, teacher1, teacher2, grade;
        double credits;
    }

    private CourseData readAndValidate() {
        String name = courseNameField.getText().trim();
        String code = courseCodeField.getText().trim();
        String creditText = courseCreditField.getText().trim();
        String t1 = teacher1Field.getText().trim();
        String t2 = teacher2Field.getText().trim();
        String grade = gradeBox.getValue();

        if (name.isEmpty() || code.isEmpty() || creditText.isEmpty() ||
                t1.isEmpty() || t2.isEmpty() || grade == null) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Fill all fields.");
            return null;
        }

        double credit;
        try {
            credit = Double.parseDouble(creditText);
            if (credit <= 0) throw new NumberFormatException();
        } catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Credit must be a valid number.");
            return null;
        }

        CourseData d = new CourseData();
        d.name = name;
        d.code = code;
        d.credits = credit;
        d.teacher1 = t1;
        d.teacher2 = t2;
        d.grade = grade;
        return d;
    }

    private void fillForm(Course c) {
        courseNameField.setText(c.getName());
        courseCodeField.setText(c.getCode());
        courseCreditField.setText(String.valueOf(c.getCredits()));
        teacher1Field.setText(c.getTeacher1());
        teacher2Field.setText(c.getTeacher2());
        gradeBox.setValue(c.getLetterGrade());
    }

    private void clearFields() {
        courseNameField.clear();
        courseCodeField.clear();
        courseCreditField.clear();
        teacher1Field.clear();
        teacher2Field.clear();
        gradeBox.setValue(null);
    }

    private void updateCreditsAndButton() {
        currentCreditsLabel.setText(String.format("%.2f", currentCredits));
        calculateGpaButton.setDisable(currentCredits != targetCredits);
    }

    private double gradePoint(String g) {
        return switch (g) {
            case "A+" -> 4.00;
            case "A"  -> 3.75;
            case "A-" -> 3.50;
            case "B+" -> 3.25;
            case "B"  -> 3.00;
            case "B-" -> 2.75;
            case "C+" -> 2.50;
            case "C"  -> 2.25;
            case "D"  -> 2.00;
            default   -> 0.00;
        };
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
