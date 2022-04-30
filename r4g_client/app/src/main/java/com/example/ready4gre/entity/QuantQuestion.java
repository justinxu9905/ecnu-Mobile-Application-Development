package com.example.ready4gre.entity;

public class QuantQuestion {

    private int id;

    private String question;

    private String options;

    private String answer;

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getOptions() {
        return options;
    }

    public String getAnswer() {
        return answer;
    }
}
