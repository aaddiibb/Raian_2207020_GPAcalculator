package com.example.gpa2207020;

public class GpaHistoryEntry {
    private int id;
    private String roll;
    private double result;

    public GpaHistoryEntry(int id, String roll, double result) {
        this.id = id;
        this.roll = roll;
        this.result = result;
    }

    public GpaHistoryEntry(String roll, double result) {
        this(-1, roll, result);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRoll() { return roll; }
    public void setRoll(String roll) { this.roll = roll; }

    public double getResult() { return result; }
    public void setResult(double result) { this.result = result; }
}
