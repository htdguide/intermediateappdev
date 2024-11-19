package com.quizapp.quizApp.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Question")
public class Question implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id") // Updated column name
    private Long questionId; // Updated field name

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false)
    private Difficulty difficulty;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "incorrect_answers", nullable = false, columnDefinition = "JSON")
    private String incorrectAnswers;

    public Question() {}

    public Question(Difficulty difficulty, String category, String text, String answer, String incorrectAnswers) {
        this.difficulty = difficulty;
        this.category = category;
        this.text = text;
        this.answer = answer;
        this.incorrectAnswers = incorrectAnswers;
    }

    // Getter and Setter for questionId
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(String incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }
}
