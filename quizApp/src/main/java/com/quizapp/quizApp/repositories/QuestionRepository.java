package com.quizapp.quizApp.repositories;

import com.quizapp.quizApp.model.Difficulty;
import com.quizapp.quizApp.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    boolean LOGGING_ENABLED = true;

    // Find a question by questionId
    default Optional<Question> findByQuestionId(Long id) {
        try {
            if (LOGGING_ENABLED) System.out.println("Finding Question by ID: " + id);
            return findById(id);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error finding Question by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    // Fetch all questions without pagination
    @Query("SELECT q FROM Question q WHERE q.category = :category ORDER BY FUNCTION('RAND')")
    List<Question> findByCategory(@Param("category") String category);

    // Find questions by difficulty
    List<Question> findByDifficulty(Difficulty difficulty);

    // Find questions by text containing a keyword (case-insensitive search)
    @Query("SELECT q FROM Question q WHERE LOWER(q.text) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Question> findByTextContainingIgnoreCase(@Param("keyword") String keyword);

    // Find questions by questionId, difficulty, and category
    @Query("SELECT q FROM Question q WHERE q.questionId = :id AND q.difficulty = :difficulty AND q.category = :category")
    Optional<Question> findByIdAndDifficultyAndCategory(@Param("id") Long id,
                                                        @Param("difficulty") String difficulty,
                                                        @Param("category") String category);
}
