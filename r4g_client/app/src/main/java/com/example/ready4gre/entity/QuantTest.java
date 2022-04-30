package com.example.ready4gre.entity;

public class QuantTest {

    private int id;

    private String name;

    private QuantQuestion questions;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String test_name) {
        this.name = test_name;
    }

    public void setQuestions(QuantQuestion questions) {
        this.questions = questions;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public QuantQuestion getQuestions() {
        return questions;
    }
}
