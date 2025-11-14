package com.example.gpa2207020;


public class Course {
    private String name;
    private double credits;
    private String letterGrade;

    public Course(String name, double credits, String letterGrade) {
        this.name = name;
        this.credits = credits;
        this.letterGrade = letterGrade;
    }

    public String getName() {
        return name;
    }

    public double getCredits() {
        return credits;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }
}
