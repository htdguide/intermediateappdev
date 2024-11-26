package com.quizapp.quizApp.dto;

import java.util.List;

public class QuizValidationResultDTO {
    private Long quizId;
    private int score;
    private int totalQuestions;
    private List<Long> correctAnswers; // List of questionIds that were answered correctly
    private List<QuestionFeedbackDTO> feedback; // Detailed feedback for each question

    // Getters and Setters
    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public List<Long> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<Long> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public List<QuestionFeedbackDTO> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<QuestionFeedbackDTO> feedback) {
        this.feedback = feedback;
    }

    // Nested DTO for question-specific feedback
    public static class QuestionFeedbackDTO {
        private Long questionId;
        private String userAnswer;
        private String correctAnswer;
        private boolean isCorrect;

        // Getters and Setters
        public Long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }

        public String getUserAnswer() {
            return userAnswer;
        }

        public void setUserAnswer(String userAnswer) {
            this.userAnswer = userAnswer;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public void setCorrectAnswer(String correctAnswer) {
            this.correctAnswer = correctAnswer;
        }

        public boolean isCorrect() {
            return isCorrect;
        }

        public void setCorrect(boolean correct) {
            isCorrect = correct;
        }
    }
}
