package com.example.dell.quizapp.models;

public class QuestionBank {
    private int id;
    private String year;

    public QuestionBank() {
    }

    public QuestionBank(int id, String year) {
        this.id = id;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
