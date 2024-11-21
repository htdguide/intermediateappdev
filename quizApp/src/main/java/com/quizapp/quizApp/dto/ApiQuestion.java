package com.quizapp.quizApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown fields like "type"
public class ApiQuestion {

    private static final boolean LOGGING_ENABLED = true; // Logging flag

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
        logDebug("Set difficulty: " + difficulty);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        logDebug("Set category: " + category);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
        logDebug("Set question: " + question);
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
        logDebug("Set correctAnswer: " + correctAnswer);
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
        logDebug("Set incorrectAnswers: " + incorrectAnswers);
    }

    @Override
    public String toString() {
        return "ApiQuestion{" +
                "difficulty='" + difficulty + '\'' +
                ", category='" + category + '\'' +
                ", question='" + question + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", incorrectAnswers=" + incorrectAnswers +
                '}';
    }

    // Validation Method
    public boolean isValid() {
        boolean valid = difficulty != null && !difficulty.isEmpty() &&
                category != null && !category.isEmpty() &&
                question != null && !question.isEmpty() &&
                correctAnswer != null && !correctAnswer.isEmpty();
        logDebug("Validation check - isValid: " + valid);
        return valid;
    }

    // Logging utility
    private void logDebug(String message) {
        if (LOGGING_ENABLED) {
            System.out.println("DEBUG: " + message);
        }
    }
}
