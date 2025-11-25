package com.example.gpa2207020;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    // Folder inside the project where DB will be stored
    private static final String DB_FOLDER = "db";
    private static final String DB_NAME   = "gpa.db";

    // JDBC URL for SQLite
    private static final String URL;

    static {
        // 1) Ensure db/ folder exists
        File folder = new File(DB_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 2) Full path to db/gpa.db
        File dbFile = new File(folder, DB_NAME);
        URL = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        // Print to console so you can see where the file is
        System.out.println("SQLite DB path = " + dbFile.getAbsolutePath());

        // 3) Create tables if they do not exist
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

            // ---- Course table (for detailed course info) ----
            String sqlCourse = "CREATE TABLE IF NOT EXISTS course (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "code TEXT NOT NULL," +
                    "credits REAL NOT NULL," +
                    "teacher1 TEXT NOT NULL," +
                    "teacher2 TEXT NOT NULL," +
                    "grade TEXT NOT NULL" +
                    ")";
            st.execute(sqlCourse);

            // ---- GPA history table (Roll + final GPA) ----
            String sqlHistory = "CREATE TABLE IF NOT EXISTS gpa_history (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "roll TEXT NOT NULL," +
                    "result REAL NOT NULL" +
                    ")";
            st.execute(sqlHistory);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 4) Get a connection to the SQLite DB
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
