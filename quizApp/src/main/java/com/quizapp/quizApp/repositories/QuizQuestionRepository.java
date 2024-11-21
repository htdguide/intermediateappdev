package com.quizapp.quizApp.repositories;

import com.quizapp.quizApp.model.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

    boolean LOGGING_ENABLED = true;

    // Find all questions for a specific quiz by quizId
    List<QuizQuestion> findByQuiz_QuizId(Long quizId);

    // Find all quizzes containing a specific question by questionId
    List<QuizQuestion> findByQuestion_QuestionId(Long questionId);

    // Count the number of questions in a specific quiz
    @Query("SELECT COUNT(q) FROM QuizQuestion q WHERE q.quiz.quizId = :quizId")
    Long countQuestionsInQuiz(@Param("quizId") Long quizId);

    // Check if a specific question is part of a specific quiz
    @Query("SELECT CASE WHEN COUNT(q) > 0 THEN TRUE ELSE FALSE END FROM QuizQuestion q WHERE q.quiz.quizId = :quizId AND q.question.questionId = :questionId")
    boolean existsByQuizIdAndQuestionId(@Param("quizId") Long quizId, @Param("questionId") Long questionId);

    // Get all QuizQuestion mappings for a quiz sorted by quizQuestionId
    @Query("SELECT q FROM QuizQuestion q WHERE q.quiz.quizId = :quizId ORDER BY q.quizQuestionId ASC")
    List<QuizQuestion> findQuestionsByQuizIdSorted(@Param("quizId") Long quizId);

    // Find a specific QuizQuestion by quizId and questionId
    @Query("SELECT q FROM QuizQuestion q WHERE q.quiz.quizId = :quizId AND q.question.questionId = :questionId")
    Optional<QuizQuestion> findByQuizIdAndQuestionId(@Param("quizId") Long quizId, @Param("questionId") Long questionId);
}
