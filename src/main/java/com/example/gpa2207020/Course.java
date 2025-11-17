package com.example.gpa2207020;

public class Course {

    private String name;
    private String code;
    private double credits;
    private String teacher1;
    private String teacher2;
    private String letterGrade;

    public Course(String name, String code, double credits, String teacher1, String teacher2, String letterGrade) {
        this.name = name;
        this.code = code;
        this.credits = credits;
        this.teacher1 = teacher1;
        this.teacher2 = teacher2;
        this.letterGrade = letterGrade;
    }

    public String getName() { return name; }
    public String getCode() { return code; }
    public double getCredits() { return credits; }
    public String getTeacher1() { return teacher1; }
    public String getTeacher2() { return teacher2; }
    public String getLetterGrade() { return letterGrade; }

    public void setName(String name) { this.name = name; }
    public void setCode(String code) { this.code = code; }
    public void setCredits(double credits) { this.credits = credits; }
    public void setTeacher1(String teacher1) { this.teacher1 = teacher1; }
    public void setTeacher2(String teacher2) { this.teacher2 = teacher2; }
    public void setLetterGrade(String letterGrade) { this.letterGrade = letterGrade; }
}
