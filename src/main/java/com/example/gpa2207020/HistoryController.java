package com.example.gpa2207020;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class HistoryController {

    @FXML private TableView<GpaHistoryEntry> historyTable;
    @FXML private TableColumn<GpaHistoryEntry, String> rollCol;
    @FXML private TableColumn<GpaHistoryEntry, Double> resultCol;

    @FXML private Label statusLabel;

    private final ObservableList<GpaHistoryEntry> entries = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Roll column
        rollCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getRoll())
        );

        // GPA / Result column: first get the value from the model
        // If your getter is getResult() instead of getGpa(), change this line.
        resultCol.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getResult())
        );

        // Then format it to 2 decimal places in the cell
        resultCol.setCellFactory(col -> new TableCell<GpaHistoryEntry, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item)); // e.g. 3.33
                }
            }
        });

        historyTable.setItems(entries);

        loadFromDatabase();
    }

    private void loadFromDatabase() {
        try {
            entries.setAll(GpaHistoryDAO.getAllEntries());
            statusLabel.setText("Loaded " + entries.size() + " record(s) from database.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not load history.");
        }
    }

    // Delete selected row right on this page
    @FXML
    private void handleDeleteSelected() {
        GpaHistoryEntry selected = historyTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.INFORMATION, "Delete", "Select a record to delete.");
            return;
        }

        try {
            GpaHistoryDAO.deleteEntry(selected);
            entries.remove(selected);
            statusLabel.setText("Record deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not delete record.");
        }
    }

    // Edit: go back to GPA calculation page
    @FXML
    private void handleEditSelected(ActionEvent event) {
        GpaHistoryEntry selected = historyTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.INFORMATION, "Edit", "Select a record to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gpa-form-view.fxml"));
            Parent root = loader.load();

            // Right now we just start a fresh session; you can extend this
            // to pre-fill roll etc. if you add a roll field to the GPA form.
            GpaFormController controller = loader.getController();
            controller.startNewSession();
            // Example if you later add a setter:
            // controller.setRoll(selected.getRoll());

            Scene scene = new Scene(root, 1050, 650);
            scene.getStylesheets().add(getClass().getResource("gpa-style.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("GPA Calculator - Edit Result");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not open GPA calculator page.");
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
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not go back to home page.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
