package com.example.gpa2207020;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class GpaHistoryDAO {

    public static ObservableList<GpaHistoryEntry> getAllEntries() throws SQLException {
        ObservableList<GpaHistoryEntry> list = FXCollections.observableArrayList();

        String sql = "SELECT id, roll, result FROM gpa_history ORDER BY id DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new GpaHistoryEntry(
                        rs.getInt("id"),
                        rs.getString("roll"),
                        rs.getDouble("result")
                ));
            }
        }
        return list;
    }

    public static void insertEntry(GpaHistoryEntry e) throws SQLException {
        String sql = "INSERT INTO gpa_history(roll, result) VALUES (?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, e.getRoll());
            ps.setDouble(2, e.getResult());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    e.setId(keys.getInt(1));
                }
            }
        }
    }

    public static void deleteEntry(GpaHistoryEntry e) throws SQLException {
        String sql = "DELETE FROM gpa_history WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, e.getId());
            ps.executeUpdate();
        }
    }
}
