package com.quizapp.quizApp.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "questionId")
@Entity
@Table(name = "Question", indexes = {
        @Index(name = "idx_category", columnList = "category"),
        @Index(name = "idx_difficulty", columnList = "difficulty")
})
public class Question implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false)
    private Difficulty difficulty;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "answer", nullable = false)
    private String answer;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "question_incorrect_answers",
            joinColumns = @JoinColumn(name = "question_id", referencedColumnName = "question_id")
    )
    @Column(name = "incorrect_answer")
    private List<String> incorrectAnswers;

    public Question() {}

    public Question(Difficulty difficulty, String category, String text, String answer, List<String> incorrectAnswers) {
        this.difficulty = difficulty;
        this.category = category;
        this.text = text;
        this.answer = answer;
        this.incorrectAnswers = incorrectAnswers;
    }

    // Getters and Setters
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

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }
}
