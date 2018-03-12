package com.example.javier.izvactivities;

public class IzvActivity {
    private int id;
    private String name;
    private int year;
    private int month;
    private int day;
    private String[] teachers;
    private String[] groups;

    public IzvActivity(String name, int year, int month, int day, String[] teachers, String[] groups) {
        this.name = name;
        this.year = year;
        this.month = month;
        this.day = day;
        this.teachers = teachers;
        this.groups = groups;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String[] getTeachers() {
        return teachers;
    }

    public String[] getGroups() {
        return groups;
    }
}
