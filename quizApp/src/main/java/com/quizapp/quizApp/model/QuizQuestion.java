package com.quizapp.quizApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.quizapp.quizApp.utils.QuizQuestionSerializer;
import jakarta.persistence.*;
import java.io.Serializable;

@JsonSerialize(using = QuizQuestionSerializer.class)
@Entity
@Table(name = "QuizQuestion")
public class QuizQuestion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_question_id")
    private Long quizQuestionId;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false, foreignKey = @ForeignKey(name = "fk_quiz_question_quiz"))
    @JsonIgnore  // Ensure quiz details are included in the serialized response
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false, foreignKey = @ForeignKey(name = "fk_quiz_question_question"))
    @JsonIgnore  // Ensure question details are included in the serialized response
    private Question question;

    public QuizQuestion() {}

    public QuizQuestion(Quiz quiz, Question question) {
        this.quiz = quiz;
        this.question = question;
    }

    // Getters and Setters
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
