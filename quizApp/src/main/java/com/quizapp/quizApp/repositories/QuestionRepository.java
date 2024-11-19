package com.quizapp.quizApp.repositories;

import com.quizapp.quizApp.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Find a question by questionId
    Optional<Question> findByQuestionId(Long id);

    // Find questions by difficulty
    List<Question> findByDifficulty(String difficulty);

    // Find questions by category
    List<Question> findByCategory(String category);

    // Find questions by text containing a keyword (case-insensitive search)
    @Query("SELECT q FROM Question q WHERE LOWER(q.text) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Question> findByTextContainingIgnoreCase(@Param("keyword") String keyword);

    // Find questions by questionId, difficulty, and category
    @Query("SELECT q FROM Question q WHERE q.questionId = :id AND q.difficulty = :difficulty AND q.category = :category")
    Optional<Question> findByIdAndDifficultyAndCategory(@Param("id") Long id,
                                                        @Param("difficulty") String difficulty,
                                                        @Param("category") String category);
}
