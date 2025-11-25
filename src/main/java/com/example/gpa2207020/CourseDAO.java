package com.example.gpa2207020;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CourseDAO {

    public static ObservableList<Course> getAllCourses() throws SQLException {
        ObservableList<Course> list = FXCollections.observableArrayList();

        String sql = "SELECT id, name, code, credits, teacher1, teacher2, grade FROM course";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Course c = new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("code"),
                        rs.getDouble("credits"),
                        rs.getString("teacher1"),
                        rs.getString("teacher2"),
                        rs.getString("grade")
                );
                list.add(c);
            }
        }
        return list;
    }

    public static Course insertCourse(Course c) throws SQLException {
        String sql = "INSERT INTO course(name, code, credits, teacher1, teacher2, grade) " +
                "VALUES (?,?,?,?,?,?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getCode());
            ps.setDouble(3, c.getCredits());
            ps.setString(4, c.getTeacher1());
            ps.setString(5, c.getTeacher2());
            ps.setString(6, c.getLetterGrade());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) c.setId(keys.getInt(1));
            }
        }
        return c;
    }

    public static void updateCourse(Course c) throws SQLException {
        String sql = "UPDATE course SET name=?, code=?, credits=?, teacher1=?, teacher2=?, grade=? " +
                "WHERE id=?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getCode());
            ps.setDouble(3, c.getCredits());
            ps.setString(4, c.getTeacher1());
            ps.setString(5, c.getTeacher2());
            ps.setString(6, c.getLetterGrade());
            ps.setInt(7, c.getId());
            ps.executeUpdate();
        }
    }

    public static void deleteCourse(Course c) throws SQLException {
        String sql = "DELETE FROM course WHERE id=?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, c.getId());
            ps.executeUpdate();
        }
    }
}
