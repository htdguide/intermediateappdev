package com.quizapp.quizApp.dto;

import java.util.Map;

public class QuizSubmissionDTO {
    private Long quizId;
    private Long userId; // Optional, for tracking purposes
    private Map<Long, String> answers; // questionId -> selectedOption

    // Getters and Setters
    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Map<Long, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Long, String> answers) {
        this.answers = answers;
    }
}
