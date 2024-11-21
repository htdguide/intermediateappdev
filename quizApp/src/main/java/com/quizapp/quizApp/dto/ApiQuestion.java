package com.quizapp.quizApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown fields like "type"
public class ApiQuestion {
    private String difficulty;
    private String category;
    private String question; // Maps to 'text' in your Question entity
    @JsonProperty("correct_answer")
    private String correctAnswer;

    @JsonProperty("incorrect_answers")
    private List<String> incorrectAnswers;

    // Getters and Setters
    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correct_answer) {
        this.correctAnswer = correct_answer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrect_answers) {
        this.incorrectAnswers = incorrect_answers;
    }
}

