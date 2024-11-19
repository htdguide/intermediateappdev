package com.quizapp.quizApp.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "QuizQuestion")
public class QuizQuestion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_question_id") // Updated column name
    private Long quizQuestionId; // Updated field name

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    public QuizQuestion() {}

    public QuizQuestion(Quiz quiz, Question question) {
        this.quiz = quiz;
        this.question = question;
    }

    // Getter and Setter for quizQuestionId
    public Long getQuizQuestionId() {
        return quizQuestionId;
    }

    public void setQuizQuestionId(Long quizQuestionId) {
        this.quizQuestionId = quizQuestionId;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
